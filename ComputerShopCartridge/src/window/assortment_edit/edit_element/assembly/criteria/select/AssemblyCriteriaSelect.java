package window.assortment_edit.edit_element.assembly.criteria.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import database.ConnectWrap;

import wicket_extension.action.IAjaxActionExecutor;
import window.Application;
import window.assortment_edit.edit_element.assembly.criteria.IGetValue;
import window.assortment_edit.edit_element.assembly.criteria.modal_choice_add.AddCriteria;
import window.assortment_edit.edit_element.assembly.criteria.modal_choice_remove.RemoveCriteria;

/** панель, содержащая элемент для редактирования ComboBox */
public class AssemblyCriteriaSelect extends Panel implements IAjaxActionExecutor, IGetValue<String>{
	private final static long serialVersionUID=1L;
	/** идентификатор описания */
	private int assortmentTypeDescription;
	/** ассортиментный код для данного описания */
	private int assortmentKod;
	/** обертка для элемента Select DropDownChoice */
	// private WebMarkupContainer selectContainer;
	/** выпадающий список доступных элементов  */
	private DropDownChoice<String> select;
	/** варианты возможного выбора */
	private Model<ArrayList<String>> modelVariants=new Model<ArrayList<String>>(null);
	/** в данный момент выбранная модель */
	private Model<String> modelSelection=new Model<String>();
	/** модальное окно, которое будет содержать либо панель удаления, либо панель добавления данных */
	private ModalWindow modalWindow;
	
	
	/** панель, содержащая элемент для редактирования ComboBox 
	 * @param wicketId - идентификатор панели на странице Wicket
	 * @param assortmentTypeDescription - идентификатор описания 
	 * @param assortmentKod - код элемента, к которому относится данное описание 
	 */
	public AssemblyCriteriaSelect(String wicketId, 
								  int assortmentTypeDescription, 
								  Integer assortmentKod){
		super(wicketId);
		this.assortmentKod=assortmentKod;
		this.assortmentTypeDescription=assortmentTypeDescription;
		initComponents();
	}

