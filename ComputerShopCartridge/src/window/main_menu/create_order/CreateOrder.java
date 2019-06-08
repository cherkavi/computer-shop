package window.main_menu.create_order;

import java.sql.Connection;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.Customer;
import database.wrap.OrderGroup;
import database.wrap.OrderList;
import database_reflect.ReflectWorker;
import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import window.main_menu.MainMenu;
import window.main_menu.WindowEmulator;
import window.main_menu.create_order.edit_model.EditModel;
import window.main_menu.create_order.find_user.FindUser;
import window.main_menu.create_order.find_user.finded_cartridge.FindedCartridgeBean;
import window.main_menu.create_order.panel_order.OrderPanelElement;
import window.main_menu.create_order.panel_order.PanelOrder;
import window.show_order.ShowOrder;

public class CreateOrder extends WindowEmulator implements IActionExecutor{
	/** панель, которая служит для поиска пользователей */
	private FindUser pageFindUser;
	private Form<Object> formMain;
	private TextField<String> textfieldSurname;
	private TextField<String> textfieldName;
	private TextField<String> textfieldDescription;
	private ArrayList<PanelOrder> listOfOrder=new ArrayList<PanelOrder>();
	private WebMarkupContainer parentListOrder;
	private final static String idPanelOrder="panel_order";
	private ModalWindow modalFindUser;
	private WebMarkupContainer wrapSurname;
	private WebMarkupContainer wrapName;
	
	public CreateOrder(){
		super("Создать заказ");
		initComponents();
	}

	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.modalFindUser=new ModalWindow("modal_find_user"){
			private final static long serialVersionUID=1L;
			@Override
			public void close(AjaxRequestTarget target) {
				super.close(target);
				onModalWindowClose(target);
			}
		};
		this.add(this.modalFindUser);
		
		formMain.add(new Label("caption_surname","Фамилия"));
		this.wrapSurname=new WebMarkupContainer("wrap_surname");
		this.wrapSurname.setOutputMarkupId(true);
		this.textfieldSurname=new TextField<String>("textfield_surname",new Model<String>(""));
		this.wrapSurname.add(this.textfieldSurname);
		formMain.add(this.wrapSurname);
		
		formMain.add(new Label("caption_name","Имя"));
		this.wrapName=new WebMarkupContainer("wrap_name");
		this.wrapName.setOutputMarkupId(true);
		this.textfieldName=new TextField<String>("textfield_name",new Model<String>(""));
		this.wrapName.add(this.textfieldName);
		formMain.add(this.wrapName);
		
		formMain.add(new Label("caption_description","Описание"));
		this.textfieldDescription=new TextField<String>("textfield_description",new Model<String>(""));
		formMain.add(textfieldDescription);
		
