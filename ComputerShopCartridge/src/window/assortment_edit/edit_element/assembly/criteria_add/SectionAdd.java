package window.assortment_edit.edit_element.assembly.criteria_add;

import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import database.bind.EAssemblyEditType;

import wicket_extension.action.IAjaxActionExecutor;

/** добавить критерий  */
public class SectionAdd extends Panel{
	private static final long serialVersionUID = 1L;
	private IAjaxActionExecutor executor;
	private ModalWindow modalWindow;
	
	private RadioGroup<EAssemblyEditType> radioGroup;

	/** добавить критерий  
	 * @param id - уникальный идентификатор панели
	 * @param title - заголовок  
	 * @param executor - исполнитель, которому нужно передать данные (возвращает, в случае успеха, [target,"ADD",new element text]
	 * @param modalWindow - модальное окно для отображения  
	 */
	public SectionAdd(String id, String title, IAjaxActionExecutor executor, ModalWindow modalWindow) {
		super(id);
		this.executor=executor;
		this.modalWindow=modalWindow;
		this.modalWindow.setTitle(title);
		this.initComponents(title);
	}

	
	private void initComponents(String title){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
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
		buttonOk.add(new SimpleAttributeModifier("value","Создать"));
		formMain.add(buttonOk);
		
		AjaxButton buttonCancel=new AjaxButton("button_cancel",formMain){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonCancel(target);
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить"));
		formMain.add(buttonCancel);
		
		radioGroup=new RadioGroup<EAssemblyEditType>("radio_group",new Model<EAssemblyEditType>(EAssemblyEditType.EDIT));
		formMain.add(radioGroup);

		Radio<EAssemblyEditType> editType=new Radio<EAssemblyEditType>("edit_type", new Model<EAssemblyEditType>(EAssemblyEditType.EDIT));
		radioGroup.add(editType);
		radioGroup.add(new Label("edit_type_caption","Edit"));

		Radio<EAssemblyEditType> selectType=new Radio<EAssemblyEditType>("select_type", new Model<EAssemblyEditType>(EAssemblyEditType.SELECT));
		radioGroup.add(selectType);
		radioGroup.add(new Label("select_type_caption","Select"));
	
		Radio<EAssemblyEditType> listType=new Radio<EAssemblyEditType>("list_type", new Model<EAssemblyEditType>(EAssemblyEditType.LIST));
		radioGroup.add(listType);
		radioGroup.add(new Label("list_type_caption","List"));
	}

	private Model<String> modelText=new Model<String>();
	
	/** реакция на нажатие кнопки OK */
	private void onButtonOk(AjaxRequestTarget target){
		this.executor.action(target, "ADD", new Object[]{this.radioGroup.getModelObject(),this.modelText.getObject()});
		this.modalWindow.close(target);
	}
	
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target);
	}
}
