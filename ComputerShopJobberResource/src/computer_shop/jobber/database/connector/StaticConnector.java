package computer_shop.jobber.database.connector;

public class StaticConnector {
	/** соединение с базой данных */
	static Connector connector;
	
	static{
		try {
			connector=new Connector();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**  
	 * @return получить соединение с базой данных ({@link ConnectWrap}) 
	 * */
	public static ConnectWrap getConnectWrap(){
		return connector.getConnector();
	}
}
