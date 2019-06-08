package database.wrap;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="assortment") 
public class Assortment {
	@Id
	@Column(name="KOD")
    	private Integer kod;

	@Column(name="NAME",length=150)
    	private String name;

	@Column(name="NOTE",length=255)
    	private String note;

	@Column(name="CLASS_KOD")
    	private Integer class_kod;

	@Column(name="PRICE_KOD")
    	private Integer price_kod;

	@Column(name="BAR_CODE",length=30)
    	private String bar_code;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	@Column(name="BAR_CODE_COMPANY",length=30)
    	private String bar_code_company;

	@Column(name="WARRANTY_MONTH")
    	private Integer warranty_month;

	public Integer getKod() {
		return kod;
	}

	public void setKod(Integer kod) {
		this.kod = kod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getClass_kod() {
		return class_kod;
	}

	public void setClass_kod(Integer classKod) {
		class_kod = classKod;
	}

	public Integer getPrice_kod() {
		return price_kod;
	}

	public void setPrice_kod(Integer priceKod) {
		price_kod = priceKod;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String barCode) {
		bar_code = barCode;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}

	public String getBar_code_company() {
		return bar_code_company;
	}

	public void setBar_code_company(String barCodeCompany) {
		bar_code_company = barCodeCompany;
	}

	public Integer getWarranty_month() {
		return warranty_month;
	}

	public void setWarranty_month(Integer warrantyMonth) {
		warranty_month = warrantyMonth;
	}

	
	
}
