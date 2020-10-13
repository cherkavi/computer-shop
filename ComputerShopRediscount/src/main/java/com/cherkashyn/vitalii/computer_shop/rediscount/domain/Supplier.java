package com.cherkashyn.vitalii.computer_shop.rediscount.domain;

// Generated 14.09.2013 14:22:22 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Supplier generated by hbm2java
 */
@Entity
@Table(name = "SUPPLIER")
public class Supplier implements java.io.Serializable {

	private int kod;
	private String name;
	private Set<Assortment> assortments = new HashSet<Assortment>(0);

	public Supplier() {
	}

	public Supplier(int kod) {
		this.kod = kod;
	}

	public Supplier(int kod, String name, Set<Assortment> assortments) {
		this.kod = kod;
		this.name = name;
		this.assortments = assortments;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
	public Set<Assortment> getAssortments() {
		return this.assortments;
	}

	public void setAssortments(Set<Assortment> assortments) {
		this.assortments = assortments;
	}

}