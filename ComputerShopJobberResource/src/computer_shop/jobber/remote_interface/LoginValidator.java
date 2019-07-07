package computer_shop.jobber.remote_interface;

import computer_shop.jobber.common_objects.JobberIdentifier;

/** ��������� ����������� �������������� Jobber �� ������ � ������ */
public interface LoginValidator {
	/** �������� ���������� ������-������������� �� Jobber
	 * @param login - �����
	 * @param password - ������
	 * @return ������-�������������
	 * */
	public JobberIdentifier checkPassword(String login, String password);
}
