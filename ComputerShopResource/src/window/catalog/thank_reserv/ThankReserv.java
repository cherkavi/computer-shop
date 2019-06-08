package window.catalog.thank_reserv;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

import database.ConnectWrap;
import database.wrap.Clients;

import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import wicket_extension.action.IActionExecutor;

/** положить указанную ассортиментную единицу товара в резерв по данному пользователю  */
public class ThankReserv extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor executor;
	
	public ThankReserv(String id,IActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}

	/** первоначальная инициализация компонентов */
	private void initComponents(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Clients client=this.getClientById(connector, ((UserSession)this.getSession()).getUser().getKod());
		connector.close();
		
		String helloMessage="Добрый день, "+((client.getName()==null)?"":client.getName())
										   +((client.getName()==null)?"":client.getSurname()+
							"спасибо за установку данного товара в резерв"
							);
		this.add(new Label("message_hello",helloMessage));
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_ok")));
		formMain.add(buttonOk);
	
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

	private void onButtonOk(){
		this.executor.action("THANK_RESERV.OK", null);
	}
	
}
