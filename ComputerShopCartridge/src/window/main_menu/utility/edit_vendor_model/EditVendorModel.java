package window.main_menu.utility.edit_vendor_model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.CartridgeModel;
import database.wrap.CartridgeVendor;
import wicket_extension.UserApplication;
import wicket_extension.action.Action;
import wicket_extension.gui.option_pane.ConfirmMessage;
import window.main_menu.MainMenu;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_vendor_model.editor.EditModel;
import window.main_menu.utility.edit_vendor_model.editor.EditVendor;

/** Редактирование/добавление Производителей/Моделей по производителям */
public class EditVendorModel extends WindowEmulator{
	private Form<Object> formMain;
	private DropDownChoice<String> dropDownVendor;
	private DropDownChoice<String> dropDownModel;
	private Model<String> modelPrice=new Model<String>("");
	private final DecimalFormat priceFormat=new DecimalFormat("#.00");
	
	/** Редактирование/добавление Производителей/Моделей по производителям */
	public EditVendorModel(){
		super("Редактирование/Добавление моделей и производителей");
		initComponents();
	}
	
	
	/** установить производителя по имени */
	public void setVendor(String vendorName){
		int index=this.dropDownVendor.getChoices().indexOf(vendorName);
		if(index>=0){
			this.dropDownVendor.setDefaultModelObject(this.dropDownVendor.getChoices().get(index));
		}
	}
	
	/** создание/инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("caption_vendor","Производитель"));
		formMain.add(new Label("caption_model","Модель"));
		
		Button buttonVendorAdd=new Button("button_vendor_add"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorAdd();
			}
		};
		buttonVendorAdd.add(new SimpleAttributeModifier("value", "добавить"));
		formMain.add(buttonVendorAdd);

		Button buttonVendorEdit=new Button("button_vendor_edit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorEdit();
			}
		};
		buttonVendorEdit.add(new SimpleAttributeModifier("value", "редактировать"));
		formMain.add(buttonVendorEdit);
		
		Button buttonVendorRemove=new Button("button_vendor_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorRemove();
			}
		};
		buttonVendorRemove.add(new SimpleAttributeModifier("value", "удалить"));
		formMain.add(buttonVendorRemove);

		Button buttonModelAdd=new Button("button_model_add"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelAdd();
			}
		};
		buttonModelAdd.add(new SimpleAttributeModifier("value","добавить"));
		formMain.add(buttonModelAdd);

		Button buttonModelEdit=new Button("button_model_edit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelEdit();
			}
		};
		buttonModelEdit.add(new SimpleAttributeModifier("value","редактировать"));
		formMain.add(buttonModelEdit);
		
		Button buttonModelRemove=new Button("button_model_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelRemove();
			}
		};
		buttonModelRemove.add(new SimpleAttributeModifier("value","удалить"));
		formMain.add(buttonModelRemove);
		
		ArrayList<String> listVendor=this.getListFromTableWithEmpty("select * from cartridge_vendor", "name");
		dropDownVendor=new DropDownChoice<String>("select_vendor"){
			private final static long serialVersionUID=1L;
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			};
			@Override
			protected void onSelectionChanged(String newSelection) {
				onChangeVendor(newSelection);
			}
		};
		dropDownVendor.setChoices(listVendor);
		dropDownVendor.setDefaultModel(new Model<String>(listVendor.get(0)));
		formMain.add(dropDownVendor);
		
		dropDownModel=new DropDownChoice<String>("select_model"){
			private final static long serialVersionUID=1L;
			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			@Override
			protected void onSelectionChanged(String newSelection) {
				onChangeModel(newSelection);
			}
		};
		ArrayList<String> selectModelChoices=this.getEmptyArrayList();
		dropDownModel.setChoices(selectModelChoices);
		dropDownModel.setDefaultModel(new Model<String>(selectModelChoices.get(0)));
		formMain.add(dropDownModel);
		
		formMain.add(new ComponentFeedbackPanel("form_error", formMain));
		
		Button buttonMainMenu=new Button("button_main_menu"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonMainMenu();
			};
		};
		buttonMainMenu.add(new SimpleAttributeModifier("value","Главное меню"));
		formMain.add(buttonMainMenu);
		
		formMain.add(new Label("price_caption","Цена:"));
		formMain.add(new Label("price_value",this.modelPrice));
	}
	
	private void onButtonMainMenu(){
		this.setResponsePage(MainMenu.class);
	}
	
	/** реакция на изменения Vendor */
	private void onChangeVendor(String newValue){
		Integer idVendor=this.getVendorCode(newValue);
		ArrayList<String> choicesModel=this.getListFromTableWithEmpty("select * from cartridge_model where id_vendor="+idVendor, "name");
		this.dropDownModel.setChoices(choicesModel);
		this.dropDownModel.setModelObject(choicesModel.get(0));
		this.modelPrice.setObject("");
	}

