package window.assortment_edit.edit_element.assembly.criteria.modal_choice_remove;

import java.util.ArrayList;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IAjaxActionExecutor;

/** добавить критерий  */
public class RemoveCriteria extends Panel{
	private static final long serialVersionUID = 1L;
	private IAjaxActionExecutor executor;
	private ModalWindow modalWindow;
	private ArrayList<String> elements;
	/** добавить критерий  
	 * @param id - уникальный идентификатор панели
	 * @param title - заголовок
	 * @param elements - элементы, которые нужно удалить 
	 * @param executor - исполнитель, которому нужно передать данные [target, "REMOVE", (ArrayList&ltString&gt)this.elements]
	 * @param modalWindow - модальное окно для отображения  
	 */
	public RemoveCriteria(String id, 
						  String title, 
						  ArrayList<String> elements,
						  IAjaxActionExecutor executor, 
						  ModalWindow modalWindow) {
		super(id);
		this.executor=executor;
		this.modalWindow=modalWindow;
		this.elements=elements;
		this.initComponents(title, elements);
	}

	
	private void initComponents(String title, ArrayList<String> elements){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("label",title));
		
		StringBuffer labelText=new StringBuffer();
		for(int counter=0;counter<elements.size();counter++){
			labelText.append(elements.get(counter));
			if(counter!=(elements.size()-1)){
				labelText.append("<br>");
			}
		}
		Label labelRemove=new Label("label_remove",labelText.toString());
		labelRemove.setEscapeModelStrings(false);
		formMain.add(labelRemove);
		
		AjaxButton buttonOk=new AjaxButton("button_ok",formMain) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonOk(target);
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value","Удалить все"));
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
	}
	
	/** реакция на нажатие кнопки OK */
	private void onButtonOk(AjaxRequestTarget target){
		this.executor.action(target, "REMOVE", this.elements);
		this.modalWindow.close(target);
	}
	
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target);
	}
}
