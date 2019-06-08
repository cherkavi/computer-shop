package window.assembly.element;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import window.assembly.AssemblyCommands;

import database.ConnectWrap;

/** один из элементов сборки, который может быть:<br>
 * <li> Mother Board </li>
 * <li> Processor </li>
 * <li> Memory </li>
 * <li> Video </li>
 * <li> HDD </li>
 * */

public class Element extends Panel{
	static final long serialVersionUID=1L;
	/** тип данного элемента: Материнская плата, процессор, память */
	private Model<String> title=new Model<String>(""); 
	/** имя таблицы, в которой лежит данное значение */
	private String tableName;
	/** уникальный идентификатор из таблицы */
	private Integer id;
	/** имя элемента, которое следует отображать */
	private Model<String> name=new Model<String>("");
	/** элемент, который принимает задачи на выполнение */
	private IActionExecutor executor;
	
	/** получить имя таблицы, по которой создан данный элемент */
	public String getTableName(){
		return tableName;
	}
	
	/** получить заголовок для отображения пользователю */
	public String getTitle(){
		return this.title.getObject();
	}
	
	/** один из элементов сборки, который может быть:<br>
	 * <li> Mother Board </li>
	 * <li> Processor </li>
	 * <li> Memory </li>
	 * <li> Video </li>
	 * <li> HDD </li>
	 * @param id - уникальный идентификатор панели
	 * @param executor - исполнитель, которому нужно передавать данные 
	 * @param title - заголовок для данного элемента
	 * @param tableName - имя таблицы, для данного элемента 
	 * @param idInTable - ( nullable ) уникальный номер в таблице  
	 * @param showRemoveButton - 
	 */
	public Element(String id, IActionExecutor executor, String title, String tableName, Integer idInTable, boolean showRemoveButton){
		super(id);
		this.executor=executor;
		this.title.setObject(title);
		this.tableName=tableName;
		this.setTableId(idInTable);
		initComponents(showRemoveButton);
	}
	
	/** установить новое значение для данного элемента */
	public void setTableId(Integer id){
		this.id=id;
		if(this.id!=null){
			// загрузить наименование из таблицы 
			this.name.setObject(this.getNameFromTable(this.tableName, this.id));
		}
	}

	/** получить уникальный номер из таблицы  */
	public Integer getTableId(){
		return this.id;
	}
	
	/** получить из таблицы и уникального идентификатора реальное название элемента */
	private String getNameFromTable(String tableName, Integer id){
		String returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery("select * from "+tableName+" where id="+id);
			if(rs.next()){
				returnValue=rs.getString("name");
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("Element#getnameFromTable: Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(NullPointerException npe){
			}
		}
		return returnValue;
	}
	
	
	private void initComponents(final boolean showRemoveButton){
		this.add(new Label("label_type",this.getString("label_type")));
		this.add(new Label("value_type",this.title));
		
		this.add(new Label("label_name",this.getString("label_name")));
		this.add(new Label("value_name",this.name));
		
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		buttonChoose=new Button("button_choose"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonChoose();
			}
		};
		formMain.add(buttonChoose);
		
		Button buttonRemove=new Button("button_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return showRemoveButton;
			}
			@Override
			public void onSubmit() {
				onButtonRemove();
			}
		};
		buttonRemove.add(new SimpleAttributeModifier("value",this.getString("caption_button_remove")));
		formMain.add(buttonRemove);
		
		Button buttonClear=new Button("button_clear"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonClear();
			}
		};
		buttonClear.add(new SimpleAttributeModifier("value",this.getString("caption_button_clear")));
		formMain.add(buttonClear);
	}
	
	private void onButtonClear(){
		this.id=null;
		this.name.setObject("");
	}
	
	private Button buttonChoose;
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(this.id==null){
			buttonChoose.add(new SimpleAttributeModifier("value",this.getString("caption_button_choose")));
		}else{
			buttonChoose.add(new SimpleAttributeModifier("value",this.getString("caption_button_replace")));
		}
	}
	
	/** нажатие на кнопку выбора другого элемента */
	private void onButtonChoose(){
		this.executor.action(AssemblyCommands.CHOOSE_ID.name(), this);
	}
	
	/** нажатие на кнопку удаления данного элемента */
	private void onButtonRemove(){
		this.executor.action(AssemblyCommands.REMOVE.name(), this); 
	}
}
