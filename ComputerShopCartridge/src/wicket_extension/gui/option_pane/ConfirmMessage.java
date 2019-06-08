package wicket_extension.gui.option_pane;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import wicket_extension.action.Action;
import window.main_menu.WindowEmulator;

/** Подтверждение действия */
public class ConfirmMessage extends WindowEmulator{
	private Action actionOk;
	private Action actionCancel;
	/** Подтверждение действия 
	 * @param title - заголовок
	 * @param message - сообщение
	 * @param actionOk - действие в случае Ok
	 * @param actionCancel - действие в случае отмены 
	 * @param captionOk - caption кнопки Ok
	 * @param captionCancel - caption кнопки Cancel
	 */
	public ConfirmMessage(String title, String message, Action actionOk, Action actionCancel, String captionOk, String captionCancel){
		this.setTitle(title);
		this.actionOk=actionOk;
		this.actionCancel=actionCancel;
		initComponents(message, captionOk,captionCancel);
	}
	
	private void initComponents(String message, String captionOk, String captionCancel){
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);

		Label label=new Label("message",new Model<String>(message));
		formMain.add(label);
		
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value", captionOk));
		formMain.add(buttonOk);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value", captionCancel));
		formMain.add(buttonCancel);
	}
	
	private void onButtonOk(){
		try{
			this.setResponsePage(this.actionOk.getRedirectPage());			
		}catch(Exception ex){
			System.err.println("ConfirmMessage#onButtonOk Exception: "+ex.getMessage());
		}
	}
	private void onButtonCancel(){
		try{
			this.setResponsePage(this.actionCancel.getRedirectPage());			
		}catch(Exception ex){
			System.err.println("ConfirmMessage#onButtonOk Exception: "+ex.getMessage());
		}
	}
}
