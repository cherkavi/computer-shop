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
@Table(name="j_list_properties") 
public class List_properties implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	@Id
	@Column(name="KOD")
	// SequenceGenerator(name="generator",sequenceName="GEN_LIST_PROPERTIES_ID")
	//GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
    	private Integer kod;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	@Column(name="COURSE_CASH")
    	private Float course_cash;

	@Column(name="COURSE_ACCOUNT")
    	private Float course_account;

	@Column(name="COURSE_JOBBER_CASH")
    	private Float course_jobber_cash;

	@Column(name="COURSE_JOBBER_ACCOUNT")
    	private Float course_jobber_account;

	@Column(name="LOAD_PERCENT")
	private Integer loadPercent;
	
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
	 * @return the course_cash
	 */
	public Float getCourse_cash() {
		return course_cash;
	}

	/**
	 * @param courseCash the course_cash to set
	 */
	public void setCourse_cash(Float courseCash) {
		course_cash = courseCash;
	}

	/**
	 * @return the course_account
	 */
	public Float getCourse_account() {
		return course_account;
	}

	/**
	 * @param courseAccount the course_account to set
	 */
	public void setCourse_account(Float courseAccount) {
		course_account = courseAccount;
	}

	/**
	 * @return the course_jobber_cash
	 */
	public Float getCourse_jobber_cash() {
		return course_jobber_cash;
	}

	/**
	 * @param courseJobberCash the course_jobber_cash to set
	 */
	public void setCourse_jobber_cash(Float courseJobberCash) {
		course_jobber_cash = courseJobberCash;
	}

	/**
	 * @return the course_jobber_account
	 */
	public Float getCourse_jobber_account() {
		return course_jobber_account;
	}

	/**
	 * @param courseJobberAccount the course_jobber_account to set
	 */
	public void setCourse_jobber_account(Float courseJobberAccount) {
		course_jobber_account = courseJobberAccount;
	}


	public Integer getLoadPercent() {
		return loadPercent;
	}

	public void setLoadPercent(Integer loadPercent) {
		this.loadPercent = loadPercent;
	}
	
}
