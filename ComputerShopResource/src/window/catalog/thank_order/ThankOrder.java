package window.catalog.thank_order;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import wicket_extension.action.IActionExecutor;

/** положить указанную ассортиментную единицу товара в резерв по данному пользователю  */
public class ThankOrder extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor executor;
	
	public ThankOrder(String id,IActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}

	/** первоначальна€ инициализаци€ компонентов */
	private void initComponents(){
		/*ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Clients client=this.getClientById(connector, ((UserSession)this.getSession()).getUser().getKod());
		connector.close();*/
		
		String helloMessage="спасибо за ¬аш заказ, с ¬ами св€жутьс€ в ближайшее врем€";
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
	
	/*private Clients getClientById(ConnectWrap connector, Integer kod) {
		Clients returnValue=null;
		try{
			Session session=connector.getSession();
			returnValue=(Clients)session.get(Clients.class, kod);
		}catch(Exception ex){
			System.err.println("Reserv#getClientById Exception: "+ex.getMessage());
		}
		return returnValue;
	}*/

	private void onButtonOk(){
		this.executor.action("THANK_ORDER.OK", null);
	}
	
}
