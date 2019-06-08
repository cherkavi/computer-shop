package window.main_menu.create_order.find_user.finded_customer;

import java.io.Serializable;

public class FindedCustomerBean implements Serializable{
	private final static long serialVersionUID=1L;
	private Integer customerId;
	private String customerName;
	private String customerSurname;

	/** ƒанные дл€ строки 
	 * @param customerId - уникальный идентификатор 
	 * @param customerName - им€ пользовател€
	 * @param customerSurname - фамили€ пользовател€
	 */
	public FindedCustomerBean(Integer customerId, String customerName, String customerSurname){
		this.customerId=customerId;
		this.customerName=customerName;
		this.customerSurname=customerSurname;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerSurname() {
		return customerSurname;
	}

	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}
	
	
}