		Button buttonOk=new Button("button_create_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value","Создать заказ"));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить"));
		formMain.add(buttonCancel);
		formMain.add(new ComponentFeedbackPanel("error_message", formMain));
		
		this.parentListOrder=new WebMarkupContainer("parent_list_order");
		this.parentListOrder.setOutputMarkupId(true);
		// элементы заказа
		ListView<PanelOrder> listOrder=new ListView<PanelOrder>("list_order",listOfOrder){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<PanelOrder> item) {
				item.add(item.getModelObject());
			}
		};
		this.parentListOrder.add(listOrder);
		// кнопка поиска пользователей 
		Button buttonFindUser=new Button("button_find_user");
		buttonFindUser.add(new SimpleAttributeModifier("value",this.getString("caption_button_find_user")));
		buttonFindUser.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonFindUser(target);
			}
		});
		formMain.add(buttonFindUser);
		
		// кнопка добавления данных
		WebMarkupContainer buttonAddElement=new WebMarkupContainer("add_element");
		buttonAddElement.add(new SimpleAttributeModifier("value", "Добавить элемент"));
		buttonAddElement.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonAddElement(target);
			}
		});
		this.parentListOrder.add(buttonAddElement);
		this.add(this.parentListOrder); 
	}

	
	/** реакция на нажатие кнопки "Добавить элемент" */
	private void onButtonAddElement(AjaxRequestTarget target){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		PanelOrder panelOrder=new PanelOrder(idPanelOrder,
			     new OrderPanelElement(connector.getConnection()),
			     this);
		panelOrder.setOutputMarkupId(true);
		this.listOfOrder.add(panelOrder);
		
		connector.close();
		target.addComponent(this.parentListOrder);
	}
	
	private void onButtonOk(){
		//System.out.println("ButtonOk");
		String surname=this.textfieldSurname.getModelObject();
		if(surname==null){
			surname="";
		}
		String name=this.textfieldName.getModelObject();
		if(name==null){
			name="";
		}
		while(true){
			if(name.equals("")&&(surname.equals(""))){
				formMain.error("Введите имя и/или фамилию ");
				break;
			};
			if(this.listOfOrder.size()==0){
				formMain.error("Необходимо добавить хотя бы один картридж ");
				break;
			}
			// не обязательное примечание 
			String description=(this.textfieldDescription.getModelObject()==null)?"":this.textfieldDescription.getModelObject();
			// создать заказ, и вывести подтверждение на экран
			//for(int counter=0;counter<this.listOfOrder.size();counter++){
				//System.out.println("Vendor: "+this.listOfOrder.get(counter).getElement().getVendor());
				//System.out.println("Model: "+this.listOfOrder.get(counter).getElement().getModel());
			//}
			Integer groupId=this.createOrder(surname, name, description, this.listOfOrder);
			if(groupId!=null){
				// Перенаправить на страницу печати Заявки на получение картриджа
				this.setResponsePage(new ShowOrder(groupId,null));
			}else{
				this.formMain.error("Ошибка создания заказа");
				break;
			}
			break;
		}
	}

	/** создать заказ и вернуть его уникальный номер 
	 * @param surname - фамилия
	 * @param name - имя 
	 * @param description - описание
	 * @param list - список элементов по заказу
	 * @return - групповой номер ( ORDER_GROUP ) 
	 */
	private Integer createOrder(String surname, String name, String description, ArrayList<PanelOrder> list){
		Integer returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Session session=null;
		try{
			session=connector.getSession();
			session.beginTransaction();
			// создать запись в таблице ORDER_GROUP
			OrderGroup orderGroup=new OrderGroup();
			orderGroup.setIdCustomer(this.getCustomerId(surname, name, session));
			orderGroup.setDescription(description);
			session.save(orderGroup);
			// создать группу записей в таблице ORDER_LIST
			Date date=new Date();
			for(int counter=0;counter<list.size();counter++){
				OrderList orderElement=new OrderList();
				orderElement.setIdModel(this.getModelByName(list.get(counter).getElement().getVendor(), list.get(counter).getElement().getModel(), connector.getConnection()));
				orderElement.setTimeCreate(date);
				orderElement.generateNumber(connector.getConnection());
				orderElement.generateControlNumber();
				orderElement.setIdOrderGroup(orderGroup.getId());
				orderElement.setForSend(1);
				session.save(orderElement);
			}
			session.getTransaction().commit();
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
			returnValue=orderGroup.getId();
		}catch(Exception ex){
			try{
				session.getTransaction().rollback();
			}catch(Exception exInner){};
			System.err.println("CreateOrder#createOrder Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	/** получить уникальный номер модели на основании имени производителя и имени модели у производителя */
	private Integer getModelByName(String vendor, String model, Connection connection){
		Integer returnValue=null;
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select * from get_cartridge_model('"+vendor.replaceAll("'", "''")+"','"+model.replaceAll("'", "''")+"')");
			rs.next();
			returnValue=rs.getInt("ID");
		}catch(Exception ex){
			System.err.println("CrateOrder#getModelByName Exception: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	
	/** вернуть имя пользователя (найти по фамилии и имени, или создать нового )
	 * @param surname - фамилия
	 * @param name - имя
	 * @param session - Hibernate транзакция должны быть в состоянии старта,<br> <b>!!! не замыкается<b> 
	 * @return уникальный номер потребителя 
	 */
	private Integer getCustomerId(String surname, String name, Session session){
		Integer returnValue=null;
		try{
			returnValue=((Customer)session.createCriteria(Customer.class).add(Restrictions.eq("surname", surname)).add(Restrictions.eq("name", name)).list().get(0)).getId();
			// пользователь найден 
		}catch(Exception ex){
			// не найден данный пользователь
		}
		if(returnValue==null){
			//создать
			Customer customer=new Customer();
			customer.setName(name);
			customer.setSurname(surname);
			customer.setTimeCreate(new Date());
			session.save(customer);
			returnValue=customer.getId();
		}
		return returnValue;
	}
	
	/** реакция на нажатие кнопки отмены заказа */
	private void onButtonCancel(){
		this.setResponsePage(MainMenu.class);
	}

	/** реакция на нажатие кнопки "Поиск пользователя" */
	private void onButtonFindUser(AjaxRequestTarget target){
		//System.out.println("Find User");
		// октрыть модальное окно с поиском пользователей
		// можно использовать PageCreator
		this.modalFindUser.setInitialHeight(500);
		this.modalFindUser.setInitialWidth(640);
		this.pageFindUser=new FindUser(this.modalFindUser.getContentId(), this.modalFindUser);
		this.modalFindUser.setContent(this.pageFindUser);
		this.modalFindUser.show(target);
	}


	
	/** реакция на закрытие модального окна */
	private void onModalWindowClose(AjaxRequestTarget target){
		//System.out.println("close");
		if(this.pageFindUser!=null){
			// вернулся результат от закрытия окна с поиском пользователей
			if(this.pageFindUser.getCustomerId()!=null){
				fillCustomerById(this.pageFindUser.getCustomerId());
				target.addComponent(this.wrapSurname);
				target.addComponent(this.wrapName);
				
				ArrayList<FindedCartridgeBean> list=this.pageFindUser.getSelectedCartridge();
				for(int counter=0;counter<list.size();counter++){
					if(list.get(counter).isSelected()){
						PanelOrder panelOrder=new PanelOrder(idPanelOrder,
	 							 new OrderPanelElement(list.get(counter).getCartridgeVendor(),
 			 							 list.get(counter).getCartridgeModel()),
 			 							 this);
						panelOrder.setOutputMarkupId(true);
						this.listOfOrder.add(panelOrder);
					}
				}
				target.addComponent(this.parentListOrder);
			}
		}
		if(this.pageEditModel!=null){
			// вернулся результат от закрытия окна с редактированием модели
			if(this.pageEditModel.getResult().equals(EditModel.ReturnValues.CANCEL)){
				//target.addComponent(this.parentListOrder);
			}
			if(this.pageEditModel.getResult().equals(EditModel.ReturnValues.CREATE)){
				target.addComponent(this.parentListOrder);
			}
			if(this.pageEditModel.getResult().equals(EditModel.ReturnValues.EDIT_MODEL)){
				target.addComponent(this.parentListOrder);
			}
			if(this.pageEditModel.getResult().equals(EditModel.ReturnValues.EDIT_PRICE)){
				target.addComponent(this.parentListOrder);
			}
		}
		this.pageFindUser=null;
		this.pageEditModel=null;
	}
	
	/** */
	private void fillCustomerById(Integer customerId){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			Customer customer=(Customer)session.get(Customer.class, customerId);
			this.textfieldName.setModelObject(customer.getName());
			this.textfieldSurname.setModelObject(customer.getSurname());
		}catch(Exception ex){
			System.err.println("CreateOrder Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
	}

	/** панель для редактирования модели картриджа */
	private EditModel pageEditModel;
	
	@Override
	public void action(String actionName, Object argument) {
		while(true){
			if(actionName.equals("REMOVE")){
				Object[] objects=(Object[])argument;
				AjaxRequestTarget target=(AjaxRequestTarget)objects[0];
				PanelOrder panelOrder=(PanelOrder)objects[1];
				this.listOfOrder.remove(panelOrder);
				target.addComponent(this.parentListOrder);
				break;
			}
			if(actionName.equals("EDIT")){
				Object[] objects=(Object[])argument;
				AjaxRequestTarget target=(AjaxRequestTarget)objects[0];
				PanelOrder panelOrder=(PanelOrder)objects[1];
				
				// отобразить окно с редактированием модели по производителю
				if((panelOrder!=null)&&(panelOrder.getElement()!=null)&&(panelOrder.getElement().getVendor()!=null)){
					this.modalFindUser.setInitialHeight(250);
					this.modalFindUser.setInitialWidth(550);
					this.pageEditModel=new EditModel(this.modalFindUser.getContentId(), 
													 this.modalFindUser, 
													 panelOrder);
					this.modalFindUser.setContent(this.pageEditModel);
					this.modalFindUser.show(target);
				}else{
					System.out.println("Vendor was not detected ");
				}
				break;
			}
			break;
		}
		
	}
	
/*	создать заказ на основании введенных данных 
	private Integer createOrder(String surname, String name, String vendor, String model, String description){
		Integer returnValue=null;
		// создать пользователя, или найти его
		Integer customerId=this.getCustomerId(surname, name);
		Integer modelId=this.getModelId(vendor, model);
		if((customerId!=null)&&(modelId!=null)){
			returnValue=this.createOrder(modelId, customerId, new Date(),description);
		}else{
			returnValue=null;
		}
		// создать заказ и вернуть его уникальный ID
		return returnValue;
	}
	
	// создать заказ на основании обработанных данных от пользователя 
	private Integer createOrder(Integer modelId, Integer customerId, Date dateCreate,String description){
		Integer returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			OrderList order=new OrderList();
			order.generateNumber(connector.getConnection());
			order.setIdCustomer(customerId);
			order.setIdModel(modelId);
			order.setTimeCreate(dateCreate);
			order.setDescription(description);
			session.beginTransaction();
			session.save(order);
			session.getTransaction().commit();
			returnValue=order.getId();
		}catch(Exception ex){
			System.err.println("CrateOrder#createOrder Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	*/	
	/* получить номер модели на основании Производителя и имени модели  
	private Integer getModelId(String vendorName, String modelName){
		Integer returnValue=null;
		Integer vendorId=this.getVendorCode(vendorName);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeModel cartridgeModel=(CartridgeModel)(session.createCriteria(CartridgeModel.class)
											                      .add(Restrictions.eq("idVendor", vendorId))
											                      .add(Restrictions.eq("name",modelName))
											                      .list().get(0));
			returnValue=cartridgeModel.getId();
		}catch(Exception ex){
			
		}finally{
			connector.close();
		}
		return returnValue;
	}
*/	
	/* получить код заказчика по его фамилии и имени 
	@SuppressWarnings("unchecked")
	private Integer getCustomerId(String surname, String name){
		Integer returnValue=null;
		ConnectWrap connector=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			Session session=connector.getSession();
			List<Object> list=session.createCriteria(Customer.class).add(Restrictions.eq("surname", surname)).add(Restrictions.eq("name", name)).list();
			if(list.size()>0){
				// вернуть код найденного Customer
				returnValue=((Customer)list.get(0)).getId();
			}else{
				// создать Customer
				Customer customer=new Customer(surname, name);
				session.beginTransaction();
				session.save(customer);
				session.getTransaction().commit();
				returnValue=customer.getId();
			}
		}catch(Exception ex){
			System.out.println("CreateOrder#getCustomerId: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	

	// реакция на изменения Vendor 
	private void onChangeVendor(String newValue){
		Integer idVendor=this.getVendorCode(newValue);
		if(idVendor!=null){
			this.buttonAddModel.setVisible(true);
		}else{
			this.buttonAddModel.setVisible(false);
		}
		ArrayList<String> choicesModel=this.getListFromTableWithEmpty("select * from cartridge_model where id_vendor="+idVendor, "name");
		this.dropDownModel.setChoices(choicesModel);
		this.dropDownModel.setModelObject(choicesModel.get(0));
		this.priceModel.setObject("");
		
	}
	
	* получить Vendor ID на основании vendorName
	 * @param vendorName - имя Vendor
	 * @return код Vendor
	 *
	private Integer getVendorCode(String vendorName){
		Integer returnValue=null;
		ConnectWrap connector=null;
		ResultSet rs=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			PreparedStatement ps=connector.getConnection().prepareStatement("select id from cartridge_vendor where name=?");
			ps.setString(1, vendorName);
			rs=ps.executeQuery();
			while(rs.next()){
				returnValue=rs.getInt(1);
			}
		}catch(Exception ex){
			System.out.println("CreateOrder#getListFromTableWithEmpty: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	* получить ArrayList на основании запроса к базе данных и имени колонки из этого запроса 
	 * @param sql - запрос к базе данных, на основании которого будут отобраны данные
	 * @param column - имя колонки, из которой будут "вытащены" данные 
	 * @return ArrayList с пустой строкой в начале 
	 *
	private ArrayList<String> getListFromTableWithEmpty(String sql, String column){
		ArrayList<String> returnValue=new ArrayList<String>();
		returnValue.add("");
		ConnectWrap connector=null;
		ResultSet rs=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			rs=connector.getConnection().createStatement().executeQuery(sql);
			while(rs.next()){
				returnValue.add(rs.getString(column));
			}
		}catch(Exception ex){
			System.out.println("CreateOrder#getListFromTableWithEmpty: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	* получить пустой ArrayList с одной пустой строкой 
	private ArrayList<String> getEmptyArrayList(){
		ArrayList<String> returnValue=new ArrayList<String>();
		returnValue.add("");
		return returnValue;
	}
	*/		
/*	
	private void onChangeModel(String modelValue){
		this.priceModel.setObject("");
		Integer vendorCode=this.getVendorCode(this.dropDownVendor.getModelObject());
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeModel currentModel=((CartridgeModel)session.createCriteria(CartridgeModel.class).add(Restrictions.eq("idVendor", vendorCode))
											   .add(Restrictions.eq("name", modelValue)).list().get(0));
			//System.out.println("Model: "+modelValue+"   Price: "+currentModel.getPrice());
			this.priceModel.setObject(this.priceFormat.format(currentModel.getPrice().floatValue()));
		}catch(Exception ex){
			//System.out.println("EditVendorModel#onChangeModel Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}

	
	private void onButtonAddModel(){
		String vendorName=this.dropDownVendor.getModelObject();
		if((vendorName!=null)&&(!vendorName.equals(""))){
			EditModel editModel=new EditModel(vendorName, null, new Action(CreateOrder.class),new Action(CreateOrder.class));
			this.setResponsePage(editModel);
		}
		
	}

	public void setModel(String vendorName, String modelName){
		onChangeVendor(vendorName);
		onChangeModel(modelName);
	}
*/	
}
