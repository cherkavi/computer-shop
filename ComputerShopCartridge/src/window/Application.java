package window;

import org.apache.wicket.Page;



import report_servlet.ReportGenerator;

import database.ConnectWrap;
import database.Connector;
import database_reflect.ReflectWorker;
import wicket_extension.UserApplication;
import window.main_menu.login.Login;

public class Application extends UserApplication{
	static{
		ReportGenerator.init("c:\\temp\\", "c:\\temp_pattern\\", "Receipt.jrxml", "Barcode.jrxml", "Cheque.jrxml");
	}

	private Connector connector;
	private Connector connectorServer;
	@Override
	protected void init() {
		super.init();
		this.setProperties("path_to_report", "c:\\temp\\");
		try{
			connector=new Connector("computer_shop_cartridge");
			connectorServer=new Connector("server");
		}catch(Exception ex){
			System.err.println("Application#constructor create Connector Error: "+ex.getMessage());
		}
		
		
		String computerShopPhoto=null;
		try{
			computerShopPhoto=this.getInitParameter("path_to_local_image");
			//pathToResource=this.getInitParameter("path_to_resource");
		}catch(Exception ex){
			System.err.println("Application#init getInitParameter(\"path_to_local_image\") ");
			computerShopPhoto="c:\\computer_shop_photo\\";
		}
		this.setProperties("path_to_local_image", computerShopPhoto);
		
		String pathToResource=null;
		try{
			pathToResource=this.getInitParameter("path_to_resource");
			//pathToResource=this.getInitParameter("path_to_resource");
		}catch(Exception ex){
			System.err.println("Application#init getInitParameter(\"path_to_resource\") ");
			pathToResource="http://localhost:8080/ComputerShopResource";
		}
		// убрать комментарии при Deploy ( закомментированные ускоряют работу )
		ReflectWorker reflectWorker=new ReflectWorker(pathToResource,this,1*1000,10*1000);
		this.setProperties("reflect_worker", reflectWorker);
		// FIXME запуск демона, который бы смотрел на таблицу ASSORTMENT и вынимал те данные, у которых есть ASSORTMENT.UPDATE_IN_SERVER=1 ( обновление фото ) или же есть ASSORTMENT.UPDATE_IN_SERVER=2 (обновление данных сборки )
		
	}
	
	public Application(){
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		// return ChoicePoint.class;
		return Login.class;
	}

	@Override
	public ConnectWrap getConnector() {
		return connector.getConnector();
	}

	/** получить соединение с основной базой данных */
	public ConnectWrap getConnectorToServer(){
		return connectorServer.getConnector();
	}
		
}
