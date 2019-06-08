package window.recognize;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import database.ConnectWrap;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import wicket_extension.panel_link_action.PanelLinkAction;
import wicket_utility.IConnectorAware;
import window.recognize.join.Join;

/** страница которая позволяет получить ассортимент, который не имеет описания */
public class Recognize extends WebPage implements IConnectorAware,IActionExecutor{
	private ArrayList<String> listOfClass=new ArrayList<String>();
	private ArrayList<String> listOfTableClass=new ArrayList<String>();
	private ArrayList<String> renderOfClass=new ArrayList<String>();
	private int dropDownSelection=0;
	
	public Recognize(){
		listOfClass.add("2");
		listOfTableClass.add("mb");
		renderOfClass.add("Материнская плата");
		
		listOfClass.add("3");
		listOfTableClass.add("hdd");
		renderOfClass.add("Жесткий диск");
		
		listOfClass.add("4");
		listOfTableClass.add("memory");
		renderOfClass.add("Модуль памяти");
		
		listOfClass.add("8");
		listOfTableClass.add("processor");
		renderOfClass.add("Процессор ");
		
		listOfClass.add("9");
		listOfTableClass.add("video");
		renderOfClass.add("Видеокарта");
		
		initComponents();
	}
	
	private ElementDataProvider dataProvider=null;
	
	private void initComponents(){
		Form<?> formMain=new Form<Object>("form_select");
		this.add(formMain);
		DropDownChoice<String> selectClass=new DropDownChoice<String>("select_class",new Model<String>(listOfClass.get(0)), listOfClass, new ClassNameRenderer(listOfClass, renderOfClass)){
			private static final long serialVersionUID = 1L;
			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			@Override
			protected void onSelectionChanged(String newSelection) {
				Recognize.this.onSelectionChanged(newSelection);
			}
		};
		formMain.add(selectClass);
		dataProvider=new ElementDataProvider(this, this.listOfClass.get(0),this.listOfTableClass.get(0));
		DefaultDataTable<Serializable> table=new DefaultDataTable<Serializable>("table_result", // уникальный идентификатор формаы 
																	this.getColumns(), // колонки, которые нужно отображать
																	dataProvider, // данные 
																	20);// кол-во на странице
		this.add(table);
	}
	
	@SuppressWarnings("unchecked")
	private IColumn<Serializable>[] getColumns(){
		return new IColumn[]{
			new AbstractColumn<Serializable>(new Model<String>("Наименование")){
				private final static long serialVersionUID=1L;
				@Override
				public void populateItem(Item<ICellPopulator<Serializable>> item,
										 String componentId, 
										 IModel<Serializable> model) {
					item.add(new PanelLinkAction(componentId,
												 Recognize.this,
												 "LINK",
												 (Integer)(((Object[])model.getObject())[0]),
												 (String)(((Object[])model.getObject())[1]), 
												 "link_class",
												 "link_class_mouseover"));
				}
				
			}
		};
	}

	@Override
	public void action(String actionName, Serializable argument) {
		Join join=new Join(this,
						   (Integer)argument,
						   this.dataProvider.getClassTableName(),
						   this.listOfTableClass.get(this.dropDownSelection));
		this.setResponsePage(join);
	}
	
	
	private void onSelectionChanged(String selection){
		//System.out.println("new Selection: "+selection);
		this.dropDownSelection=this.listOfClass.indexOf(selection);
		this.dataProvider.setClass(this.listOfClass.get(this.dropDownSelection), 
								   this.listOfTableClass.get(this.dropDownSelection)
								   );
	}

	@Override
	public ConnectWrap getConnector() {
		return ((UserApplication)this.getApplication()).getConnector();
	}
}

/** класс, который описывает */
class ClassNameRenderer implements IChoiceRenderer<String>{
	private final static long serialVersionUID=1L;
	private ArrayList<String> values;
	private ArrayList<String> renders;
	
	public ClassNameRenderer(ArrayList<String> values, ArrayList<String> renders){
		this.values=values;
		this.renders=renders;
	}
	
	@Override
	public Object getDisplayValue(String arg0) {
		try{
			// получить по значению объект для отображения
			return this.renders.get(this.values.indexOf(arg0));
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public String getIdValue(String arg0, int arg1) {
		//System.out.println("getIdValue: "+arg0+"   Index:"+arg1);
		return arg0;
	}
}

/** объект, который выдает данные */
class ElementDataProvider extends SortableDataProvider<Serializable>{
	private final static long serialVersionUID=1L;
	private IConnectorAware connectorAware;
	private String currentClassId;
	private String currentTableName;
	
	public ElementDataProvider(IConnectorAware connectorAware, String currentClassId, String currentTable){
		this.connectorAware=connectorAware;
		this.currentClassId=currentClassId;
		this.currentTableName=currentTable;
	}
	
	/** установить новый класс */
	public void setClass(String classId, String tableName){
		this.currentClassId=classId;
		this.currentTableName=tableName;
	}
	/** получить уникальный идентификатор класса из базы данных ComputerShop */
	public String getClassId(){
		return currentClassId;
	}
	
	/** получить название таблицы в базе данных Словаря */
	public String getClassTableName(){
		return currentTableName;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<? extends Serializable> iterator(int first, int count) {
		Iterator<? extends Serializable> returnValue=null;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			StringBuffer query=new StringBuffer();
			query.append("select assortment.kod, assortment.name from assortment ");
			query.append("inner join class on class.kod=assortment.class_kod ");
			query.append("left join "+this.currentTableName+" on "+this.currentTableName+".id_assortment=assortment.kod ");
			query.append("where "+this.currentTableName+".name is null and class.kod="+this.currentClassId);
			//System.out.println(query.toString());
			Session session=connector.getSession();
			returnValue=(Iterator<? extends Serializable>)session.createSQLQuery(query.toString()).addScalar("kod").addScalar("name").setFirstResult(first).setMaxResults(count).list().iterator();
		}catch(Exception ex){
			System.err.println("Recognize.ElementDataProvider#iterator Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	@Override
	public IModel<Serializable> model(Serializable element) {
		return new Model<Serializable>(element);
	}

	@Override
	public int size() {
		int returnValue=0;
		StringBuffer query=new StringBuffer();
		query.append("select count(assortment.kod) from assortment ");
		query.append("inner join class on class.kod=assortment.class_kod ");
		query.append("left join "+this.currentTableName+" on "+this.currentTableName+".id_assortment=assortment.kod ");
		query.append("where "+this.currentTableName+".name is null and class.kod="+this.currentClassId);
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery(query.toString());
			rs.next();
			returnValue=rs.getInt(1);
		}catch(Exception ex){
			System.err.println("");
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
}
