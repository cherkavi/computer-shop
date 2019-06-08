package window;

import org.apache.wicket.Page;
import database.ConnectWrap;
import database.StaticConnector;
import wicket_extension.UserApplication;
import window.catalog.Catalog;
import window.recognize.Recognize;

public class Application extends UserApplication{

	@Override
	protected void init() {
		// при вводе адреса /join - переход на страницу, которая содержит интерфейс для поиска ассортимента без описания
		this.mountBookmarkablePage("join", Recognize.class);
		/** положить путь к изображениям 
		try{
			String parameter=this.getInitParameter("path_to_image");
			this.setProperties("path_to_image", parameter);
		}catch(Exception ex){
			System.err.println("Application#init path_to_image Exception:"+ex.getMessage());
		}
		// положить путь к файлу-отчёту 
		try{
			String parameter=this.getInitParameter("path_to_astronomy_jrxml");
			this.setProperties("path_to_astronomy_jrxml", parameter);
		}catch(Exception ex){
			System.err.println("Application#init path_to_astronomy_jrxml Exception:"+ex.getMessage());
		}*/ 
	}
	
	public Application(){
		/*try{
			connector=new Connector();
		}catch(Exception ex){
			System.err.println("Application#constructor create Connector Error: "+ex.getMessage());
		}*/
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		return Catalog.class;
	}

	@Override
	public ConnectWrap getConnector() {
			return StaticConnector.getConnector();
	}

	
}
