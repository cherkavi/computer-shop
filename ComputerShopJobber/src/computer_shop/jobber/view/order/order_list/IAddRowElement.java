package computer_shop.jobber.view.order.order_list;

import computer_shop.jobber.common_objects.RowElement;

public interface IAddRowElement {
	/** добавить еще один элемент в список  
	 * @param element - элемент, который добавляется в список 
	 * @param quantity - кол-во данного элемента в списке 
	 * */
	public void addRowElement(RowElement element, int quantity);
}
