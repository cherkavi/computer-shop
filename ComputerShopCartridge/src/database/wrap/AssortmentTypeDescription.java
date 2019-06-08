package database.wrap;

import javax.persistence.*;

@Entity
@Table(name="ASSORTMENT_TYPE_DESCRIPTION")
public class AssortmentTypeDescription {
	@Id
	@SequenceGenerator(name="generator", sequenceName="GEN_ASSORTMENT_TYPE_DESC_ID" )
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	private int kod;
	
	@Column(name="KOD_ASSORTMENT_TYPE")
	private int kodAssortmentType;
	@Column(name="NAME",length=255)
	private String name;
	@Column(name="KOD_OLD")
	private int kodOld;
	@Column(name="ASSEMBLY_EDIT_TYPE")
	private int assemblyEditType;
	
	
	public int getKod() {
		return kod;
	}
	public void setKod(int kod) {
		this.kod = kod;
	}
	public int getKodAssortmentType() {
		return kodAssortmentType;
	}
	public void setKodAssortmentType(int kodAssortmentType) {
		this.kodAssortmentType = kodAssortmentType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getKodOld() {
		return kodOld;
	}
	public void setKodOld(int kodOld) {
		this.kodOld = kodOld;
	}
	public int getAssemblyEditType() {
		return assemblyEditType;
	}
	public void setAssemblyEditType(int assemblyEditType) {
		this.assemblyEditType = assemblyEditType;
	}
	
	
}
