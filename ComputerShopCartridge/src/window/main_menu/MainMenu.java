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
		super("������� ����");
		initComponents();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		Button buttonCreateOrder=new Button("button_create_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCreateOrder();
			};
		};
		buttonCreateOrder.add(new SimpleAttributeModifier("value","������� �����"));
		buttonCreateOrder.add(new SimpleAttributeModifier("title","���������������� ����� ������� "));
		formMain.add(buttonCreateOrder);
		/*
		Button buttonTakeOrder=new Button("button_take_order"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonTakeOrder();
			};
		};
		buttonTakeOrder.add(new SimpleAttributeModifier("value","����� ����� �� ������"));
		buttonTakeOrder.add(new SimpleAttributeModifier("title","��������� ���� ����� �� ������"));
		formMain.add(buttonTakeOrder);
		
		Button buttonReturnToStoreHouse=new Button("button_return_to_storehouse"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonToStoreHouse();
			}
		};
		buttonReturnToStoreHouse.add(new SimpleAttributeModifier("value","������� ������������ �������� �� �����"));
		buttonReturnToStoreHouse.add(new SimpleAttributeModifier("title","��������� ���������� �������� �� ����� "));
		formMain.add(buttonReturnToStoreHouse);
		*/
		Button buttonReturnToCustomer=new Button("button_return_to_customer"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonToCustomer();
			}
		};
		buttonReturnToCustomer.add(new SimpleAttributeModifier("value","������ �������� �������"));
		buttonReturnToCustomer.add(new SimpleAttributeModifier("title","������� ������� ������������ ��������"));
		formMain.add(buttonReturnToCustomer);
		
		Button buttonUtility=new Button("button_utility"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonUtility();
			}
		};
		buttonUtility.add(new SimpleAttributeModifier("value","�������������� ������"));
		buttonUtility.add(new SimpleAttributeModifier("title","������ ��� ���������� ������� "));
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
