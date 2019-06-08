package window.main_menu.utility.edit_user.remove;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import database.wrap.Users;

import wicket_extension.action.IActionExecutor;

/** панель, отображающая интерфейс подтверждения удаления/отмены удаления пользователя  */
public class UserRemove extends Panel{
	private final static long serialVersionUID=1L;
	private ModalWindow modalWindow;
	private IActionExecutor executor;
	private Users user;
	/** панель, отображающая интерфейс подтверждения удаления/отмены удаления пользователя  
	 * @param id - уникальный идентификатор 
	 * @param modalWindow - модальное окно 
	 * @param executor - исполнитель, которому нужно передать команду на выполнение
	 * @param user - пользователь, над которым нависла угроза удаления  
	 */
	public UserRemove(String id, ModalWindow modalWindow, IActionExecutor executor, Users user ){
		super(id);
		this.modalWindow=modalWindow;
		this.executor=executor;
		this.user=user;
		initComponents();
	}
	
	private void initComponents(){
		this.add(new Label("title", "Удаление пользователя"));
		Label question=new Label("question","Уверены в удалении <br> "+this.user.getUserName()+"?");
		question.setEscapeModelStrings(false);
		this.add(question);
		
		WebComponent buttonOk=new WebComponent("button_ok");
		this.add(buttonOk);
		buttonOk.add(new SimpleAttributeModifier("value", "Удалить"));
		buttonOk.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonOk(target);
			}
		});
		
		WebComponent buttonCancel=new WebComponent("button_cancel");
		this.add(buttonCancel);
		buttonCancel.add(new SimpleAttributeModifier("value", "Отменить"));
		buttonCancel.add(new AjaxEventBehavior("onclick") {
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onButtonRemove(target);
			}
		});
		
	}
	
	private void onButtonOk(AjaxRequestTarget target){
		this.executor.action("REMOVE_USER", this.user);
		this.executor.action("LIST_UPDATE", target);
		this.modalWindow.close(target);
	}
	
	private void onButtonRemove(AjaxRequestTarget target){
		this.modalWindow.close(target);
	}
}
