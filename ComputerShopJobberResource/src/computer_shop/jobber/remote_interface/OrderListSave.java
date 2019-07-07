package computer_shop.jobber.remote_interface;

import computer_shop.jobber.common_objects.JobberIdentifier;


public interface OrderListSave {
	/** сохранить заказ и отобразить его номер
	 * @param identifier - уникальный идентификатор Jobber-а
	 * @param kodes - коды заказа 
	 * @param quantities - соответствующие кодам кол-ва
	 * @param currency - 
	 * <li> true - заказ в USD </li> 
	 * <li> false - заказ в грн </li> 
	 * */
	public String saveOrderListAndGetNumber(JobberIdentifier identifier, String[] kodes, int[] quantities, boolean currency);
}
