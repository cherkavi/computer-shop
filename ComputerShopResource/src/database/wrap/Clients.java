package database.wrap;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="clients") 
public class Clients {
	@Id
	@Column(name="KOD")
   	private Integer kod;

	@Column(name="BAR_CODE",length=10)
   	private String bar_code;

	@Column(name="EMAIL",length=20)
   	private String email;

	@Column(name="PHONE",length=20)
    private String phone;

	@Column(name="PHONE_2",length=20)
    	private String phone_2;

	@Column(name="ADDRESS",length=75)
    	private String address;

	@Column(name="NAME",length=20)
    	private String name;

	@Column(name="SURNAME",length=20)
    	private String surname;

	@Column(name="FATHER_NAME",length=20)
    	private String father_name;

	@Column(name="NOTE",length=75)
    	private String note;

	@Column(name="PERCENT")
    	private Float percent;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	public Integer getKod() {
		return kod;
	}

	public void setKod(Integer kod) {
		this.kod = kod;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String barCode) {
		bar_code = barCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone_2() {
		return phone_2;
	}

	public void setPhone_2(String phone_2) {
		this.phone_2 = phone_2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String fatherName) {
		father_name = fatherName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Float getPercent() {
		return percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}
	

}
