package computer_shop.jobber.remote_implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.remote_interface.CurrencyValue;

/** получить из базы данных последний курс валют для выдачи удаленному клиенту */
public class DatabaseCurrencyValue implements CurrencyValue {

	@Override
	public float getCurrencyValue() throws Exception{
		float returnValue=1;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			//Session session=connector.getSession();
			rs=connection.createStatement().executeQuery("SELECT j_list_properties.* FROM j_list_properties ORDER BY KOD DESC limit 1");
			if(rs.next()){
				returnValue=rs.getFloat("COURSE_JOBBER_CASH");
			}
		}catch(Exception ex){
			System.err.println("DatabaseCurrencyValue#getCurrencyValue Exception:"+ex.getMessage());
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

}
