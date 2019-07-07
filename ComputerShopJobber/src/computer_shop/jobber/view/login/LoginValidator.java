package computer_shop.jobber.view.login;

import computer_shop.jobber.common_objects.JobberIdentifier;

public interface LoginValidator {
	/** �������� ���������� ������-������������� �� Jobber
	 * @param login - �����
	 * @param password - ������
	 * @return ������-�������������
	 * @throws Exception 
	 * */
	public JobberIdentifier checkPassword(String login, String password) throws Exception;
}
