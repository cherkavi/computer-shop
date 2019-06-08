package window.main_menu.create_order.find_user;

import java.sql.Connection;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.wicket.ajax.AjaxEventBehavior;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import database.ConnectWrap;
import database.wrap.Customer;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import window.main_menu.create_order.find_user.finded_cartridge.FindedCartridge;
import window.main_menu.create_order.find_user.finded_cartridge.FindedCartridgeBean;
import window.main_menu.create_order.find_user.finded_customer.FindedCustomer;
import window.main_menu.create_order.find_user.finded_customer.FindedCustomerBean;

/** панель, которая поиск пользователя */
public class FindUser extends Panel implements IActionExecutor{
	private final static long serialVersionUID=1L;
	
	/** родительское окно, которое следует закрывать */
	private ModalWindow parentModalWindow;
	/** компонент, который содержит информацию обо всех пользователях */
	private WebMarkupContainer panelCustomer;
	/** компонент, который содержит информацию обо всех картриджах пользователя */
	private WebMarkupContainer panelCartridge;
	/** модель, которая содержит список всех пользователй */
	private ArrayList<FindedCustomerBean> modelFindedCustomer;
	/** заголовок для модели картриджа */
	private Model<String> modelCartridgeCaption;
	/** уникальный номер клиента */
	private Integer customerId;
	/** список найденных картриджей */
	private ArrayList<FindedCartridgeBean> modelFindedCartridge;
	
	/** панель, которая поиск пользователя 
	 * @param id - уникальный номер идентификатора 
	 * @param parentModalWindow - окно, которому следует передать управление 
	 */
	public FindUser(String id, ModalWindow parentModalWindow){
		super(id);
		this.parentModalWindow=parentModalWindow;
		initComponents();
	}
	
	private void initComponents(){
		// форма, которая содержит поле поиска по фамилии и имени 
		Form<Object> formNameFind=new Form<Object>("form_name_find");
		this.add(formNameFind);
		
		formNameFind.add(new TextField<String>("edit_name",new Model<String>("")));
		
		AjaxButton buttonName=new AjaxButton("button_name"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onFormNameSubmit(target,form);
			}
		};
		buttonName.add(new SimpleAttributeModifier("value",this.getString("caption_button_name")));
		formNameFind.add(buttonName);

		// форма, содержит поле поиска по номеру картриджа
		Form<Object> formFindCartridge=new Form<Object>("form_find_cartridge");
		this.add(formFindCartridge);
		
