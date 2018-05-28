package uoc.tfg.raulberme.pruchase.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.pruchase.dto.AdDTO;
import uoc.tfg.raulberme.pruchase.dto.PurchaseRequestDTO;
import uoc.tfg.raulberme.pruchase.entity.Ad;
import uoc.tfg.raulberme.pruchase.entity.AdStatusType;
import uoc.tfg.raulberme.pruchase.entity.PurchaseRequest;
import uoc.tfg.raulberme.pruchase.entity.RolUserType;
import uoc.tfg.raulberme.pruchase.entity.Sale;
import uoc.tfg.raulberme.pruchase.exception.NotFoundPurchaseException;
import uoc.tfg.raulberme.pruchase.exception.UnauthorizedPurchaseException;
import uoc.tfg.raulberme.pruchase.form.AdForm;
import uoc.tfg.raulberme.pruchase.provider.CurrencyExchangeProvider;
import uoc.tfg.raulberme.pruchase.provider.UserManagementProvider;
import uoc.tfg.raulberme.pruchase.repository.AdRepository;
import uoc.tfg.raulberme.pruchase.repository.PurchaseRequestRepository;
import uoc.tfg.raulberme.pruchase.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	private static final String ERROR_AD_NOT_FOUND = "ERROR: Ad not found.";
	private static final String ERROR_AD_CANT_BE_REMOVED = "ERROR: Ad can't be removed.";
	private static final String ERROR_AD_CANT_BE_UPDATED = "ERROR: Ad can't be updated.";
	private static final String ERROR_AD_CANT_BE_BUY = "ERROR: Ad can't be buy.";
	private static final String ERROR_AD_CANT_BE_SOLD = "ERROR: Ad can't be sold.";
	private static final String ERROR_NOT_SELLER_OF_AD = "ERROR: You aren't the seller of this ad.";
	private static final String ERROR_NO_APPLICANT_OF_PURCHASE_REQUEST = "ERROR: You aren't the applicant of this purchase request.";
	private static final String ERROR_SELLER_AND_APPLICANT_MUST_BE_DIFFERENT = "ERROR: seller and applicant must be different.";
	private static final String ERROR_PURCHASE_REQUEST_NOT_FOUND = "ERROR: Purchase Request not found.";
	private static final String ERROR_PURCHASE_REQUEST_DUPLICATED = "ERROR: purchase request duplicated.";

	private final AdRepository adRepository;
	private final PurchaseRequestRepository purchaseRequestRepository;
	private final UserManagementProvider userManagementProvider;
	private final CurrencyExchangeProvider currencyExchangeProvider;

	@Autowired
	public PurchaseServiceImpl(final AdRepository adRepository,
			final PurchaseRequestRepository purchaseRequestRepository,
			final UserManagementProvider userManagementProvider,
			final CurrencyExchangeProvider currencyExchangeProvider) {
		this.adRepository = adRepository;
		this.purchaseRequestRepository = purchaseRequestRepository;
		this.userManagementProvider = userManagementProvider;
		this.currencyExchangeProvider = currencyExchangeProvider;
	}

	@Override
	public Collection<AdDTO> listActivedAdsWithOptionalCurrencies(final Optional<String> offeredCurrency,
			final Optional<String> demandedCurrency) {
		final Collection<Ad> ads;
		if (offeredCurrency.isPresent() && demandedCurrency.isPresent()) {
			ads = adRepository.findByStatusAndOfferedCurrencyAndDemandedCurrency(AdStatusType.ACTIVATED,
					offeredCurrency.get(), demandedCurrency.get());
		} else if (offeredCurrency.isPresent()) {
			ads = adRepository.findByStatusAndOfferedCurrency(AdStatusType.ACTIVATED, offeredCurrency.get());
		} else if (demandedCurrency.isPresent()) {
			ads = adRepository.findByStatusAndDemandedCurrency(AdStatusType.ACTIVATED, demandedCurrency.get());
		} else {
			ads = adRepository.findByStatus(AdStatusType.ACTIVATED);
		}
		return ads.stream().map(this::convertToDTO).sorted().collect(Collectors.toList());
	}

	@Override
	public Collection<AdDTO> listAdsBySeller(final String tokenId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		return adRepository.findBySeller(seller).stream().map(this::convertToDTO).sorted().collect(Collectors.toList());
	}

	@Override
	public void createAd(final String tokenId, final AdForm adForm) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		adRepository.save(createAd(adForm, seller));
	}

	@Override
	public AdDTO retrieveAd(final Long adId) {
		return convertToDTO(adRepository.getOne(adId));
	}

	@Override
	public void updatetAd(final String tokenId, final AdForm adForm) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final Ad ad = getAd(adForm.getId());
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException(ERROR_AD_CANT_BE_UPDATED);
		adRepository.save(updateAd(ad, adForm));
	}

	@Override
	public void removeAd(final String tokenId, final Long adId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final Ad ad = getAd(adId);
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException(ERROR_AD_CANT_BE_REMOVED);
		adRepository.delete(ad);
	}

	@Override
	public void buyAd(final String tokenId, final Long adId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = getAd(adId);
		if (applicant.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_SELLER_AND_APPLICANT_MUST_BE_DIFFERENT);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException(ERROR_AD_CANT_BE_BUY);
		if (purchaseRequestRepository.existsByAdAndApplicant(ad, applicant))
			throw new UnauthorizedPurchaseException(ERROR_PURCHASE_REQUEST_DUPLICATED);
		purchaseRequestRepository.save(createPurchaseRequest(applicant, ad));
	}

	@Override
	public void sellAd(final String tokenId, final Long requestId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final PurchaseRequest purchaseRequest = getPurchaseRequest(requestId);
		final Ad ad = purchaseRequest.getAd();
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException(ERROR_AD_CANT_BE_SOLD);
		ad.setSale(createSale(purchaseRequest));
		ad.setStatus(AdStatusType.SOLD);
		adRepository.save(ad);
	}

	@Override
	public void removePurchaseRequest(final String tokenId, final Long requestId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		final PurchaseRequest purchaseRequest = getPurchaseRequest(requestId);
		if (!applicant.equals(purchaseRequest.getApplicant()))
			throw new UnauthorizedPurchaseException(ERROR_NO_APPLICANT_OF_PURCHASE_REQUEST);
		purchaseRequestRepository.delete(purchaseRequest);
	}

	@Override
	public Collection<PurchaseRequestDTO> listPurchaseRequestsBySeller(final String tokenId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Collection<Ad> activedAds = adRepository.findBySellerAndStatus(seller, AdStatusType.ACTIVATED);
		final Collection<PurchaseRequest> purchaseRequests = new ArrayList<>();
		activedAds.forEach(ad -> purchaseRequests.addAll(purchaseRequestRepository.findByAd(ad)));
		return purchaseRequests.stream().map(this::convertToDTO).sorted().collect(Collectors.toList());
	}

	@Override
	public Collection<PurchaseRequestDTO> listPurchaseRequestsByApplicant(final String tokenId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		return purchaseRequestRepository.findByApplicant(applicant).stream().map(this::convertToDTO).sorted()
				.collect(Collectors.toList());
	}

	private Ad getAd(final Long adId) {
		final Optional<Ad> ad = adRepository.findById(adId);
		if (!ad.isPresent())
			throw new NotFoundPurchaseException(ERROR_AD_NOT_FOUND);
		return ad.get();
	}

	private PurchaseRequest getPurchaseRequest(final Long requestId) {
		final Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(requestId);
		if (!purchaseRequest.isPresent())
			throw new NotFoundPurchaseException(ERROR_PURCHASE_REQUEST_NOT_FOUND);
		return purchaseRequest.get();
	}

	private AdDTO convertToDTO(final Ad ad) {
		final String buyer = ad.getSale() != null ? ad.getSale().getBuyer() : null;
		final LocalDateTime date = ad.getSale() != null ? ad.getSale().getDate() : ad.getDate();
		// @formatter:off
		return AdDTO.builder()
				.id(ad.getId())
				.demandedAmount(ad.getDemandedAmount())
				.demandedCurrency(ad.getDemandedCurrency())
				.offeredAmount(ad.getOfferedAmount())
				.offeredCurrency(ad.getOfferedCurrency())
				.seller(ad.getSeller())
				.location(ad.getLocation())
				.buyer(buyer)
				.status(ad.getStatus())
				.date(date)
				.build();
		// @formatter:on
	}

	private PurchaseRequestDTO convertToDTO(final PurchaseRequest purchaseRequest) {
		final Ad ad = purchaseRequest.getAd();
		// @formatter:off
		return PurchaseRequestDTO.builder()
				.id(purchaseRequest.getId())
				.demandedAmount(ad.getDemandedAmount())
				.demandedCurrency(ad.getDemandedCurrency())
				.offeredAmount(ad.getOfferedAmount())
				.offeredCurrency(ad.getOfferedCurrency())
				.seller(ad.getSeller())
				.location(ad.getLocation())
				.buyer(purchaseRequest.getApplicant())
				.status(ad.getStatus())
				.date(purchaseRequest.getDate())
				.build();
		// @formatter:on
	}

	private PurchaseRequest createPurchaseRequest(final String applicant, final Ad ad) {
		// @formatter:off
		return PurchaseRequest.builder()
				.applicant(applicant)
				.ad(ad)
				.date(LocalDateTime.now())
				.build();
		// @formatter:on
	}

	private Sale createSale(final PurchaseRequest purchaseRequest) {
		// @formatter:off
		return Sale.builder()
				.ad(purchaseRequest.getAd())
				.buyer(purchaseRequest.getApplicant())
				.date(LocalDateTime.now())
				.build();
		// @formatter:on
	}

	private Ad createAd(final AdForm adForm, final String seller) {
		final float demandedAmount = currencyExchangeProvider.calculateAmount(adForm.getOfferedCurrency(),
				adForm.getDemandedCurrency(), adForm.getOfferedAmount());
		// @formatter:off
		return Ad.builder()
				.offeredAmount(adForm.getOfferedAmount())
				.offeredCurrency(adForm.getOfferedCurrency())
				.demandedAmount(demandedAmount)
				.demandedCurrency(adForm.getDemandedCurrency())
				.seller(seller)
				.location(adForm.getLocation())
				.build();
		// @formatter:on
	}

	private Ad updateAd(final Ad ad, final AdForm adForm) {
		final float demandedAmount = currencyExchangeProvider.calculateAmount(adForm.getOfferedCurrency(),
				adForm.getDemandedCurrency(), adForm.getOfferedAmount());
		ad.setOfferedAmount(adForm.getOfferedAmount());
		ad.setOfferedCurrency(adForm.getOfferedCurrency());
		ad.setDemandedAmount(demandedAmount);
		ad.setDemandedCurrency(adForm.getDemandedCurrency());
		ad.setLocation(adForm.getLocation());
		ad.setDate(LocalDateTime.now());
		return ad;
	}

}
