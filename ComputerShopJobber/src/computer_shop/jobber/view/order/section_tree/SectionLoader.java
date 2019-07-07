package computer_shop.jobber.view.order.section_tree;

import javax.swing.tree.TreePath;
import computer_shop.jobber.common_objects.StringArrayElement;

/** �����, ������� �������� �� ��������� �������� ������ �� ���� */
public interface SectionLoader {
	/** �������� ���� �������� �������, ������� ����� ��������� ��� ������ � ������ ������ */
	public StringArrayElement getTreeRootNode();
	/** ��������  �������� ��������� �������� 
	 * @param ������� ������ �� String, ������� ���������� � ����� ������� �������� 
	 * */
	public String[] getSubRootElements();
	
	/** 
	 * �������� ����������� �� ��������� ���������� ��������
	 * @param path - ������ ���� � ��������, �� �������� ����� �������� �����������
	 * @return 
	 * <li> null - ���� ������������ ��� </li>
	 * <li> String[] - ���� ����������� ���� </li>
	 * */
	public String[] getSubElements(TreePath path);

	/** 
	 * �������� ����������� �� ��������� ���������� ��������
	 * @param path - ������ ���� � ��������, �� �������� ����� �������� �����������
	 * @return 
	 * <li> null - ���� ������������ ��� </li>
	 * <li> String[] - ���� ����������� ���� </li>
	 * */
	public String[] getSubElements(String lastElement);
	
	
	/** @return true ���� ������ ������� ����� ����� 
	 * @param path ������ ���� � �������� 
	 * */
	public boolean elementHasChild(TreePath path);

	/** @return true ���� ������ ������� ����� ����� 
	 * @param path ������ ���� � �������� 
	 * */
	public abstract boolean elementHasChild(String lastElement);
	
	
}
