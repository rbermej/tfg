package uoc.tfg.raulberme.pruchase.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import uoc.tfg.raulberme.pruchase.entity.Ad;
import uoc.tfg.raulberme.pruchase.entity.AdStatusType;

public interface AdRepository extends JpaRepository<Ad, Long>, PagingAndSortingRepository<Ad, Long> {

	public Collection<Ad> findByStatus(final AdStatusType adStatusType);

	public Collection<Ad> findByStatusAndOfferedCurrency(final AdStatusType adStatusType, final String offeredCurrency);

	public Collection<Ad> findByStatusAndDemandedCurrency(final AdStatusType adStatusType,
			final String demandedCurrency);

	public Collection<Ad> findByStatusAndOfferedCurrencyAndDemandedCurrency(final AdStatusType adStatusType,
			final String offeredCurrency, final String demandedCurrency);

	public Collection<Ad> findBySeller(final String seller);

	public Collection<Ad> findBySellerAndStatus(final String seller, final AdStatusType status);

}
