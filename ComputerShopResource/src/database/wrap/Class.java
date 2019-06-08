package database.wrap;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name="class") 
public class Class {
	@Id
	@Column(name="KOD")
    private Integer kod;

	@Column(name="NAME",length=255)
    private String name;

	@Column(name="NOTE",length=255)
    private String note;

	@Column(name="DATE_WRITE")
    private Date date_write;

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

	public Date getDate_write() {
		return date_write;
	}

	public void setDate_write(Date dateWrite) {
		date_write = dateWrite;
	}
	

}
