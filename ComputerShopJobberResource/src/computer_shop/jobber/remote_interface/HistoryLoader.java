package computer_shop.jobber.remote_interface;

import computer_shop.jobber.common_objects.HistoryOrder;
import computer_shop.jobber.common_objects.HistoryRow;
import computer_shop.jobber.common_objects.JobberIdentifier;

/** ��������� ������ � ������� ������� �� ��������� ������ */
public interface HistoryLoader {
	/** �������� ������ ������� �� ��������� ������ */
	public HistoryRow[] getHistoryOrders(JobberIdentifier identifier, int daysBefore);
	/** �������� ������ ��������� ������ �� ���������� ������ */
	public HistoryOrder[] getHistoryElementsByOrder(JobberIdentifier identifier, HistoryRow historyRow);
}
