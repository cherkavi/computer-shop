package window.assortment_edit.edit_element.assembly.save_ok;

import org.apache.wicket.ajax.AjaxRequestTarget;


import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IAjaxActionExecutor;

/** добавить критерий  */
public class SaveOk extends Panel{
	private static final long serialVersionUID = 1L;
	private IAjaxActionExecutor executor;
	private ModalWindow modalWindow;
	public static String MENU_FIND="MENU_FIND";
	public static String MENU_EXIT="MENU_EXIT";
	public static String MENU_STAY="MENU_STAY";
	
	/** добавить критерий  
	 * @param id - уникальный идентификатор панели
	 * @param title - заголовок  
	 * @param executor - исполнитель, которому нужно передать данные (возвращает, в случае успеха, [target,"ADD",new element text]
	 * @param modalWindow - модальное окно дл€ отображени€  
	 */
	public SaveOk(String id, String title, IAjaxActionExecutor executor, ModalWindow modalWindow) {
		super(id);
		this.executor=executor;
		this.modalWindow=modalWindow;
		this.modalWindow.setTitle(title);
		this.initComponents(title);
	}

	
	private void initComponents(String title){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		
		AjaxButton buttonFind=new AjaxButton("button_find",formMain) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonFind(target);
			}
		};
		buttonFind.add(new SimpleAttributeModifier("value","ћеню поиска"));
		formMain.add(buttonFind);
		
		AjaxButton buttonExit=new AjaxButton("button_exit",formMain) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonExit(target);
			}
		};
		buttonExit.add(new SimpleAttributeModifier("value","¬ыход из редактировани€"));
		formMain.add(buttonExit);

		AjaxButton buttonStay=new AjaxButton("button_stay",formMain) {
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonStay(target);
			}
		};
		buttonStay.add(new SimpleAttributeModifier("value","ѕродолжить"));
		formMain.add(buttonStay);
	}

	/** реакци€ на нажатие кнопки OK */
	private void onButtonFind(AjaxRequestTarget target){
		this.executor.action(target, "GOTO", MENU_FIND);
	}
	
	private void onButtonExit(AjaxRequestTarget target){
		this.executor.action(target, "GOTO", MENU_EXIT);
	}
	
	private void onButtonStay(AjaxRequestTarget target){
		this.executor.action(target, "GOTO", MENU_STAY);
	}
}
