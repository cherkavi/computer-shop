package computer_shop.jobber.view.order.order_list;


/** слушатель для оповещения об изменениях в заказе */
public interface IAmountChangeListener {
	/** слушатель для изменения в сумме заказа и в кол-ве заказа */
	public void amountChanged(float amount, int quantity);
}
