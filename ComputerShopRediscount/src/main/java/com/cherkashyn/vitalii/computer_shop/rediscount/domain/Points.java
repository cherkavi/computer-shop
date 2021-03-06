package com.cherkashyn.vitalii.computer_shop.rediscount.domain;

// Generated 14.09.2013 14:22:22 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Points generated by hbm2java
 */
@Entity
@Table(name = "POINTS")
public class Points implements java.io.Serializable {

	private int kod;
	private String name;
	private String address;
	private String note;
	private Short isTrade;
	private String tel;
	private String email;

	public Points() {
	}

	public Points(int kod) {
		this.kod = kod;
	}

	public Points(int kod, String name, String address, String note,
			Short isTrade, String tel, String email) {
		this.kod = kod;
		this.name = name;
		this.address = address;
		this.note = note;
		this.isTrade = isTrade;
		this.tel = tel;
		this.email = email;
	}

	@Id
	@Column(name = "KOD", unique = true, nullable = false)
	public int getKod() {
		return this.kod;
	}

	public void setKod(int kod) {
		this.kod = kod;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "IS_TRADE")
	public Short getIsTrade() {
		return this.isTrade;
	}

	public void setIsTrade(Short isTrade) {
		this.isTrade = isTrade;
	}

	@Column(name = "TEL", length = 40)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "EMAIL", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
