package database.wrap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;
import javax.persistence.*;

@Entity
@Table(name="order_list")
public class OrderList {
	@Id
	@Column(name="ID")
	private int id;
	/** уникальный идентификатор модели картриджа, который заправляется */
	@Column(name="ID_MODEL")
	private Integer idModel;
	/** дата создания данного заказа */
	@Column(name="TIME_CREATE")
	private Date timeCreate;
	/** дата взятия картриджа в процесс заправки заправщиком */
	@Column(name="TIME_GET_TO_PROCESS")
	private Date timeGetToProcess;
	/** дата возврата картриджа заправщиком на склад заправленных картриджей */
	@Column(name="TIME_RETURN_FROM_PROCESS")
	private Date timeReturnFromProcess;
	/** дата возврата данного картриджа клиенту, который заказал услугу заправки */
	@Column(name="TIME_RETURN_TO_CUSTOMER")
	private Date timeReturnToCustomer;
	
	@Column(name="UNIQUE_NUMBER")
	private Integer uniqueNumber;
	
	@Column(name="CONTROL_NUMBER")
	private String controlNumber;
	
	@Column(name="ID_ORDER_GROUP")
	private Integer idOrderGroup;
	
	@Column(name = "FOR_SEND")
	private Integer forSend=1;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the idModel
	 */
	public Integer getIdModel() {
		return idModel;
	}
	/**
	 * @param idModel the idModel to set
	 */
	public void setIdModel(Integer idModel) {
		this.idModel = idModel;
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

	
	public void generateControlNumber(){
        StringBuffer return_value=new StringBuffer();
        Random random=new java.util.Random();
        int temp_value;
        for(int counter=0;counter<9;counter++){
            temp_value=random.nextInt(9);
            return_value.append(temp_value);
        }
        this.controlNumber=return_value.toString();
	}
	
	public void generateNumber(Connection connection){
		if((this.controlNumber==null)||(this.controlNumber.equals(""))){
			this.generateControlNumber();
		}
		
		// generated new this.unique_number
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select gen_id(gen_order_number,1) from rdb$database ");
			rs.next();
			this.uniqueNumber=rs.getInt(1);
		}catch(Exception ex){
			
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
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
	public Integer getIdOrderGroup() {
		return idOrderGroup;
	}
	public void setIdOrderGroup(Integer idGroup) {
		this.idOrderGroup = idGroup;
	}
	public Integer getForSend() {
		return forSend;
	}
	public void setForSend(Integer forSend) {
		this.forSend = forSend;
	}
	
}
