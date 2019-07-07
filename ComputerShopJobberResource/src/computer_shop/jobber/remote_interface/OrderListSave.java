package computer_shop.jobber.remote_interface;

import computer_shop.jobber.common_objects.JobberIdentifier;


public interface OrderListSave {
	/** ��������� ����� � ���������� ��� �����
	 * @param identifier - ���������� ������������� Jobber-�
	 * @param kodes - ���� ������ 
	 * @param quantities - ��������������� ����� ���-��
	 * @param currency - 
	 * <li> true - ����� � USD </li> 
	 * <li> false - ����� � ��� </li> 
	 * */
	public String saveOrderListAndGetNumber(JobberIdentifier identifier, String[] kodes, int[] quantities, boolean currency);
}
