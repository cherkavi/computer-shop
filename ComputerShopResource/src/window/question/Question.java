package window.question;

import java.util.Date;
import java.util.Random;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import database.ConnectWrap;
import wicket_extension.UserApplication;
import window.BasePage;
import window.catalog.Catalog;
import window.question.answer.Answer;

/** ������ �� ������� */
public class Question extends BasePage{
	private final static long serialVersionUID=1L;
	/** ��������� � ������������ */
	private TextField<String> name;
	/** ���� ��������� � ��������� ���� */
	private TextArea<String> message;
	/** ����� ����������� ����� */
	private TextField<String> email;
	/** ������� */
	private TextField<String> phone;
	/** ����������� ����� � ����������� */
	private TextField<String> controlText;
	/** ������� � �������������� ������� */
	private CaptchaImageResource captchaImageResouce;
	/** ������ ��� �������� ������������ ����������� */
	private Model<String> captchaModel;
	/** �����, ������� �������� ��� ��������� �������� */
	private Form<?> formMain;
	
	/** ������ �� ������� */
	public Question(){
		initComponents();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.name=new TextField<String>("name",new Model<String>());
		formMain.add(this.name);
		
		this.message=new TextArea<String>("message",new Model<String>(""));
		formMain.add(this.message);
		
		this.email=new TextField<String>("email",new Model<String>(""));
		this.email.setRequired(false);
		formMain.add(this.email);
		
		this.phone=new TextField<String>("phone",new Model<String>(""));
		this.phone.setRequired(false);
		formMain.add(this.phone);
		
		captchaModel=new Model<String>("");
		this.generateNewCaptcha();
		this.captchaImageResouce=new CaptchaImageResource(this.captchaModel);
		Image controlImage=new Image("control_image",this.captchaImageResouce);
		formMain.add(controlImage);
		
		controlText=new TextField<String>("control_text",new Model<String>(""));
		controlText.setRequired(false);
		formMain.add(controlText);
		
		formMain.add(new ComponentFeedbackPanel("feedback_form",formMain));
		
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("caption_button_ok")));
		formMain.add(buttonOk);

		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("caption_button_cancel")));
		formMain.add(buttonCancel);
	}

	/** ������� �� ������� ������� OK */
	private void onButtonOk(){
		while(true){
			// ��������� ���
			if(this.name.getModelObject()==null){
				formMain.error(this.getString("error_name"));
				break;
			}
			// ��������� �������� �������
			if((this.email.getModelObject()==null)&&(this.phone.getModelObject()==null)){
				formMain.error(this.getString("error_contakt"));
				break;
			}else{
				// ��������� �������
				if(this.phone.getModelObject()!=null){
					String tempPhone=this.phone.getModelObject().replaceAll("[+ - () _]", "");
					if(tempPhone.matches("[0-9]{12}")||tempPhone.matches("[0-9]{7}")){
						// phone is valid
					}else{
						formMain.error(this.getString("error_phone"));
						break;
					}
				}
				// ��������� E-mail
				if(this.email.getModelObject()!=null){
					if(this.email.getModelObject().matches("[A-Za-z]+[A-Za-z0-9]*@[A-Za-z0-9]*.[A-Za-z]{1,3}")){
						// E-mail is valid
					}else{
						formMain.error(this.getString("error_email"));
						break;
					}
				}
			}
			// ��������� �� ������ �����
			if(this.message.getModelObject()==null){
				formMain.error(this.getString("error_message"));
				break;
			}
			// ��������� ����������� �����
			if((this.controlText.getModelObject()==null)||(!this.isImageTextValid())){
				formMain.error(this.getString("error_control_text"));
				break;
			}
			
			database.wrap.Question question=new database.wrap.Question();
			question.setName(this.name.getModelObject());
			question.setEmail(this.email.getModelObject());
			question.setTel(this.phone.getModelObject());
			question.setMessage_text(this.message.getModelObject());
			question.setState(null);
			
			ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
			try{
				Session session=connector.getSession();
				session.beginTransaction();
				session.save(question);
				session.getTransaction().commit();
				session.close();
				this.setResponsePage(Answer.class);
			}catch(Exception ex){
				System.err.println("Question Exception: "+ex.getMessage());
				formMain.error(this.getString("error_question_save"));
			}finally{
				try{
					connector.close();
				}catch(Exception ex){};
			}
			break;
		}
	}
	
	/** ������� �� ������� ������� Cancel */
	private void onButtonCancel(){
		this.setResponsePage(Catalog.class);
	}
	
	
	/** ������������� ����� ������������������ */
	private void generateNewCaptcha(){
		this.captchaModel.setObject(this.getRandomString(5));		
	}
	
	/**  
	 * @param size - ������ 
	 * @return ��������� ����� �������� � HEX ������ �� ���������� ���-�� ��������
	 * */
	private String getRandomString(int size){
		Random random=new Random((new Date()).getTime());
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<size;counter++){
			int value=random.nextInt(10);
			//char nextChar=(char)(value+48);
			//System.out.println("next char: "+nextChar);
			returnValue.append(value);
		}
		//System.out.println(returnValue.toString());
		return returnValue.toString();
	}
	
	
	/** �������� �� ��������� � ���� ����������� ����� ������������� �������� */
	private boolean isImageTextValid(){
		try{
			return this.controlText.getModelObject().equals(this.captchaModel.getObject());
		}catch(Exception ex){
			return false;
		}
		
	}
	
}
