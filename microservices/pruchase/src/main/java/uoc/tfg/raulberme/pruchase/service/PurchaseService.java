package uoc.tfg.raulberme.pruchase.service;

import java.util.Collection;
import java.util.Optional;

import uoc.tfg.raulberme.pruchase.dto.AdDTO;
import uoc.tfg.raulberme.pruchase.dto.PurchaseRequestDTO;
import uoc.tfg.raulberme.pruchase.form.AdForm;

public interface PurchaseService {

	public Collection<AdDTO> listActivedAdsWithOptionalCurrencies(final Optional<String> offeredCurrency,
			final Optional<String> demandedCurrency);

	public Collection<AdDTO> listAdsBySeller(final String tokenId);

	public void createAd(final String tokenId, final AdForm ad);

	public AdDTO retrieveAd(final Long adId);

	public void updatetAd(final String tokenId, final AdForm ad);

	public void removeAd(final String tokenId, final Long adId);

	public void buyAd(final String tokenId, final Long adId);

	public void sellAd(final String tokenId, final Long requestId);

	public void removePurchaseRequest(final String tokenId, final Long purchaseRequestId);

	public Collection<PurchaseRequestDTO> listPurchaseRequestsBySeller(final String tokenId);

	public Collection<PurchaseRequestDTO> listPurchaseRequestsByApplicant(final String tokenId);

}
