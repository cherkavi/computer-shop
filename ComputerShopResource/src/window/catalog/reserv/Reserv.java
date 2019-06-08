package window.catalog.reserv;

import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

import database.ConnectWrap;
import database.wrap.Assortment;
import database.wrap.Clients;
import database.wrap.Price;

import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import wicket_extension.action.IActionExecutor;

/** положить указанную ассортиментную единицу товара в резерв по данному пользователю  */
public class Reserv extends Panel{
	private final static long serialVersionUID=1L;
	private Integer assortmentKod;
	private IActionExecutor executor;
	private Clients client;
	
	public Reserv(String id,IActionExecutor executor, Integer assortmentKod){
		super(id);
		this.executor=executor;
		this.assortmentKod=assortmentKod;
		initComponents();
	}

	/** формат цены  */
	private DecimalFormat priceFormat=new DecimalFormat("#.00");
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		client=this.getClientById(connector, ((UserSession)this.getSession()).getUser().getKod());
		Assortment assortment=this.getAssortmentByCommodityId(connector, assortmentKod);
		database.wrap.Class assortmentClass=this.getClassById(connector, assortment.getClass_kod());
		Price price=this.getPriceById(connector, assortment.getPrice_kod());
		float course=this.getCourse(connector);
		connector.close();
		
		String helloMessage="Добрый день, "+((client.getName()==null)?"":client.getName())
										   +((client.getName()==null)?"":client.getSurname());
		this.add(new Label("message_hello",helloMessage));
		this.add(new Label("class",assortmentClass.getName()));
		this.add(new Label("name",assortment.getName()));
		this.add(new Label("note",assortment.getNote()));
		this.add(new Label("warranty",assortment.getWarranty_month()+" мес."));
		this.add(new Label("price_usd",price.getPrice()+" $"));
		this.add(new Label("price",priceFormat.format(price.getPrice()*course)+" грн."));
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		Button buttonReserv=new Button("button_reserv"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonReserv();
			};
		};
		buttonReserv.add(new SimpleAttributeModifier("value",this.getString("caption_button_reserv")));
		formMain.add(buttonReserv);
	

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}
	
	private Clients getClientById(ConnectWrap connector, Integer kod) {
		Clients returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(Clients)session.get(Clients.class, kod);
		}catch(Exception ex){
			System.err.println("Reserv#getClientById Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	private void onButtonReserv(){
		//TODO положить в базу по данному клиенту резерв данного товара
		// client
		this.executor.action("RESERV.OK", this.assortmentKod);
	}
	private void onButtonCancel(){
		this.executor.action("RESERV.CANCEL", this.assortmentKod); 
	}
	
	/** получить Ассортимент товара */
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
	
	/** получить Класс товара */
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
	
	/** получить курс валют по сегодняшней дате*/
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
