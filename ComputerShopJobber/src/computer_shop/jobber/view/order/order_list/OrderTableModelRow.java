package computer_shop.jobber.view.order.order_list;

import computer_shop.jobber.common_objects.RowElement;

/** элемент для модели данных таблицы {@link OrderTableModel}*/
public class OrderTableModelRow {
	/** элемент, по которому формируется заказ */
	private RowElement element;
	/** кол-во элементов в заказе */
	private Integer quantity;
	
	/** элемент для модели данных таблицы {@link OrderTableModel}*/	
	public OrderTableModelRow(RowElement element, Integer quantity){
		this.element=element;
		this.quantity=quantity;
	}

	/** getting RowElement */
	public RowElement getElement() {
		return element;
	}

	/** set RowElement */
	public void setElement(RowElement element) {
		this.element = element;
	}

	/** get order quantity */
	public Integer getQuantity() {
		return quantity;
	}

	/** set order quantity*/
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
