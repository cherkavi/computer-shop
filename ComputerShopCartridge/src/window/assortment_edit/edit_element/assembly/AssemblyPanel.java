package window.assortment_edit.edit_element.assembly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import database.ConnectWrap;
import database.bind.EAssemblyEditType;

import wicket_extension.action.IAjaxActionExecutor;
import wicket_extension.ajax.AjaxEventIndicatorBehavior;
import window.Application;
import window.assortment_edit.edit_element.assembly.criteria.AssemblyCriteria;
import window.assortment_edit.edit_element.assembly.criteria.IGetValue;
import window.assortment_edit.edit_element.assembly.criteria.modal_choice.PanelChoice;
import window.assortment_edit.edit_element.assembly.criteria_add.SectionAdd;
import window.assortment_edit.edit_element.assembly.save_ok.SaveOk;

/** Панель наполнения сборочной информацией ассортиментной единицы 
 * <br>
 * содержит группу панелей, которые описывают данную ассортиментную единицу  
 * */
public class AssemblyPanel extends Panel implements IAjaxActionExecutor{
	private final static long serialVersionUID=1L;
	/** уникальный ассортиментный код  */
	private Integer assortmentKod;
	/** модальное  окно  */
	private ModalWindow modalWindow;
	/** модель, содержащая все критерии сборки для данной ассортиментной единицы  */
	private Model<ArrayList<AssemblyCriteria>> modelListOfCriteria=new Model<ArrayList<AssemblyCriteria>>(null);
	/** wicket:id элемента из списка  */
	private final String idCriteriaElement="criteria_element";
	/** обертка для всех элементов, которые есть  */
	private ListView<AssemblyCriteria> listOfCriteria=null;
	/** главная форма, которая содержит все визуальные элементы */
	private Form<?> formMain;
	
	/** HTML ID кнопки меню */
	private String idButtonMenu;
	/** HTML ID кнопки выход из редактирования */
	private String idButtonExit;
	
	/** Панель наполнения сборочной информацией ассортиментной единицы 
	 * <br>
	 * содержит группу панелей, которые описывают данную ассортиментную единицу
	 * @param wicketId - код элемента на странице Wicket 
	 * @param assortmentKod - ассортиментный код
	 * @param idButtonMenu - HTML id кнопки, которая служит для вход в режим поиска 
	 * @param idButtonExit - HTML id кнопки, которая служит для выхода из меню редактирования 
	 * */ 
	public AssemblyPanel(String wicketId, 
						 Integer assortmentKod,
						 String idButtonMenu,
						 String idButtonExit){
		super(wicketId);
		this.assortmentKod=assortmentKod;
		this.modelListOfCriteria.setObject(getListCriteriaExists());
		this.idButtonExit=idButtonExit;
		this.idButtonMenu=idButtonMenu;
		this.initComponents();
	}
	
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		formMain.setOutputMarkupId(true);

