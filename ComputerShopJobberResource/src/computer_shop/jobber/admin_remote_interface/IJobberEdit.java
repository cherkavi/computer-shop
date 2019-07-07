package computer_shop.jobber.admin_remote_interface;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;

/** ���������, ������� ������ ��� �������������� Jobber-�� */
public interface IJobberEdit {
	
	/** �������� ���� ������ Jobber-�� 
	 * @param adminIdentifier - ���������� ������������� �������������� 
	 * @return ������ ���� Jobber-�� � ������� 
	 */
	public JobberElement[] getAllJobbers(AdminIdentifier adminIdentifier) throws Exception;
	
	
	/** ������ �� ��������� ������ �� ������������� Jobber 
	 * @param adminIdentifier - ���������� ������������� ��������������
	 * @param jobberElement - �������, ������� ���������� ��������, �������� ���� - JobberElement.kod
	 * @return
	 * <li> <b>null</b> ������ ������� ��������� </li> 
	 * <li> <b>String</b>������ ���������� ������ - �������� ������</li> 
	 */
	public String update(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;
	
	
	/** ������ �� ���������� ������ ��������  - Jobber-�
	 * @param adminIdentifier - ���������� ������������� �������������� 
	 * @param jobberElement - �������, ������� ���������� ��������, ���� JobberElement.kod ����� �������������
	 * @return
	 * <li> <b>null</b> ������ ������� ��������� </li> 
	 * <li> <b>String</b>������ ���������� ������ �������� ������ </li> 
	 */
	public String add(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;

	
	/** ������ �� �������� �������� 
	 * @param adminIdentifier - ������������� �������������� 
	 * @param jobberElement - �������, ������� ������� ������� 
	 * @return 
	 * <li> <b>true</b> ������ ������� ������� </li> 
	 * <li> <b>false</b>������ �� ����� ���� ������� </li> 
	 */
	public String remove(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;
}
