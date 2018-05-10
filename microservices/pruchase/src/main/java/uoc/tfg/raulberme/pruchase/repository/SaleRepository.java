package uoc.tfg.raulberme.pruchase.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.pruchase.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	// public Collection<Sale> findByAd(final Ad ad);

	public Collection<Sale> findByBuyer(final String buyer);

}
