package computer_shop.jobber.common_objects;

public class RowElement {
	private String kpiKod;
	private String name;
	private Float price;
	private Integer warranty;
	private Integer quantity;

	public RowElement(){
	}
	
	public RowElement(String kpiKod, String name, Float price, Integer warranty, Integer quantity){
		this.kpiKod=kpiKod;
		this.name=name;
		this.price=price;
		this.warranty=warranty;
		this.quantity=quantity;
	}

	public String getKpiKod() {
		return kpiKod;
	}

	public void setKpiKod(String kpiKod) {
		this.kpiKod = kpiKod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getWarranty() {
		return warranty;
	}

	public void setWarranty(Integer warranty) {
		this.warranty = warranty;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
