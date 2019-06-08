package database.wrap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name="j_jobber_orders") 
public class Jobber_orders implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	@Id
	@Column(name="KOD")
	//SequenceGenerator(name="generator",sequenceName="GEN_JOBBER_ORDERS_ID")
	//GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
    	private Integer kod;

	@Column(name="KOD_JOBBER")
    	private Integer kod_jobber;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	@Column(name="IS_CURRENCY")
		private Integer isCurrency;
	@Column(name="KOD_ORDER_STATUS")
	private Integer kodOrderStatus;
	
	/**
	 * @return the kod
	 */
	public Integer getKod() {
		return kod;
	}

	/**
	 * @param kod the kod to set
	 */
	public void setKod(Integer kod) {
		this.kod = kod;
	}

	/**
	 * @return the kod_jobber
	 */
	public Integer getKod_jobber() {
		return kod_jobber;
	}

	/**
	 * @param kodJobber the kod_jobber to set
	 */
	public void setKod_jobber(Integer kodJobber) {
		kod_jobber = kodJobber;
	}

	/**
	 * @return the date_write
	 */
	public Date getDate_write() {
		return date_write;
	}

	/**
	 * @param dateWrite the date_write to set
	 */
	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}

	/**
	 * @return the isCurrency
	 */
	public Integer getIsCurrency() {
		return isCurrency;
	}

	/**
	 * @param isCurrency the isCurrency to set
	 */
	public void setIsCurrency(Integer isCurrency) {
		this.isCurrency = isCurrency;
	}

	public Integer getKodOrderStatus() {
		return kodOrderStatus;
	}

	public void setKodOrderStatus(Integer kodOrderStatus) {
		this.kodOrderStatus = kodOrderStatus;
	}
	
}
