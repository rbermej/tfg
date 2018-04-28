package uoc.tfg.raulberme.currencyexchange.repository;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;

public interface RatioRepository extends JpaRepository<Ratio, Long> {
	public Collection<Ratio> findByDay(LocalDate day);

	public Ratio findByCurrencyAndDay(Currency currency, LocalDate day);

	public Collection<Ratio> findByCurrency(Currency currency);

}
