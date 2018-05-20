package uoc.tfg.raulberme.pruchase.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.pruchase.entity.Ad;
import uoc.tfg.raulberme.pruchase.entity.PurchaseRequest;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

	public Collection<PurchaseRequest> findByAd(final Ad ad);

	public Collection<PurchaseRequest> findByApplicant(final String applicant);

	public void deleteByAd(final Ad ad);

	public boolean existsByAdAndApplicant(final Ad ad, final String applicant);

}
