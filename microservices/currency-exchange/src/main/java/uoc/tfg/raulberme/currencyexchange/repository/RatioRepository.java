package uoc.tfg.raulberme.currencyexchange.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;

public interface RatioRepository extends JpaRepository<Ratio, Long> {

	public Collection<Ratio> findByDay(final LocalDate day);

	public Ratio findByCurrencyAndDay(final Currency currency, final LocalDate day);

	public boolean existsByDay(final LocalDate day);

}
