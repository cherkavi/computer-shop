package computer_shop.jobber.view.choice_currency;
/** получение текущего курса валют для заказа */
public interface CurrencyValue {
	/** получить розничный наличный курс валют */
	public float getCurrencyValue() throws Exception;
}
