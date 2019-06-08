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
	/** ������, ������� ������ ��� ������ ������������� */
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
		super("������� �����");
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
		
		formMain.add(new Label("caption_surname","�������"));
		this.wrapSurname=new WebMarkupContainer("wrap_surname");
		this.wrapSurname.setOutputMarkupId(true);
		this.textfieldSurname=new TextField<String>("textfield_surname",new Model<String>(""));
		this.wrapSurname.add(this.textfieldSurname);
		formMain.add(this.wrapSurname);
		
		formMain.add(new Label("caption_name","���"));
		this.wrapName=new WebMarkupContainer("wrap_name");
		this.wrapName.setOutputMarkupId(true);
		this.textfieldName=new TextField<String>("textfield_name",new Model<String>(""));
		this.wrapName.add(this.textfieldName);
		formMain.add(this.wrapName);
		
		formMain.add(new Label("caption_description","��������"));
		this.textfieldDescription=new TextField<String>("textfield_description",new Model<String>(""));
		formMain.add(textfieldDescription);
		
		Button buttonOk=new Button("button_create_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value","������� �����"));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","��������"));
		formMain.add(buttonCancel);
		formMain.add(new ComponentFeedbackPanel("error_message", formMain));
		
		this.parentListOrder=new WebMarkupContainer("parent_list_order");
		this.parentListOrder.setOutputMarkupId(true);
		// �������� ������
		ListView<PanelOrder> listOrder=new ListView<PanelOrder>("list_order",listOfOrder){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<PanelOrder> item) {
				item.add(item.getModelObject());
			}
		};
		this.parentListOrder.add(listOrder);
		// ������ ������ ������������� 
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
		
		// ������ ���������� ������
		WebMarkupContainer buttonAddElement=new WebMarkupContainer("add_element");
		buttonAddElement.add(new SimpleAttributeModifier("value", "�������� �������"));
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

	
	/** ������� �� ������� ������ "�������� �������" */
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
				formMain.error("������� ��� �/��� ������� ");
				break;
			};
			if(this.listOfOrder.size()==0){
				formMain.error("���������� �������� ���� �� ���� �������� ");
				break;
			}
			// �� ������������ ���������� 
			String description=(this.textfieldDescription.getModelObject()==null)?"":this.textfieldDescription.getModelObject();
			// ������� �����, � ������� ������������� �� �����
			//for(int counter=0;counter<this.listOfOrder.size();counter++){
				//System.out.println("Vendor: "+this.listOfOrder.get(counter).getElement().getVendor());
				//System.out.println("Model: "+this.listOfOrder.get(counter).getElement().getModel());
			//}
			Integer groupId=this.createOrder(surname, name, description, this.listOfOrder);
			if(groupId!=null){
				// ������������� �� �������� ������ ������ �� ��������� ���������
				this.setResponsePage(new ShowOrder(groupId,null));
			}else{
				this.formMain.error("������ �������� ������");
				break;
			}
			break;
		}
	}

	/** ������� ����� � ������� ��� ���������� ����� 
	 * @param surname - �������
	 * @param name - ��� 
	 * @param description - ��������
	 * @param list - ������ ��������� �� ������
	 * @return - ��������� ����� ( ORDER_GROUP ) 
	 */
	private Integer createOrder(String surname, String name, String description, ArrayList<PanelOrder> list){
		Integer returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Session session=null;
		try{
			session=connector.getSession();
			session.beginTransaction();
			// ������� ������ � ������� ORDER_GROUP
			OrderGroup orderGroup=new OrderGroup();
			orderGroup.setIdCustomer(this.getCustomerId(surname, name, session));
			orderGroup.setDescription(description);
			session.save(orderGroup);
			// ������� ������ ������� � ������� ORDER_LIST
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

	/** �������� ���������� ����� ������ �� ��������� ����� ������������� � ����� ������ � ������������� */
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

	
	/** ������� ��� ������������ (����� �� ������� � �����, ��� ������� ������ )
	 * @param surname - �������
	 * @param name - ���
	 * @param session - Hibernate ���������� ������ ���� � ��������� ������,<br> <b>!!! �� ����������<b> 
	 * @return ���������� ����� ����������� 
	 */
	private Integer getCustomerId(String surname, String name, Session session){
		Integer returnValue=null;
		try{
			returnValue=((Customer)session.createCriteria(Customer.class).add(Restrictions.eq("surname", surname)).add(Restrictions.eq("name", name)).list().get(0)).getId();
			// ������������ ������ 
		}catch(Exception ex){
			// �� ������ ������ ������������
		}
		if(returnValue==null){
			//�������
			Customer customer=new Customer();
			customer.setName(name);
			customer.setSurname(surname);
			customer.setTimeCreate(new Date());
			session.save(customer);
			returnValue=customer.getId();
		}
		return returnValue;
	}
	
	/** ������� �� ������� ������ ������ ������ */
	private void onButtonCancel(){
		this.setResponsePage(MainMenu.class);
	}

	/** ������� �� ������� ������ "����� ������������" */
	private void onButtonFindUser(AjaxRequestTarget target){
		//System.out.println("Find User");
		// ������� ��������� ���� � ������� �������������
		// ����� ������������ PageCreator
		this.modalFindUser.setInitialHeight(500);
		this.modalFindUser.setInitialWidth(640);
		this.pageFindUser=new FindUser(this.modalFindUser.getContentId(), this.modalFindUser);
		this.modalFindUser.setContent(this.pageFindUser);
		this.modalFindUser.show(target);
	}


	
	/** ������� �� �������� ���������� ���� */
	private void onModalWindowClose(AjaxRequestTarget target){
		//System.out.println("close");
		if(this.pageFindUser!=null){
			// �������� ��������� �� �������� ���� � ������� �������������
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
			// �������� ��������� �� �������� ���� � ��������������� ������
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

	/** ������ ��� �������������� ������ ��������� */
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
				
				// ���������� ���� � ��������������� ������ �� �������������
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
	
/*	������� ����� �� ��������� ��������� ������ 
	private Integer createOrder(String surname, String name, String vendor, String model, String description){
		Integer returnValue=null;
		// ������� ������������, ��� ����� ���
		Integer customerId=this.getCustomerId(surname, name);
		Integer modelId=this.getModelId(vendor, model);
		if((customerId!=null)&&(modelId!=null)){
			returnValue=this.createOrder(modelId, customerId, new Date(),description);
		}else{
			returnValue=null;
		}
		// ������� ����� � ������� ��� ���������� ID
		return returnValue;
	}
	
	// ������� ����� �� ��������� ������������ ������ �� ������������ 
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
	/* �������� ����� ������ �� ��������� ������������� � ����� ������  
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
	/* �������� ��� ��������� �� ��� ������� � ����� 
	@SuppressWarnings("unchecked")
	private Integer getCustomerId(String surname, String name){
		Integer returnValue=null;
		ConnectWrap connector=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			Session session=connector.getSession();
			List<Object> list=session.createCriteria(Customer.class).add(Restrictions.eq("surname", surname)).add(Restrictions.eq("name", name)).list();
			if(list.size()>0){
				// ������� ��� ���������� Customer
				returnValue=((Customer)list.get(0)).getId();
			}else{
				// ������� Customer
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
	
	

	// ������� �� ��������� Vendor 
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
	
	* �������� Vendor ID �� ��������� vendorName
	 * @param vendorName - ��� Vendor
	 * @return ��� Vendor
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

	* �������� ArrayList �� ��������� ������� � ���� ������ � ����� ������� �� ����� ������� 
	 * @param sql - ������ � ���� ������, �� ��������� �������� ����� �������� ������
	 * @param column - ��� �������, �� ������� ����� "��������" ������ 
	 * @return ArrayList � ������ ������� � ������ 
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
	
	* �������� ������ ArrayList � ����� ������ ������� 
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
