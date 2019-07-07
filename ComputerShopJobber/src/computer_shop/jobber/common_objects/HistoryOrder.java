package computer_shop.jobber.common_objects;

/** объект, который чётко идентефицирует элемент заказа из таблицы Order */
public class HistoryOrder {
	/** уникальный код позиции заказа */
	private int kod;
	/** наименование секции, которой принадлежит товар */
	private String sectionName;
	/** наименование товара*/
	private String name;
	/** кол-во элементов в заказе */
	private int quantity;
	/** цена в заказе */
	private float amount;
	
	/** объект, который чётко идентефицирует элемент заказа из таблицы Order */
	public HistoryOrder(){
	}
	
	/** объект, который чётко идентефицирует элемент заказа из таблицы Order 
	 * @param kod - код записи в таблице Order
	 * @param sectionName - имя секции, которой принадлежит данная запись
	 * @param name - наименование позиции
	 * @param quantity - кол-во в заказе 
	 * @param amount - цена позиции 
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
