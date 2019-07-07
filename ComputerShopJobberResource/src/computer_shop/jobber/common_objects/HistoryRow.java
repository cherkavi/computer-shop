package computer_shop.jobber.common_objects;

import java.util.Date;

/** одна строка в таблице отображения истории заказов */
public class HistoryRow {
	private String number;
	private Date date;
	private int currency;
	private int quantity;
	private float amount;
	
	public HistoryRow(){
	}
	
	public HistoryRow(String number, Date date, int currency, int quantity, float amount){
		this.number=number;
		this.date=date;
		this.currency=currency;
		this.quantity=quantity;
		this.amount=amount;
	}

	/**
	 * @return номер заказа (номер уникального кода строки из таблицы )
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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

	/**
	 * @return the currency
	 */
	public int getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(int currency) {
		this.currency = currency;
	}
}
