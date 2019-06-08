package wicket_extension.gui.panel_button;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_extension.action.Action;

/** панель, которая содержит только одну кнопку */
public class PanelButton extends Panel{
	private final static long serialVersionUID=1L;
	private Action action;
	
	/** панель, которая содержит только одну кнопку */
	public PanelButton(String id, Action action, String caption){
		super(id);
		this.action=action;
		initComponents(caption);
	}

	private void initComponents(String caption){
		Form<Object> formMain=new Form<Object>("form_main");
		Button button=new Button("button",new Model<String>(caption)){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButton();
			};
		};
		formMain.add(button);
		this.add(formMain);
	}
	
	/** реакция на нажатие кнопки */
	private void onButton(){
		try{
			this.setResponsePage(this.action.getRedirectPage());
		}catch(Exception ex){
			System.err.println("PanelButton#onButton: "+ex.getMessage());
		}
	}
}
