package window.catalog.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import wicket_extension.panel_link_action.PanelLinkAction;
import wicket_utility.IConnectorAware;
import database.ConnectWrap;
import database.wrap.List_section;

/** панель-заголовок дл€ базовой страницы */
public class Table extends Panel implements IConnectorAware{
	private final static long serialVersionUID=1L;
	private Model<String> model;
	/** таблица, котора€ состоит из элементов дл€ отображени€ пользователю  */
	private DefaultDataTable<TableElement> dataTable;
	private CommodityDataProvider dataProvider;
	private int defaultClassId=29;
	private Integer classId=defaultClassId;
	private int linePerPage=20;
	private IActionExecutor executor;
	
	public Table(String id,IActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}
	
	/** первоначальна€ инициализаци€ компонентов */
	private void initComponents(){
		this.model=new Model<String>("");
		this.add(new Label("label",this.model));
		
		dataProvider=new CommodityDataProvider(classId,this);
		dataTable=new DefaultDataTable<TableElement>("table_main",
													 this.getColumns(),
													 dataProvider,
													 this.linePerPage);
		
		this.add(dataTable);
	}
	
	/** установить кол-во элементов на странице */
	public void setLinePerPage(int linePerPage){
		this.linePerPage=linePerPage;
		dataTable.setRowsPerPage(linePerPage);
	}
	
	public void showClass(Integer classId){
		if(classId==null){
			classId=this.defaultClassId;
		}
		this.classId=classId;
		List_section currentClass=this.getClassById(classId);
		this.model.setObject(currentClass.getName());
		this.dataProvider.setClassId(classId);
	}
	
	private List_section getClassById(Integer classId){
		List_section returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			
			returnValue=(List_section)connector.getSession().get(List_section.class,classId);
		}catch(Exception ex){
			System.err.println("Table#getListOfClass Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
		
	}

	@Override
	public ConnectWrap getConnector() {
		return ((UserApplication)this.getApplication()).getConnector();
	}

	private final DecimalFormat priceFormat=new DecimalFormat("#.00");
	
	@SuppressWarnings("unchecked")
	private IColumn<TableElement>[] getColumns(){
		return new IColumn[]{
				new AbstractColumn(new Model<String>("Ќаименование")){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, 
											 String componentId, 
											 IModel model) {
						TableElement element=(TableElement)model.getObject();
						cellItem.add(new PanelLinkAction(componentId, 
														 Table.this.executor, 
														 "COMMODITY",
														 new Integer(element.getAssortmentKod()),
														 element.getName(),
														 null));
					}
					public String getSortProperty() {
						return "name";
					};
				},
				new AbstractColumn(new Model<String>("÷ена")){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, 
											 String componentId, 
											 IModel model) {
						TableElement element=(TableElement)model.getObject();
						String price="";
						try{
							price=priceFormat.format(element.getPrice());
						}catch(Exception ex){};
						cellItem.add(new PanelLinkAction(componentId, 
								 Table.this.executor, 
								 "COMMODITY",
								 new Integer(element.getAssortmentKod()),
								 price,
								 null));
						cellItem.add(new SimpleAttributeModifier("align","right"));
					}
					public String getSortProperty() {
						return "price_4";
					};
					@Override
					public String getCssClass() {
						return "price";
					}
				},
				new AbstractColumn(new Model<String>("√аранти€")){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, 
											 String componentId, 
											 IModel model) {
						TableElement element=(TableElement)model.getObject();
						cellItem.add(new PanelLinkAction(componentId, 
								 Table.this.executor, 
								 "COMMODITY",
								 new Integer(element.getAssortmentKod()),
								 element.getWarrantyMonth().toString(),
								 null));
						cellItem.add(new SimpleAttributeModifier("align","right"));
					}
					public String getSortProperty() {
						return "warranty";
					};
					public String getCssClass() {
						return "warranty";
					};
				}
		};
	}
}

/** Data Provider дл€ таблицы */
class CommodityDataProvider extends SortableDataProvider<TableElement>{
	private final static long serialVersionUID=1L;
	private Integer idSection;
	private IConnectorAware connectorAware;
	
	public CommodityDataProvider(Integer idSection,IConnectorAware connectorAware){
		this.idSection=idSection;
		this.connectorAware=connectorAware;
	}

	public void setClassId(Integer idSection) {
		this.idSection=idSection;
	}

	@Override
	public Iterator<? extends TableElement> iterator(int first, int count) {
		ArrayList<TableElement> returnValue=new ArrayList<TableElement>();
		ConnectWrap connector=this.connectorAware.getConnector();
		// получить курс валют
		float course=getCourse(connector);
		try{
			Connection connection=connector.getConnection();
			//System.out.println("Sort: "+this.getSort());
			String query=null;
			if(this.getSort()!=null){
				//query="select first "+count+" skip "+first+" commodity.* from commodity where commodity.id_section=? order by "+this.getSort().getProperty();
				query="select kod, kod_section, name, price_4 price_4, warranty from j_list where j_list.kod_section=? order by "+this.getSort().getProperty()+" limit "+first+", "+count;
			}else{
				//query="select first "+count+" skip "+first+" commodity.* from commodity where commodity.id_section=? ";
				query="select kod, kod_section, name, price_4 price_4, warranty from j_list where j_list.kod_section=? limit "+first+", "+count;
			}
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setInt(1, idSection);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				//returnValue.add(new TableElement(rs,course));
				returnValue.add(new TableElement(rs,course));
			}
		}catch(Exception ex){
			System.err.println("Table#iterator Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue.iterator();
	}

	/** получить курс валют по сегодн€шней дате*/
	protected float getCourse(ConnectWrap connector) {
		float returnValue=0;
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select course.currency_value from course where course.date_set<='now' order by course.kod desc limit 1");
			if(rs.next()){
				returnValue=rs.getFloat(1);
			}
		}catch(Exception ex){
			System.err.println("Table#getCourse Exception ex: "+ex.getMessage());
		}
		return returnValue;
	}
	
	@Override
	public IModel<TableElement> model(TableElement element) {
		return new Model<TableElement>(element);
	}

	@Override
	public int size() {
		int returnValue=0;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			Connection connection=connector.getConnection();
			// String query="SELECT count(commodity.id) from commodity inner join assortment on commodity.kod_assortment=assortment.kod inner join price on price.kod=assortment.price_kod where assortment.class_kod=?";
			String query="select count(j_list.kod) from j_list where j_list.kod_section=?";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setInt(1, idSection);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=rs.getInt(1);
			}
		}catch(Exception ex){
			System.err.println("Table#size Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
}

