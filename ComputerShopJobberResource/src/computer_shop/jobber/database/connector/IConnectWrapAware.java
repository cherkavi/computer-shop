package computer_shop.jobber.database.connector;

/** ������, ������� ��������� ������ ��������� "�������" ��� �������� �������� �� ������ {@link database.wrap.ConnectWrap}*/
public interface IConnectWrapAware {
	/** �������� �����-������� ��� ���������� � ����� ������ */
	public ConnectWrap getConnectWrap();
}