	/** инициализация элементов */
	private void initComponents(){
		Button buttonAdd=new Button("button_add_select");
		this.add(buttonAdd);
		
		buttonAdd.add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonAdd(target);
			}
		});
		buttonAdd.add(new SimpleAttributeModifier("value","Добавить вариант критерия"));
		
		Button buttonRemove=new Button("button_remove_select");
		this.add(buttonRemove);
		buttonRemove.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonRemove(target);
			}
		});
		buttonRemove.add(new SimpleAttributeModifier("value","Удалить вариант критерия"));
		
		// selectContainer=new WebMarkupContainer("select_container");
		// selectContainer.setOutputMarkupId(true);
		// this.add(selectContainer);
		
		// получить список всех значений
		this.modelVariants.setObject(this.getValues());
		this.modelSelection.setObject(this.getCurrentValue());
		select=new DropDownChoice<String>("select", this.modelSelection, this.modelVariants);
		select.setOutputMarkupId(true);
		// selectContainer.add(select);
		this.add(select);
		
		this.modalWindow=new ModalWindow("modal_window");
		this.add(this.modalWindow);
	}
	
	/** получить список допустимых значений для данного элемента <br> (список уже введенных значений ) */
	private ArrayList<String> getValues(){
		ArrayList<String> returnValue=new ArrayList<String>();
		String query="select distinct name from assortment_description where kod_assortment_type_description="+this.assortmentTypeDescription;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query);
			while(rs.next()){
				String currentValue=rs.getString(1);
				if(currentValue!=null){
					returnValue.add(currentValue);
				}
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaSelect#getValues() Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue; 
	}
	
	/** получить текущее значение данного элемента */
	private String getCurrentValue(){
		String returnValue=null;
		String query="select name from assortment_description where kod_assortment="+this.assortmentKod+" and kod_assortment_type_description="+this.assortmentTypeDescription;
		// System.out.println("getCurrentValue: "+query);
		// String query="select * from assortment_description where kod="+this.assortmentDescription;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query);
			if(rs.next()){
				returnValue=rs.getString(1);
			}else{
				returnValue=null;
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaSelect#getCurrentValue() Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue; 
	}
	
	/** нажатие на кнопку редактирования */
	private void onButtonAdd(AjaxRequestTarget target){
		this.modalWindow.setInitialHeight(80);
		this.modalWindow.setInitialWidth(270);
		
		this.modalWindow.setContent(new AddCriteria(this.modalWindow.getContentId(), 
													this.getTitle(this.assortmentTypeDescription), 
													this, 
													modalWindow));
		this.modalWindow.show(target);
	}

	/**  получить заголовок для данного окна */
	private String getTitle(int assortmentTypeDescriptionKod){
		String returnValue="";
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select name from assortment_type_description where kod="+assortmentTypeDescriptionKod);
			if(rs.next()){
				returnValue=rs.getString(1);
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaList#GetTitle Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	
	/** нажатие на кнопку удалить */
	private void onButtonRemove(AjaxRequestTarget target){
		String removeElement=this.select.getModelObject();
		ArrayList<String> elements=new ArrayList<String>();
		elements.add(removeElement);
		
		this.modalWindow.setInitialHeight(80);
		this.modalWindow.setInitialWidth(270);
		this.modalWindow.setContent(new RemoveCriteria(this.modalWindow.getContentId(), 
													   "Уверены в удалении элемента ?", 
													   elements, 
													   this, 
													   modalWindow));
		this.modalWindow.show(target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int action(AjaxRequestTarget target, 
					  String actionName,
					  Object argument) {
		if(actionName!=null){
			if(actionName.equals("ADD")&&(argument!=null)){
				//  проверить аргумент на повторяемость по данному критерию
				if(!isCriteriaExists(this.assortmentKod, this.assortmentTypeDescription, (String)argument)){
					// добавить критерий в текущий элемент 
					updateCriteria(this.assortmentKod, this.assortmentTypeDescription,(String)argument);
					
					String currentValue=this.getCurrentValue();
					ArrayList<String> values=this.getValues();
					int selectedItem=values.indexOf(currentValue);

					this.modelVariants.setObject(values);
					this.modelSelection.setObject(values.get(selectedItem));
					this.select.updateModel();
					this.select.getModel().setObject(values.get(selectedItem));
					
					target.addComponent(this.select);
					System.out.println("SelectedItem: "+selectedItem);
				}else{
					// данный элемент уже есть по указанному критерию
				}
			}else if(actionName.equals("REMOVE")){
				String elementForRemove=((ArrayList<String>)argument).get(0);
				removeCriteria(this.assortmentKod, this.assortmentTypeDescription, elementForRemove);

				this.modelVariants.setObject(this.getValues());
				this.modelSelection.setObject(this.getCurrentValue());
				this.select.setModel(this.modelSelection);
				this.select.updateModel();
				target.addComponent(this.select);
			}
		}else{
			assert false:"ActionName does not recognized";
		}
		return 0;
	}
	
	/** удалить указанный критерий 
	 * @param assortmentKod - код ассортимента 
	 * @param assortmentTypeDescription - ассортиментный код 
	 * @param elmentName - наименование элемента 
	 */
	private void removeCriteria(int assortmentKod, int assortmentTypeDescription, String elementName){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			System.out.println("AssortmentKod:"+assortmentKod+"   AssortmentTypeDescription:"+assortmentTypeDescription+"  Value:"+elementName);
			Connection connection=connector.getConnection();
			StringBuffer query=new StringBuffer();
			query.append("delete \n");
			query.append("from ASSORTMENT_DESCRIPTION \n");
			query.append("where \n");
			query.append("kod_assortment="+assortmentKod+" \n");
			query.append("and \n");
			query.append("kod_assortment_type_description="+assortmentTypeDescription+" \n");
			query.append("and rupper(name) in ( '"+elementName.toUpperCase().replaceAll("'", "''")+"' )\n");
			connection.createStatement().executeUpdate(query.toString());
			connection.commit();
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaSelect#removeCriteria Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** обновить по данному ассортименту, по данному типу значение новое значение ассортимента  
	 * @param connection - соединение с базой данных 
	 * @param assortmentKod - ASSORTMENT.KOD
	 * @param assortmentTypeDescription - ASSORTMENT_TYPE_DESCRIPTION.KOD
	 * @param value - новое значение, которое нужно добавить 
	 * @return
	 * <ul><b> true </b> - успешно добавлено </ul>
	 * <ul><b> false </b> - ошибка добавления  </ul>
	 */
	private boolean updateCriteria(int assortmentKod, int assortmentTypeDescription, String value){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		boolean returnValue=false;
		try{
			Connection connection=connector.getConnection();
			// удалить по указанному ассортименту все элементу по указанному типу критерию
			connection.createStatement().executeUpdate("delete from ASSORTMENT_DESCRIPTION where kod_assortment="+assortmentKod+" and kod_assortment_type_description="+assortmentTypeDescription);
			PreparedStatement ps=connection.prepareStatement("insert into ASSORTMENT_DESCRIPTION (kod_assortment, kod_assortment_type_description, name) values (?,?,?)");
			ps.setInt(1, assortmentKod);
			ps.setInt(2, assortmentTypeDescription);
			ps.setString(3, value);
			ps.executeUpdate();
			ps.getConnection().commit();
			returnValue=true;
			System.out.println("AssortmentKod:"+assortmentKod+"   AssortmentTypeDescription:"+assortmentTypeDescription+"  Value:"+value);
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaSelect#addCriteria Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** проверить по данному ассортименту, данному критерию есть ли уже переданное значение - убирание двойственности 
	 * @param connection - соединение с базой данных 
	 * @param assortmentKod - код ассортимента 
	 * @param assortmentTypeDescription - тип ассортимента 
	 * @param value - проверяемое значение  
	 * @return
	 * <ul> 
	 * 	<li><b>true</b> - есть уже данный критерий по указанному ассортименту, типу </li>
	 * 	<li><b>false</b> - нет данного значения в указанном ассортименте по указанному критерию </li>
	 * </ul>
	 */
	private boolean isCriteriaExists(int assortmentKod, int assortmentTypeDescription, String value){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		boolean returnValue=true;
		try{
			Connection connection=connector.getConnection();
			PreparedStatement st=connection.prepareStatement("select count(name) from ASSORTMENT_DESCRIPTION where kod_assortment=? and kod_assortment_type_description=? and trim(rupper(name)) like ?");
			st.setInt(1, assortmentKod);
			st.setInt(2, assortmentTypeDescription);
			st.setString(3, value.toUpperCase());
			ResultSet rs=st.executeQuery();
			rs.next();
			int repeatCount=rs.getInt(1);
			if(repeatCount>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaSelect#isCriteriaExists: "+ex.getMessage());
		}
		return returnValue;
	}

	@Override
	public String getValue() {
		return this.modelSelection.getObject();
	}

	@Override
	public int getAssortmentTypeDescriptionKod() {
		return this.assortmentTypeDescription;
	}

	@Override
	public String getAssortmentTypeDescriptionTitle() {
		return this.getTitle(this.assortmentTypeDescription);
	}
	
}
