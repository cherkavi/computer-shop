package window.main_menu.utility.edit_vendor_model.editor;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

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
import database.wrap.CartridgeModel;
import database_reflect.ReflectWorker;
import wicket_extension.UserApplication;
import wicket_extension.action.Action;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_vendor_model.EditVendorModel;

public class EditModel extends WindowEmulator{
	private Form<Object> formMain;
	private String editValue=null;
	private String vendorName=null;
	private TextField<String> model;
	private TextField<String> price;
	private final DecimalFormat priceFormat=new DecimalFormat("#.00");
	private Action actionOk;
	private Action actionCancel;
	
	/** добавление/редактирование "Производителя" */
	public EditModel(String vendorName, String editValue){
		super();
		this.vendorName=vendorName;
		if(editValue==null){
			// добавление данных
			this.setTitle("Добавление Модели по производителю ("+vendorName+")");
		}else{
			//редактирование данных
			this.setTitle("Редактирование модели ("+editValue+") по производителю ("+vendorName+")");
			this.editValue=editValue;
		}
		initComponents();
	}

	/** добавление/редактирование "Производителя" */
	public EditModel(String vendorName, String editValue, Action actionOk, Action actionCancel){
		super();
		this.actionOk=actionOk;
		this.actionCancel=actionCancel;
		this.vendorName=vendorName;
		if(editValue==null){
			// добавление данных
			this.setTitle("Добавление Модели по производителю ("+vendorName+")");
		}else{
			//редактирование данных
			this.setTitle("Редактирование модели ("+editValue+") по производителю ("+vendorName+")");
			this.editValue=editValue;
		}
		initComponents();
	}
	
	
	/** получить прайсовую цену по модели */
	private Float getPriceByModelName(){
		Float price=null;
		Integer vendorCode=this.getVendorCode(this.vendorName);
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			price=((CartridgeModel)session.createCriteria(CartridgeModel.class)
												.add(Restrictions.eq("idVendor", vendorCode))
											   	.add(Restrictions.eq("name", this.editValue))
											   		.list().get(0))
											   			.getPrice();
		}catch(Exception ex){
			System.out.println("EditVendorModel#onChangeModel Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return price;
	}
	
	
	public void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("model_caption","Наименование модели"));
		this.model=new TextField<String>("model_text",new Model<String>(editValue));
		formMain.add(this.model);
		
		formMain.add(new Label("price_caption","Цена"));
		this.price=new TextField<String>("price_text",new Model<String>(""));
		if(this.editValue!=null){
			try{
				this.price.setModelObject(this.priceFormat.format(this.getPriceByModelName()));
			}catch(Exception ex){
			}
		}
		formMain.add(this.price);
		
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
		String modelNewName=this.model.getModelObject();
		String priceNew=this.price.getModelObject();
		while(true){
			// проверить на ввод валидной цены 
			if(priceNew==null){
				this.formMain.error("Введите цену");
				break;
			}

			Float priceValue=null;
			try{
				priceValue=Float.parseFloat(priceNew);
			}catch(Exception ex){
				this.formMain.error("Цена введена неверно");
			}
			
			// проверить на не пустой ввод значения
			if((modelNewName==null)||(modelNewName.trim().equals(""))){
				this.formMain.error("Введите имя Модели");
				break;
			}
			
			// проверить на нажатие Сохранить не изменяя значения 
			/*if((this.editValue!=null)&&(this.editValue.equals(modelNewName))){
				this.formMain.error("Имя не изменено, введите другое имя");
				break;
			}*/

			// проверить на наличие данного значения в базе данных 
			if((this.editValue==null)&&(isModelExists(this.vendorName, modelNewName))){
				this.formMain.error("Данное имя уже существует");
				break;
			}
			
			if(saveModel(this.vendorName,modelNewName,priceValue)){
				if(this.actionOk!=null){
					try{
						this.actionOk.addMethodForCall("setModel", 
													   new Class[]{String.class,String.class}, 
													   new Object[]{this.vendorName, modelNewName});
						this.setResponsePage(this.actionOk.getRedirectPage());
					}catch(Exception ex){
						formMain.error("Ошибка движения по графу - перейдите в главное меню");
					}
				}else{
					EditVendorModel editVendorModel=new EditVendorModel();
					editVendorModel.setModel(vendorName, modelNewName);
					this.setResponsePage(editVendorModel); 
				}
			}else{
				this.formMain.error("Ошибка сохранения данных");
				break;
			}
			
			break;
		}
	}
	
	/** сохранить значение Vendor */
	private boolean saveModel(String vendorName, String modelNewName,Float priceValue){
		boolean returnValue=false;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Integer vendorCode=getVendorCode(vendorName);
		try{
			Session session=connector.getSession();
			CartridgeModel cartridgeModel=null;
			Object object=null;
			try{
				object=session.createCriteria(CartridgeModel.class)
								.add(Restrictions.eq("idVendor", vendorCode))
				                .add(Restrictions.eq("name", editValue))
				                .list().get(0);
			}catch(Exception ex){};
			if(object!=null){
				cartridgeModel=(CartridgeModel)object;
				cartridgeModel.setForSend(cartridgeModel.getForSend()+1);
			}else{
				cartridgeModel=new CartridgeModel();
				cartridgeModel.setIdVendor(vendorCode);
			}
			cartridgeModel.setName(modelNewName);
			cartridgeModel.setPrice(priceValue);
			session.beginTransaction();
			if(this.editValue!=null){
				session.update(cartridgeModel);
			}else{
				session.save(cartridgeModel);
			}
			session.getTransaction().commit();
			returnValue=true;
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
		}catch(Exception ex){
			System.err.println("EditModel#saveVendor Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** проверить Модель по базе данных на повторение в базе */
	private boolean isModelExists(String controlVendorName, String controlModelName){
		boolean returnValue=true;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Integer vendorCode=getVendorCode(controlVendorName);
		try{
			Session session=connector.getSession();
			if(session.createCriteria(CartridgeModel.class).add(Restrictions.eq("idVendor",vendorCode))
													    .add(Restrictions.eq("name",controlModelName)).list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("EditModel#isVendorExists Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		
		return returnValue;
	}
	
	private void onButtonCancel(){
		if(this.actionCancel==null){
			EditVendorModel editVendorModel=new EditVendorModel();
			if(this.editValue!=null){
				editVendorModel.setModel(this.vendorName, this.editValue);
			}
			this.setResponsePage(editVendorModel); 
		}else{
			try{
				this.actionCancel.addMethodForCall("setModel", new Class[]{String.class,String.class}, new Object[]{this.vendorName, this.editValue});
				this.setResponsePage(this.actionCancel.getRedirectPage());
			}catch(Exception ex){
				formMain.error("Ошибка движения по графу - перейдите в главное меню");
			}
		}
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
	
}
