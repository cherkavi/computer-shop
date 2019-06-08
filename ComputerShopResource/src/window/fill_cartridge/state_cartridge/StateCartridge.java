package window.fill_cartridge.state_cartridge;

import java.text.SimpleDateFormat;

import java.util.Date;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.OrderList;

import wicket_extension.UserApplication;
import window.BasePage;
import window.catalog.Catalog;
import window.fill_cartridge.FillCartridge;

/** ��������� ������ �� �������� ��������� */
public class StateCartridge extends BasePage{
	private final static long serialVersionUID=1L;
	
	/** ��������� ������ �� �������� ��������� 
	 * @param order - ����� ������ 
	 * @param controlNumber - ����������� ����� 
	 */
	public StateCartridge(String order, String controlNumber){
		String message=this.getOrderMessage(order, controlNumber);
		initComponents(message);
	}

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	/**
	 * @param order ����� ������ 
	 * @param controlNumber - ����������� ����� 
	 * @return 
	 */
	private String getOrderMessage(String order, String controlNumber){
		String returnValue="����� �� ������ "+order+"  ("+controlNumber+")";
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			int orderNumber=Integer.parseInt(order);
			OrderList orderList=(OrderList)connector.getSession().createCriteria(OrderList.class)
			                                                     .add(Restrictions.eq("uniqueNumber", new Integer(orderNumber)))
			                                                     .add(Restrictions.eq("controlNumber", controlNumber)).uniqueResult();
			if(!isDateEqualsNull(orderList.getTimeReturnToCustomer())){
				returnValue="����� ����� �������: "+sdf.format(orderList.getTimeReturnToCustomer());
			}else if(!isDateEqualsNull(orderList.getTimeReturnFromProcess())){
				returnValue="�������� ���������: "+sdf.format(orderList.getTimeReturnFromProcess());
			}else if(!isDateEqualsNull(orderList.getTimeGetToProcess())){
				returnValue="�������� ���� �� ��������: "+sdf.format(orderList.getTimeGetToProcess());
			}else if(!isDateEqualsNull(orderList.getTimeCreate())){
				returnValue="�������� ������� �� �������: "+sdf.format(orderList.getTimeCreate());
			}
		}catch(Exception ex){
			System.err.println("StateCartridge#getOrderMessage Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	private boolean isDateEqualsNull(Date date){
		return date==null;
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(String message){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);

		formMain.add(new Label("state",message));
		
		Button buttonOk=new Button("button_ok"){
			private static final long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_ok")));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}

	/** ������� ������� �� ������� */
	private void onButtonOk(){
		this.setResponsePage(FillCartridge.class);
	}
	
	/** ������� ������� �� ������� ������ */
	private void onButtonCancel(){
		this.setResponsePage(Catalog.class);
	}
}
