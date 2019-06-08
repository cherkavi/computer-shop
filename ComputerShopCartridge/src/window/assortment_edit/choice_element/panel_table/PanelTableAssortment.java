package window.assortment_edit.choice_element.panel_table;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import window.Application;
import window.assortment_edit.edit_element.EditElement;
import database.ConnectWrap;

/** панель, которая отображает результаты поисков по таблице ассортимент */
public class PanelTableAssortment extends Panel{
	private final static long serialVersionUID=1L;
	private Model<ArrayList<TableAssortmentElement>> listModel=new Model<ArrayList<TableAssortmentElement>>();
	/** панель, которая отображает результаты поисков по таблице ассортимент 
	 * @param id -уникальный идентификатор
	 * @param filterName - фильтр имени 
	 * @param filterBarCode - фильтр BarCode
	 * @param filterAssemlby - фильтр  
	 */
	public PanelTableAssortment(String id, 
								String filterName, 
								String filterBarCode,
								boolean filterNotAssembly){
		super(id);
		listModel.setObject(getResultByFilter(filterName, filterBarCode, filterNotAssembly));
		this.add(new ListView<TableAssortmentElement>("result_list", listModel){
			private final static long serialVersionUID=1L;
			
			@Override
			protected void populateItem(ListItem<TableAssortmentElement> item) {
				final TableAssortmentElement element=item.getModelObject();
				item.add(new Label("class_name",element.getClassName()));
				Link<?> linkName=new Link<Object>("link_name"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onAssortment(element);
					}
				};
				item.add(linkName);
				linkName.add(new Label("name",element.getName()));
				
				item.add(new Label("price",Float.toString(element.getPrice())));
			}
		});
	}
	
	/** реакция на нажатие ссылки выбора ассортимента  */
	private void onAssortment(TableAssortmentElement element){
		if(element!=null){
			try{
				this.setResponsePage(new EditElement(element.getId()));
			}catch(Exception ex){};
		}
	}
	
	// private final static String query="select assortment.kod kod, class.name class_name , assortment.name name , price.price price  from assortment inner join class on assortment.class_kod=class.kod left join price on price.kod=assortment.price_kod \n  where assortment.name like ? \n and assortment.bar_code_company like ? \n order by assortment.class_kod, assortment.kod ";
	
	/** получить данные на основании фильтров  
	 * @param filterName - имя элемента
	 * @param filterBarCode - BarCode элемента 
	 * @param filterNotAssembly - отображать только те, которые не имеют ссылку на таблицу сборки
	 * @return
	 */
	private ArrayList<TableAssortmentElement> getResultByFilter(
																String filterName, 
																String filterBarCode,
																boolean filterNotAssembly
																) {
		ArrayList<TableAssortmentElement> returnValue=new ArrayList<TableAssortmentElement>();
		ConnectWrap connector=this.getConnector();
		try{
			if((filterName==null)&&(filterBarCode==null)){
				return returnValue;
			}
			StringBuffer query=new StringBuffer();
			query.append(" SELECT distinct assortment.kod kod, class.name class_name, assortment.name name, price.price price \n");
			query.append(" FROM assortment \n");
			query.append(" inner join class on class.kod=assortment.class_kod \n");
			query.append(" inner join price on price.kod=assortment.price_kod \n");

			if(filterBarCode!=null){
				if(filterBarCode.trim().length()>0){
					String currentValue=filterBarCode.trim().replaceAll("'", "''");
					if(currentValue.substring(0,1).equals("0")){
						currentValue=currentValue.substring(1);
					}
					query.append(" inner join commodity on assortment.kod=commodity.assortment_kod \n");
					query.append(" inner join serial on commodity.serial_kod=serial.kod and serial.number like '%"+currentValue+"%' \n ");
				}
			}
			StringBuffer whereQuery=new StringBuffer();
			
			if(filterName!=null){
				if(whereQuery.length()>0){
					whereQuery.append(" and ");
				};
				whereQuery.append(" rupper(assortment.name) like '%"+filterName.trim().toUpperCase().replaceAll("'", "''")+"%' \n");
				whereQuery.append(" and assortment.not_assembly is null ");
			}else{
				if(whereQuery.length()>0){
					whereQuery.append(" and ");
				};
				query.append(" assortment.not_assembly is null ");
			}

			if(filterNotAssembly==true){
				if(whereQuery.length()>0){
					whereQuery.append(" and ");
				}
				whereQuery.append(" (select count(*) from assortment_description where assortment_description.kod_assortment=assortment.kod)=0 ");
			}
			if(whereQuery.length()>0){
				query.append(" where "+whereQuery.toString());
			}
			query.append(" order by assortment.kod");
			
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new TableAssortmentElement(rs));
			}
		}catch(Exception ex){
			System.err.println("PanelTableAssortment#getResultByFilter Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	/** установить фильтр на таблицу */
	public void setFilter(String filterName, String filterBarCode,boolean showNotAssembly){
		this.listModel.setObject(this.getResultByFilter(filterName, filterBarCode,showNotAssembly)); 
	}
	
	/** получить соединение с базой данных */
	private ConnectWrap getConnector(){
		return ((Application)this.getApplication()).getConnectorToServer();
	}
}


/** элемент из таблицы  */
class TableAssortmentElement implements Serializable{
	private final static long serialVersionUID=1L;
	private int id;
	private String className;
	private String name;
	private float price;
	
	/** элемент из таблицы  */
	public TableAssortmentElement(int id, String className, String name, float price ){
		this.id=id;
		this.className=className;
		this.name=name;
		this.price=price;
	}

	/** элемент из таблицы  */
	public TableAssortmentElement(ResultSet rs) throws SQLException {
		this.id=rs.getInt("kod");
		this.className=rs.getString("class_name");
		this.name=rs.getString("name");
		this.price=rs.getFloat("price");
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}