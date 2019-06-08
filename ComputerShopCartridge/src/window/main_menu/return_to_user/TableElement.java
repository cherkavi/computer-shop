package window.main_menu.return_to_user;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Date;

/** объект-строка из таблицы картриджей, которые нужно выдать потребителю */
public class TableElement implements Serializable{
	private final static long serialVersionUID=1L;
	private int id;
	private String customerName;
	private String customerSurname;
	private String vendorName;
	private String modelName;
	private Date timeCreate;
	private Date timeGetToProcess;
	private Date timeReturnFromProcess;
	private Date timeReturnToCustomer;
	private Integer uniqueNumber;
	private String controlNumber;
	private String description;  
	
	/** объект-строка из таблицы картриджей, которые нужно выдать потребителю */
	public TableElement(ResultSet rs){
		try{
			this.id=rs.getInt("id");
			this.customerName=rs.getString("CUSTOMER_NAME");
			this.customerSurname=rs.getString("CUSTOMER_SURNAME");
			this.vendorName=rs.getString("VENDOR_NAME");
			this.modelName=rs.getString("MODEL_NAME");
			this.timeCreate=this.getDateFromResultSet(rs, "TIME_CREATE");
			this.timeGetToProcess=this.getDateFromResultSet(rs, "TIME_GET_TO_PROCESS");
			this.timeReturnFromProcess=this.getDateFromResultSet(rs, "TIME_RETURN_FROM_PROCESS");
			this.timeReturnToCustomer=this.getDateFromResultSet(rs, "TIME_RETURN_TO_CUSTOMER");
			this.uniqueNumber=rs.getInt("UNIQUE_NUMBER");
			this.controlNumber=rs.getString("CONTROL_NUMBER");
			this.description=rs.getString("DESCRIPTION");
		}catch(Exception ex){
			System.err.println("TableElement#constructor Exception: "+ex.getMessage());
		}
	}
	
	private Date getDateFromResultSet(ResultSet rs, String columnName){
		Date returnValue=null;
		try{
			returnValue=new Date(rs.getDate(columnName).getTime());
		}catch(Exception ex){
		}
		return returnValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Date getTimeCreate() {
		return timeCreate;
	}

	public void setTimeCreate(Date timeCreate) {
		this.timeCreate = timeCreate;
	}

	public Date getTimeGetToProcess() {
		return timeGetToProcess;
	}

	public void setTimeGetToProcess(Date timeGetToProcess) {
		this.timeGetToProcess = timeGetToProcess;
	}

	public Date getTimeReturnFromProcess() {
		return timeReturnFromProcess;
	}

	public void setTimeReturnFromProcess(Date timeReturnFromProcess) {
		this.timeReturnFromProcess = timeReturnFromProcess;
	}

	public Date getTimeReturnToCustomer() {
		return timeReturnToCustomer;
	}

	public void setTimeReturnToCustomer(Date timeReturnToCustomer) {
		this.timeReturnToCustomer = timeReturnToCustomer;
	}

	public Integer getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(Integer uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public String getControlNumber() {
		return controlNumber;
	}

	public void setControlNumber(String controlNumber) {
		this.controlNumber = controlNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
