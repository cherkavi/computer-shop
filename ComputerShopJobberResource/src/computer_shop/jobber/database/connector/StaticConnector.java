package computer_shop.jobber.database.connector;

public class StaticConnector {
	/** ���������� � ����� ������ */
	static Connector connector;
	
	static{
		try {
			connector=new Connector();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**  
	 * @return �������� ���������� � ����� ������ ({@link ConnectWrap}) 
	 * */
	public static ConnectWrap getConnectWrap(){
		return connector.getConnector();
	}
}
