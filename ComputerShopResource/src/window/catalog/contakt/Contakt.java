package window.catalog.contakt;

import java.sql.ResultSet;

import java.text.DecimalFormat;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import database.ConnectWrap;
import database.wrap.Assortment;
import database.wrap.Price;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;

/** панель-заголовок дл€ базовой страницы */
public class Contakt extends Panel{
	private final static long serialVersionUID=1L;
	private Integer assortmentKod;
	private IActionExecutor executor;
	private TextField<String> editName;
	private TextField<String> editAddress;
	private TextField<String> editPhone;
	
	public Contakt(String id,IActionExecutor executor, Integer assortmentKod){
		super(id);
		this.executor=executor;
		this.assortmentKod=assortmentKod;
		initComponents();
	}

	/** формат цены  */
	private DecimalFormat priceFormat=new DecimalFormat("#.00");
	
	/** первоначальна€ инициализаци€ компонентов */
	private void initComponents(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Assortment assortment=this.getAssortmentByCommodityId(connector, assortmentKod);
		database.wrap.Class assortmentClass=this.getClassById(connector, assortment.getClass_kod());
		Price price=this.getPriceById(connector, assortment.getPrice_kod());
		float course=this.getCourse(connector);
		connector.close();
		

		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("class",assortmentClass.getName()));
		formMain.add(new Label("name",assortment.getName()));
		formMain.add(new Label("note",assortment.getNote()));
		formMain.add(new Label("warranty",assortment.getWarranty_month()+" мес."));
		formMain.add(new Label("price_usd",price.getPrice()+" $"));
		formMain.add(new Label("price",priceFormat.format(price.getPrice()*course)+" грн."));
		
		editName=new TextField<String>("edit_name",new Model<String>(""));
		editName.setRequired(false);
		formMain.add(editName);
		formMain.add(new ComponentFeedbackPanel("feedback_name",editName));
		
		editAddress=new TextField<String>("edit_address",new Model<String>(""));
		editAddress.setRequired(false);
		formMain.add(editAddress);
		formMain.add(new ComponentFeedbackPanel("feedback_address",editAddress));
		
		editPhone=new TextField<String>("edit_phone",new Model<String>(""));
		editPhone.setRequired(false);
		formMain.add(editPhone);
		formMain.add(new ComponentFeedbackPanel("feedback_phone",editPhone));
		
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_ok")));
		formMain.add(buttonOk);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}
	
	private void onButtonOk(){
		while(true){
			String name=this.editName.getModelObject();
			//String address=this.editAddress.getModelObject();
			String phone=this.editPhone.getModelObject();

			// проверка »мени
			if((name==null)||(name.equals(""))){
				this.editName.error(this.getString("error_name"));
				break;
			}
			// проверка јдреса
			/*if((address==null)||(address.equals(""))){
				this.editAddress.error(this.getString("error_address"));
				break;
			}*/
			// проверка телефона
			if((phone==null)||(phone.equals(""))){
				this.editPhone.error(this.getString("error_phone_is_empty"));
				break;
			}else{
				String clearPhone=phone.replaceAll("[+ - () _]", "");
				if((clearPhone.matches("[0-9]{12}"))||(clearPhone.matches("[0-9]{7}"))){
					// Phone is OK
				}else{
					this.editPhone.error(this.getString("error_phone"));
					break;
				}
			}
			// TODO оформить заказ 
			this.executor.action("CONTAKT.OK", this.assortmentKod);
			break;
		}
	}
	private void onButtonCancel(){
		this.executor.action("CONTAKT.CANCEL", null); 
	}
	
	/** получить јссортимент товара */
	private Assortment getAssortmentByCommodityId(ConnectWrap connector,Integer assortmentKod){
		Assortment returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(Assortment)session.get(Assortment.class, assortmentKod);
		}catch(Exception ex){
			System.err.println("Description#getAssortmentByCommodityId Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	/** получить  ласс товара */
	private database.wrap.Class getClassById(ConnectWrap connector, Integer classId){
		database.wrap.Class returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(database.wrap.Class)session.get(database.wrap.Class.class, classId);
		}catch(Exception ex){
			System.err.println("Description#getAssortmentByCommodityId Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	/** получить цену товара */
	private Price getPriceById(ConnectWrap connector, Integer priceId){
		Price returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(Price)session.get(Price.class, priceId);
		}catch(Exception ex){
			System.err.println("Description#getPriceById Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	/** получить курс валют по сегодн€шней дате*/
	private float getCourse(ConnectWrap connector) {
		float returnValue=0;
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select course.currency_value from course where course.date_set<='now' order by course.kod desc limit 1");
			if(rs.next()){
				returnValue=rs.getFloat(1);
			}
		}catch(Exception ex){
			System.err.println("Table#getCourse Exception ex: "+ex.getMessage());
		}
		return returnValue;
	}
	
}
