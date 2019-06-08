package window.main_menu.utility;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import window.main_menu.MainMenu;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_vendor_model.EditVendorModel;
import window.main_menu.utility.point_settings.PointSettingsForm;

/** Служебные режимы для проекта */
public class Utility extends WindowEmulator{
	/** Служебные режимы для проекта */
	public Utility(){
		super("Служебные режимы");
		initComponents();
	}
	
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonVendor=new Button("button_vendor"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonVendor();
			};
		};
		buttonVendor.add(new SimpleAttributeModifier("value","Редактирование Производителя/модели"));
		formMain.add(buttonVendor);

		Button buttonPointSettings=new Button("button_point_settings"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonPointSettings();
			};
		};
		buttonPointSettings.add(new SimpleAttributeModifier("value","Настройка рабочего места"));
		formMain.add(buttonPointSettings);

		Button buttonBack=new Button("button_back"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonBack();
			};
		};
		buttonBack.add(new SimpleAttributeModifier("value","Главное меню"));
		formMain.add(buttonBack);
	}
	
	
	private void onButtonVendor(){
		this.setResponsePage(EditVendorModel.class);
	}
	
	private void onButtonPointSettings(){
		this.setResponsePage(PointSettingsForm.class); 
	}
	
	private void onButtonBack(){
		this.setResponsePage(MainMenu.class);
	}
}