		Button buttonAddCriteria=new Button("button_add_criteria");
		buttonAddCriteria.setOutputMarkupId(true);
		formMain.add(buttonAddCriteria);
		buttonAddCriteria.add(new SimpleAttributeModifier("value", 
														  this.getString("caption_button_add_criteria")));
		buttonAddCriteria.add(new AjaxEventIndicatorBehavior("onclick",
															 "ajax_indicator") {
			private final static long serialVersionUID=1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonAddCriteria(target);
			}
		});
		
		AjaxButton buttonSave=new AjaxButton("button_save"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, 
									Form<?> form) {
				onButtonParametersSave(target);
			}
		};
		buttonSave.setOutputMarkupId(true);
		formMain.add(buttonSave);
		buttonSave.add(new SimpleAttributeModifier("value",this.getString("caption_button_assembly_save")));
		
		listOfCriteria=new ListView<AssemblyCriteria>("list_of_criteria", 
													  modelListOfCriteria){
			private  final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<AssemblyCriteria> item) {
				item.add(item.getModelObject());
			}
		};
		formMain.add(listOfCriteria);
		
		this.modalWindow=new ModalWindow("modal_window_add_criteria");
		this.modalWindow.setWindowClosedCallback(new WindowClosedCallback(){
			private final static long serialVersionUID=1L;
			@Override
			public void onClose(AjaxRequestTarget target) {
				onModalWindowClose(target);
			}
		});
		this.add(this.modalWindow);
	}
	
	/** INFO запрос на базу данных по получению по указанному критерию всех доступных описаний */
	private String queryCriteriaExists=null;
	{
		StringBuffer query=new StringBuffer();
		  query.append("	select  assortment.kod	\n");
		  query.append("	        ,assortment.class_kod	\n");
		  query.append("	        ,assortment_type_description.kod A_T_D_KOD \n");
		  query.append("	        ,assortment_type_description.kod_assortment_type A_T_D_KOD_ASSORTMENT_TYPE \n");
		  query.append("	        ,assortment_type_description.name DESCRIPTION_NAME	\n");
		  query.append("	        ,assortment_type_description.assembly_edit_type ASSEMBLY_EDIT_TYPE \n");
		  query.append("	from assortment	\n");
		  query.append("	inner join assortment_type_description	\n");
		  query.append("	        on assortment_type_description.kod_assortment_type=assortment.class_kod	\n");
		  query.append("	        and assortment.class_kod=assortment_type_description.kod_assortment_type	\n");
		  query.append("	where assortment.kod=?	\n");
		queryCriteriaExists=query.toString();
	}
	
	/** прочесть список критериев для отображения */
	private ArrayList<AssemblyCriteria> getListCriteriaExists(){
		ArrayList<AssemblyCriteria> returnValue=new ArrayList<AssemblyCriteria>();
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			PreparedStatement ps=connector.getConnection().prepareStatement(queryCriteriaExists);
			// System.out.println("AssortmentKod:"+this.assortmentKod);
			ps.setInt(1, this.assortmentKod);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				// System.out.println("AssemblyCriteria: "+rs.getString("DESCRIPTION_NAME"));
				returnValue.add(new AssemblyCriteria(this.idCriteriaElement,
													 this,
													 rs.getString("DESCRIPTION_NAME"),
													 this.assortmentKod,
													 rs.getInt("A_T_D_KOD"),
													 rs.getInt("ASSEMBLY_EDIT_TYPE")
													 )
								);
			}
		}catch(Exception ex){
			System.err.println("AssemblyPanel#listCriteriaExists Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	/** нажатие на кнопку "Сохранить выбранные параметры" */
	@SuppressWarnings("unchecked")
	private void onButtonParametersSave(AjaxRequestTarget target){
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		Connection connection=connector.getConnection();
		try{
			System.out.println("AssortmentKod: " + this.assortmentKod);
			for(int counter=0;counter<this.listOfCriteria.size();counter++){
				Object panel=this.modelListOfCriteria.getObject().get(counter).getCriteriaPanel();
				if(this.modelListOfCriteria.getObject().get(counter).isEdit()){
					IGetValue<String> getValue=(IGetValue<String>)panel;
					// System.out.println(counter+" : Title:"+getValue.getAssortmentTypeDescriptionTitle()+" ("+getValue.getAssortmentTypeDescriptionKod()+"):"+getValue.getValue());
					if(updateCriteria(connection, assortmentKod, getValue.getAssortmentTypeDescriptionKod(), getValue.getValue())==false){
						throw new Exception("Save Exception: "+getValue.getAssortmentTypeDescriptionKod());
					}
				}else if(this.modelListOfCriteria.getObject().get(counter).isSelect()){
					IGetValue<String> getValue=(IGetValue<String>)panel;
					// System.out.println(counter+" : Title:"+getValue.getAssortmentTypeDescriptionTitle()+" ("+getValue.getAssortmentTypeDescriptionKod()+"):"+getValue.getValue());
					if(updateCriteria(connection, assortmentKod, getValue.getAssortmentTypeDescriptionKod(), getValue.getValue())==false){
						throw new Exception("Save Exception: "+getValue.getAssortmentTypeDescriptionKod());
					}
				}else if(this.modelListOfCriteria.getObject().get(counter).isList()){
					IGetValue<Collection<String>> getValue=(IGetValue<Collection<String>>)panel;
					// System.out.println(counter+" : Title:"+getValue.getAssortmentTypeDescriptionTitle()+" ("+getValue.getAssortmentTypeDescriptionKod()+"):");
					ArrayList<String> list=this.convertArrayListFromIterator(getValue.getValue().iterator());
					if(updateCriteria(connection, assortmentKod, getValue.getAssortmentTypeDescriptionKod(), list)==false){
						throw new Exception("Save Exception: "+getValue.getAssortmentTypeDescriptionKod());
					}
					/* Collection<String> collection=getValue.getValue();
					System.out.print(counter+" Collection: ");
					Iterator<String> iterator=collection.iterator();
					while(iterator.hasNext()){
						System.out.print(iterator.next()+"  ");
					}
					System.out.println();*/
				}else{
					assert false:"Element does not recognized";
				}
			}
			// установить флаг изменения в таблице ASSORTMENT
			if(this.updateAssortment(connection, assortmentKod, 2)==false){
				throw new Exception("update assortment Error");
			}
			connection.commit();
			System.out.println("Save Ok");
			
			this.modalWindow.setContent(new SaveOk(this.modalWindow.getContentId(), "Сохранено", this, this.modalWindow));
			this.modalWindow.setInitialHeight(90);
			this.modalWindow.setInitialWidth(220);
			this.modalWindow.show(target);
			// target.appendJavascript("alert('save ok')");
		}catch(Exception ex){
			try{
				connection.rollback();
			}catch(Exception ex2){};
			System.err.println("AssemblyPanel#onButtonParameterSave: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** обновить ассортимент, установив флаг обновления данного ассортимента:
	 * @param connection - соединение с базой
	 * @param assortmentKod - код ассортимента 
	 * @param flag - флаг, который устанавливается для данной единицы 
	 *  */
	private boolean updateAssortment(Connection connection, int assortmentKod, int flag){
		boolean returnValue=false;
		try{
			connection.createStatement().executeUpdate("update assortment set update_in_server="+flag+"  where kod="+assortmentKod);
			returnValue=true;
		}catch(Exception ex){
			System.err.println("AssemlbyPanel#updateAssortment Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	/** конвертировать Iterator в ArrayList */
	private ArrayList<String> convertArrayListFromIterator(Iterator<String> iterator){
		ArrayList<String> returnValue=new ArrayList<String>();
		if(iterator!=null){
			while(iterator.hasNext()){
				returnValue.add(iterator.next());
			}
		}
		return returnValue;
	}
	
	/** обновить указанный критерий 
	 * @param connection - соединение с базой данных
	 * @param assortmentKod - код ассортимента 
	 * @param assortmentTypeDescription - код типа описания
	 * @param value - новое значение типа описания
	 * @return 
	 * <ul> 
	 * 	<li> <b>true</b> - обновление  прошло успешно </li>
	 * 	<li> <b>true</b> - ошибка обновления данных </li>
	 * </ul> 
	 */
	private boolean updateCriteria(Connection connection, int assortmentKod, int assortmentTypeDescription, String value){
		try{
			// получить значение  по указанному элементу
			ArrayList<String> listValues=this.getCriterias(connection, assortmentKod, assortmentTypeDescription);
			if(listValues.indexOf(value)<0){
				// обновить список элементов
					// удалить
				if(this.removeCriterias(connection, assortmentKod, assortmentTypeDescription)==false){
					return false;
				}
					// добавить 
				if(this.insertCriterias(connection,assortmentKod, assortmentTypeDescription, value)==false){
					return false;
				}
				return true;
			}else{
				// данный элемент присутствует в списке
				return true;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	private boolean updateCriteria(Connection connection, int assortmentKod, int assortmentTypeDescription, ArrayList<String> values){
		try{
			// получить значение  по указанному элементу
			ArrayList<String> listValues=this.getCriterias(connection, assortmentKod, assortmentTypeDescription);
			if(this.isArrayListEquals(values, listValues)==false){
				// обновить список элементов
					// удалить
				if(this.removeCriterias(connection, assortmentKod, assortmentTypeDescription)==false){
					return false;
				}
					// добавить 
				if(this.insertCriterias(connection,assortmentKod, assortmentTypeDescription, values)==false){
					return false;
				}
				return true;
			}else{
				// данный элемент присутствует в списке
				return true;
			}
		}catch(Exception ex){
			return false;
		}
	}
	
	/** сравнить два ArrayList и вернуть положительный результат, если все элементы одного есть и в другом */
	private boolean isArrayListEquals(ArrayList<String> source, ArrayList<String> destination){
		if((source==null)||(destination==null)){
			return false;
		}else{
			if(source.size()!=destination.size()){
				return false;
			}else{
				boolean returnValue=true;
				while(true){
					for(int counter=0;counter<source.size();counter++){
						if(destination.indexOf(source.get(counter))<0){
							returnValue=false;
							break;
						}
					}
					for(int counter=0;counter<destination.size();counter++){
						if(source.indexOf(destination.get(counter))<0){
							returnValue=false;
							break;
						}
					}
					break;
				}
				return returnValue;
			}
		}
	}
	
	/** добавить критерий по указанному ассортименту и типу ассортимента */
	private boolean insertCriterias(Connection connection, int assortmentKod, int assortmentTypeDescription, String value){
		try{
			PreparedStatement ps=connection.prepareStatement("insert into assortment_description(kod_assortment, kod_assortment_type_description, name) values(?,?,?)");
			ps.setInt(1, assortmentKod);
			ps.setInt(2, assortmentTypeDescription);
			ps.setString(3, value);
			ps.executeUpdate();
			return true;
		}catch(Exception ex){
			System.err.println("InsertCriterias: "+ex.getMessage());
			return false;
		}
	}

	/** добавить критерий по указанному ассортименту и типу ассортимента */
	private boolean insertCriterias(Connection connection, int assortmentKod, int assortmentTypeDescription, ArrayList<String> values){
		try{
			PreparedStatement ps=connection.prepareStatement("insert into assortment_description(kod_assortment, kod_assortment_type_description, name) values(?,?,?)");
			for(int counter=0;counter<values.size();counter++){
				ps.clearParameters();
				ps.setInt(1, assortmentKod);
				ps.setInt(2, assortmentTypeDescription);
				ps.setString(3, values.get(counter));
				ps.executeUpdate();
			}
			return true;
		}catch(Exception ex){
			System.err.println("InsertCriterias: "+ex.getMessage());
			return false;
		}
	}
	
	
	/** удалить все критерии по указаному ассортименту и типу описания 
	 * @param connection - соединение с базой данных 
	 * @param assortmentKod - код ассортимента 
	 * @param assortmentTypeDescription - код типа описания 
	 * @return
	 */
	private boolean removeCriterias(Connection connection, int assortmentKod, int assortmentTypeDescription){
		try{
			String query="delete from ASSORTMENT_DESCRIPTION where kod_assortment="+assortmentKod+" and kod_assortment_type_description="+assortmentTypeDescription;
			connection.createStatement().executeUpdate(query);
			return true;
		}catch(Exception ex){
			System.err.println("removeCriterias: "+ex.getMessage());
			return false;
		}
	}
	
	/** получить значения по выделенному ассортименту, по указанному типу 
	 * @param connection - соединение с базой данных 
	 * @param assortmentKod - код ассортимента
	 * @param assortmentTypeDescription
	 * @return значения по указанным критериям выборки 
	 */
	private ArrayList<String> getCriterias(Connection connection, int assortmentKod, int assortmentTypeDescription) throws SQLException {
		ArrayList<String> returnValue=new ArrayList<String>();
		String query="select name from ASSORTMENT_DESCRIPTION where kod_assortment="+assortmentKod+" and kod_assortment_type_description="+assortmentTypeDescription;
		ResultSet rs=connection.createStatement().executeQuery(query);
		while(rs.next()){
			String currentValue=rs.getString(1);
			if(currentValue!=null){
				returnValue.add(currentValue);
			}
		}
		return returnValue;
	}
	
	/** реакция нажатия на кнопку добавить критерий  */
	private void onButtonAddCriteria(AjaxRequestTarget target){
		this.modalWindow.setContent(new SectionAdd(this.modalWindow.getContentId(),"Добавить критерий", this, this.modalWindow));
		this.modalWindow.setInitialHeight(140);
		this.modalWindow.setInitialWidth(220);
		this.modalWindow.show(target);
	}
	
	/** удаление указанного критерия по данному ассортименту  */
	private void removeCriteria(AjaxRequestTarget target, 
								AssemblyCriteria criteria){
		System.out.println("remove criteria");
	}

	/** редактирование типа указанного критерия по данному ассортименту  
	 * возможная смена типа критерия на один из:
	 * <ul>
	 * 	<li>EDIT</li>
	 * 	<li>COMBOBOX</li>
	 * 	<li>LIST</li>
	 * </ul>
	 * */
	private void editCriteria(AjaxRequestTarget target, 
							  AssemblyCriteria criteria){
		this.modalWindow.setContent(new PanelChoice(this.modalWindow.getContentId(), this, criteria, this.modalWindow));
		this.modalWindow.setTitle("Тип критерия:");
		this.modalWindow.setInitialHeight(90);
		this.modalWindow.setInitialWidth(150);
		this.modalWindow.show(target);
	}

	@Override
	public int action(AjaxRequestTarget target, 
					  String actionName,
					  Object argument) {
		System.out.println("AssemblyPanel Action:");
		if(actionName.equals("DELETE")){
			if(argument instanceof AssemblyCriteria){
				removeCriteria(target, (AssemblyCriteria)argument);
				return IAjaxActionExecutor.RETURN_OK;
			}else{
				assert false:"AssemblyPanel#action check algorithm";
				return IAjaxActionExecutor.RETURN_ERROR;
			}
		}else if(actionName.equals("EDIT")){
			if(argument instanceof AssemblyCriteria){
				editCriteria(target,(AssemblyCriteria)argument);
				return IAjaxActionExecutor.RETURN_OK;
			}else{
				assert false:"AssemblyPanel#action check algorithm";
				return IAjaxActionExecutor.RETURN_ERROR;
			}
		}else if(actionName.equals("CHANGE")){
			// появился запрос на изменения типа критерия 
			if(argument instanceof Object[]){
				Object[] values=(Object[])argument;
				AssemblyCriteria criteria=(AssemblyCriteria)values[0];
				this.changeCriteriaElement(target, criteria, (String)values[1]);
			}else{
				assert false: "AssemblyPanel#action does not recognize argument value";
			}
		}else if(actionName.equals("ADD")){
			if(argument instanceof Object[]){
				Object[] values=(Object[])argument;
				EAssemblyEditType type=(EAssemblyEditType)values[0];
				String value=(String)values[1];
				// добавить критерий по данному ассортименту
				if(values[1]!=null){
					// проверить критерий на существование 
					if(isCriteriaTypeExists(this.assortmentKod, value)){
						System.out.println("Данный критерий уже существует");
					}else{
						// добавить критерий по данному ассортименту ( точнее по типу класса данного ассортимента )
						if(insertCriteriaType(this.assortmentKod, type, value)==true){
							this.modelListOfCriteria.setObject(getListCriteriaExists());
							// this.listOfCriteria.modelChanged();
							target.addComponent(this.formMain);
							System.out.println("Section Added");
						}else{
							System.err.println("insertCriteriaType Exception");
						}
					}
				}
			}else{
				assert false:"AssemblyPanel#action ADD argument must be String[] ";
			}
		}else if(actionName.equals("GOTO")){
			this.modalWindow.close(target);
			if(argument instanceof String){
				this.actionGoto=true;
					  if(argument.equals(SaveOk.MENU_STAY)){
					// nothing
				}else if(argument.equals(SaveOk.MENU_FIND)){
					// Find
					this.actionGoto=true;
					this.actionGotoScript="document.getElementById('"+idButtonMenu+"').click()";
				}else if(argument.equals(SaveOk.MENU_EXIT)){
					// Exit
					this.actionGoto=true;
					this.actionGotoScript="document.getElementById('"+idButtonExit+"').click()";
				}
			}else{
				assert false:"AssemblyPanel#action check action GOTO";
			}
		}
		else{
			assert false:"AssemblyPanel#action check algorithm";
		}
		return IAjaxActionExecutor.RETURN_ERROR;
	}
	
	private boolean actionGoto=false;
	String actionGotoScript="";
	private void onModalWindowClose(AjaxRequestTarget target){
		if(actionGoto==true){
			if((actionGotoScript!=null)&&(!actionGotoScript.equals(""))){
				target.appendJavascript(actionGotoScript);
			}
			actionGoto=false;
		}
	}
	
	/** добавить по указанному критерию значение  */
	private boolean insertCriteriaType(int assortmentKod, 
									   EAssemblyEditType type, 
									   String value){
		boolean returnValue=false;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select class_kod from assortment where assortment.kod="+assortmentKod);
			if(rs.next()){
				int classKod=rs.getInt(1);
				PreparedStatement ps=connector.getConnection().prepareStatement("insert into assortment_type_description(kod_assortment_type, name, assembly_edit_type) values(?,?,? )");
				ps.setInt(1, classKod);
				ps.setString(2, value);
				ps.setInt(3, type.getDatabaseKey());
				ps.executeUpdate();
				connector.getConnection().commit();
				returnValue=true;
			}else{
				assert false: "указанный ассортиментный код не найден в таблице ASSORTMENT.KOD="+assortmentKod;
			}
		}catch(Exception ex){
			System.err.println("#InsertCriteriaType:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** существует ли данный тип критерия по текущему ассортименту (классу) */
	private boolean isCriteriaTypeExists(int assortmentKod, String newCriteriaType){
		boolean returnValue=true;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			StringBuffer query=new StringBuffer();
			query.append("select assortment_type_description.name from assortment \n ");
			query.append("inner join assortment_type_description on assortment_type_description.kod_assortment_type=assortment.class_kod \n");
			query.append("where assortment.kod=? \n");
			query.append("and rupper(assortment_type_description.name) like ? \n");
			PreparedStatement ps=connector.getConnection().prepareStatement(query.toString());
			ps.setInt(1, assortmentKod);
			ps.setString(2, newCriteriaType.trim().toUpperCase());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.out.println("Assembly#isCriteriaTypeExists Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** произвести замену элемента из списка в соответствии с установленным правилом */
	private void changeCriteriaElement(AjaxRequestTarget target, AssemblyCriteria criteria, String changeValue){
		System.out.println("Assortment Kod:"+criteria.getAssortmentKod());
		System.out.println("Assortment Type Description:"+criteria.getAssortmentTypeDescriptionKod());
		if(changeValue.equals(PanelChoice.idEdit)){
			// заменить criteria на Edit
			updateAssortmentTypeDescriptionKod(criteria, EAssemblyEditType.EDIT.getDatabaseKey());
		}else if(changeValue.equals(PanelChoice.idSelect)){
			// заменить criteria на Select
			updateAssortmentTypeDescriptionKod(criteria, EAssemblyEditType.SELECT.getDatabaseKey());			
		}else if(changeValue.equals(PanelChoice.idList)){
			// заменить criteria на List
			updateAssortmentTypeDescriptionKod(criteria, EAssemblyEditType.LIST.getDatabaseKey());			
		}else{
			assert false:"AssemblyPanel#action check your action";
		}
		criteria.reInit();
		target.addComponent(this.formMain);
	}

	private void updateAssortmentTypeDescriptionKod(AssemblyCriteria criteria, int databaseKey) {
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			String query="update assortment_type_description set assembly_edit_type="+databaseKey+" where kod="+criteria.getAssortmentTypeDescriptionKod();
			// System.out.println(query);
			connector.getConnection().createStatement().executeUpdate(query);
			connector.getConnection().commit();
		}catch(Exception ex){
			System.err.println("AssemblyPanely#getValue Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
}
