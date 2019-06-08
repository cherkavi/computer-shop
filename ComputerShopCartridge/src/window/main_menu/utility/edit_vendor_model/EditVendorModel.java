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

/** ��������������/���������� ��������������/������� �� �������������� */
public class EditVendorModel extends WindowEmulator{
	private Form<Object> formMain;
	private DropDownChoice<String> dropDownVendor;
	private DropDownChoice<String> dropDownModel;
	private Model<String> modelPrice=new Model<String>("");
	private final DecimalFormat priceFormat=new DecimalFormat("#.00");
	
	/** ��������������/���������� ��������������/������� �� �������������� */
	public EditVendorModel(){
		super("��������������/���������� ������� � ��������������");
		initComponents();
	}
	
	
	/** ���������� ������������� �� ����� */
	public void setVendor(String vendorName){
		int index=this.dropDownVendor.getChoices().indexOf(vendorName);
		if(index>=0){
			this.dropDownVendor.setDefaultModelObject(this.dropDownVendor.getChoices().get(index));
		}
	}
	
	/** ��������/������������� ����������� */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("caption_vendor","�������������"));
		formMain.add(new Label("caption_model","������"));
		
		Button buttonVendorAdd=new Button("button_vendor_add"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorAdd();
			}
		};
		buttonVendorAdd.add(new SimpleAttributeModifier("value", "��������"));
		formMain.add(buttonVendorAdd);

		Button buttonVendorEdit=new Button("button_vendor_edit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorEdit();
			}
		};
		buttonVendorEdit.add(new SimpleAttributeModifier("value", "�������������"));
		formMain.add(buttonVendorEdit);
		
		Button buttonVendorRemove=new Button("button_vendor_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onVendorRemove();
			}
		};
		buttonVendorRemove.add(new SimpleAttributeModifier("value", "�������"));
		formMain.add(buttonVendorRemove);

		Button buttonModelAdd=new Button("button_model_add"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelAdd();
			}
		};
		buttonModelAdd.add(new SimpleAttributeModifier("value","��������"));
		formMain.add(buttonModelAdd);

		Button buttonModelEdit=new Button("button_model_edit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelEdit();
			}
		};
		buttonModelEdit.add(new SimpleAttributeModifier("value","�������������"));
		formMain.add(buttonModelEdit);
		
		Button buttonModelRemove=new Button("button_model_remove"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onModelRemove();
			}
		};
		buttonModelRemove.add(new SimpleAttributeModifier("value","�������"));
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
		buttonMainMenu.add(new SimpleAttributeModifier("value","������� ����"));
		formMain.add(buttonMainMenu);
		
		formMain.add(new Label("price_caption","����:"));
		formMain.add(new Label("price_value",this.modelPrice));
	}
	
	private void onButtonMainMenu(){
		this.setResponsePage(MainMenu.class);
	}
	
	/** ������� �� ��������� Vendor */
	private void onChangeVendor(String newValue){
		Integer idVendor=this.getVendorCode(newValue);
		ArrayList<String> choicesModel=this.getListFromTableWithEmpty("select * from cartridge_model where id_vendor="+idVendor, "name");
		this.dropDownModel.setChoices(choicesModel);
		this.dropDownModel.setModelObject(choicesModel.get(0));
		this.modelPrice.setObject("");
	}

	/** �������� ArrayList �� ��������� ������� � ���� ������ � ����� ������� �� ����� ������� 
	 * @param sql - ������ � ���� ������, �� ��������� �������� ����� �������� ������
	 * @param column - ��� �������, �� ������� ����� "��������" ������ 
	 * @return ArrayList � ������ ������� � ������ 
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
	
	/** �������� ������ ArrayList � ����� ������ ������� */
	private ArrayList<String> getEmptyArrayList(){
		ArrayList<String> returnValue=new ArrayList<String>();
		returnValue.add("");
		return returnValue;
	}

	
	/** �������� Vendor ID �� ��������� vendorName
	 * @param vendorName - ��� Vendor
	 * @return ��� Vendor
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
	
	
	/** �������� ������������� */
	private void onVendorAdd(){
		this.setResponsePage(new EditVendor(null)); 
	}
	
	/** ������������� ������������� */
	private void onVendorEdit(){
		String vendorValue=this.dropDownVendor.getModelObject();
		if((vendorValue==null)||(vendorValue.equals(""))){
			// ������������� ������ ����
			this.formMain.error("�������� ���� �����");
		}else{
			this.setResponsePage(new EditVendor(vendorValue));
		}
	}
	/** ������� ������������� */
	private void onVendorRemove(){
		while(true){
			String vendor=this.dropDownVendor.getModelObject();
			// ��������� �� ������� ���������� ������������� 
			if((vendor==null)||(vendor.trim().equals(""))){
				this.formMain.error("�������� ���� �����");
				break;
			}
			// ��������� �� ������� �������� ��������� � ������
			if(this.isVendorExistsChild(this.dropDownVendor.getModelObject())){
				this.formMain.error("���������� ������� ��� �������� �������� ��� ������� �������������");
				break;
			}

			Action actionOk=new Action(EditVendorModel.class);
			actionOk.addMethodForCall("removeVendor",new Class<?>[]{String.class},new Object[]{vendor});
			Action actionCancel=new Action(EditVendorModel.class);
			this.setResponsePage(new ConfirmMessage("������������� ��������", "������� � �������� ������������� "+vendor+" ?", actionOk, actionCancel, "�������", "��������"));
			
			break;
		}
		
	}
	
	/** ������� Vendor �� ��� ����� */
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
	
	/** ���������, ���������� �� �������� �������� �� ������� ������������� */
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
	
	/** �������� ������������� */
	private void onModelAdd(){
		String vendor=this.dropDownVendor.getModelObject();
		while(true){
			if((vendor==null)||(vendor.trim().equals(""))){
				formMain.error("�������� ������������� ");
				break;
			}
			EditModel editModel=new EditModel(vendor,null);
			this.setResponsePage(editModel);

			break;
		}
	}
	/** ������������� ������������� */
	private void onModelEdit(){
		String vendor=this.dropDownVendor.getModelObject();
		String model=this.dropDownModel.getModelObject();
		while(true){
			if((vendor==null)||(vendor.trim().equals(""))){
				formMain.error("�������� ������������� ");
				break;
			}
			if((model==null)||(model.trim().equals(""))){
				formMain.error("�������� ������");
				break;
			}
			EditModel editModel=new EditModel(vendor,model);
			this.setResponsePage(editModel);

			break;
		}
	}
	/** ������� ������������� */
	private void onModelRemove(){
		while(true){
			String vendor=this.dropDownVendor.getModelObject();
			String model=this.dropDownModel.getModelObject();
			// ��������� �� ������� ��������� ������ 
			if((vendor==null)||(vendor.trim().equals(""))){
				this.formMain.error("�������� ���� �����");
				break;
			}

			Action actionOk=new Action(EditVendorModel.class);
			actionOk.addMethodForCall("removeModel",new Class<?>[]{String.class,String.class},new Object[]{vendor,model});
			Action actionCancel=new Action(EditVendorModel.class);
			this.setResponsePage(new ConfirmMessage("������������� ��������", "������� � �������� ������ "+model+" ("+vendor+") ?", actionOk, actionCancel, "�������", "��������"));
			
			break;
		}
	}
	
	/** ������� ��������� ������ 
	 * @param vendorName - ��� ������������� 
	 * @param modelName - ��� ������ � ������� ������������� 
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

	/** ���������� ��������� ������ �� ��������� */
	public void setModel(String vendorName, String editValue) {
		this.setVendor(vendorName);
		// ��������� ������� 
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
