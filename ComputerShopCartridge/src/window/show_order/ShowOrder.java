package window.show_order;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.FileResourceStream;
import report_servlet.ReportGenerator;
import database.ConnectWrap;
import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import window.BasePage;
import window.commons.GroupOrder;
import window.commons.OrderElement;
import window.main_menu.MainMenu;

/** страница, которая отображает необходимый заказ, который был создан */
public class ShowOrder extends BasePage{
	GroupOrder groupOrder=null;
	
	/** 
	 * @param orderGroupId - отобразить групповой заказ
	 * @param orderListId - отобразить одиночный заказ
	 */
	public ShowOrder(Integer orderGroupId, Integer orderListId){
		initComponents(orderGroupId,orderListId);
	}
	/*
	private String getUniqueNumber(){
		String returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from order_list where order_list.id="+this.orderId);
			rs.next();
			returnValue=rs.getString("UNIQUE_NUMBER");
		}catch(Exception ex){
			System.out.println("InvoiceOrder#getControlNumber Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	private String getControlNumber(){
		String returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from order_list where order_list.id="+this.orderId);
			rs.next();
			returnValue=rs.getString("CONTROL_NUMBER");
		}catch(Exception ex){
			System.out.println("InvoiceOrder#getControlNumber Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	// получить имя принтера 
	private String getPrinterName(){
		String returnValue=null;
		Integer userKod=((UserSession)this.getSession()).getUser().getKod();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from point_settings where id_points="+userKod);
			rs.next();
			returnValue=rs.getString("PRINTER_NAME");
			rs.getStatement().close();
		}catch(Exception ex){
			System.out.println("InvoiceOrder#getPrinterName Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	// получить имя принтера штрих-кодов 
	private String getPrinterBarCodeName(){
		String returnValue=null;
		Integer userKod=((UserSession)this.getSession()).getUser().getKod();
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from point_settings where id_points="+userKod);
			rs.next();
			returnValue=rs.getString("PRINTER_BARCODE_NAME");
			rs.getStatement().close();
		}catch(Exception ex){
			System.out.println("InvoiceOrder#getPrinterName Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	// получить полный контекст сервлета 
	private String getFullContext(HttpServletRequest request){
		return "http://"+request.getLocalName()
        +":"+request.getLocalPort()
        +request.getContextPath();
	}
	*/
	/** первоначальная инициализация компонентов */
	private void initComponents(Integer orderGroupId,Integer orderListId){
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
		if(orderGroupId!=null){
			// групповой заказ
			groupOrder=new GroupOrder(connector, orderGroupId,((UserSession)this.getSession()).getPointNumber(),true);
		}else{
			// одиночный заказ
			groupOrder=new GroupOrder();
			groupOrder.loadByElementCode(connector, orderListId,((UserSession)this.getSession()).getUser().getKod());
		}
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
		//SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm:sss");
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		Button buttonMainMenu=new Button("button_main_menu"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonMainMenu();
			};
		};
		buttonMainMenu.add(new SimpleAttributeModifier("value","Вернуться в главное меню"));
		formMain.add(buttonMainMenu);
	}
	
	/** реакция на нажатие кнопки возврата в главное меню */
	private void onButtonMainMenu(){
		this.setResponsePage(MainMenu.class);
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
	
	private void onBarcode(String uniqueNumber){
		ReportGenerator reportGenerator=ReportGenerator.getInstance();
		String pathToFile=reportGenerator.createBarCodePdf(uniqueNumber);
		try{
			getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"barcode.pdf"));
		}catch(Exception ex){
			System.err.println("ReturnToUserMark#onPrintBarcode Exception: "+ex.getMessage());
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
}

/** класс, который содержит шапку заказа  */
class OrderHeader implements Serializable{
	private final static long serialVersionUID=1L;
	
	private String name;
	private String surname;
	private String description;
	
	public OrderHeader(String name, String surname, String description){
		this.name=name;
		this.surname=surname;
		this.description=description;
	}
	
	public OrderHeader(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}


