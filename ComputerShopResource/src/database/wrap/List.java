package database.wrap;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="j_list") 
public class List implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	@Column(name="KOD")
	@SequenceGenerator(name="generator",sequenceName="GEN_LIST_ID")
	// GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
    	private Integer kod;

	@Column(name="KOD_KPI")
    	private String kod_kpi;

	@Column(name="KOD_PRODUCER",length=100)
    	private String kod_producer;

	@Column(name="KOD_SECTION")
		private Integer kod_section;
	
	@Column(name="NAME",length=255)
    	private String name;

	@Column(name="PRICE_1")
    	private Float price_1;

	@Column(name="PRICE_2")
    	private Float price_2;

	@Column(name="PRICE_3")
    	private Float price_3;

	@Column(name="PRICE_4")
    	private Float price_4;

	@Column(name="WARRANTY")
    	private Integer warranty;

	@Column(name="STORE")
    	private Integer store;

	/**
	 * @return the kod
	 */
	public Integer getKod() {
		return kod;
	}

	/**
	 * @param kod the kod to set
	 */
	public void setKod(Integer kod) {
		this.kod = kod;
	}

	/**
	 * @return the kod_kpi
	 */
	public String getKod_kpi() {
		return kod_kpi;
	}

	/**
	 * @param kodKpi the kod_kpi to set
	 */
	public void setKod_kpi(String kodKpi) {
		kod_kpi = kodKpi;
	}

	/**
	 * @return the kod_producer
	 */
	public String getKod_producer() {
		return kod_producer;
	}

	/**
	 * @param kodProducer the kod_producer to set
	 */
	public void setKod_producer(String kodProducer) {
		kod_producer = kodProducer;
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
	 * @return the price_1
	 */
	public Float getPrice_1() {
		return price_1;
	}

	/**
	 * @param price_1 the price_1 to set
	 */
	public void setPrice_1(Float price_1) {
		this.price_1 = price_1;
	}

	/**
	 * @return the price_2
	 */
	public Float getPrice_2() {
		return price_2;
	}

	/**
	 * @param price_2 the price_2 to set
	 */
	public void setPrice_2(Float price_2) {
		this.price_2 = price_2;
	}

	/**
	 * @return the price_3
	 */
	public Float getPrice_3() {
		return price_3;
	}

	/**
	 * @param price_3 the price_3 to set
	 */
	public void setPrice_3(Float price_3) {
		this.price_3 = price_3;
	}

	/**
	 * @return the price_4
	 */
	public Float getPrice_4() {
		return price_4;
	}

	/**
	 * @param price_4 the price_4 to set
	 */
	public void setPrice_4(Float price_4) {
		this.price_4 = price_4;
	}

	/**
	 * @return the warranty
	 */
	public Integer getWarranty() {
		return warranty;
	}

	/**
	 * @param warranty the warranty to set
	 */
	public void setWarranty(Integer warranty) {
		this.warranty = warranty;
	}

	/**
	 * @return the store
	 */
	public Integer getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(Integer store) {
		this.store = store;
	}

	/**
	 * @return the kod_section
	 */
	public Integer getKod_section() {
		return kod_section;
	}

	/**
	 * @param kodSection the kod_section to set
	 */
	public void setKod_section(Integer kodSection) {
		kod_section = kodSection;
	}

	
	
}
