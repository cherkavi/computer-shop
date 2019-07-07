package computer_shop.jobber.remote_interface;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;

public interface ElementsLoader {
	/** �������� �������� �� ��������� ����������� ����� ������ � ����������� �������������� ������������ 
	 * @param sectionName - ��� ������ �� ������� ������������ 
	 * @param jobberIdentifier - ���������� ������������� ��������  
	 * */
	public RowElement[] getElementsFromSection(String sectionName, JobberIdentifier jobberIdentifier,boolean isCurrency);
}
