package computer_shop.jobber.database.wrap;
import javax.persistence.*;

/** класс-обертка для заказов, которые делает оптовик; */
@Entity
@Table(name="j_orders")
public class Orders {
	@Id
	//SequenceGenerator(name="generator",sequenceName="GEN_ORDERS_ID")
	//GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
	@Column(name="KOD")
	private int kod;
	@Column(name="KOD_JOBBER_ORDERS")
	private Integer kod_jobber_orders;
	@Column(name="KOD_KPI")
	private String kod_kpi;
	@Column(name="QUANTITY")
	private Integer quantity;
	@Column(name="AMOUNT")
	private Float amount;
	
	public int getKod() {
		return kod;
	}
	public void setKod(int kod) {
		this.kod = kod;
	}
	public Integer getKod_jobber_orders() {
		return kod_jobber_orders;
	}
	public void setKod_jobber_orders(Integer kodJobberOrders) {
		kod_jobber_orders = kodJobberOrders;
	}
	public String getKod_kpi() {
		return kod_kpi;
	}
	public void setKod_kpi(String kodKpi) {
		kod_kpi = kodKpi;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
