package computer_shop.jobber.view.history;

import computer_shop.jobber.common_objects.HistoryOrder;
import computer_shop.jobber.common_objects.HistoryRow;
import computer_shop.jobber.common_objects.JobberIdentifier;

/** загрузчик данных о заказах клиента за указанный период */
public interface HistoryLoader {
	/** получить список заказов за указанный период */
	public HistoryRow[] getHistoryOrders(JobberIdentifier identifier, int daysBefore);
	/** получить список элементов заказа по указанному заказу */
	public HistoryOrder[] getHistoryElementsByOrder(JobberIdentifier identifier, HistoryRow historyRow);
}
