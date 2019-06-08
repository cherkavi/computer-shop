package database.wrap;

import javax.persistence.*;

@Entity
@Table(name = "cartridge_model")
public class CartridgeModel {
	@Id
	@Column(name = "ID")
	private int id;
	@Column(name = "ID_VENDOR")
	private Integer idVendor;
	@Column(name = "NAME", length = 100)
	private String name;
	@Column(name = "PRICE")
	private Float price;
	@Column(name = "FOR_SEND")
	private Integer forSend=1;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the idVendor
	 */
	public Integer getIdVendor() {
		return idVendor;
	}

	/**
	 * @param idVendor
	 *            the idVendor to set
	 */
	public void setIdVendor(Integer idVendor) {
		this.idVendor = idVendor;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getForSend() {
		return forSend;
	}

	public void setForSend(Integer forSend) {
		this.forSend = forSend;
	}
}
