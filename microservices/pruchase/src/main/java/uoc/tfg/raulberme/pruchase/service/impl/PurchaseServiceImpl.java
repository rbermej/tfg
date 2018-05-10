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
import uoc.tfg.raulberme.pruchase.entity.Sale;
import uoc.tfg.raulberme.pruchase.exception.UnauthorizedPurchaseException;
import uoc.tfg.raulberme.pruchase.form.AdForm;
import uoc.tfg.raulberme.pruchase.provider.CurrencyExchangeProvider;
import uoc.tfg.raulberme.pruchase.provider.UserManagementProvider;
import uoc.tfg.raulberme.pruchase.repository.AdRepository;
import uoc.tfg.raulberme.pruchase.repository.PurchaseRequestRepository;
import uoc.tfg.raulberme.pruchase.repository.SaleRepository;
import uoc.tfg.raulberme.pruchase.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	private static final String ERROR_NOT_SELLER_OF_AD = "ERROR: You aren't the seller of this ad";
	private final AdRepository adRepository;
	private final PurchaseRequestRepository purchaseRequestRepository;
	private final SaleRepository saleRepository;
	private final UserManagementProvider userManagementProvider;
	private final CurrencyExchangeProvider currencyExchangeProvider;

	@Autowired
	public PurchaseServiceImpl(final AdRepository adRepository,
			final PurchaseRequestRepository purchaseRequestRepository, final SaleRepository saleRepository,
			final UserManagementProvider userManagementProvider,
			final CurrencyExchangeProvider currencyExchangeProvider) {
		this.adRepository = adRepository;
		this.purchaseRequestRepository = purchaseRequestRepository;
		this.saleRepository = saleRepository;
		this.userManagementProvider = userManagementProvider;
		this.currencyExchangeProvider = currencyExchangeProvider;
	}

	@Override
	public Collection<AdDTO> listActivedAdsWithOptionalCurrencies(final Optional<String> offeredCurrency,
			final Optional<String> demandedCurrency) {
		final Collection<Ad> ads;
		if (offeredCurrency.isPresent() && demandedCurrency.isPresent())
			ads = adRepository.findByStatusAndOfferedCurrencyAndDemandedCurrency(AdStatusType.ACTIVATED,
					offeredCurrency.get(), demandedCurrency.get());
		else if (offeredCurrency.isPresent())
			ads = adRepository.findByStatusAndOfferedCurrency(AdStatusType.ACTIVATED, offeredCurrency.get());
		else if (demandedCurrency.isPresent())
			ads = adRepository.findByStatusAndDemandedCurrency(AdStatusType.ACTIVATED, demandedCurrency.get());
		else
			ads = adRepository.findByStatus(AdStatusType.ACTIVATED);
		return ads.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Collection<AdDTO> listAdsBySeller(final String tokenId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		return adRepository.findBySeller(seller).stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public void createAd(final String tokenId, final AdForm adForm) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		adRepository.save(createAd(adForm, seller));
	}

	@Override
	public AdDTO getAd(final Long adId) {
		return convertToDTO(adRepository.getOne(adId));
	}

	@Override
	public void updatetAd(final String tokenId, final AdForm adForm) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adForm.getId());
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus() && AdStatusType.DEACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be updated");
		adRepository.save(updateAd(ad, adForm));
	}

	@Override
	public void removeAd(final String tokenId, final Long adId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus() && AdStatusType.DEACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be removed");
		adRepository.delete(ad);
	}

	@Override
	public void buyAd(final String tokenId, final Long adId) {
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adId);
		purchaseRequestRepository.save(createPurchaseRequest(applicant, ad));
	}

	@Override
	public void sellAd(final String tokenId, final Long requestId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final PurchaseRequest purchaseRequest = purchaseRequestRepository.getOne(requestId);
		final Ad ad = purchaseRequest.getAd();
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.DEACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be sold");
		// saleRepository.save(createSale(purchaseRequest));
		ad.setSale(createSale(purchaseRequest));
		ad.setStatus(AdStatusType.SOLD);
		adRepository.save(ad);
		purchaseRequestRepository.deleteByAd(ad);
	}

	@Override
	public void cancelRequest(final String tokenId, final Long requestId) {
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		final PurchaseRequest purchaseRequest = purchaseRequestRepository.getOne(requestId);
		if (!applicant.equals(purchaseRequest.getApplicant()))
			throw new UnauthorizedPurchaseException("ERROR: You aren't the applicant of this purchase request");
		purchaseRequestRepository.delete(purchaseRequest);
	}

	@Override
	public void hideAd(final String tokenId, final Long adId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be hide");
		ad.setStatus(AdStatusType.DEACTIVATED);
		adRepository.save(ad);
	}

	@Override
	public void republishAd(final String tokenId, final Long adId, final Float minimumExpectedAmount) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.DEACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be republished");
		ad.setStatus(AdStatusType.ACTIVATED);
		adRepository.save(ad);
	}

	@Override
	public void lockAd(final String tokenId, final Long adId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Ad ad = adRepository.getOne(adId);
		if (!seller.equals(ad.getSeller()))
			throw new UnauthorizedPurchaseException(ERROR_NOT_SELLER_OF_AD);
		if (AdStatusType.ACTIVATED != ad.getStatus())
			throw new UnauthorizedPurchaseException("ERROR: Ad can't be canceled");
		ad.setStatus(AdStatusType.BLOQUED);
		adRepository.save(ad);
	}

	@Override
	public Collection<PurchaseRequestDTO> listPurchaseRequestsBySeller(final String tokenId) {
		final String seller = userManagementProvider.retrieveUsernameByToken(tokenId);
		Collection<Ad> activedAds = adRepository.findBySellerAndStatus(seller, AdStatusType.ACTIVATED);
		Collection<PurchaseRequest> purchaseRequests = new ArrayList<>();
		activedAds.forEach(ad -> purchaseRequests.addAll(purchaseRequestRepository.findByAd(ad)));
		return purchaseRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Collection<PurchaseRequestDTO> listPurchaseRequestsByApplicant(final String tokenId) {
		final String applicant = userManagementProvider.retrieveUsernameByToken(tokenId);
		return purchaseRequestRepository.findByApplicant(applicant).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	private AdDTO convertToDTO(final Ad ad) {
		// @formatter:off
		return AdDTO.builder()
				.id(ad.getId())
				.amount(ad.getAmount())
				.minimumExpectedAmount(ad.getMinimumExpectedAmount())
				.location(ad.getLocation())
				.offeredCurrency(ad.getOfferedCurrency())
				.demandedCurrency(ad.getDemandedCurrency())
				.seller(ad.getSeller())
				.buyer(ad.getSale().getBuyer())
				.status(ad.getStatus())
				.date(ad.getDate())
				.build();
		// @formatter:on
	}

	private PurchaseRequestDTO convertToDTO(final PurchaseRequest purchaseRequest) {
		// @formatter:off
		return PurchaseRequestDTO.builder()
				.id(purchaseRequest.getId())
				.amount(purchaseRequest.getAd().getAmount())
				.minimumExpectedAmount(purchaseRequest.getAd().getMinimumExpectedAmount())
				.location(purchaseRequest.getAd().getLocation())
				.offeredCurrency(purchaseRequest.getAd().getOfferedCurrency())
				.demandedCurrency(purchaseRequest.getAd().getDemandedCurrency())
				.seller(purchaseRequest.getAd().getSeller())
				.buyer(purchaseRequest.getAd().getSale().getBuyer())
				.status(purchaseRequest.getAd().getStatus())
				.date(purchaseRequest.getDate())
				.build();
		// @formatter:on
	}

	private PurchaseRequest createPurchaseRequest(final String applicant, final Ad ad) {
		final float amount = currencyExchangeProvider.calculateAmount(ad.getOfferedCurrency(), ad.getDemandedCurrency(),
				ad.getAmount());
		// @formatter:off
		return PurchaseRequest.builder()
				.applicant(applicant)
				.ad(ad)
				.date(LocalDateTime.now())
				.amount(amount)
				.build();
		// @formatter:on
	}

	private Sale createSale(final PurchaseRequest request) {
		// @formatter:off
		return Sale.builder()
				.buyer(request.getApplicant())
				.date(LocalDateTime.now())
				.amount(request.getAmount())
				.build();
		// @formatter:on
	}

	private Ad createAd(final AdForm adForm, final String seller) {
		// @formatter:off
		return Ad.builder()
				.amount(adForm.getAmount())
				.minimumExpectedAmount(adForm.getMinimumExpectedAmount())
				.location(adForm.getLocation())
				.offeredCurrency(adForm.getOfferedCurrency())
				.demandedCurrency(adForm.getDemandedCurrency())
				.seller(seller)
				.build();
		// @formatter:on
	}

	private Ad updateAd(final Ad ad, final AdForm adForm) {
		ad.setAmount(adForm.getAmount());
		ad.setMinimumExpectedAmount(adForm.getMinimumExpectedAmount());
		ad.setLocation(adForm.getLocation());
		ad.setOfferedCurrency(adForm.getOfferedCurrency());
		ad.setDemandedCurrency(adForm.getDemandedCurrency());
		ad.setDate(LocalDateTime.now());
		return ad;
	}

}
