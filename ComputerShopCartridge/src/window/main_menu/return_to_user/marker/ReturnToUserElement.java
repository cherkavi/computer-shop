package window.main_menu.return_to_user.marker;

import java.io.File;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.FileResourceStream;
import org.hibernate.Session;

import report_servlet.ReportGenerator;
import database.ConnectWrap;
import database.wrap.OrderList;
import database_reflect.ReflectWorker;
import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import wicket_extension.action.Action;
import wicket_extension.gui.panel_button.PanelButton;
import window.BasePage;
import window.commons.GroupOrder;
import window.commons.OrderElement;
import window.main_menu.return_to_user.ReturnToUser;

/** отобразить единственный указанный товар в качестве возможного возврата пользователю ( с возможностью выхода на группу товара ) */
public class ReturnToUserElement extends BasePage{
	private Integer orderId;
	private GroupOrder groupOrder;
	
	/** отобразить единственный указанный товар в качестве возможного возврата пользователю с возможностью выхода на группу товара */
	public ReturnToUserElement(Integer orderId){
		this.orderId=orderId;
		initComponents();
	}

	/** имеет ли указанный номер группу, и есть ли в этой группе еще картриджы, которые могут быть выданы пользователю 
	 * @return
	 * <li> null - в группе (остался) только один картридж</li>
	 * <li> Integer - уникальный номер группы </li>
	 * */
	private Integer isOrderHaveGroup(Integer orderId){
		Integer returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		ResultSet rs=null;
		try{
			Connection connection=connector.getConnection();
			String query="select count(*), id_order_group "
				+"from get_cartridge_for_customer "
				+"where get_cartridge_for_customer.time_return_to_customer is null "
				+"and get_cartridge_for_customer.id_order_group=( "
				+"select get_cartridge_for_customer.id_order_group "
				+"from get_cartridge_for_customer where id="+orderId+") "
				+"group by id_order_group ";
			rs=connection.createStatement().executeQuery(query);
			rs.next();
			if(rs.getInt(1)>1){
				returnValue=rs.getInt(2);
			}else{
				returnValue=null;
			}
		}catch(Exception ex){
			System.err.println("ReturnToUserElement#isOrderHaveGroup Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Panel panelGroupDetect=null;
		Integer groupId=isOrderHaveGroup(this.orderId);
		if(groupId!=null){
			Action action=new Action(ReturnToUserGroup.class,new Class[]{Integer.class}, new Object[]{groupId});
			panelGroupDetect=new PanelButton("panel_detect_group",action,"Отобразить всю группу");
		}else{
			panelGroupDetect=new EmptyPanel("panel_detect_group");
		}
		this.add(panelGroupDetect);

		Link<Object> linkReceipt=new Link<Object>("link_receipt"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onReceipt();
			}
		};
		linkReceipt.add(new Label("caption_receipt",new Model<String>("Квитанция")));
		this.add(linkReceipt);

		Link<Object> linkCheque=new Link<Object>("link_cheque"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onCheque();
			}
		};
		linkCheque.add(new Label("caption_cheque",new Model<String>("Cheque")));
		this.add(linkCheque);

		ConnectWrap connector=((UserApplication)getApplication()).getConnector();
		groupOrder=new GroupOrder();
		groupOrder.loadByElementCode(connector, this.orderId,((UserSession)this.getSession()).getPointNumber());
		connector.close();
		
		this.add(new Label("caption_surname","Фамилия: "));
		this.add(new Label("caption_name","Имя: "));
		this.add(new Label("caption_description","Описание: "));
		this.add(new Label("value_surname",groupOrder.getSurname()));
		this.add(new Label("value_name",groupOrder.getName()));
		this.add(new Label("value_description",groupOrder.getDescription()));
		
		this.add(new Label("caption_vendor","Производитель"));
		this.add(new Label("caption_model","Модель"));
		this.add(new Label("caption_price","Прайс"));
		this.add(new Label("caption_barcode","BarCode"));

		final DecimalFormat priceFormat=new DecimalFormat("#0.00");
		ListView<OrderElement> listOrder=new ListView<OrderElement>("list_order",groupOrder.getListOfOrder()){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<OrderElement> item) {
				final OrderElement element=item.getModelObject();
				item.add(new Label("value_vendor",element.getVendor()));
				item.add(new Label("value_model",element.getModel()));
				item.add(new Label("value_price",priceFormat.format(element.getPrice())));
				Link<Object> link=new Link<Object>("link_print_barcode"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onBarcode(element.getUniqueNumber());
					}
				};
				link.add(new Label("caption_print_barcode",element.getUniqueNumber()));
				item.add(link);
			}
		};
		this.add(listOrder);
		
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonMark=new Button("button_mark"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonMark();
			};
		};
		buttonMark.add(new SimpleAttributeModifier("value","Выдать пользователю"));
		formMain.add(buttonMark);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отмена"));
		formMain.add(buttonCancel);
		
	}
	
	/** реакция на нажатие кнопки возврата в главное меню */
	private void onButtonMark(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			OrderList orderList=(OrderList)session.get(OrderList.class, this.orderId);
			orderList.setTimeReturnToCustomer(new Date());
			try{
				orderList.setForSend(orderList.getForSend()+1);
			}catch(Exception ex){
				orderList.setForSend(1);
			}
			session.beginTransaction();
			session.update(orderList);
			session.getTransaction().commit();
			((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
		}catch(Exception ex){
			System.out.println("ReturnToUserElement#onButtonMark "+ex.getMessage());
		}finally{
			connector.close();
		}
		this.setResponsePage(ReturnToUser.class);
	}

	private void onButtonCancel(){
		this.setResponsePage(ReturnToUser.class);
	}

	private void onReceipt(){
		ReportGenerator reportGenerator=ReportGenerator.getInstance();
		String pathToFile=null;
		pathToFile=reportGenerator.createReceiptPdf(this.groupOrder);
		try{
			getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"receipt.pdf"));
		}catch(Exception ex){
			System.err.println("ReturnToUserMark#onPrintCheque Exception: "+ex.getMessage());
		}
	}
	
	private void onCheque(){
		ReportGenerator reportGenerator=ReportGenerator.getInstance();
		String pathToFile=null;
		pathToFile=reportGenerator.createChequePdf(this.groupOrder);
		try{
			getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"cheque.pdf"));
		}catch(Exception ex){
			System.err.println("ReturnToUserMark#onPrintCheque Exception: "+ex.getMessage());
		}
	}
	
	private void onBarcode(String uniqueNumber){
		ReportGenerator reportGenerator=ReportGenerator.getInstance();
		String pathToFile=reportGenerator.createBarCodePdf(uniqueNumber);
		try{
			getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"barcode.pdf"));
		}catch(Exception ex){
			System.err.println("ReturnToUserMark#onPrintBarcode Exception: "+ex.getMessage());
		}
	}
	
}
