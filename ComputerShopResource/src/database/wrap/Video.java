package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="video")
public class Video extends Base{
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=255)
	private String name;

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
	
	
}
