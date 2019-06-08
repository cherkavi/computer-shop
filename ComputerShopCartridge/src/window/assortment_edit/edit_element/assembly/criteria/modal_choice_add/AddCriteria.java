package window.assortment_edit.edit_element.assembly.criteria.modal_choice_add;

import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_extension.action.IAjaxActionExecutor;

/** �������� ��������  */
public class AddCriteria extends Panel{
	private static final long serialVersionUID = 1L;
	private IAjaxActionExecutor executor;
	private ModalWindow modalWindow;

	/** �������� ��������  
	 * @param id - ���������� ������������� ������
	 * @param title - ���������  
	 * @param executor - �����������, �������� ����� �������� ������ (����������, � ������ ������, [target,"ADD",new element text]
	 * @param modalWindow - ��������� ���� ��� �����������  
	 */
	public AddCriteria(String id, String title, IAjaxActionExecutor executor, ModalWindow modalWindow) {
		super(id);
		this.executor=executor;
		this.modalWindow=modalWindow;
		this.initComponents(title);
	}

	
	private void initComponents(String title){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("label",title));
		
		TextField<String> textField=new TextField<String>("text", modelText);
		textField.setRequired(false);
		formMain.add(textField);
		
		AjaxButton buttonOk=new AjaxButton("button_ok",formMain) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonOk(target);
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value","�������/��������"));
		formMain.add(buttonOk);
		
		AjaxButton buttonCancel=new AjaxButton("button_cancel",formMain){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonCancel(target);
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","��������"));
		formMain.add(buttonCancel);
	}
	
	private Model<String> modelText=new Model<String>();
	
	/** ������� �� ������� ������ OK */
	private void onButtonOk(AjaxRequestTarget target){
		this.executor.action(target, "ADD", this.modelText.getObject());
		this.modalWindow.close(target);
	}
	
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target);
	}
}
