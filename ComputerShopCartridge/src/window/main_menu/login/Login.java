package window.main_menu.login;


import java.sql.ResultSet;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.Users;

import wicket_extension.User;
import wicket_extension.UserSession;
import window.Application;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.point_choice.ChoicePoint;

/** страница для ввода логина и пароля  */
public class Login extends WindowEmulator{
	private Form<?> formMain=null;
	private Model<String> modelLogin=new Model<String>(null);
	private Model<String> modelPassword=new Model<String>(null);
	
	/** страница для ввода логина и пароля  */
	public Login(){
		super("Ввод логина и пароля пользователя");
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		ComponentFeedbackPanel componentFeedback=new ComponentFeedbackPanel("login_error", formMain);
		formMain.add(componentFeedback);
		
		TextField<String> login=new TextField<String>("edit_login", modelLogin);
		formMain.add(login);
		login.setRequired(false);
		
		PasswordTextField password=new PasswordTextField("edit_password", modelPassword);
		formMain.add(password);
		password.setRequired(false);
		
		Button buttonEnter=new Button("button_enter"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit(){
				onButtonEnter();
			}
			
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				if(((UserSession)this.getSession()).getUser()!=null){
					this.add(new SimpleAttributeModifier("value",((UserSession)this.getSession()).getUser().getName()));
				}else{
					this.add(new SimpleAttributeModifier("value",this.getString("button_enter_caption")));
				}
			}
		};
		formMain.add(buttonEnter);
		
		Button buttonExit=new Button("button_exit"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonExit();
			}
			
			@Override
			public boolean isVisible(){
				return ((UserSession)this.getSession()).getUser()!=null;
			}
		};
		buttonExit.add(new SimpleAttributeModifier("value",this.getString("button_exit")));
		formMain.add(buttonExit);
	}
	
	private void onButtonExit(){
		((UserSession)this.getSession()).setUser(null);
	}
	
	/** пользователь нажал на клавишу входа */
	private void onButtonEnter(){
		while(true){
			// проверить вход при уже введенном логине и пароле 
			if( ((this.modelLogin.getObject()==null)||(this.modelLogin.getObject().equals("")))
			  &&((this.modelPassword.getObject()==null)||(this.modelPassword.getObject().equals("")))){
				if(((UserSession)this.getSession()).getUser()!=null){
					this.setResponsePage(new ChoicePoint());
					break;
				}
			}
			// проверить логин на ввод Root-пароля
			if(checkRoot(this.modelLogin.getObject(), this.modelPassword.getObject())){
				this.setResponsePage(new ChoicePoint());
				break;
			}
			// проверить логин на ввод пароля пользователя
			if(checkUser(this.modelLogin.getObject(), this.modelPassword.getObject())){
				this.setResponsePage(new ChoicePoint());
				break;
			}
			formMain.error(this.getString("login_password_error"));
			break;
		}
	}
	
	/** проверка на вход root пользователя, и сохранение кода пользователя в сессии   */
	private boolean checkRoot(String login, String password){
		boolean returnValue=false;
		ConnectWrap connector=((Application)this.getApplication()).getConnectorToServer();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from people where kod=5");
			if(rs.next()){
				if(rs.getString("KEY_KOD").equals(password)){
					// просходит вход по Root ключу
					((UserSession)this.getSession()).setUser(new User(0,"root"));
					returnValue=true;
				}
			}
		}catch(Exception ex){
			System.err.println("Login#checkRoot Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** проверка на вход пользователя  */
	private boolean checkUser(String login, String password){
		boolean returnValue=false;
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			Users user=(Users)connector.getSession().createCriteria(Users.class).add(Restrictions.eq("userLogin", login)).add(Restrictions.eq("userPassword", password)).setMaxResults(1).uniqueResult();
			((UserSession)this.getSession()).setUser(new User(user.getId(),user.getUserName(),user.getUserRole()));
			returnValue=true;
		}catch(Exception ex){
			System.err.println("Login#checkUser Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
}
