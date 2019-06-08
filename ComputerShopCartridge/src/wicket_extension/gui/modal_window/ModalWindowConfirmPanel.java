package wicket_extension.gui.modal_window;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;

/** панель, с информацией и двумя кнопками */
public abstract class ModalWindowConfirmPanel extends Panel{
	private final static long serialVersionUID=1L;
	
	/** панель, с информацией и двумя кнопками */
	public ModalWindowConfirmPanel(String id,
								   String message,
								   String messageCssStyle,
								   String buttonOkTitle,
								   String buttonOkCssStyle,
								   String buttonCancelTitle,
								   String buttonCancelCssStyle){
		super(id);
		
		Label textMessage=new Label("message",message);
		this.add(textMessage);
		if(messageCssStyle!=null){
			textMessage.add(new SimpleAttributeModifier("style",messageCssStyle));
		}
		
		Button buttonOk=new Button("button_ok");
		this.add(buttonOk);
		buttonOk.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonOkClick(target);
			}
		});
		if(buttonOkTitle!=null){
			buttonOk.add(new SimpleAttributeModifier("value",buttonOkTitle));
		}else{
			buttonOk.add(new SimpleAttributeModifier("value",this.getString("button_ok_caption")));
		}
		if(buttonOkCssStyle!=null){
			buttonOk.add(new SimpleAttributeModifier("class",buttonOkCssStyle));
		}
		
		Button buttonCancel=new Button("button_cancel");
		this.add(buttonCancel);
		buttonCancel.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonCancelClick(target);
			}
		});
		if(buttonCancelTitle!=null){
			buttonCancel.add(new SimpleAttributeModifier("value",buttonCancelTitle));
		}else{
			buttonCancel.add(new SimpleAttributeModifier("value",this.getString("button_cancel_caption")));
		}
			
		if(buttonCancelCssStyle!=null){
			buttonCancel.add(new SimpleAttributeModifier("class",buttonCancelCssStyle));
		}
	}
	
	
	public abstract void onButtonOkClick(AjaxRequestTarget target);
	
	public abstract void onButtonCancelClick(AjaxRequestTarget target);
}
