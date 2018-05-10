package uoc.tfg.raulberme.pruchase.provider;

public interface CurrencyExchangeProvider {

	public float calculateAmount(final String baseCurrency, final String destinationCurrency, final Float quantity);

}
