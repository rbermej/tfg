package uoc.tfg.raulberme.communication.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.communication.entity.Valuation;

public interface ValuationRepository extends JpaRepository<Valuation, Long> {

	public Collection<Valuation> findByEvaluated(final String evaluated);

}
