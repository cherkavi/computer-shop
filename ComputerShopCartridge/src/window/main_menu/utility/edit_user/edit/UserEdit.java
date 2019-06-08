package window.main_menu.utility.edit_user.edit;

import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.Users;

import wicket_extension.action.IActionExecutor;
import window.Application;

/** редактирование пользователя системы Картриджы */
public class UserEdit extends Panel{
	private final static long serialVersionUID=1L;
	
	private Model<String> modelName=new Model<String>();
	private Model<String> modelLogin=new Model<String>();
	private Model<String> modelPassword=new Model<String>();
	/** сообщение об ошибке  */
	private Model<String> modelErrorMessage=new Model<String>(null);
	/** модальное окно, в контексте которого происходит отображение */
	private ModalWindow modalWindow;
	/** объект, выполняющий действия  */
	private IActionExecutor actionExecutor;
	/** объект, который отображает сообщение об ошибке на панели редактирования/создания */
	private WebMarkupContainer errorMessageContainer;
	
	/** редактирование пользователя системы Картриджы
	 * @param id - уникальный идентификатор 
	 * @param modalWindow - модальное окно, в контексте которого отображается данное окно
	 * @param actionExecutor - исполнитель 
	 */
	public UserEdit(String id, ModalWindow modalWindow, IActionExecutor actionExecutor){
		super(id);
		this.modalWindow=modalWindow;
		this.actionExecutor=actionExecutor;
		initComponents();
	}
	
	private void initComponents(){
		Form<?> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		formMain.add(new Label("title","Создание пользователя"));
		
		formMain.add(new Label("edit_name_label","Полное имя пользователя"));
		formMain.add(new TextField<String>("edit_name",modelName));
		
		formMain.add(new Label("edit_login_label","Логин:"));
		formMain.add(new TextField<String>("edit_login",modelLogin));
		
		formMain.add(new Label("edit_password_label","Пароль:"));
		formMain.add(new TextField<String>("edit_password",modelPassword));
		
		AjaxButton buttonOk=new AjaxButton("button_save"){
			private final static long serialVersionUID=1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonOk(target);
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value","Сохранить"));
		formMain.add(buttonOk);
		
		AjaxButton buttonCancel=new AjaxButton("button_cancel"){
			private final static long serialVersionUID=1L;
			
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				onButtonCancel(target);
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить"));
		formMain.add(buttonCancel);
		
		errorMessageContainer=new WebMarkupContainer("error_message_container");
		errorMessageContainer.setOutputMarkupId(true);
		formMain.add(errorMessageContainer);

		Label errorMessage=
		new Label("error_message",this.modelErrorMessage){
			private final static long serialVersionUID=1L;
			@Override
			public boolean isVisible() {
				return UserEdit.this.modelErrorMessage.getObject()!=null;
			}
		};
		errorMessageContainer.add(errorMessage);
	}
	
	/** реакция на нажатие кнопки Save */
	private void onButtonOk(AjaxRequestTarget target){
		this.modelErrorMessage.setObject(null);
		while(true){
			if((modelName.getObject()==null)||(modelName.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("Введите имя");
				target.addComponent(errorMessageContainer);
				break;
			}
			if((modelLogin.getObject()==null)||(modelLogin.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("Введите логин");
				target.addComponent(errorMessageContainer);
				break;
			}
			if((modelPassword.getObject()==null)||(modelPassword.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("Введите пароль");
				target.addComponent(errorMessageContainer);
				break;
			}
			// валидаторы формы пройдены  
			// проверка на повторение логина
			if(this.isLoginRepeated(modelLogin.getObject())){
				this.modelErrorMessage.setObject("Такой логин уже существует");
				target.addComponent(errorMessageContainer);
				break;
			}
			// все валидаторы пройдены - сохранение
			String saveError=saveUser(modelName.getObject(), modelLogin.getObject(), modelPassword.getObject());
			if(saveError==null){
				// System.out.println("Name: "+modelName.getObject());
				// System.out.println("Login: "+modelLogin.getObject());
				// System.out.println("Password: "+modelPassword.getObject());
				// данные сохранены 
				this.actionExecutor.action("LIST_UPDATE", target); 
				this.modalWindow.close(target);
			}else{
				this.modelErrorMessage.setObject(saveError);
				target.addComponent(errorMessageContainer);
				break;
			}
			break;
		}
	}
	
	/** сохранить пользователя
	 * @return 
	 * <ul>
	 * 	<li><b>null</b> пользователь успешно сохранен </li>
	 * 	<li><b>String</b> ошибка сохранения пользователя в текстовом виде </li>
	 * </ul>
	 *  */
	private String saveUser(String name, String login, String password) {
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			// проверить логин на уникальность
			if(!isLoginUnique(connector, login)){
				throw new Exception("Login does not unique ");
			}
			Session session=connector.getSession();
			session.beginTransaction();
			Users user=new Users();
			user.setUserName(name);
			user.setUserLogin(login);
			user.setUserPassword(password);
			user.setUserRole(1);
			session.save(user);
			session.getTransaction().commit();
			return null;
		}catch(Exception ex){
			System.err.println("isLoginRepeated Exception: "+ex.getMessage());
			return ex.getMessage();
		}finally{
			connector.close();
		}
	}
	
	/** проверить логин на уникальность */
	private boolean isLoginUnique(ConnectWrap connector, String login){
		boolean returnValue=false;
		try{
			returnValue=!connector.getConnection().createStatement().executeQuery("select * from users where rupper(user_login) like '"+login.toUpperCase()+"'").next();
		}catch(Exception ex){
			System.err.println("isLoginUnique Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/** проверка на повторение логина  */
	private boolean isLoginRepeated(String login){
		boolean returnValue=false;
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			if(connector.getSession().createCriteria(Users.class)
								     .add(Restrictions.eq("login",login))
								     .list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("isLoginRepeated Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	
	/** реакция на нажатие кнопки Отменить */
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target); 
	}
}
