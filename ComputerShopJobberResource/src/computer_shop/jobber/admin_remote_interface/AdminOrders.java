package computer_shop.jobber.admin_remote_interface;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber.common_objects.ElementOfOrder;

/** ������ ��������� �������� ����������� ���������� �� ������ �������������� � �������� */
public interface AdminOrders {
	/** �������� ������ ������� �� ���������:
	 * @param adminIdentifier - ���������� ������������� ��������������
	 * @param status - ������ ������
	 * @param dayBefore - ���-�� ���� �� ������� ����� ���������� ������ �����
	 * @return - ������ �� �������� 
	 * @throws - ���������� ����������, � ������, ���� ��������� ������-���� ���� ������
	 */
	public AdminTableOrdersRow[] getListOfOrders(AdminIdentifier adminIdentifier, int status, int dayBefore) throws Exception;
	
	/**
	 * ���������� ��� ���������� ������ ( �� ��� ����������� ������) ����� ������ 
	 * @param adminIdentifier - ���������� ������������� �������������� 
	 * @param orderKod - ���������� ����� ������, �� �������� ����� ������������� ������ 
	 * @param statusForSet - ������, ������� ����� ���������� 
	 * @return 
	 * <li> <b>true</b> - ������ ������� ����������  </li>
	 * <li> <b>false</b> - ������ ��������� ������� </li>
	 * @throws - ���������� ����������, � ������, ���� ��������� ������-���� ���� ������
	 */
	public boolean changeStatus(AdminIdentifier adminIdentifier, int orderKod, int statusForSet) throws Exception ;
	
	
	/** �������� �� ��������� ������ ������ ��� �������, ������� ������ � ���� �����
	 * @param adminIdentifier - ���������� ������������� �������������� 
	 * @param orderKod - ��� ������, �������� �������� ����� 
	 * @return ������ �� ��������� ������
	 * @throws Exception - ���� ��������� �����-���� ������ �� ����� ��������� ������
	 */
	public ElementOfOrder[] getElementsForOrder(AdminIdentifier adminIdentifier, int orderKod) throws Exception;
}