	/** получить ArrayList на основании запроса к базе данных и имени колонки из этого запроса 
	 * @param sql - запрос к базе данных, на основании которого будут отобраны данные
	 * @param column - имя колонки, из которой будут "вытащены" данные 
	 * @return ArrayList с пустой строкой в начале 
	 */
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
	
	/** получить пустой ArrayList с одной пустой строкой */
	private ArrayList<String> getEmptyArrayList(){
		ArrayList<String> returnValue=new ArrayList<String>();
		returnValue.add("");
		return returnValue;
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
	
	
	/** добавить производителя */
	private void onVendorAdd(){
		this.setResponsePage(new EditVendor(null)); 
	}
	
	/** редактировать производителя */
	private void onVendorEdit(){
		String vendorValue=this.dropDownVendor.getModelObject();
		if((vendorValue==null)||(vendorValue.equals(""))){
			// редактировать пустое поле
			this.formMain.error("Сделайте свой выбор");
		}else{
			this.setResponsePage(new EditVendor(vendorValue));
		}
	}
	/** удалить производителя */
	private void onVendorRemove(){
		while(true){
			String vendor=this.dropDownVendor.getModelObject();
			// проверить на наличие выбранного производителя 
			if((vendor==null)||(vendor.trim().equals(""))){
				this.formMain.error("Сделайте свой выбор");
				break;
			}
			// проверить на наличие дочерних элементов в модели
			if(this.isVendorExistsChild(this.dropDownVendor.getModelObject())){
				this.formMain.error("Необходимо удалить все дочерние элементы для данного производителя");
				break;
			}

			Action actionOk=new Action(EditVendorModel.class);
			actionOk.addMethodForCall("removeVendor",new Class<?>[]{String.class},new Object[]{vendor});
			Action actionCancel=new Action(EditVendorModel.class);
			this.setResponsePage(new ConfirmMessage("Подтверждение удаления", "Уверены в удалении Производителя "+vendor+" ?", actionOk, actionCancel, "Удалить", "Отменить"));
			
			break;
		}
		
	}
	
	/** Удалить Vendor по его имени */
	public void removeVendor(String vendorName){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			Object object=session.createCriteria(CartridgeVendor.class).add(Restrictions.eq("name", vendorName)).list().get(0);
			session.beginTransaction();
			session.delete(object);
			session.getTransaction().commit();
			
			ArrayList<String> listVendor=this.getListFromTableWithEmpty("select * from cartridge_vendor", "name");
			dropDownVendor.setChoices(listVendor);
			dropDownVendor.setDefaultModel(new Model<String>(listVendor.get(0)));
		}catch(Exception ex){
			System.out.println("CreateOrder#getListFromTableWithEmpty: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
	}
	
	/** проверить, существуют ли дочерние элементы по данному Производителю */
	private boolean isVendorExistsChild(String vendorName){
		boolean returnValue=true;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		ResultSet rs=null;
		Integer vendorId=this.getVendorCode(vendorName);
		try{
			Connection connection=connector.getConnection();
			rs=connection.createStatement().executeQuery("SELECT * FROM CARTRIDGE_MODEL WHERE ID_VENDOR="+vendorId);
			if(rs.next()){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.out.println("CreateOrder#getListFromTableWithEmpty: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** добавить производителя */
	private void onModelAdd(){
		String vendor=this.dropDownVendor.getModelObject();
		while(true){
			if((vendor==null)||(vendor.trim().equals(""))){
				formMain.error("Выберите производителя ");
				break;
			}
			EditModel editModel=new EditModel(vendor,null);
			this.setResponsePage(editModel);

			break;
		}
	}
	/** редактировать производителя */
	private void onModelEdit(){
		String vendor=this.dropDownVendor.getModelObject();
		String model=this.dropDownModel.getModelObject();
		while(true){
			if((vendor==null)||(vendor.trim().equals(""))){
				formMain.error("Выберите производителя ");
				break;
			}
			if((model==null)||(model.trim().equals(""))){
				formMain.error("Выберите модель");
				break;
			}
			EditModel editModel=new EditModel(vendor,model);
			this.setResponsePage(editModel);

			break;
		}
	}
	/** удалить производителя */
	private void onModelRemove(){
		while(true){
			String vendor=this.dropDownVendor.getModelObject();
			String model=this.dropDownModel.getModelObject();
			// проверить на наличие выбранной модели 
			if((vendor==null)||(vendor.trim().equals(""))){
				this.formMain.error("Сделайте свой выбор");
				break;
			}

			Action actionOk=new Action(EditVendorModel.class);
			actionOk.addMethodForCall("removeModel",new Class<?>[]{String.class,String.class},new Object[]{vendor,model});
			Action actionCancel=new Action(EditVendorModel.class);
			this.setResponsePage(new ConfirmMessage("Подтверждение удаления", "Уверены в удалении Модели "+model+" ("+vendor+") ?", actionOk, actionCancel, "Удалить", "Отменить"));
			
			break;
		}
	}
	
	/** удалить указанную модель 
	 * @param vendorName - имя производителя 
	 * @param modelName - имя модели у данного производителя 
	 */
	public void removeModel(String vendorName, String modelName){
		Integer vendorId=this.getVendorCode(vendorName);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			Object removeObject=session.createCriteria(CartridgeModel.class).add(Restrictions.eq("idVendor", vendorId)).add(Restrictions.eq("name", modelName)).list().get(0);
			session.beginTransaction();
			session.delete(removeObject);
			session.getTransaction().commit();
		}catch(Exception ex){
			System.out.println("EditVendorModel#removeModel: "+ex.getMessage());
		}finally{
			connector.close();
		}
		
	}

	/** установить указанную модель по умолчанию */
	public void setModel(String vendorName, String editValue) {
		this.setVendor(vendorName);
		// наполнить данными 
		ArrayList<String> selectModelChoices=this.getListFromTableWithEmpty("select * from cartridge_model where id_vendor="+this.getVendorCode(vendorName), "NAME");
		dropDownModel.setChoices(selectModelChoices);
		int index=this.dropDownModel.getChoices().indexOf(editValue);
		if(index>=0){
			this.dropDownModel.setDefaultModelObject(this.dropDownModel.getChoices().get(index));
			onChangeModel(this.dropDownModel.getChoices().get(index));
		}
	}
	
	private void onChangeModel(String modelValue){
		this.modelPrice.setObject("");
		Integer vendorCode=this.getVendorCode(this.dropDownVendor.getModelObject());
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeModel currentModel=((CartridgeModel)session.createCriteria(CartridgeModel.class).add(Restrictions.eq("idVendor", vendorCode))
											   .add(Restrictions.eq("name", modelValue)).list().get(0));
			//System.out.println("Model: "+modelValue+"   Price: "+currentModel.getPrice());
			this.modelPrice.setObject(this.priceFormat.format(currentModel.getPrice().floatValue()));
		}catch(Exception ex){
			//System.out.println("EditVendorModel#onChangeModel Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
}
