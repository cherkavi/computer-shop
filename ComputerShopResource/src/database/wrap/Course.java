package database.wrap;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name="course") 
public class Course {
	@Id
	@Column(name="KOD")
    	private Integer kod;

	@Column(name="CURRENCY_VALUE")
    	private Float currency_value;

	@Column(name="DATE_SET")
    	private Date date_set;

	@Column(name="DATE_WRITE")
    	private Date date_write;

	public Integer getKod() {
		return kod;
	}

	public void setKod(Integer kod) {
		this.kod = kod;
	}

	public Float getCurrency_value() {
		return currency_value;
	}

	public void setCurrency_value(Float currencyValue) {
		currency_value = currencyValue;
	}

	public Date getDate_set() {
		return date_set;
	}

	public void setDate_set(Date dateSet) {
		date_set = dateSet;
	}

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}

	
}
