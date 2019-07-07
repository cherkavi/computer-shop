package computer_shop.jobber.admin_remote_implementation;

import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.IConnectWrapAware;
import computer_shop.jobber.price_loader.PriceLoader;

/** класс, который содержит необходимые данные для загрузки прайса из Microsoft Excel в Connector*/
public class ThreadPriceLoader implements Runnable{
	private IConnectWrapAware connectorAware;
	private PriceLoader priceLoader;
	private String pathToXls;
	private int xlsPositionBegin;
	
	/** Создать отдельный поток, в котором загрузить все необходимые данные в прайс 
	 * @param connection - соединение с базой данных
	 * @param session - hibernate соединение с базой данных
	 * @param priceLoader - загрузчик прайса 
	 * @param pathToXls - путь к Excel файлу 
	 * @param xlsPositionBegin - первая позиция для загрузки данных из Excel файла 
	 */
	public ThreadPriceLoader(IConnectWrapAware connectorAware,
							 PriceLoader priceLoader,
							 String pathToXls,
							 int xlsPositionBegin){
		this.connectorAware=connectorAware;
		this.priceLoader=priceLoader;
		this.pathToXls=pathToXls;
		this.xlsPositionBegin=xlsPositionBegin;
		Thread thread=new Thread(this);
		thread.start();
	}
	
	@Override
	public void run(){
		ConnectWrap connector=this.connectorAware.getConnectWrap();
		try{
			priceLoader.writeListToDatabaseFromXls(connector.getSession(), pathToXls, this.xlsPositionBegin);
		}catch(Exception ex){
			System.err.println("ThreadPriceLoader#run Exception: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
	}
}
