package window.recognize.confirm;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import database.ConnectWrap;
import database.wrap.Assortment;
import database.wrap.Base;
import wicket_extension.UserApplication;
/** страница, которая подтверждает сопряжение товара */
public class Confirm extends WebPage{
	/** страница, на которую следует передать управление */
	private WebPage pageOk;
	private WebPage pageCancel;
	private Integer computerShopId;
	private String table;
	private Integer dictionaryId;
	
	public Confirm(WebPage pageOk, 
				   WebPage pageCancel, 
				   Integer computerShopId, 
				   String table, 
				   Integer dictionaryId ){
		this.pageOk=pageOk;
		this.pageCancel=pageCancel;
		this.table=table;
		this.computerShopId=computerShopId;
		this.dictionaryId=dictionaryId;
		initComponents();
	}
	
	private void initComponents(){
		Assortment assortment=this.getAssortment(this.computerShopId);
		Base dictionary=this.getDictionary(this.table,this.dictionaryId);
		this.add(new Label("computer_shop_name",assortment.getName()));
		this.add(new Label("dictionary_name",dictionary.getName()));
		
		Form<Object> formMain=new Form<Object>("form_main");
		this.add(formMain);
		
		Button buttonOk=new Button("button_commit"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonOk();
			};
		};
		buttonOk.add(new SimpleAttributeModifier("value",this.getString("button_commit_caption")));
		formMain.add(buttonOk);
		
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonCancel();
			};
		};
		buttonCancel.add(new SimpleAttributeModifier("value",this.getString("button_cancel_caption")));
		formMain.add(buttonCancel);
	}
	
	private void onButtonOk(){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			Connection connection=connector.getConnection();
			connection.createStatement().executeUpdate("update "+this.table+" set id_assortment="+this.computerShopId+" where id="+dictionaryId);
			connection.commit();
			connection.close();
			this.setResponsePage(this.pageOk);
		}catch(Exception ex){
			System.err.println("Confirm#onButtonOk");
			this.setResponsePage(this.pageCancel);
		}finally{
			connector.close();
		}
	}
	
	private void onButtonCancel(){
		this.setResponsePage(this.pageCancel);
	}
	
	/** получить значение из словаря, на основании имени таблицы и занчения */
	private Base getDictionary(String table, Integer dictionaryId){
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		Base returnValue=null;
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery("select * from "+table+" where id="+dictionaryId);
			if(rs.next()){
				final int id=rs.getInt(1);
				final String name=rs.getString(2);
				returnValue=new Base(){
					private final static long serialVersionUID=1L;
					@Override
					public int getId() {
						return id;
					}
					@Override
					public String getName() {
						return name;
					}
					@Override
					public void setId(int id) {
					}
					@Override
					public void setName(String name) {
					}
				};
				
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("Confirm#getDictionary Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** получить значение ассортимента */
	private Assortment getAssortment(Integer kod){
		Assortment returnValue=null;
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		try{
			returnValue=(Assortment)connector.getSession().get(Assortment.class, kod);
		}catch(Exception ex){
			System.err.println("Join#getAssortment Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
}
