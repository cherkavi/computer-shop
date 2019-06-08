package database.wrap;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name="price") 
public class Price {
	@Id
	@Column(name="KOD")
    	private Integer kod;

	@Column(name="PRICE_BUYING")
    	private Float price_buying;

	@Column(name="PRICE")
    	private Float price;

	@Column(name="VALID")
    	private Integer valid;

	@Column(name="NOTE",length=255)
    	private String note;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	@Column(name="NEXT_KOD")
    	private Integer next_kod;

	public Integer getKod() {
		return kod;
	}

	public void setKod(Integer kod) {
		this.kod = kod;
	}

	public Float getPrice_buying() {
		return price_buying;
	}

	public void setPrice_buying(Float priceBuying) {
		price_buying = priceBuying;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}

	public Integer getNext_kod() {
		return next_kod;
	}

	public void setNext_kod(Integer nextKod) {
		next_kod = nextKod;
	}
}
