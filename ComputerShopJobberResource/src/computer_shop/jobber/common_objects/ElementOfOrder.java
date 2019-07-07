package computer_shop.jobber.common_objects;

/** элемент заказа, то есть одна из строк заказа */
public class ElementOfOrder {
	/** код товара у поставщика */
	private String kod;
	/** кол-во товара у поставщика */
	private int quantity;

	/** элемент заказа, то есть одна из строк заказа 
	 * <br>
	 * обязательный конструктор без параметров 
	 * */
	public ElementOfOrder(){
	}

	
	public ElementOfOrder(String kod, int quantity){
		this.kod=kod;
		this.quantity=quantity;
	}
	/**
	 * @return код товара у поставщика 
	 */
	public String getKod() {
		return kod;
	}

	/**
	 * @param код товара у поставщика
	 */
	public void setKod(String kod) {
		this.kod = kod;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
}
