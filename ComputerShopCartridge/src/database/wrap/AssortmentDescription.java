package database.wrap;

import javax.persistence.*;

@Entity
@Table (name="ASSORTMENT_DESCRIPTION")
public class AssortmentDescription {
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator", sequenceName="GEN_ASSORTMENT_DESCRIPTION_ID")
	@Column(name="KOD")
	private int kod;

	@Column(name="KOD_ASSORTMENT")
	private int kodAssortment;
	@Column(name="KOD_ASSORTMENT_TYPE_DESCRIPTION")
	private int kodAssortmentTypeDescription;
	@Column(name="NAME",length=255)
	private String name;
	
	public int getKod() {
		return kod;
	}
	public void setKod(int kod) {
		this.kod = kod;
	}
	public int getKodAssortment() {
		return kodAssortment;
	}
	public void setKodAssortment(int kodAssortment) {
		this.kodAssortment = kodAssortment;
	}
	public int getKodAssortmentTypeDescription() {
		return kodAssortmentTypeDescription;
	}
	public void setKodAssortmentTypeDescription(int kodAssortmentTypeDescription) {
		this.kodAssortmentTypeDescription = kodAssortmentTypeDescription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
