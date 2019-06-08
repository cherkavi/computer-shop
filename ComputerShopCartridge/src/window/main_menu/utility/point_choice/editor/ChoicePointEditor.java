package window.main_menu.utility.point_choice.editor;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.Points;

import wicket_extension.UserApplication;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.point_choice.ChoicePoint;

/** страница, которая служит для редактирования торговой точки */
public class ChoicePointEditor extends WindowEmulator{
	private String editValue=null;
	private Form<Object> formMain;
	private TextField<String> textFieldPoint;
	
	public ChoicePointEditor(String editValue){
		super();
		this.editValue=editValue;
		if(editValue==null){
			this.setTitle("Добавление нового значения");
		}else{
			this.setTitle("Редактирование ("+editValue+")");
		}
		initComponents(editValue);
	}
	
	private void initComponents(String editValue){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		this.textFieldPoint=new TextField<String>("textfield_point",new Model<String>(editValue));
		formMain.add(this.textFieldPoint);
		
		Button buttonOk=new Button("button_ok"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value","Сохранить"));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value","Отменить"));
		formMain.add(buttonCancel);
		
		formMain.add(new ComponentFeedbackPanel("form_error",formMain));
		
	}
	
	
	private void onButtonOk(){
		while(true){
			String newValue=this.textFieldPoint.getModelObject();
			// проверка на пустое значение 
			if((newValue==null)||(newValue.trim().equals(""))){
				this.formMain.error("Введите значение");
				break;
			}
			// проверка на повторение в базе
			if(isRepeatValue(newValue)){
				this.formMain.error("Такое значение уже есть, введите другое");
				break;
			}
			if(savePoint(newValue)==true){
				ChoicePoint choicePoint=new ChoicePoint();
				choicePoint.updateAndSetValue(newValue);
				this.setResponsePage(choicePoint);
			}else{
				this.formMain.error("Ошибка сохранения данных");
				break;
			}
			// сохранение данных 
			break;
		}
	}
	
	/** сохранить данные */
	private boolean savePoint(String value){
		boolean returnValue=false;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			Points points=null;
			if(this.editValue==null){
				// insert
				points=new Points();
				points.setName(value);
			}else{
				// update
				points=(Points)session.createCriteria(Points.class).add(Restrictions.eq("name",this.editValue)).list().get(0);
			}
			points.setName(value);
			session.beginTransaction();
			session.saveOrUpdate(points);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.out.println("ChoicePointEditor#savePoint: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** проверка значения на повторение по базе */
	private boolean isRepeatValue(String newValue) {
		boolean returnValue=true;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connector.getSession();
			if(session.createCriteria(Points.class).add(Restrictions.eq("name",newValue)).list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.out.println("ChoicePointEditor#isRepeatValue: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	private void onButtonCancel(){
		this.setResponsePage(ChoicePoint.class);
	}
}
