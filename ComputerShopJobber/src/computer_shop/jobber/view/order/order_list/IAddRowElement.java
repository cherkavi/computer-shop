package computer_shop.jobber.view.order.order_list;

import computer_shop.jobber.common_objects.RowElement;

public interface IAddRowElement {
	/** �������� ��� ���� ������� � ������  
	 * @param element - �������, ������� ����������� � ������ 
	 * @param quantity - ���-�� ������� �������� � ������ 
	 * */
	public void addRowElement(RowElement element, int quantity);
}
