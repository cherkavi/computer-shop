package database.wrap;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="customer")
public class Customer {
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="SURNAME")
	private String surname;
	@Column(name="NAME")
	private String name;
	@Column(name="TIME_CREATE")
	private Date timeCreate;
	@Column(name = "FOR_SEND")
	private Integer forSend=1;
	
	public Customer(){
	}
	
	public Customer(String surname2, String name2) {
		this.surname=surname2;
		this.name=name2;
		this.timeCreate=new Date();
	}
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
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
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

	public Integer getForSend() {
		return forSend;
	}

	public void setForSend(Integer forSend) {
		this.forSend = forSend;
	}
	
	
	
}
