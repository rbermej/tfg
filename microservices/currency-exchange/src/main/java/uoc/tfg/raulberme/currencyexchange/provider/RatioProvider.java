package uoc.tfg.raulberme.currencyexchange.provider;

import java.time.LocalDate;
import java.util.Map;

public interface RatioProvider {

	public Map<String, Float> findByDay(LocalDate day);

}
