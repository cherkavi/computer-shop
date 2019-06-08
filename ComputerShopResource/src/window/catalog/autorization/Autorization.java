package window.catalog.autorization;

import java.io.Serializable;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket_extension.User;
import wicket_extension.UserSession;
import wicket_extension.action.IActionExecutor;

/** панель авторизации пользователя  */
public class Autorization extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor executor;
	private Form<?> formMain;
	private TextField<String> editLogin;
	private PasswordTextField editPassword;
	/** событие, которое нужно вызвать в случае успешной авторизации */
	private String actionOk=null;
	/** событие, которое нужно вызвать в случае не успешной авторизации */
	private String actionCancel=null;
	/** аргумент, который нужно передать вместе с событием в случае успешной авторизации */
	private Serializable argument=null;
	private static String autorizationOk="AUTORIZATION.OK"; 
	private static String autorizationCancel="AUTORIZATION.CANCEL"; 
	
	/** панель авторизации пользователя 
	 * @param id - уникальный идентификатор панели
	 * @param executor - объект, которому будет передано управление 
	 * <li>AUTORIZATION.OK</li>
	 * <li>AUTORIZATION.CANCEL</li>
	 */
	public Autorization(String id,IActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}

	/** панель авторизации пользователя 
	 * @param id - уникальный идентификатор панели 
	 * @param executor - исполнитель, которому будет передано управление 
	 * @param actionOk - имя действия, которое будет выполнено в случае успешной передачи 
	 * @param actionCancel - имя действия, которое будет выполнено в случае не успешной передачи 
	 * @param argument - аргумент, который будет передан в случае успешной передачи 
	 */
	public Autorization(String id,
						IActionExecutor executor, 
						String actionOk, 
						String actionCancel, 
						Serializable argument){
		super(id);
		this.executor=executor;
		this.actionOk=actionOk;
		this.actionCancel=actionCancel;
		this.argument=argument;
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.formMain.add(new Label("caption","Авторизация пользователя"));
		this.editLogin=new TextField<String>("login",new Model<String>(""));
		this.editLogin.setRequired(false);
		formMain.add(this.editLogin);

		this.editPassword=new PasswordTextField("password",new Model<String>(""));
		this.editPassword.setRequired(false);
		formMain.add(this.editPassword);

		this.formMain.add(new ComponentFeedbackPanel("feedback_form", formMain));
		
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_login")));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}
	
	/** */
	private void onButtonOk(){
		while(true){
			// проверка на наличие незаполненных ячеек, либо же на невозможность входа 
			String login=this.editLogin.getModelObject();
			String password=this.editPassword.getModelObject();
			if(login==null){
				this.formMain.error(this.getString("login_is_empty"));
				break;
			};
			if(password==null){
				this.formMain.error(this.getString("password_is_empty"));
			}
			User user=this.getUserByLoginAndPassword(login, password);
			if(user!=null){
				((UserSession)this.getSession()).setUser(user);
				doOk();
				break;
			}else{
				this.formMain.error(this.getString("login_error"));
				break;
			}
			//break;
		}
	}
	
	
	private User getUserByLoginAndPassword(String login, String password){
		return null;
	}
	
	/** авторизация прошла успешно, передать управление в Executor */
	private void doOk(){
		if(this.actionOk!=null){
			this.executor.action(this.actionOk, this.argument);
		}else{
			this.executor.action(autorizationOk, null);
		}
	}
	/** авторизация отменена, передать управление в Executor */
	private void onButtonCancel(){
		if(this.actionCancel!=null){
			this.executor.action(this.actionCancel, this.argument);
		}else{
			this.executor.action(autorizationCancel, null);
		}
	}
}
