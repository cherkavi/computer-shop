package window.main_menu.utility.point_choice;

import java.sql.ResultSet;

import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import database.ConnectWrap;
import database.wrap.Points;
import wicket_extension.User;
import wicket_extension.UserApplication;
import wicket_extension.UserSession;
import wicket_extension.action.Action;
import wicket_extension.gui.option_pane.ConfirmMessage;
import window.assortment_edit.choice_element.ChoiceElement;
import window.main_menu.MainMenu;
import window.main_menu.WindowEmulator;
import window.main_menu.utility.edit_user.UserList;
import window.main_menu.utility.point_choice.editor.ChoicePointEditor;

public class ChoicePoint extends WindowEmulator{
	private Form<Object> formMain;
	private DropDownChoice<String> dropDownChoice;
	private final static String cookieName="point";
	
	public ChoicePoint(){
		super("Выбор торговой точки");
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonAdd=new Button("button_add"){
			private static final long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonAdd();
			}
			@Override
			public boolean isVisible() {
				return ((UserSession)this.getSession()).getUser().isRoot();
			}
			
		};
		buttonAdd.add(new SimpleAttributeModifier("value","Добавить"));
		formMain.add(buttonAdd);
		
		Button buttonEdit=new Button("button_edit"){
			private static final long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonEdit();
			}
			@Override
			public boolean isVisible() {
				return ((UserSession)this.getSession()).getUser().isRoot();
			}
		};
		buttonEdit.add(new SimpleAttributeModifier("value","Редактировать"));
		formMain.add(buttonEdit);
		
		Button buttonRemove=new Button("button_remove"){
			private static final long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonRemove();
			}
			@Override
			public boolean isVisible() {
				return ((UserSession)this.getSession()).getUser().isRoot();
			}
		};
		buttonRemove.add(new SimpleAttributeModifier("value","Удалить"));
		formMain.add(buttonRemove);
		
		Button buttonEnter=new Button("button_enter"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonEnter();
			}
		};
		buttonEnter.add(new SimpleAttributeModifier("value","Вход"));
		formMain.add(buttonEnter);
		
		formMain.add(new ComponentFeedbackPanel("form_error",formMain));
		
		this.dropDownChoice=new DropDownChoice<String>("select_point");
		ArrayList<String> list=this.getListFromTableWithEmpty("select * from points order by id desc", "name");
		this.dropDownChoice.setChoices(list);
		this.dropDownChoice.setModel(new Model<String>(list.get(0)));
		formMain.add(this.dropDownChoice);
		
		// проверка на установленный Cookie
		javax.servlet.http.Cookie cookie=((WebRequest)getRequestCycle().getRequest()).getCookie(cookieName);
		if(cookie!=null){
			String pointName=this.getPointByKod(cookie.getValue());
			if(pointName!=null){
				int index=this.dropDownChoice.getChoices().indexOf(pointName);
				if(index>=0){
					this.dropDownChoice.setModelObject(this.dropDownChoice.getChoices().get(index));
				}
			}
		}

		Form<?> userForm=new Form<Object>("user_form");
		this.add(userForm);
		
		Button userButton=new Button("user_button"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonUserEdit();
			}
			@Override
			public boolean isVisible() {
				return ((UserSession)this.getSession()).getUser().isRoot();
			}
		};
		userButton.add(new SimpleAttributeModifier("value","Редактирование пользователей"));
		userForm.add(userButton);
		
		
		Form<?> assortmentForm=new Form<Object>("assortment_form");
		this.add(assortmentForm);
		
		Button assortmentButton=new Button("assortment_button"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonAssortmentEdit();
			}
		};
		assortmentButton.add(new SimpleAttributeModifier("value","Редактирование ассортимента"));
		assortmentForm.add(assortmentButton);
	}

	/** реакция нажатия на клавишу редактирования пользователей  */
	private void onButtonUserEdit(){
		this.setResponsePage(new UserList());
	}
	
	/** реакция нажатия на клавишу редактирования ассортимента */
	private void onButtonAssortmentEdit(){
		this.setResponsePage(new ChoiceElement(null,null));
	}
	
	private String getPointByKod(String pointKod){
		String returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from points where id="+Integer.parseInt(pointKod));
			if(rs.next()){
				returnValue=rs.getString("NAME");
			}
		}catch(Exception ex){
			System.out.println("ChoicePoint#getPointByKod Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		
		return returnValue;
	}
	
	/** обновить модель данных в Combobox и установить текущее значение */
	public void updateAndSetValue(String newValue){
		// обновить данные в DropDownChoice
		ArrayList<String> list=this.getListFromTableWithEmpty("select * from points order by id desc", "name");
		this.dropDownChoice.setChoices(list);
		int index=list.indexOf(newValue);
		if(index>=0){
			this.dropDownChoice.setDefaultModelObject(list.get(index));
		}
	}
	
	/** получить ArrayList на основании запроса к базе данных и имени колонки из этого запроса 
	 * @param sql - запрос к базе данных, на основании которого будут отобраны данные
	 * @param column - имя колонки, из которой будут "вытащены" данные 
	 * @return ArrayList с пустой строкой в начале 
	 */
	private ArrayList<String> getListFromTableWithEmpty(String sql, String column){
		ArrayList<String> returnValue=new ArrayList<String>();
		returnValue.add("");
		ConnectWrap connector=null;
		ResultSet rs=null;
		try{
			connector=((UserApplication)this.getApplication()).getConnector();
			rs=connector.getConnection().createStatement().executeQuery(sql);
			while(rs.next()){
				returnValue.add(rs.getString(column));
			}
		}catch(Exception ex){
			System.out.println("CreateOrder#getListFromTableWithEmpty: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** удалить значение из базы и обновить данные в модели */
	public void removeValue(String value){
		ConnectWrap connect=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connect.getSession();
			Points points=(Points)session.createCriteria(Points.class).add(Restrictions.eq("name", value)).list().get(0);
			session.beginTransaction();
			session.delete(points);
			session.getTransaction().commit();
		}catch(Exception ex){
			System.out.println("ChoicePoint#getPointNumber Exception:"+ex.getMessage());
		}finally{
			connect.close();
		}
		// обновить данные в DropDownChoice
		ArrayList<String> list=this.getListFromTableWithEmpty("select * from points order by id desc", "name");
		this.dropDownChoice.setChoices(list);
		this.dropDownChoice.setModelObject(list.get(0));
	}
	
	
	/** Добавить торговую точку */
	private void onButtonAdd(){
		this.setResponsePage(new ChoicePointEditor(null));
	}
	
	/** Редактировать торговую точку */
	private void onButtonEdit(){
		String currentValue=this.dropDownChoice.getModelObject();
		if((currentValue!=null)&&(!currentValue.equals(""))){
			this.setResponsePage(new ChoicePointEditor(currentValue));
		}else{
			formMain.error("Сделайте свой выбор");
		}
	}
	
	/** Удалить торговую точку */
	private void onButtonRemove(){
		String currentValue=this.dropDownChoice.getModelObject();
		if((currentValue!=null)&&(!currentValue.equals(""))){
			Action actionRemove=new Action(ChoicePoint.class);
			actionRemove.addMethodForCall("removeValue", new Class<?>[]{String.class}, new Object[]{currentValue});
			Action actionCancel=new Action(ChoicePoint.class);
			this.setResponsePage(new ConfirmMessage("Предупреждение", 
													"Вы уверены в удалении торговой точки ? ("+currentValue+")", 
													actionRemove, 
													actionCancel, 
													"Удалить", 
													"Отменить"));
		}else{
			formMain.error("Сделайте свой выбор");
		}
	}

	
	
	/** осуществить вход */
	private void onButtonEnter(){
		String value=this.dropDownChoice.getModelObject();
		if((value==null)||(value.equals(""))){
			formMain.error("Сделайте свой выбор");
		}else{
			Integer pointCode=this.getPointNumber(this.dropDownChoice.getModelObject());
			if((pointCode!=null)&&(pointCode.intValue()>0)){
				getWebRequestCycle().getWebResponse().addCookie(new javax.servlet.http.Cookie(cookieName, pointCode.toString()));
				((UserSession)this.getSession()).setPointNumber(pointCode);
				this.setResponsePage(MainMenu.class);
			}else{
				formMain.error("Ошибка выбора торговой точки");
			}
		}
	}
	
	/** получить номер торговой точки */
	private Integer getPointNumber(String value){
		Integer returnValue=null;
		ConnectWrap connect=((UserApplication)this.getApplication()).getConnector();
		try{
			Session session=connect.getSession();
			Points points=(Points)session.createCriteria(Points.class).add(Restrictions.eq("name", value)).list().get(0);
			returnValue=points.getId();
		}catch(Exception ex){
			System.out.println("ChoicePoint#getPointNumber Exception:"+ex.getMessage());
		}finally{
			connect.close();
		}
		return returnValue;
	}
}
