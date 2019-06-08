package window.main_menu.return_to_user.marker;

import java.io.File;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
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
import window.BasePage;
import window.commons.GroupOrder;
import window.commons.OrderElement;
import window.main_menu.return_to_user.ReturnToUser;

public class ReturnToUserGroup extends BasePage{
	private Integer groupId;
	private GroupOrder groupOrder;
	private ArrayList<Integer> listOfId=new ArrayList<Integer>();
	private ArrayList<Model<Boolean>> listOfModel=new ArrayList<Model<Boolean>>();
	private Form<Object> formMain;
	
	public ReturnToUserGroup(Integer groupId){
		this.groupId=groupId;
		initComponents();
	}

	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);

		 
		SubmitLink linkReceipt=new SubmitLink("link_receipt"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onReceipt();
			}
		};
		linkReceipt.add(new Label("caption_receipt",new Model<String>("Квитанция")));
		formMain.add(linkReceipt);

		SubmitLink linkCheque=new SubmitLink("link_cheque"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onCheque();
			}
		};
		linkCheque.add(new Label("caption_cheque",new Model<String>("Cheque")));
		formMain.add(linkCheque);

		ConnectWrap connector=((UserApplication)getApplication()).getConnector();
		groupOrder=new GroupOrder(connector,
								  this.groupId,
								  ((UserSession)this.getSession()).getPointNumber(),
								  true);
		connector.close();
		
		formMain.add(new Label("caption_surname","Фамилия: "));
		formMain.add(new Label("caption_name","Имя: "));
		formMain.add(new Label("caption_description","Описание: "));
		formMain.add(new Label("value_surname",groupOrder.getSurname()));
		formMain.add(new Label("value_name",groupOrder.getName()));
		formMain.add(new Label("value_description",groupOrder.getDescription()));
		
		formMain.add(new Label("caption_vendor","Производитель"));
		formMain.add(new Label("caption_model","Модель"));
		formMain.add(new Label("caption_price","Прайс"));
		formMain.add(new Label("caption_selected","Выдать"));

		final DecimalFormat priceFormat=new DecimalFormat("#0.00");
		ListView<OrderElement> listOrder=new ListView<OrderElement>("list_order",groupOrder.getListOfOrder()){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<OrderElement> item) {
				final OrderElement element=item.getModelObject();
				item.add(new Label("value_vendor",element.getVendor()));
				item.add(new Label("value_model",element.getModel()));
				item.add(new Label("value_price",priceFormat.format(element.getPrice())));
				
				ReturnToUserGroup.this.listOfId.add(element.getId());
				final Model<Boolean> model=new Model<Boolean>(Boolean.FALSE);
				ReturnToUserGroup.this.listOfModel.add(model);
				item.add(new CheckBox("value_selected",model));
			}
		};
		formMain.add(listOrder);
		
		
		Button buttonMark=new Button("button_mark"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonMark();
			};
		};
		buttonMark.add(new SimpleAttributeModifier("value","Выдать выделенные"));
		formMain.add(buttonMark);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отмена"));
		formMain.add(buttonCancel);
		formMain.add(new ComponentFeedbackPanel("form_error", formMain));
	}
	
	/** реакция на нажатие кнопки возврата в главное меню */
	private void onButtonMark(){
		if(this.isSelected()==true){
			ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
			GroupOrder selectedGroup=this.getSelectedGroup();
			try{
				Date date=new Date();
				Session session=connector.getSession();
				session.beginTransaction();
				for(int counter=0;counter<selectedGroup.getListOfOrder().size();counter++){
					OrderList orderList=(OrderList)session.get(OrderList.class, selectedGroup.getListOfOrder().get(counter).getId());
					orderList.setTimeReturnToCustomer(date);
					orderList.setForSend(orderList.getForSend()+1);
					session.update(orderList);
				}
				session.getTransaction().commit();
				((ReflectWorker)((UserApplication)this.getApplication()).getPropertis("reflect_worker")).runProcess();
				this.setResponsePage(ReturnToUser.class);
			}catch(Exception ex){
				System.out.println("ReturnToUserMark#onButtonMark "+ex.getMessage());
			}finally{
				connector.close();
			}
		}else{
			// не выделен ни один объект
			formMain.error("Выделите элементы");
		}
	}

	private void onButtonCancel(){
		this.setResponsePage(ReturnToUser.class);
	}

	private void onReceipt(){
		if(this.isSelected()==true){
			// получить копию группы, и удалить из нее все элементы, которые не выделены
			GroupOrder selectedGroup=this.getSelectedGroup();
			ReportGenerator reportGenerator=ReportGenerator.getInstance();
			String pathToFile=null;
			pathToFile=reportGenerator.createReceiptPdf(selectedGroup);
			try{
				getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"receipt.pdf"));
			}catch(Exception ex){
				System.err.println("ReturnToUserMark#onPrintCheque Exception: "+ex.getMessage());
			}
		}else{
			// не выделен ни один объект
			formMain.error("Выделите элементы");
		}
	}
	
	private void onCheque(){
		if(this.isSelected()==true){
			// получить копию группы, и удалить из нее все элементы, которые не выделены
			GroupOrder selectedGroup=this.getSelectedGroup();
			ReportGenerator reportGenerator=ReportGenerator.getInstance();
			String pathToFile=null;
			pathToFile=reportGenerator.createChequePdf(selectedGroup);
			try{
				getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(new FileResourceStream(new File(ReportGenerator.dir+pathToFile)),"cheque.pdf"));
			}catch(Exception ex){
				System.err.println("ReturnToUserMark#onPrintCheque Exception: "+ex.getMessage());
			}
		}else{
			// не выделен ни один объект
			formMain.error("Выделите элементы");
		}
	}
	
	/** проверить, выделен ли хоть один элемент */
	private boolean isSelected(){
		boolean returnValue=false;
		for(int counter=0;counter<this.listOfModel.size();counter++){
			if(this.listOfModel.get(counter).getObject()==true){
				returnValue=true;
				break;
			}
		}
		return returnValue;
	}
	
	/** получить группу, которая выделена */
	private GroupOrder getSelectedGroup(){
		GroupOrder returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			returnValue=new GroupOrder(connector,
									   this.groupId,
									   ((UserSession)this.getSession()).getPointNumber(),
									   true);
			ArrayList<OrderElement> listOfElement=returnValue.getListOfOrder();
			for(int counter=0;counter<this.listOfId.size();counter++){
				// если текущий элемент не выделен - убрать из выборки 
				if(this.listOfModel.get(counter).getObject()==false){
					// удалить данный элемент из выборки
					for(int index=0;index<listOfElement.size();index++){
						if(listOfElement.get(index).getId().intValue()==this.listOfId.get(counter).intValue()){
							listOfElement.remove(index);
							break;
						}
					}
				}
			}
		}catch(Exception ex){
			System.err.println("ReturnToUserGroup#getSelectedGroup Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
}
