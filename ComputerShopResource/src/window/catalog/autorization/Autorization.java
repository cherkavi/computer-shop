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

/** ������ ����������� ������������  */
public class Autorization extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor executor;
	private Form<?> formMain;
	private TextField<String> editLogin;
	private PasswordTextField editPassword;
	/** �������, ������� ����� ������� � ������ �������� ����������� */
	private String actionOk=null;
	/** �������, ������� ����� ������� � ������ �� �������� ����������� */
	private String actionCancel=null;
	/** ��������, ������� ����� �������� ������ � �������� � ������ �������� ����������� */
	private Serializable argument=null;
	private static String autorizationOk="AUTORIZATION.OK"; 
	private static String autorizationCancel="AUTORIZATION.CANCEL"; 
	
	/** ������ ����������� ������������ 
	 * @param id - ���������� ������������� ������
	 * @param executor - ������, �������� ����� �������� ���������� 
	 * <li>AUTORIZATION.OK</li>
	 * <li>AUTORIZATION.CANCEL</li>
	 */
	public Autorization(String id,IActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}

	/** ������ ����������� ������������ 
	 * @param id - ���������� ������������� ������ 
	 * @param executor - �����������, �������� ����� �������� ���������� 
	 * @param actionOk - ��� ��������, ������� ����� ��������� � ������ �������� �������� 
	 * @param actionCancel - ��� ��������, ������� ����� ��������� � ������ �� �������� �������� 
	 * @param argument - ��������, ������� ����� ������� � ������ �������� �������� 
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
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.formMain.add(new Label("caption","����������� ������������"));
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
			// �������� �� ������� ������������� �����, ���� �� �� ������������� ����� 
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
	
	/** ����������� ������ �������, �������� ���������� � Executor */
	private void doOk(){
		if(this.actionOk!=null){
			this.executor.action(this.actionOk, this.argument);
		}else{
			this.executor.action(autorizationOk, null);
		}
	}
	/** ����������� ��������, �������� ���������� � Executor */
	private void onButtonCancel(){
		if(this.actionCancel!=null){
			this.executor.action(this.actionCancel, this.argument);
		}else{
			this.executor.action(autorizationCancel, null);
		}
	}
}
