package wicket_utility;

import database.ConnectWrap;

/** �������, ����������� ������ ��������� ������� ����������� � ����� ������*/
public interface IConnectorAware {
	/** �������� ���������� � ����� ������ */
	public ConnectWrap getConnector();
}
