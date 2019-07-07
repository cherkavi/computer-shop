package computer_shop.jobber.common_objects;

/** ������� ������, �� ���� ���� �� ����� ������ */
public class ElementOfOrder {
	/** ��� ������ � ���������� */
	private String kod;
	/** ���-�� ������ � ���������� */
	private int quantity;

	/** ������� ������, �� ���� ���� �� ����� ������ 
	 * <br>
	 * ������������ ����������� ��� ���������� 
	 * */
	public ElementOfOrder(){
	}

	
	public ElementOfOrder(String kod, int quantity){
		this.kod=kod;
		this.quantity=quantity;
	}
	/**
	 * @return ��� ������ � ���������� 
	 */
	public String getKod() {
		return kod;
	}

	/**
	 * @param ��� ������ � ����������
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
