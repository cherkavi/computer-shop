package window.assortment_edit.edit_element.assembly.criteria.list;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import database.ConnectWrap;

import wicket_extension.action.IAjaxActionExecutor;
import window.Application;
import window.assortment_edit.edit_element.assembly.criteria.IGetValue;
import window.assortment_edit.edit_element.assembly.criteria.modal_choice_add.AddCriteria;
import window.assortment_edit.edit_element.assembly.criteria.modal_choice_remove.RemoveCriteria;

/** ������, ���������� ������� ��� �������������� ComboBox */
public class AssemblyCriteriaList extends Panel implements IAjaxActionExecutor, IGetValue<Collection<String>>{
	private final static long serialVersionUID=1L;
	/** ������������� �������� */
	private int assortmentTypeDescription;
	/** ��� �� ������� �����������  */
	private int assortmentKod;
	
	
	/** ������, ���������� ������� ��� �������������� ComboBox 
	 * @param wicketId - ������������� ������ �� �������� Wicket
	 * @param assortmentTypeDescription - ������������� �������� 
	 * @param assortmentKod - ��� � ������� �����������  
	 */
	public AssemblyCriteriaList(String wicketId, 
								  int assortmentTypeDescription, 
								  Integer assortmentKod){
		super(wicketId);
		this.assortmentKod=assortmentKod;
		this.assortmentTypeDescription=assortmentTypeDescription;
		initComponents();
	}

	/**  �������� ��������� ��� ������� ���� */
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
	
	/** ������������� ��������� */
	@SuppressWarnings("unchecked")
	private void initComponents(){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonAdd=new Button("button_add");
		formMain.add(buttonAdd);
		buttonAdd.add(new AjaxEventBehavior("onclick"){
			private static final long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonAdd(target);
			}
		});
		buttonAdd.add(new SimpleAttributeModifier("value","�������� ������� ��������"));
		
