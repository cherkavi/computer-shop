package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="order_group")
public class OrderGroup {
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="ID_CUSTOMER")
	private Integer idCustomer;
	@Column(name="DESCRIPTION",length=255)
	private String description;
	@Column(name = "FOR_SEND")
	private Integer forSend=1;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(Integer idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getForSend() {
		return forSend;
	}
	public void setForSend(Integer forSend) {
		this.forSend = forSend;
	}
	
}
