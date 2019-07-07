package computer_shop.jobber.database.wrap;

import java.util.Date;
import javax.persistence.*;

/** Таблица в базе данных, которая содержит необходимые для работоспособности Properties:
 * <table style="border-style=solid;border-color:gray; border-width:1px;">
 * 	<tr>
 * 		<th style="border-bottom-width:1px; border-bottom-style:solid;border-bottom-color:black;" > Имя свойства </th> 
 * 		<th style="border-bottom-width:1px; border-bottom-style:solid;border-bottom-color:black;" > Значение этого свойства </th>
 * 	</tr>
 * <tr>
 * 		<td> PRICE.URL</td>
 * 		<td> Место, откуда необходимо загружать прайс-лист </td>
 * </tr>
 * <tr>
 * 		<td> PRICE.REPOSITORY</td>
 * 		<td> место где необходимо хранить все загруженные прайс-листы </td>
 * </tr>
 * </table>
 * */
@Entity
@Table(name="j_properties")
public class Properties {
	@Id
	//SequenceGenerator(name="generator", sequenceName="GEN_PROPERTIES_ID")
	//GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	@GeneratedValue
	@Column(name="KOD")
	private int kod;
	@Column(name="PROP_NAME")
	private String name;
	@Column(name="PROP_VALUE")
	private String value;
	@Column(name="DATE_WRITE")
	private Date dateWrite;

	
	public int getKod() {
		return kod;
	}
	public void setKod(int kod) {
		this.kod = kod;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getDateWrite() {
		return dateWrite;
	}
	public void setDateWrite(Date dateWrite) {
		this.dateWrite = dateWrite;
	}
	
	
}
