package com.cherkashyn.vitalii.computer_shop.rediscount.domain;

// Generated 14.09.2013 14:22:22 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CommodityPaper generated by hbm2java
 */
@Entity
@Table(name = "COMMODITY_PAPER")
public class CommodityPaper implements java.io.Serializable {

	private int id;
	private Integer idCommodity;
	private Short showPaperWarranty;
	private Short showPaperCommodity;

	public CommodityPaper() {
	}

	public CommodityPaper(int id) {
		this.id = id;
	}

	public CommodityPaper(int id, Integer idCommodity, Short showPaperWarranty,
			Short showPaperCommodity) {
		this.id = id;
		this.idCommodity = idCommodity;
		this.showPaperWarranty = showPaperWarranty;
		this.showPaperCommodity = showPaperCommodity;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "ID_COMMODITY")
	public Integer getIdCommodity() {
		return this.idCommodity;
	}

	public void setIdCommodity(Integer idCommodity) {
		this.idCommodity = idCommodity;
	}

	@Column(name = "SHOW_PAPER_WARRANTY")
	public Short getShowPaperWarranty() {
		return this.showPaperWarranty;
	}

	public void setShowPaperWarranty(Short showPaperWarranty) {
		this.showPaperWarranty = showPaperWarranty;
	}

	@Column(name = "SHOW_PAPER_COMMODITY")
	public Short getShowPaperCommodity() {
		return this.showPaperCommodity;
	}

	public void setShowPaperCommodity(Short showPaperCommodity) {
		this.showPaperCommodity = showPaperCommodity;
	}

}
