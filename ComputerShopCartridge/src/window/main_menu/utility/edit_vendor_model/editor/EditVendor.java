package window.main_menu.utility.edit_vendor_model.editor;

import java.sql.Connection;

import java.sql.PreparedStatement;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.CartridgeVendor;
import database_reflect.ReflectWorker;

import wicket_extension.UserApplication;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_vendor_model.EditVendorModel;

public class EditVendor extends WindowEmulator{
	private Form<Object> formMain;
	private String editValue=null;
	private TextField<String> vendor;
	/** добавление/редактирование "Производителя" */
	public EditVendor(String editValue){
		super();
		if(editValue==null){
			// добавление данных
			this.setTitle("Добавление производителя");
		}else{
			//редактирование данных
			this.setTitle("Редактирование производителя ("+editValue+")");
			this.editValue=editValue;
		}
		initComponents();
	}
	
	
	public void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.vendor=new TextField<String>("vendor_text",new Model<String>(editValue));
		formMain.add(this.vendor);
		
		Button buttonOk=new Button("button_save"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value","Сохранить"));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить"));
		formMain.add(buttonCancel);
		
		formMain.add(new ComponentFeedbackPanel("form_error",formMain));
	}
	
	private void onButtonOk(){
		String vendorNewName=this.vendor.getModelObject();
		while(true){
			// проверить на не пустой ввод значения
			if((vendorNewName==null)||(vendorNewName.trim().equals(""))){
				this.formMain.error("Введите имя Производителя");
				break;
			}
			
			// проверить на нажатие Сохранить не изменяя значения 
			if((this.editValue!=null)&&(this.editValue.equals(vendorNewName))){
				this.formMain.error("Имя не изменено, введите другое имя");
				break;
			}

			// проверить на наличие данного значения в базе данных 
			if(isVendorExists(vendorNewName)){
				this.formMain.error("Данное имя уже существует");
				break;
			}
			
			if(saveVendor(vendorNewName)){
				EditVendorModel editVendorModel=new EditVendorModel();
				editVendorModel.setVendor(vendorNewName);
				this.setResponsePage(editVendorModel); 
				break;
			}else{
				this.formMain.error("Ошибка сохранения данных");
			}
			
			break;
		}
	}
	
	/** сохранить значение Vendor */
	private boolean saveVendor(String vendorName){
		boolean returnValue=false;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			CartridgeVendor cartridgeVendor=null;
			if(this.editValue!=null){
				cartridgeVendor=(CartridgeVendor)session.createCriteria(CartridgeVendor.class).add(Restrictions.eq("name", editValue)).list().get(0);
				cartridgeVendor.setForSend(cartridgeVendor.getForSend()+1);
			}else{
				cartridgeVendor=new CartridgeVendor();
			}
			cartridgeVendor.setName(vendorName);
			session.beginTransaction();
			if(this.editValue!=null){
				session.update(cartridgeVendor);
			}else{
				session.save(cartridgeVendor);
			}
			session.getTransaction().commit();
			returnValue=true;
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
		}catch(Exception ex){
			System.err.println("EditVendor#saveVendor Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** проверить Производителя по базе данных на повторение в базе */
	private boolean isVendorExists(String controlVendorName){
		boolean returnValue=true;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			PreparedStatement ps=connection.prepareStatement("select * from cartridge_vendor where rupper(cartridge_vendor.name)=rupper(?)");
			ps.setString(1, controlVendorName);
			if(ps.executeQuery().next()){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("EditVendor#isVendorExists Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		
		return returnValue;
	}
	
	private void onButtonCancel(){
		EditVendorModel editVendorModel=new EditVendorModel();
		if(this.editValue!=null){
			editVendorModel.setVendor(editValue);
		}
		this.setResponsePage(editVendorModel); 
	}
}
