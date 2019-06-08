package database;

public class StaticConnector {
	private static Connector connector;
	static{
		try{
			// INFO ������������� ���� ������ 
			connector=new Connector();
		}catch(Exception ex){
			System.err.println("StaticConnector: Exception:"+ex.getMessage());
		}
	}
	
	public static ConnectWrap getConnector(){
		try{
			synchronized(connector){
				return connector.getConnector();
			}
		}catch(Exception ex){
			System.err.println("StaticConnector#getConnector: Exception:"+ex.getMessage());
			return null;
		}
	}
}
