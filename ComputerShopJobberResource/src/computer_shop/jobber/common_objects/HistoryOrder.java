package computer_shop.jobber.common_objects;

/** ������, ������� ����� �������������� ������� ������ �� ������� Order */
public class HistoryOrder {
	/** ���������� ��� ������� ������ */
	private int kod;
	/** ������������ ������, ������� ����������� ����� */
	private String sectionName;
	/** ������������ ������*/
	private String name;
	/** ���-�� ��������� � ������ */
	private int quantity;
	/** ���� � ������ */
	private float amount;
	
	/** ������, ������� ����� �������������� ������� ������ �� ������� Order */
	public HistoryOrder(){
	}
	
	/** ������, ������� ����� �������������� ������� ������ �� ������� Order 
	 * @param kod - ��� ������ � ������� Order
	 * @param sectionName - ��� ������, ������� ����������� ������ ������
	 * @param name - ������������ �������
	 * @param quantity - ���-�� � ������ 
	 * @param amount - ���� ������� 
	 */
	public HistoryOrder(int kod, String sectionName, String name, int quantity, float amount){
		this.kod=kod;
		this.sectionName=sectionName;
		this.name=name;
		this.quantity=quantity;
		this.amount=amount;
	}

	/**
	 * @return the kod
	 */
	public int getKod() {
		return kod;
	}

	/**
	 * @param kod the kod to set
	 */
	public void setKod(int kod) {
		this.kod = kod;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
}
