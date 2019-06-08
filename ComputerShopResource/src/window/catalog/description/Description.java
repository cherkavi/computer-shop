package window.catalog.description;

import java.sql.ResultSet;

import java.text.DecimalFormat;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

import database.ConnectWrap;
import database.wrap.List_section;

import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;

/** панель-заголовок для базовой страницы */
public class Description extends Panel{
	private final static long serialVersionUID=1L;
	private Integer assortmentKod;
	private IActionExecutor executor;
	
	public Description(String id,IActionExecutor executor, Integer assortmentKod){
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
		database.wrap.List assortment=this.getAssortmentByCommodityId(connector, assortmentKod);
		//Assortment assortment=this.getAssortmentByCommodityId(connector, assortmentKod);
		//database.wrap.Class assortmentClass=this.getClassById(connector, assortment.getClass_kod());
		List_section assortmentClass=this.getClassById(connector, assortment.getKod_section());
		
		float course=this.getCourse(connector);
		connector.close();
		this.add(new Label("class",assortmentClass.getName()));
		this.add(new Label("name",assortment.getName()));
		this.add(new Label("warranty",assortment.getWarranty()+" мес."));
		this.add(new Label("price_usd",assortment.getPrice_4()+" $"));
		this.add(new Label("price",priceFormat.format(assortment.getPrice_4()*course)+" грн."));
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
	
		Button buttonOrder=new Button("button_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOrder();
			};
		};
		buttonOrder.add(new SimpleAttributeModifier("value",this.getString("caption_button_order")));
		formMain.add(buttonOrder);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}
	
	private void onButtonReserv(){
		this.executor.action("DESCRIPTION.RESERV", this.assortmentKod);
	}
	private void onButtonOrder(){
		this.executor.action("DESCRIPTION.ORDER", this.assortmentKod); 
	}
	private void onButtonCancel(){
		this.executor.action("DESCRIPTION.CANCEL", this.assortmentKod); 
	}
	
	/** получить Ассортимент товара */
	private database.wrap.List getAssortmentByCommodityId(ConnectWrap connector,Integer assortmentKod){
		database.wrap.List returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(database.wrap.List)session.get(database.wrap.List.class, assortmentKod);
		}catch(Exception ex){
			System.err.println("Description#getAssortmentByCommodityId Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	/** получить Класс товара */
	private List_section getClassById(ConnectWrap connector, Integer classId){
		List_section returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(List_section)session.get(List_section.class, classId);
		}catch(Exception ex){
			System.err.println("Description#getAssortmentByCommodityId Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	/** получить цену товара */
	/*private Price getPriceById(ConnectWrap connector, Integer priceId){
		Price returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(Price)session.get(Price.class, priceId);
		}catch(Exception ex){
			System.err.println("Description#getPriceById Exception: "+ex.getMessage());
		}
		return returnValue;
	}*/
	
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
