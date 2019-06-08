package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="memory_describe_name")
public class MemoryDescribeName extends BaseDescribeName{
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="NAME")
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
