package database.wrap;
import javax.persistence.*;

@Entity
@Table(name="cartridge_vendor")
public class CartridgeVendor {
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=100)
	private String name;
	@Column(name = "FOR_SEND")
	private Integer forSend=1;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public Integer getForSend() {
		return forSend;
	}
	public void setForSend(Integer forSend) {
		this.forSend = forSend;
	}
	
}
