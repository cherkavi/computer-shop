package computer_shop.jobber.remote_interface;
/** получение текущего курса валют для заказа */
public interface CurrencyValue {
	/** получить розничный наличный курс валют */
	public float getCurrencyValue() throws Exception;
}
