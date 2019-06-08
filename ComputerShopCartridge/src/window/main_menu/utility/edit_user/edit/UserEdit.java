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

/** �������������� ������������ ������� ��������� */
public class UserEdit extends Panel{
	private final static long serialVersionUID=1L;
	
	private Model<String> modelName=new Model<String>();
	private Model<String> modelLogin=new Model<String>();
	private Model<String> modelPassword=new Model<String>();
	/** ��������� �� ������  */
	private Model<String> modelErrorMessage=new Model<String>(null);
	/** ��������� ����, � ��������� �������� ���������� ����������� */
	private ModalWindow modalWindow;
	/** ������, ����������� ��������  */
	private IActionExecutor actionExecutor;
	/** ������, ������� ���������� ��������� �� ������ �� ������ ��������������/�������� */
	private WebMarkupContainer errorMessageContainer;
	
	/** �������������� ������������ ������� ���������
	 * @param id - ���������� ������������� 
	 * @param modalWindow - ��������� ����, � ��������� �������� ������������ ������ ����
	 * @param actionExecutor - ����������� 
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
		
		formMain.add(new Label("title","�������� ������������"));
		
		formMain.add(new Label("edit_name_label","������ ��� ������������"));
		formMain.add(new TextField<String>("edit_name",modelName));
		
		formMain.add(new Label("edit_login_label","�����:"));
		formMain.add(new TextField<String>("edit_login",modelLogin));
		
		formMain.add(new Label("edit_password_label","������:"));
		formMain.add(new TextField<String>("edit_password",modelPassword));
		
		AjaxButton buttonOk=new AjaxButton("button_save"){
			private final static long serialVersionUID=1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onButtonOk(target);
			}
		};
		buttonOk.add(new SimpleAttributeModifier("value","���������"));
		formMain.add(buttonOk);
		
		AjaxButton buttonCancel=new AjaxButton("button_cancel"){
			private final static long serialVersionUID=1L;
			
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				onButtonCancel(target);
			}
		};
		buttonCancel.add(new SimpleAttributeModifier("value","��������"));
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
	
	/** ������� �� ������� ������ Save */
	private void onButtonOk(AjaxRequestTarget target){
		this.modelErrorMessage.setObject(null);
		while(true){
			if((modelName.getObject()==null)||(modelName.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("������� ���");
				target.addComponent(errorMessageContainer);
				break;
			}
			if((modelLogin.getObject()==null)||(modelLogin.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("������� �����");
				target.addComponent(errorMessageContainer);
				break;
			}
			if((modelPassword.getObject()==null)||(modelPassword.getObject().trim().equals(""))){
				this.modelErrorMessage.setObject("������� ������");
				target.addComponent(errorMessageContainer);
				break;
			}
			// ���������� ����� ��������  
			// �������� �� ���������� ������
			if(this.isLoginRepeated(modelLogin.getObject())){
				this.modelErrorMessage.setObject("����� ����� ��� ����������");
				target.addComponent(errorMessageContainer);
				break;
			}
			// ��� ���������� �������� - ����������
			String saveError=saveUser(modelName.getObject(), modelLogin.getObject(), modelPassword.getObject());
			if(saveError==null){
				// System.out.println("Name: "+modelName.getObject());
				// System.out.println("Login: "+modelLogin.getObject());
				// System.out.println("Password: "+modelPassword.getObject());
				// ������ ��������� 
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
	
	/** ��������� ������������
	 * @return 
	 * <ul>
	 * 	<li><b>null</b> ������������ ������� �������� </li>
	 * 	<li><b>String</b> ������ ���������� ������������ � ��������� ���� </li>
	 * </ul>
	 *  */
	private String saveUser(String name, String login, String password) {
		ConnectWrap connector=((Application)this.getApplication()).getConnector();
		try{
			// ��������� ����� �� ������������
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
	
	/** ��������� ����� �� ������������ */
	private boolean isLoginUnique(ConnectWrap connector, String login){
		boolean returnValue=false;
		try{
			returnValue=!connector.getConnection().createStatement().executeQuery("select * from users where rupper(user_login) like '"+login.toUpperCase()+"'").next();
		}catch(Exception ex){
			System.err.println("isLoginUnique Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/** �������� �� ���������� ������  */
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
	
	
	/** ������� �� ������� ������ �������� */
	private void onButtonCancel(AjaxRequestTarget target){
		this.modalWindow.close(target); 
	}
}
