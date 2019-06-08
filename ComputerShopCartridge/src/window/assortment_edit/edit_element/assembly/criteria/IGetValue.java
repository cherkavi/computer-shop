package window.assortment_edit.edit_element.assembly.criteria;

/** �������� ������� �������� ����������� ���������� */
public interface IGetValue <T>{
	/** �������� ������� �������� �������� (�� ����������� ����������) */
	public T getValue();
	
	/** �������� ��� �������� �������� */
	public int getAssortmentTypeDescriptionKod();
	
	/** �������� ������������ ���� ��������  */
	public String getAssortmentTypeDescriptionTitle();
}
