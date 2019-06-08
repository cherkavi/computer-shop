package window.fill_cartridge;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.OrderList;

import wicket_extension.UserApplication;
import window.BasePage;
import window.catalog.Catalog;
import window.fill_cartridge.state_cartridge.StateCartridge;

/** панель-заголовок для базовой страницы */
public class FillCartridge extends BasePage{
	private final static long serialVersionUID=1L;
	
	private Form<?> formMain;
	private TextField<String> editOrder;
	private TextField<String> editControl;
	
	
	public FillCartridge(){
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.editControl=new TextField<String>("edit_control",new Model<String>(""));
		formMain.add(this.editControl);
		
		this.editOrder=new TextField<String>("edit_order",new Model<String>(""));
		formMain.add(this.editOrder);
		
		formMain.add(new ComponentFeedbackPanel("feedback_form",formMain));
		
		Button buttonOk=new Button("button_ok"){
			private static final long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_ok")));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}

	/** реакция нажатия на клавишу */
	private void onButtonOk(){
		while(true){
			if((this.editOrder.getModelObject()==null)||(this.editOrder.getModelObject().trim().equals(""))){
				formMain.error(this.getString("form_main.edit_order.Required"));
				break;
			}
			if((this.editControl.getModelObject()==null)||(this.editControl.getModelObject().trim().equals(""))){
				formMain.error(this.getString("form_main.edit_control.Required"));
				break;
			}
			if(this.checkValues(this.editOrder.getModelObject(), this.editControl.getModelObject())){
				StateCartridge stateCartridge=new StateCartridge(this.editOrder.getModelObject(), this.editControl.getModelObject());
				this.setResponsePage(stateCartridge);
			}else{
				formMain.error(this.getString("error_order_not_found"));
			}
			break;
		}
	}
	
	/** проверить наличие указанного номера в базе данных */
	private boolean checkValues(String orderNumber, String controlNumber){
		boolean returnValue=false;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Object object=connector.getSession().createCriteria(OrderList.class)
			                                                     .add(Restrictions.eq("uniqueNumber", new Integer(orderNumber)))
			                                                     .add(Restrictions.eq("controlNumber", controlNumber)).uniqueResult();
			returnValue=(object!=null);
		}catch(Exception ex){
			System.err.println("FillCartridge#checkValues Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** реакция нажатия на клавишу отмены */
	private void onButtonCancel(){
		this.setResponsePage(Catalog.class);
	}
}
