package window.main_menu.create_order.edit_model;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.CartridgeModel;
import database_reflect.ReflectWorker;

import wicket_extension.UserApplication;
import wicket_extension.gui.ajax_feedback.AjaxFeedbackLabel;
import window.main_menu.create_order.panel_order.PanelOrder;

/** панель для редактирования модели, цены по модели, возможность добавления новой модели */
public class EditModel extends Panel{
	public enum ReturnValues{
		CANCEL, CREATE, EDIT_PRICE, EDIT_MODEL
	}
	/** результат работы данного модального окна */
	private ReturnValues returnValue=ReturnValues.CANCEL;
	
	private final static long serialVersionUID=1L;
	/** панель, которая редактируется */
	private PanelOrder panelOrder;
	/** цена товара, которая изначально заявлена */
	private TextField<String> price;
	/** ошибочные сообщения для поля EditPrice*/
	private AjaxFeedbackLabel feedbackPrice;
	/** модальное окно, на котором расположен данный компонент */
	private ModalWindow parentModalWindow;
	/** поле для создания новой модели (имя)*/
	private TextField<String> editCreateName;
	/** поле для создания новой модели (цена)*/
	private TextField<String> editCreatePrice;
	
	private AjaxFeedbackLabel feedbackCreate;
	
	/** панель для редактирования модели, цены по модели, возможность добавления новой модели 
	 * @param id - уникальный идентификатор модели 
	 * @param parentModalWindow - родительское окно, которое содержит данную панель
	 * @param editPanelOrder - панель для редактирования
	 * @param executor - исполнитель, который принимает текстовые команды 
	 */
	public EditModel(String id, ModalWindow parentModalWindow, PanelOrder editPanelOrder){
		super(id);
		this.panelOrder=editPanelOrder;
		this.parentModalWindow=parentModalWindow;
		this.initComponents();
	}
	
	private void initComponents(){
		// Vendor
		Label labelTitle=new Label("label_title","Редактирование модели по производителю (<b>"+this.panelOrder.getElement().getVendor()+"</b>)");
		labelTitle.setEscapeModelStrings(false);
		this.add(labelTitle);

		Form<?> formPrice=new Form<Object>("form_price");
		this.add(formPrice);

		// Edit Price
		Label labelEditModel=new Label("label_edit_model","<b>("+this.panelOrder.getElement().getModel()+")</b>");
		labelEditModel.setEscapeModelStrings(false);
		formPrice.add(labelEditModel);

		Float priceCurrent=getPriceByModelName(this.panelOrder.getElement().getVendor(),
				 this.panelOrder.getElement().getModel());
		price=new TextField<String>("price",
								    new Model<String>(Float.toString(priceCurrent)));
		formPrice.add(price);
		
		AjaxButton buttonSavePrice=new AjaxButton("button_save_price"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonSavePrice(target);
			}
		};
		buttonSavePrice.add(new SimpleAttributeModifier("value","Сохранить новую цену"));
		formPrice.add(buttonSavePrice);

		feedbackPrice=new AjaxFeedbackLabel("feedback_price");
		formPrice.add(feedbackPrice);

		// Create Model
		Form<?> formCreate=new Form<Object>("form_create");
		this.add(formCreate);
		
		editCreateName=new TextField<String>("edit_create_name",new Model<String>(this.panelOrder.getElement().getModel()));
		formCreate.add(editCreateName);
		editCreatePrice=new TextField<String>("edit_create_price",new Model<String>(Float.toString(priceCurrent)));
		formCreate.add(editCreatePrice);
		