		AjaxButton buttonCartridge=new AjaxButton("button_find_cartridge"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onFormCartridgeSubmit(target,form);
			}
		};
		buttonCartridge.setOutputMarkupId(true);
		buttonCartridge.add(new SimpleAttributeModifier("value",this.getString("caption_button_cartridge")));
		formFindCartridge.add(buttonCartridge);

		TextField<String> editCartridge=new TextField<String>("edit_name_cartridge",new Model<String>(""));
		editCartridge.setOutputMarkupId(true);
		editCartridge.add(new SimpleAttributeModifier("onkeypress",
													  "if(event.keyCode==13){"+buttonCartridge.getMarkupId()+".click();return false;}"));
		editCartridge.add(new SimpleAttributeModifier("onclick",editCartridge.getMarkupId()+".value=''"));
		
		formFindCartridge.add(editCartridge);
		
		// панель найденных пользователей
		panelCustomer=new WebMarkupContainer("panel_customer");
		panelCustomer.setOutputMarkupId(true);
		this.add(panelCustomer);
		
		this.modelFindedCustomer=new ArrayList<FindedCustomerBean>();
		ListView<FindedCustomerBean> customerList=new ListView<FindedCustomerBean>("customer_list",this.modelFindedCustomer){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<FindedCustomerBean> item) {
				item.add(new FindedCustomer("panel_find_customer",item.getModelObject(),FindUser.this));
			}
		};
		panelCustomer.add(customerList);
		
		// панель картриджей
		panelCartridge=new WebMarkupContainer("panel_cartridge");
		panelCartridge.setOutputMarkupId(true);
		this.add(panelCartridge);
		
		this.add(new Label("fieldset_legend_cartridge",this.getString("fieldset_legend_cartridge")));
		
		this.modelCartridgeCaption=new Model<String>("");
		panelCartridge.add(new Label("client_name",this.modelCartridgeCaption));
		this.modelFindedCartridge=new ArrayList<FindedCartridgeBean>();
		ListView<FindedCartridgeBean> listCartridge=new ListView<FindedCartridgeBean>("cartridge_list",
																					  this.modelFindedCartridge){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<FindedCartridgeBean> item) {
				item.add(new FindedCartridge("panel_find_cartridge",item.getModelObject()));
			}
		};
		panelCartridge.add(listCartridge);
		
		// кнопка передачи управления родительском окну 
		Button buttonClose=new Button("button_close");
		this.add(buttonClose);
		buttonClose.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonClose(target);
			}
		});
		buttonClose.add(new SimpleAttributeModifier("value"," Зафиксировать пользователя/картриджы "));
	}
	/** нажатие на кнопку закрытия модального окна */
	private void onButtonClose(AjaxRequestTarget target) {
		/*for(int counter=0;counter<this.modelFindedCartridge.size();counter++){
			if(this.modelFindedCartridge.get(counter).isSelected()){
				System.out.println(this.modelFindedCartridge.get(counter).getCartridgeId()+"   "+this.modelFindedCartridge.get(counter).getCartridgeVendor()+"  "+this.modelFindedCartridge.get(counter).getCartridgeModel());
			}
		}*/
		this.parentModalWindow.close(target);
	}
	/** нажатие на кнопку поиска пользователя для окна */
	@SuppressWarnings("unchecked")
	private void onFormNameSubmit(AjaxRequestTarget target, Form<?> form) {
		String findName=(String)((TextField<String>)form.get("edit_name")).getModelObject();
		// наполнить модель новыми данными
		this.fillFindedCustomer(findName);
		target.addComponent(this.panelCustomer);
		//target.addComponent(this.panelCartridge);
	}
	
	@SuppressWarnings("unchecked")
	private void onFormCartridgeSubmit(AjaxRequestTarget target, Form<?> form) {
		String findCartridge=(String)((TextField<String>)form.get("edit_name_cartridge")).getModelObject();
		this.fillFindedCustomerByCartridge(findCartridge);
		target.addComponent(this.panelCustomer);
		target.addComponent(this.panelCartridge);
	}

	/** */
	private void fillFindedCustomerByCartridge(String cartridgeCode){
		// очистить всех пользователей
		this.modelFindedCustomer.clear();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			// найти всех пользователей, которые подходят под маску поиска
			ResultSet rs=connection.createStatement().executeQuery(" select customer.id, customer.surname, customer.name from order_list "+
																   " inner join order_group on order_group.id=order_list.id_order_group "+
																   " inner join customer on customer.id=order_group.id_customer "+
																   " where order_list.unique_number="+cartridgeCode );
			if(rs.next()){
				// модель поиска по пользователю
				this.customerId=rs.getInt("ID");
				this.modelFindedCustomer.add(new FindedCustomerBean(rs.getInt("ID"), 
																	rs.getString("NAME"), 
																	rs.getString("SURNAME")));
				// заполнить по пользователю картриджы
				this.fillFindedCartridge(this.customerId);
			}else{
				// нет пользователя по данному картриджу
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("FindUser#fillFindedCustomer Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** наполнить модель найденных пользователей необходимыми данными */
	private void fillFindedCustomer(String findName){
		// очистить всех пользователей
		this.modelFindedCustomer.clear();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			// найти всех пользователей, которые подходят под маску поиска
			ResultSet rs=connection.createStatement().executeQuery("select * from customer"+
																   " where rupper(customer.surname) like rupper('%"+findName+"%')"+
																   " or (rupper(customer.name) like rupper('%"+findName+"%'))");
			while(rs.next()){
				this.modelFindedCustomer.add(new FindedCustomerBean(rs.getInt("ID"), 
																	rs.getString("NAME"), 
																	rs.getString("SURNAME")));
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("FindUser#fillFindedCustomer Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	/** наполнить данными по картриджам указанного пользователя */
	private void fillFindedCartridge(Integer customerId){
		// заполнить фамилию и отчество 
		this.modelCartridgeCaption.setObject(this.getString("fieldset_legend_cartridge"));
		// заполнить по клиенту все картриджы, которые он приносил
		this.modelFindedCartridge.clear();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			Customer customer=(Customer)session.get(Customer.class,customerId);
			String title= 
			             ((customer.getSurname()==null)?"":customer.getSurname())+" "
			             +((customer.getName()==null)?"":customer.getName());
			this.modelCartridgeCaption.setObject(title);
			
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery("select * from get_cartridge_by_customer("+customerId+")");
			while(rs.next()){
				this.modelFindedCartridge.add(new FindedCartridgeBean(rs.getInt("ID_MODEL"),
																	  rs.getString("VENDOR_NAME"),
																	  rs.getString("MODEL_NAME")));
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("FindUser#fillFindedCustomer Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		
	}
	
	@Override
	public void action(String actionName, Object argument) {
		while(true){
			if(actionName.equals("CUSTOMER")){
				Object[] objects=(Object[])argument;
				AjaxRequestTarget target=(AjaxRequestTarget)objects[0];
				this.customerId=(Integer)objects[1];
				this.fillFindedCartridge(this.customerId);
				target.addComponent(this.panelCartridge);
				break;
			}
			break;
		}
	}
	
	/** получить уникальный номер пользователя, по которому выделены картриджы*/
	public Integer getCustomerId(){
		return this.customerId;
	}
	/** получить все перечисленные пользователем картриджы */
	public ArrayList<FindedCartridgeBean> getSelectedCartridge(){
		return this.modelFindedCartridge;
	}
}
