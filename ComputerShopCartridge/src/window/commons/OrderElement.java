package window.commons;

import java.io.Serializable;
import java.util.Date;

/** класс, который содержит всю информацию о заказе */
public class OrderElement implements Serializable{
	private final static long serialVersionUID=1L;
	private Integer id;
	private String controlNumber;
	private String uniqueNumber;
	private Date timeCreate;
	private Date timeGetToProcess;
	private Date timeReturnFromProcess;
	private Date timeReturnToCustomer;
	private String vendor;
	private String model;
	private float price;

	public OrderElement(){
	}
	
	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the timeCreate
	 */
	public Date getTimeCreate() {
		return timeCreate;
	}

	/**
	 * @param timeCreate the timeCreate to set
	 */
	public void setTimeCreate(Date timeCreate) {
		this.timeCreate = timeCreate;
	}

	/**
	 * @return the timeGetToProcess
	 */
	public Date getTimeGetToProcess() {
		return timeGetToProcess;
	}

	/**
	 * @param timeGetToProcess the timeGetToProcess to set
	 */
	public void setTimeGetToProcess(Date timeGetToProcess) {
		this.timeGetToProcess = timeGetToProcess;
	}

	/**
	 * @return the timeReturnFromProcess
	 */
	public Date getTimeReturnFromProcess() {
		return timeReturnFromProcess;
	}

	/**
	 * @param timeReturnFromProcess the timeReturnFromProcess to set
	 */
	public void setTimeReturnFromProcess(Date timeReturnFromProcess) {
		this.timeReturnFromProcess = timeReturnFromProcess;
	}

	/**
	 * @return the timeReturnToCustomer
	 */
	public Date getTimeReturnToCustomer() {
		return timeReturnToCustomer;
	}

	/**
	 * @param timeReturnToCustomer the timeReturnToCustomer to set
	 */
	public void setTimeReturnToCustomer(Date timeReturnToCustomer) {
		this.timeReturnToCustomer = timeReturnToCustomer;
	}

	/**
	 * @return the uniqueNumber
	 */
	public String getUniqueNumber() {
		return uniqueNumber;
	}

	/**
	 * @param uniqueNumber the uniqueNumber to set
	 */
	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	/**
	 * @return the controlNumber
	 */
	public String getControlNumber() {
		return controlNumber;
	}

	/**
	 * @param controlNumber the controlNumber to set
	 */
	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
