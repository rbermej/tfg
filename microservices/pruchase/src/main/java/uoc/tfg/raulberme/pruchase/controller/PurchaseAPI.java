package uoc.tfg.raulberme.pruchase.controller;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uoc.tfg.raulberme.pruchase.dto.AdDTO;
import uoc.tfg.raulberme.pruchase.dto.PurchaseRequestDTO;
import uoc.tfg.raulberme.pruchase.form.AdForm;
import uoc.tfg.raulberme.pruchase.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
@Api(value = "Purchase", tags = { "Purchase" })
public class PurchaseAPI {

	private final PurchaseService service;

	@Autowired
	public PurchaseAPI(PurchaseService service) {
		this.service = service;
	}

	@ApiOperation(value = "Get all Ads", notes = "Returns all ads")
	@GetMapping("/ads")
	public @ResponseBody Collection<AdDTO> listAdsWithOptionalCurrencies(
			@RequestParam(name = "from") final Optional<String> offeredCurrency,
			@RequestParam(name = "to") final Optional<String> demandedCurrency) {
		return service.listActivedAdsWithOptionalCurrencies(offeredCurrency, demandedCurrency);
	}

	@ApiOperation(value = "Get all Ads by Seller", notes = "Returns all ads by seller")
	@GetMapping("/ads/seller")
	public @ResponseBody Collection<AdDTO> listAdsBySeller(@RequestParam final String tokenId) {
		return service.listAdsBySeller(tokenId);
	}

	@ApiOperation(value = "Create an Ad", notes = "Create a new ad")
	@PutMapping("/ads")
	public void createAd(@RequestParam final String tokenId, @Valid @RequestBody final AdForm ad) {
		service.createAd(tokenId, ad);
	}

	@ApiOperation(value = "Get an Ad", notes = "Get an ad by its id")
	@GetMapping("/ads/{adId}")
	public @ResponseBody AdDTO getAd(@PathVariable final Long adId) {
		return service.retrieveAd(adId);
	}

	@ApiOperation(value = "Update an Ad", notes = "Update an ad")
	@PostMapping("/ads")
	public void updatetAd(@RequestParam final String tokenId, @Valid @RequestBody final AdForm ad) {
		service.updatetAd(tokenId, ad);
	}

	@ApiOperation(value = "Delete an Ad", notes = "Delete an ad")
	@DeleteMapping("/ads/{adId}")
	public void removeAd(@RequestParam final String tokenId, @PathVariable final Long adId) {
		service.removeAd(tokenId, adId);
	}

	@ApiOperation(value = "Buy an Ad", notes = "Buy an ad")
	@PutMapping("/ads/{adId}/buy")
	public void buyAd(@RequestParam final String tokenId, @PathVariable final Long adId) {
		service.buyAd(tokenId, adId);
	}

	@ApiOperation(value = "Accept a Purchase Request", notes = "Accept a purchase request to sell one ad")
	@PostMapping("/requests/{requestId}/sell")
	public void sellAd(@RequestParam final String tokenId, @PathVariable final Long requestId) {
		service.sellAd(tokenId, requestId);
	}

	@ApiOperation(value = "Delete a Purchase Request", notes = "Delete a purchase request")
	@DeleteMapping("/requests/{purchaseRequestId}")
	public void removePurchaseRequest(@RequestParam final String tokenId, @PathVariable final Long purchaseRequestId) {
		service.removePurchaseRequest(tokenId, purchaseRequestId);
	}

	@ApiOperation(value = "Get all Purchase Request by Seller", notes = "Get all purchase request by seller")
	@GetMapping("/requests/seller")
	public @ResponseBody Collection<PurchaseRequestDTO> listPurchaseRequestsBySeller(
			@RequestParam final String tokenId) {
		return service.listPurchaseRequestsBySeller(tokenId);
	}

	@ApiOperation(value = "Get all Purchase Request by Applicant", notes = "Get all purchase request by applicant")
	@GetMapping("/requests/applicant")
	public @ResponseBody Collection<PurchaseRequestDTO> listPurchaseRequestsByApplicant(
			@RequestParam final String tokenId) {
		return service.listPurchaseRequestsByApplicant(tokenId);
	}

}
