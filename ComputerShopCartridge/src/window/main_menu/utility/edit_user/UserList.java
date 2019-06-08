package window.main_menu.utility.edit_user;

import java.util.ArrayList;

import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import database.ConnectWrap;
import database.wrap.Users;

import wicket_extension.action.IActionExecutor;
import window.Application;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_user.edit.UserEdit;
import window.main_menu.utility.edit_user.remove.UserRemove;
import window.main_menu.utility.point_choice.ChoicePoint;

/** список пользователей по ресурсу Картриджы  */
public class UserList extends WindowEmulator implements IActionExecutor{
	private Model<ArrayList<Users>> modelUsers=new Model<ArrayList<Users>>();
	private ModalWindow modalWindow;
	/** список пользователей по ресурсу Картриджы  */
	public UserList(){
		super("Пользователи");
		initComponents();
	}
	
	/** инициализация объектов */
	private void initComponents(){
		Form<?> formExit=new Form<Object>("form_exit");
		this.add(formExit);
		
		AjaxButton buttonAdd=new AjaxButton("button_add"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonAdd(target);
			}
		};
		buttonAdd.add(new SimpleAttributeModifier("value",this.getString("label_button_add")));
		formExit.add(buttonAdd);
		
		Button buttonExit=new Button("button_exit"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonExit();
			};
		};
		buttonExit.add(new SimpleAttributeModifier("value","Выбор торговой точки"));
		formExit.add(buttonExit);
		
		WebMarkupContainer userListContainer=new WebMarkupContainer("user_list_container");
		userListContainer.setOutputMarkupId(true);
		this.add(userListContainer);
		
		this.modelUsers.setObject(this.getUsers());
		ListView<Users> userList=new ListView<Users>("user_list", modelUsers){
			private final static long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<Users> item) {
				final Users user=item.getModelObject();
				item.add(new Label("user_name",user.getUserName()));
				
				WebComponent buttonEdit=new WebComponent("button_edit");
				buttonEdit.add(new AjaxEventBehavior("onclick") {
					private final static long serialVersionUID=1L;
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						onButtonEdit(target,user);
					}
				});
				buttonEdit.add(new SimpleAttributeModifier("value", UserList.this.getString("label_button_edit")));
				item.add(buttonEdit);
				
				WebComponent buttonRemove=new WebComponent("button_remove");
				buttonRemove.add(new AjaxEventBehavior("onclick") {
					private final static long serialVersionUID=1L;
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						onButtonRemove(target,user);
					}
				});
				buttonRemove.add(new SimpleAttributeModifier("value", UserList.this.getString("label_button_remove")));
				item.add(buttonRemove);
				
				/*AjaxButton buttonEdit=new AjaxButton("button_edit"){
					private final static long serialVersionUID=1L;
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> arg1) {
						onButtonEdit(target, user);
					}
				};
				buttonEdit.add(new SimpleAttributeModifier("value", UserList.this.getString("label_button_edit")));
				item.add(buttonEdit);
				
				AjaxButton buttonRemove=new AjaxButton("button_remove"){
					private final static long serialVersionUID=1L;
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> arg1) {
						onButtonRemove(target, user);
					}
				};
				buttonEdit.add(new SimpleAttributeModifier("value", UserList.this.getString("label_button_remove")));
				item.add(buttonRemove);
				*/
			}
		};
		userListContainer.add(userList);
		
		this.modalWindow=new ModalWindow("modal_window");
		this.add(this.modalWindow);
	}

	/** получить всех пользователей данной системы для отображения */
	@SuppressWarnings("unchecked")
	private ArrayList<Users> getUsers() {
		ArrayList<Users> returnValue=new ArrayList<Users>();
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			List<database.wrap.Users> list=(List<database.wrap.Users>)connector.getSession().createCriteria(database.wrap.Users.class).addOrder(Order.asc("id")).list();
			if((list!=null)&&(list.size()>0)){
				for(int counter=0;counter<list.size();counter++){
					returnValue.add(list.get(counter));
				}
			}
		}catch(Exception ex){
			System.err.println("UserList#getUsers  Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	/** редактирование пользователя */
	private void onButtonEdit(AjaxRequestTarget target, Users user){
		// TODO 
		System.out.println("Edit User: "+user.getId());
	}
	
	/** удаление пользователя */
	private void onButtonRemove(AjaxRequestTarget target, Users user){
		// System.out.println("Edit Remove: "+user.getId());
		UserRemove userRemove=new UserRemove(this.modalWindow.getContentId(), this.modalWindow, this,user);
		this.modalWindow.setContent(userRemove);
		this.modalWindow.setInitialWidth(200);
		this.modalWindow.setInitialHeight(150);
		this.modalWindow.show(target);
	}

	/** реакция на нажатие кнопки "добавить"  */
	private void onButtonAdd(AjaxRequestTarget target){
		UserEdit userEdit=new UserEdit(this.modalWindow.getContentId(), 
									   this.modalWindow, 
									   this);
		this.modalWindow.setContent(userEdit);
		modalWindow.setInitialHeight(200);
		modalWindow.setInitialWidth(300);
		modalWindow.show(target);
	}
	
	private void onButtonExit(){
		this.setResponsePage(ChoicePoint.class);
	}

	@Override
	public void action(String actionName, Object argument) {
		if(actionName!=null){
			if(actionName.equals("LIST_UPDATE")){
				// поступила команда обновления List
				AjaxRequestTarget target=(AjaxRequestTarget)argument;
				this.modelUsers.setObject(this.getUsers());
				target.addComponent(this.get("user_list_container"));
			}else if(actionName.equals("REMOVE_USER")){
				if((argument!=null)&&(argument instanceof Users)){
					removeUser((Users)argument);
				}
			}
		}else{
			System.err.println("ActionName is null");
		}
	}
	
	/** удалить пользователя  */
	private void removeUser(Users user){
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
		}catch(Exception ex){
			System.err.println("RemoveUser Error: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
}
