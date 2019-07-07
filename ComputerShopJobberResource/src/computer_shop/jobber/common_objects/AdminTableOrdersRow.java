package computer_shop.jobber.common_objects;

import java.util.Date;

/** объект, который содержит необходимые данные для отображения одной строки в таблице заказов для администратора */
public class AdminTableOrdersRow {
	/** уникальный код для данного заказа в базе данных - уникальный номер заказа */
	private int kodOrder;
	/** имя оптовика */
	private String jobberName;
	/** фамилия оптовика */
	private String jobberSurname;
	/** время записи/получения данного заказа в базу данных */
	private Date timeWrite;
	/** 
	 * <li> true - данный заказ в USD  </li>
	 * <li> false - данный заказ в ГРН </li>
	 * */
	private boolean isCurrency;
	/** кол-во элементов в заказе */
	private int quantity;
	/** сумма заказа */
	private float amount;
	/** статус заказа:
	 * <li> <b>0</b> - Новый</li>
	 * <li> <b>1</b> - Отправленный поставщику</li>
	 * <li> <b>2</b> - Полученный от поставщика</li>
	 * <li> <b>3</b> - Доставленный оптовику</li>
	 * */
	private int status;
	
	/** объект, который содержит необходимые данные для отображения одной строки в таблице заказов для администратора 
	 * <br>
	 * <b> пустой коснтруктор необходим для XFire</b>
	 * */
	public AdminTableOrdersRow(){
	}


	public int getKodOrder() {
		return kodOrder;
	}

	public void setKodOrder(int kodOrder) {
		this.kodOrder = kodOrder;
	}

	public String getJobberName() {
		return jobberName;
	}

	public void setJobberName(String jobberName) {
		this.jobberName = jobberName;
	}

	public String getJobberSurname() {
		return jobberSurname;
	}

	public void setJobberSurname(String jobberSurname) {
		this.jobberSurname = jobberSurname;
	}

	public Date getTimeWrite() {
		return timeWrite;
	}

	public void setTimeWrite(Date timeWrite) {
		this.timeWrite = timeWrite;
	}

	public boolean isCurrency() {
		return isCurrency;
	}

	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
