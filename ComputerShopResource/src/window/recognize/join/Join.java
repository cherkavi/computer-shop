package window.recognize.join;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import database.ConnectWrap;
import database.wrap.Assortment;
import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import wicket_extension.panel_link_action.PanelLinkAction;
import wicket_utility.IConnectorAware;
import window.recognize.confirm.Confirm;

/** страница которая позволяет получить ассортимент, который не имеет описания */
public class Join extends WebPage implements IConnectorAware,IActionExecutor{
	/** страница, на которую следует передать управление */
	private WebPage pageForReturn;
	/** таблица, в которой следует производить поиск по названию */
	private String tableSearch;
	private Integer assortmentKod;
	private Model<ArrayList<ResultElement>> listModel=null;
	private Model<String> modelEditSearch;
	/** код класса, по которому нужно производить поиск соответствия */
	private String nameTable;
	
	public Join(WebPage pageForReturn, Integer assortmentKod, String tableSearch, String nameTable){
		this.pageForReturn=pageForReturn;
		this.assortmentKod=assortmentKod;
		this.tableSearch=tableSearch;
		this.nameTable=nameTable;
		initComponents();
		
	}
	
	private void initComponents(){
		// получить код по данному ассортименту 
		Assortment assortment=this.getAssortment(this.assortmentKod);
		
		this.add(new Label("computer_shop_name",assortment.getName()));
		
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		modelEditSearch=new Model<String>(assortment.getName());
		TextField<String> editSearch=new TextField<String>("edit_search",modelEditSearch);
		formMain.add(editSearch);
		
		Button buttonSearch=new Button("button_search"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				onButtonSearch();
			}
		};
		buttonSearch.add(new SimpleAttributeModifier("value",this.getString("button_search_caption")));
		formMain.add(buttonSearch);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("button_cancel_caption")));
		formMain.add(buttonCancel);
		
		listModel=new Model<ArrayList<ResultElement>>();
		listModel.setObject(this.getListOfElement(this.modelEditSearch.getObject()));
		ListView<ResultElement> listOfResult=new ListView<ResultElement>("list_of_result",listModel){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<ResultElement> item) {
				ResultElement modelObject=(ResultElement)item.getModelObject();
				item.add(new PanelLinkAction("search_element", Join.this, "LINK", new Integer(modelObject.getId()), modelObject.getName(), "link_class","link_class_mouseover"));
			}
		};
		this.add(listOfResult);
	}
	/** получить список элементов, на основании введенного значения в поле */
	private ArrayList<ResultElement> getListOfElement(String findString){
		ArrayList<ResultElement> returnValue=new ArrayList<ResultElement>();
		StringTokenizer token=new StringTokenizer(findString," ");
		// создать запрос 
		StringBuffer query=new StringBuffer();
		query.append("select id,name from "+this.nameTable+" where \n");
		StringBuffer where=new StringBuffer();
		while(token.hasMoreTokens()){
			if(where.length()>0){
				where.append("\n and ");
			};
			where.append(" name like '%"+token.nextToken().trim().replaceAll("'", "''")+"%'");
		}
		query.append(where);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery(query.toString());
			while(rs.next()){
				returnValue.add(new ResultElement(rs.getInt("id"),rs.getString("name")));
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("Join#getListOfElement Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	@Override
	public void action(String actionName, Serializable argument) {
		Confirm confirm=new Confirm(this.pageForReturn,this, this.assortmentKod,this.tableSearch,(Integer)argument);
		this.setResponsePage(confirm);
	}
	
	private void onButtonSearch(){
		this.listModel.setObject(getListOfElement(this.modelEditSearch.getObject()));
	}
	
	private void onButtonCancel(){
		this.setResponsePage(this.pageForReturn);
	}
	
	@Override
	public ConnectWrap getConnector() {
		return ((UserApplication)this.getApplication()).getConnector();
	}
	
	private Assortment getAssortment(Integer kod){
		Assortment returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			returnValue=(Assortment)connector.getSession().get(Assortment.class, kod);
		}catch(Exception ex){
			System.err.println("Join#getAssortment Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
}
/** элемент, который идентифицирует позицию найденного элемента */
class ResultElement implements Serializable{
	private final static long serialVersionUID=1L;
	private int id;
	private String name;
	
	/**элемент, который идентифицирует позицию найденного элемента 
	 * @param id - уникальный идентификатор найденной позиции 
	 * @param name - yf
	 */
	public ResultElement(int id, String name){
		this.id=id;
		this.name=name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
