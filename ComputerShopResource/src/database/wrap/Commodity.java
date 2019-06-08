package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="commodity")
public class Commodity {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;
	@Column(name="QUANTITY")
	private int quantity;
	@Column(name="KOD_OPERATION")
	private Integer kodOperation;
	@Column(name="KOD_ASSORTMENT")
	private Integer kodAssortment;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Integer getKodOperation() {
		return kodOperation;
	}
	public void setKodOperation(Integer kodOperation) {
		this.kodOperation = kodOperation;
	}
	public Integer getKodAssortment() {
		return kodAssortment;
	}
	public void setKodAssortment(Integer kodAssortment) {
		this.kodAssortment = kodAssortment;
	}
	
	
}
