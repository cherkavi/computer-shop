package database.wrap;
import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="j_list_section")
public class List_section implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	//SequenceGenerator(name="generator",sequenceName="GEN_LIST_SECTION_ID")
	// GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
	@Column(name="KOD")
	private int kod;
	@Column(name="NAME")
	private String name;
	@Column(name="KOD_PARENT")
	private Integer kod_parent;
	/**
	 * @return the kod
	 */
	public int getKod() {
		return kod;
	}
	/**
	 * @param kod the kod to set
	 */
	public void setKod(int kod) {
		this.kod = kod;
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
	/**
	 * @return the kod_parent
	 */
	public Integer getKod_parent() {
		return kod_parent;
	}
	/**
	 * @param kodParent the kod_parent to set
	 */
	public void setKod_parent(Integer kodParent) {
		kod_parent = kodParent;
	}
	
	
}
