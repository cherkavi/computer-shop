package computer_shop.jobber.admin_remote_interface;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber.common_objects.ElementOfOrder;

/** данный интерфейс содержит необходимые интерфейсы по работе администратора с заказами */
public interface AdminOrders {
	/** получить массив заказов на основании:
	 * @param adminIdentifier - уникальный идентификатор администратора
	 * @param status - статус заказа
	 * @param dayBefore - кол-во дней за которые нужно отобразить данный заказ
	 * @return - массив из значений 
	 * @throws - выбрасывае исключение, в случае, если произошла какого-либо рода ошибка
	 */
	public AdminTableOrdersRow[] getListOfOrders(AdminIdentifier adminIdentifier, int status, int dayBefore) throws Exception;
	
	/**
	 * установить для указанного заказа ( по его уникальному номеру) новый статус 
	 * @param adminIdentifier - уникальный идентификатор администратора 
	 * @param orderKod - уникальный номер заказа, по которому нужно устанавливать статус 
	 * @param statusForSet - статус, который нужно установить 
	 * @return 
	 * <li> <b>true</b> - статус успешно установлен  </li>
	 * <li> <b>false</b> - ошибка установки статуса </li>
	 * @throws - выбрасывае исключение, в случае, если произошла какого-либо рода ошибка
	 */
	public boolean changeStatus(AdminIdentifier adminIdentifier, int orderKod, int statusForSet) throws Exception ;
	
	
	/** получить на основании номера заказа все позиции, которые входят в этот заказ
	 * @param adminIdentifier - уникальный идентификатор администратора 
	 * @param orderKod - код заказа, элементы которого нужны 
	 * @return массив из элементов заказа
	 * @throws Exception - если произошла какая-либо ошибка во время обработки заказа
	 */
	public ElementOfOrder[] getElementsForOrder(AdminIdentifier adminIdentifier, int orderKod) throws Exception;
}
