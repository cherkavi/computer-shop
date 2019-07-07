package computer_shop.jobber_admin.view.login;

import computer_shop.jobber.common_objects.AdminIdentifier;

/** ��������� ��� ��������� �������-���������� ��� �������������� */
public interface AdminLoginValidator {
	/** �������� ���������� ������-������������� ��� ��������������
	 * @param login - �����
	 * @param password - ������
	 * @return ������-������������� �������������� 
	 * @throws Exception ���� �� ������� �������� ����� �� ���������� �������
	 * */
	public AdminIdentifier checkPassword(String login, String password) throws Exception; 
}
