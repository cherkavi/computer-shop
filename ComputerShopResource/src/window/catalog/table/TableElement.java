package window.catalog.table;

import java.io.Serializable;
import java.sql.ResultSet;

/** элемент из таблицы для отображения товара  */
public class TableElement implements Serializable{
	private final static long serialVersionUID=1L;
	
	private String name;
	private String note;
	private int warrantyMonth;
	private float price;
	private Integer assortmentKod;
	
	/** элемент из таблицы для отображения товара  */
	public TableElement(){
	}

	/** элемент из таблицы для отображения товара  */
	public TableElement(ResultSet rs, float course){
		try{
			name=rs.getString("NAME");
			//note=rs.getString("NOTE");
			warrantyMonth=rs.getInt("WARRANTY");
			price=rs.getFloat("PRICE_4")*course;
			assortmentKod=rs.getInt("kod");
		}catch(Exception ex){
			System.err.println("TableElement#constructor Exception: "+ex.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getWarrantyMonth() {
		return warrantyMonth;
	}

	public void setWarrantyMonth(Integer warrantyMonth) {
		this.warrantyMonth = warrantyMonth;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getAssortmentKod() {
		return assortmentKod;
	}

	public void setAssortmentKod(Integer assortmentKod) {
		this.assortmentKod = assortmentKod;
	}

	public void setWarrantyMonth(int warrantyMonth) {
		this.warrantyMonth = warrantyMonth;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
