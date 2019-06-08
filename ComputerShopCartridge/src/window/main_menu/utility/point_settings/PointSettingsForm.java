package window.main_menu.utility.point_settings;

import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.PointSettings;

import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.Utility;

/** настройки рабочего места */
public class PointSettingsForm extends WindowEmulator{
	private Form<Object> formMain;
	/** принтер для накладных */
	private TextField<String> textFieldPrinter;
	/** принтер для штрих-кодов */
	private TextField<String> textFieldPrinterBarCode;
	/** заголовок для накладных */
	private TextField<String> textFieldReceiptTitle;
	public PointSettingsForm(){
		super("Настройка рабочего места");
		initComponents();
	}
	
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		formMain.add(new ComponentFeedbackPanel("form_error",formMain));
		
		PointSettings pointSettings=this.getCurrentPointSettings();
		
		formMain.add(new Label("label_printer","Принтер формата A4"));
		textFieldPrinter=new TextField<String>("textfield_printer",new Model<String>(pointSettings.getPrinter()));
		formMain.add(textFieldPrinter);
		
		formMain.add(new Label("label_printer_barcode","Принтер штрих-кодов"));
		textFieldPrinterBarCode=new TextField<String>("textfield_printer_barcode",
												      new Model<String>(pointSettings.getPrinterBarcode()));
		formMain.add(textFieldPrinterBarCode);

		formMain.add(new Label("label_receipt_title","Заголовок для Квитанции"));
		textFieldReceiptTitle=new TextField<String>("textfield_receipt_title",
													new Model<String>(pointSettings.getReceiptTitle()));
		formMain.add(textFieldReceiptTitle);
		
		Button buttonOk=new Button("button_ok"){
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
		
	}
	
	private PointSettings getCurrentPointSettings(){
		PointSettings pointSettings=null;
		Integer pointKod=((UserSession)this.getSession()).getPointNumber();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			pointSettings=(PointSettings)session.createCriteria(PointSettings.class).add(Restrictions.eq("idPoints", pointKod)).list().get(0);
		}catch(Exception ex){
			System.out.println("PointSettings#getCurrentPointSettings Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return (pointSettings==null)?(new PointSettings()):pointSettings;
	}
	
	private void onButtonOk(){
		if(save()==true){
			this.setResponsePage(Utility.class);
		}else{
			formMain.error("Ошибка сохранения данных");
		}
	}
	
	private boolean save(){
		boolean returnValue=false;
		String printer=this.textFieldPrinter.getModelObject();
		String printerBarCode=this.textFieldPrinterBarCode.getModelObject();
		String title=this.textFieldReceiptTitle.getModelObject();
		
		PointSettings pointSettings=null;
		Integer pointKod=((UserSession)this.getSession()).getPointNumber();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			
			List<?> list=session.createCriteria(PointSettings.class).add(Restrictions.eq("idPoints", pointKod)).list();
			if(list.size()>0){
				// есть данные по текущей точке - перезаписать
				pointSettings=(PointSettings)list.get(0);
			}else{
				pointSettings=new PointSettings();
				pointSettings.setIdPoints(pointKod);
			}
			pointSettings.setPrinter(printer);
			pointSettings.setPrinterBarcode(printerBarCode);
			pointSettings.setReceiptTitle(title);
			session.beginTransaction();
			session.saveOrUpdate(pointSettings);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.out.println("PointSettings#getCurrentPointSettings Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	private void onButtonCancel(){
		this.setResponsePage(Utility.class);
	}
}
