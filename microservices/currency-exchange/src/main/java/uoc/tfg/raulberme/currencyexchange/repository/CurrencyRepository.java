package uoc.tfg.raulberme.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

}