		AjaxButton buttonSaveCreate=new AjaxButton("button_create"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonSaveCreate(target);
			}
		};
		buttonSaveCreate.add(new SimpleAttributeModifier("value","Создать Новую модель"));
		formCreate.add(buttonSaveCreate);
		
		this.feedbackCreate=new AjaxFeedbackLabel("feedback_create");
		formCreate.add(this.feedbackCreate);
		
		// кнопка отмены 
		Button buttonCancel=new Button("button_cancel");
		buttonCancel.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonCancel(target);
			}
		});
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить редактирование"));
		this.add(buttonCancel);
	}
	
	
	/** получить прайсовую цену по модели */
	private Float getPriceByModelName(String vendorName, String modelName){
		Float price=null;
		Integer vendorCode=this.getVendorCode(vendorName);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			price=((CartridgeModel)session.createCriteria(CartridgeModel.class)
												.add(Restrictions.eq("idVendor", vendorCode))
											   	.add(Restrictions.eq("name", modelName))
											   		.list().get(0))
											   			.getPrice();
		}catch(Exception ex){
			System.out.println("EditVendorModel#onChangeModel Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return price;
	}
	

	/** получить Vendor ID на основании vendorName
	 * @param vendorName - имя Vendor
	 * @return код Vendor
	 */
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

	
	private void onButtonSavePrice(AjaxRequestTarget target){
		Float price=this.getFloatFromString(this.price.getModelObject());
		if(price!=null){
			// установить цену, сохранить данные
			this.panelOrder.setPriceToModel(price);
			// сохранить цену в базе данных по данному товару
			String saveResult=this.savePriceToModel(this.panelOrder.getElement().getModel(), this.panelOrder.getElement().getVendor(), price);
			if(saveResult==null){
				// данные успешно сохранены
				target.addComponent(this.panelOrder);
				this.returnValue=ReturnValues.EDIT_PRICE;
				// закрыть данное окно  
				this.parentModalWindow.close(target);
			}else{
				// данные сохранены не успешно
				this.feedbackPrice.setText("Ошибка сохранения данных");
				this.sendFeedback(target);
			}
		}else{
			this.feedbackPrice.setText("Введите корректную сумму ");
			this.sendFeedback(target);
		}
	}
	
	/** получить результат выполнения данного модального окна */
	public ReturnValues getResult(){
		return this.returnValue;
	}
	
	/** сохранить цену по данному товару и ассортименту */
	private String savePriceToModel(String modelName, String vendorName, Float price ){
		String returnValue=null;
		Integer vendorCode=this.getVendorCode(vendorName);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeModel cartridgeModel=((CartridgeModel)session.createCriteria(CartridgeModel.class)
												.add(Restrictions.eq("idVendor", vendorCode))
											   	.add(Restrictions.eq("name", modelName))
											   		.list().get(0));
			session.beginTransaction();
			cartridgeModel.setPrice(price);
			cartridgeModel.setForSend(cartridgeModel.getForSend()+1);
			session.update(cartridgeModel);
			session.getTransaction().commit();
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
		}catch(Exception ex){
			System.out.println("EditVendorModel#onChangeModel Exception: "+ex.getMessage());
			returnValue=ex.getMessage();
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** получить из текстовой строки float значение и вернуть его */
	private Float getFloatFromString(String value){
		Float returnValue=null;
		try{
			returnValue=Float.parseFloat(value.replace(',', '.'));
		}catch(Exception ex){
		}
		return returnValue;
	}
	
	/** отменить редактирование */
	private void onButtonCancel(AjaxRequestTarget target){
		this.returnValue=ReturnValues.CANCEL;
		this.parentModalWindow.close(target);
	}
	
	/** реакция на нажатие кнопки создания новой модели по данному производителю */
	private void onButtonSaveCreate(AjaxRequestTarget target){
		String model=this.editCreateName.getModelObject();
		String priceString=this.editCreatePrice.getModelObject();
		while(true){
			Float price=this.getFloatFromString(priceString);
			if(price==null){
				this.feedbackCreate.setText("Введите валидную цену");
				this.sendFeedback(target);
				break;
			}
			if((model==null)||(model.trim().equals(""))){
				this.feedbackCreate.setText("Введите имя новой модели ");
				this.sendFeedback(target);
				break;
			}
			// проверить новую модель на повторение по указанному Производителю			
			if(isModelOnVendorExists(this.panelOrder.getElement().getVendor(), model)==true){
				// данная модель уже существует
				this.feedbackCreate.setText("Такая модель уже существует");
				this.sendFeedback(target);
				break;
			}
			// сохранить новую модель по производителю
			String saveResult=this.saveNewModel(this.panelOrder.getElement().getVendor(), model);
			if(saveResult!=null){
				this.feedbackCreate.setText("Ошибка сохранения данных");
				this.sendFeedback(target);
				break;
			}else{
				this.returnValue=ReturnValues.CREATE;
				this.panelOrder.refreshModelList();
				this.panelOrder.setModel(model);
				this.parentModalWindow.close(target);
			}
			break;
		}
	}

	/**  
	 * @param vendor - производитель
	 * @param model - модель 
	 * @return 
	 * <li> null - данные успешно сохранены </li>
	 * <li> false - ошибка сохранения данных </li>
	 */
	private String saveNewModel(String vendor, String model){
		String returnValue=null;
		Integer vendorKod=this.getVendorCode(vendor);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeModel cartridgeModel=new CartridgeModel();
			cartridgeModel.setIdVendor(vendorKod);
			cartridgeModel.setName(this.editCreateName.getModelObject());
			cartridgeModel.setPrice(this.getFloatFromString(this.editCreatePrice.getModelObject()));
			session.beginTransaction();
			session.save(cartridgeModel);
			session.getTransaction().commit();
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
		}catch(Exception ex){
			System.err.println("EditModel#saveNewModel Exception: "+ex.getMessage());
			returnValue=ex.getMessage();
		}finally{
			
		}
		return returnValue;
	}
	
	
	private final static String queryCheckModelRepeat="select * from cartridge_model where id_vendor=? and trim(rupper(name)) like trim(rupper(?))";
	/**  
	 * @param vendor - производитель
	 * @param model - модель 
	 * @return 
	 * <li> true - модель существует </li>
	 * <li> false - не существует </li>
	 */
	private boolean isModelOnVendorExists(String vendor, String model){
		boolean returnValue=true;
		Integer vendorKod=this.getVendorCode(vendor);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			PreparedStatement ps=connector.getConnection().prepareStatement(queryCheckModelRepeat);
			ps.setInt(1, vendorKod);
			ps.setString(2, model);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("EditModel#isModelOnVendorExists Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	private void sendFeedback(AjaxRequestTarget target){
		target.addComponent(this.feedbackCreate);
		target.addComponent(this.feedbackPrice);
	}
}
