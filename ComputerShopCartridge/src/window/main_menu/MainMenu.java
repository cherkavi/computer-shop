package window.main_menu;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import window.main_menu.create_order.CreateOrder;
import window.main_menu.return_to_user.ReturnToUser;
import window.main_menu.utility.Utility;

public class MainMenu extends WindowEmulator{
	public MainMenu(){
		super("Главное меню");
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		Button buttonCreateOrder=new Button("button_create_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCreateOrder();
			};
		};
		buttonCreateOrder.add(new SimpleAttributeModifier("value","Создать заказ"));
		buttonCreateOrder.add(new SimpleAttributeModifier("title","Зарегестрировать заказ клиента "));
		formMain.add(buttonCreateOrder);
		/*
		Button buttonTakeOrder=new Button("button_take_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonTakeOrder();
			};
		};
		buttonTakeOrder.add(new SimpleAttributeModifier("value","Взять заказ со склада"));
		buttonTakeOrder.add(new SimpleAttributeModifier("title","Заправщик берёт заказ со склада"));
		formMain.add(buttonTakeOrder);
		
		Button buttonReturnToStoreHouse=new Button("button_return_to_storehouse"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonToStoreHouse();
			}
		};
		buttonReturnToStoreHouse.add(new SimpleAttributeModifier("value","Вернуть заправленный картридж на склад"));
		buttonReturnToStoreHouse.add(new SimpleAttributeModifier("title","Заправщик возвращает картридж на склад "));
		formMain.add(buttonReturnToStoreHouse);
		*/
		Button buttonReturnToCustomer=new Button("button_return_to_customer"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonToCustomer();
			}
		};
		buttonReturnToCustomer.add(new SimpleAttributeModifier("value","Выдать картридж клиенту"));
		buttonReturnToCustomer.add(new SimpleAttributeModifier("title","Вернуть клиенту заправленный картридж"));
		formMain.add(buttonReturnToCustomer);
		
		Button buttonUtility=new Button("button_utility"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonUtility();
			}
		};
		buttonUtility.add(new SimpleAttributeModifier("value","Дополнительные режимы"));
		buttonUtility.add(new SimpleAttributeModifier("title","Режимы для управления данными "));
		formMain.add(buttonUtility);
		
		Panel panelInformation=new EmptyPanel("panel_information");
		formMain.add(panelInformation);
		
		this.add(formMain);
	}
	
	private void onButtonCreateOrder(){
		// INFO Create Order
		setResponsePage(CreateOrder.class);
	}
	
	private void onButtonTakeOrder(){
		// INFO Take Order
		//setResponsePage(TakeOrder.class);
	}
	private void onButtonToStoreHouse(){
		// INFO To StoreHouse
		//setResponsePage(ReturnOrder.class);
	}
	
	private void onButtonToCustomer(){
		// INFO To Customer
		setResponsePage(ReturnToUser.class);
	}
	
	private void onButtonUtility(){
		setResponsePage(Utility.class);
	}
}