		AjaxButton buttonRemove=new AjaxButton("button_remove"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonRemove(target);
			}
		};
		buttonRemove.add(new SimpleAttributeModifier("value","������� ����������� ������� ��������"));
		formMain.add(buttonRemove);

		selectWrap=new WebMarkupContainer("select_wrap");
		selectWrap.setOutputMarkupId(true);
		formMain.add(selectWrap);
		// this.modelSelection.setObject(this.getCurrentValue());
		// modelVariants.setObject(this.getValues());
		select=new ListMultipleChoice<String>("select",this.getValues()).setMaxRows(5);
		
		select.setOutputMarkupId(true);
		Model<? extends Collection<String>> model=new Model<ArrayList<String>>(this.getCurrentValue());
		select.setModel((IModel<Collection<String>>) model);
		// select.setModelObject(this.getCurrentValue());
		// select.setChoices(this.getValues());
		selectWrap.add(select);
		
		WebComponent buttonIncrease=new WebComponent("button_increase");
		buttonIncrease.add(new SimpleAttributeModifier("onclick","increase('"+select.getMarkupId()+"')"));
		formMain.add(buttonIncrease);
		
		WebComponent buttonDecrease=new WebComponent("button_decrease");
		buttonDecrease.add(new SimpleAttributeModifier("onclick","decrease('"+select.getMarkupId()+"')"));
		formMain.add(buttonDecrease);
		
		this.modalWindow=new ModalWindow("modal_window");
		formMain.add(modalWindow);
	}
	
	private WebMarkupContainer selectWrap;
	private ModalWindow modalWindow;
	/** ������ ������ �������� */
	private ListMultipleChoice<String> select;
	
	/** �������� ������ ���������� �������� ��� ������� �������� <br> (������ ��� ��������� �������� ) */
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
			System.err.println("AssemblyCriteriaList#getValues() Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue; 
	}
	
	/** �������� ������� �������� ������� �������� */
	private ArrayList<String> getCurrentValue(){
		ArrayList<String> returnValue=new ArrayList<String>();
		String query="select name from assortment_description where kod_assortment="+this.assortmentKod+" and kod_assortment_type_description="+this.assortmentTypeDescription;
		// String query="select * from assortment_description where kod="+this.assortmentDescription;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query);
			while(rs.next()){
				returnValue.add(rs.getString(1));
			}
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaList#getCurrentValue() Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue; 
	}
	
	
	/** ������� �� ������ �������� �������� */
	private void onButtonAdd(AjaxRequestTarget target){
		
		this.modalWindow.setTitle("�������� �������");
		this.modalWindow.setInitialHeight(80);
		this.modalWindow.setInitialWidth(270);
		
		this.modalWindow.setContent(new AddCriteria(this.modalWindow.getContentId(),
												    this.getTitle(this.assortmentTypeDescription),
												    this, 
												    this.modalWindow
												    )
									);
		this.modalWindow.show(target);
	}

	/** ������� �� ������ �������� ��������*/
	private void onButtonRemove(AjaxRequestTarget target){
		System.out.println("Button Remove click");
		Collection<String> value=select.getModelObject();
		Iterator<String> keys=value.iterator();
		ArrayList<String> elements=new ArrayList<String>();
		// elements.addAll(select.getModelObject());
		while(keys.hasNext()){
			String element=keys.next();
			// System.out.println("Selected Value:"+element);
			elements.add(element);
		}
		
		
		this.modalWindow.setTitle("������� ��������");
		this.modalWindow.setInitialHeight(180);
		this.modalWindow.setInitialWidth(270);
		
		this.modalWindow.setContent(new RemoveCriteria(this.modalWindow.getContentId(),
												    this.getTitle(this.assortmentTypeDescription),
												    elements, // this.getCurrentValue() 
												    this, 
												    this.modalWindow
												    )
									);
		this.modalWindow.show(target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int action(AjaxRequestTarget target, String actionName,
			Object argument) {
		if(actionName!=null){
			if(actionName.equals("ADD")){
				if(argument!=null){
					System.out.println("�������� �������� �� ������� ��������");
					this.addToAssortmentDescription(target, this.assortmentKod, this.assortmentTypeDescription, (String)argument);
				}else{
					System.out.println("AssemblyCriteriaList:  argument is null "); 
				}
			}else if(actionName.equals("REMOVE")){
				if(argument!=null){
					ArrayList<String> elements=(ArrayList<String>)argument;
					this.removeElements(target, 
										this.assortmentKod, 
										this.assortmentTypeDescription, 
										elements);
				}else{
					assert false: "AssemblyCriteria#action REMOVE has no argument consists";
				}
			}
		}else{
			assert false: "AssemblyCriteriaList#action actionName is null ";
		}
		return 0;
	}

	/** �������� � ���� ������ ��������� �������� */
	private void removeElements(AjaxRequestTarget target, 
								int assortmentKod, 
								int assortmentTypeDescription, 
								ArrayList<String> elements){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			Connection connection=connector.getConnection();
			StringBuffer query=new StringBuffer();
			query.append("delete \n");
			query.append("from ASSORTMENT_DESCRIPTION \n");
			query.append("where \n");
			query.append("kod_assortment="+assortmentKod+" \n");
			query.append("and \n");
			query.append("kod_assortment_type_description="+assortmentTypeDescription+" \n");
			query.append("and rupper(name) in ( \n");
			for(int counter=0;counter<elements.size();counter++){
				if(elements.get(counter)!=null){
					query.append("'"+elements.get(counter).toUpperCase().replaceAll("'", "''")+"'");
				}else{
					query.append("null");
				}
				if(counter!=(elements.size()-1)){
					query.append(", \n");
				}
			}
			query.append(")");
			connection.createStatement().executeUpdate(query.toString());
			connection.commit();
			// �������� ���������� ���� �� ��������� ������ 
/*			
  			String queryGetDescriptionForRemove="select kod, name from assortment_description where kod_assortment_type_description=? and kod_assortment=?";
  			PreparedStatement ps=connection.prepareStatement(queryGetDescriptionForRemove);
			ps.setInt(1, assortmentTypeDescription);
			ps.setInt(2, assortmentKod);
			ResultSet rs=ps.executeQuery();
			ArrayList<Integer> removeIndexes=new ArrayList<Integer>();
			while(rs.next()){
				String currentName=rs.getString(2);
				if(this.getElementCount(connection, assortmentTypeDescription, currentName)==1){ // �� ���� ������������ ������ � �������� �������� 
					removeIndexes.add(rs.getInt(1));
				}else{
					System.out.println("Element can't remove, becasue another Assortment consist's him: "+rs.getInt(1));
				}
			}
			// �������� �� ��������� ������� �������� ������� �������� - ���� �� ������ �������� ��� � ����-��
			if(removeIndexes.size()>0){
				System.out.println("elements for remove count:"+removeIndexes.size());
				Model<? extends Collection<String>> model=new Model<ArrayList<String>>(this.getCurrentValue());
				StringBuffer query=new StringBuffer();
				query.append("delete from assortment_description where kod in (");
				for(int counter=0;counter<removeIndexes.size();counter++){
					query.append(removeIndexes.get(counter));
					if(counter<removeIndexes.size()-1){
						query.append(", \n");
					}
				}
				query.append(")");
				connection.createStatement().executeUpdate(query.toString());
				connection.commit();
				select.setModel((IModel<Collection<String>>) model);
				select.setChoices(new Model<ArrayList<String>>(this.getValues()));
				target.addComponent(selectWrap);
			}else{
				System.out.println("no elements for remove");
			}
*/			
		}catch(Exception ex){
			System.err.println("addToAssortmentDescription: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}

	
	/** �������� � ���� ������ ��������� �������� */
	@SuppressWarnings("unchecked")
	private void addToAssortmentDescription(AjaxRequestTarget target, int assortmentKod, int assortmentTypeDescription, String value){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			Connection connection=connector.getConnection();
			// ���������, ���� �� ������ �������� � ���� �� AssortmentTypeDescription ��������
			if(!isCriteriaExists(connection, this.assortmentKod, assortmentTypeDescription, value)){
				// �������� � ������ ������� ��������
				if(addCriteria(connection, assortmentKod, assortmentTypeDescription, value)){
					// ��������� ������ � �������� 
					Model<? extends Collection<String>> model=new Model<ArrayList<String>>(this.getCurrentValue());
					select.setModel((IModel<Collection<String>>) model);
					select.setChoices(new Model<ArrayList<String>>(this.getValues()));
					target.addComponent(selectWrap);
				}
			}else{
				System.out.println("AssemblyCriteriaList ������ �������� ��� ����������"); 
			}
		}catch(Exception ex){
			System.err.println("addToAssortmentDescription: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** �������� �� ������� ������������, �� ������� ���� �������� ����� �������� ������������  
	 * @param connection - ���������� � ����� ������ 
	 * @param assortmentKod - ASSORTMENT.KOD
	 * @param assortmentTypeDescription - ASSORTMENT_TYPE_DESCRIPTION.KOD
	 * @param value - ����� ��������, ������� ����� �������� 
	 * @return
	 * <ul><b> true </b> - ������� ��������� </ul>
	 * <ul><b> false </b> - ������ ����������  </ul>
	 */
	private boolean addCriteria(Connection connection, int assortmentKod, int assortmentTypeDescription, String value){
		boolean returnValue=false;
		try{
			PreparedStatement ps=connection.prepareStatement("insert into assortment_description (kod_assortment, kod_assortment_type_description, name) values (?,?,?)");
			ps.setInt(1, assortmentKod);
			ps.setInt(2, assortmentTypeDescription);
			ps.setString(3, value);
			ps.executeUpdate();
			ps.getConnection().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AssemblyCriteriaList#addCriteria Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	/** ��������� �� ������� ������������, ������� �������� ���� �� ��� ���������� �������� - �������� �������������� 
	 * @param connection - ���������� � ����� ������ 
	 * @param assortmentKod - ��� ������������ 
	 * @param assortmentTypeDescription - ��� ������������ 
	 * @param value - ����������� ��������  
	 * @return
	 * <ul> 
	 * 	<li><b>true</b> - ���� ��� ������ �������� �� ���������� ������������, ���� </li>
	 * 	<li><b>false</b> - ��� ������� �������� � ��������� ������������ �� ���������� �������� </li>
	 * </ul>
	 */
	private boolean isCriteriaExists(Connection connection, int assortmentKod, int assortmentTypeDescription, String value){
		boolean returnValue=true;
		try{
			PreparedStatement st=connection.prepareStatement("select count(name) from assortment_description where kod_assortment=? and kod_assortment_type_description=? and trim(rupper(name)) like ?");
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
			System.err.println("AssemblyCriteriaList#isCriteriaExists: "+ex.getMessage());
		}
		return returnValue;
	}

	/** �������� ���������� ������� */
	public Collection<String> getValue(){
		return this.select.getModelObject();
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
