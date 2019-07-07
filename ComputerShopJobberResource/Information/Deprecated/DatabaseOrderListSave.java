package computer_shop.jobber.view.order.order_list;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import computer_shop.jobber.common_objects.JobberIdentifier;
import database.Connector;

public class DatabaseOrderListSave implements OrderListSave{

	@Override
	public String saveOrderListAndGetNumber(JobberIdentifier identifier,
			String[] kodes, int[] quantities) {
		String returnValue=null;
		Connection connection=null;
		try{
			connection=Connector.getConnection();
			// получить необходимые поля для сохранения
			/** уникальный код поставщика */
			int jobberId=1; // identifier
				// получить код из таблицы JOBBER_ORDERS
			ResultSet rs=connection.createStatement().executeQuery("select gen_id(gen_jobber_orders_id,1) from rdb$database");
			rs.next();
			/** уникальный код заказа */
			int jobberOrdersId=rs.getInt(1);
			rs.close();
			PreparedStatement psJobber=connection.prepareStatement("insert into jobber_orders(kod,kod_jobber,date_write) values(?,?,?)");
			psJobber.setInt(1, jobberOrdersId);
			psJobber.setInt(2, jobberId);
			psJobber.setTimestamp(3, new Timestamp(new Date().getTime()));
			psJobber.executeUpdate();
			connection.commit();
			// записать все данные в таблицу ORDERS
			PreparedStatement getKod=connection.prepareStatement("select gen_id(gen_orders_id,1) from rdb$database");
			PreparedStatement ps=connection.prepareStatement("INSERT INTO ORDERS(KOD,KOD_JOBBER_ORDERS,KOD_KPI,QUANTITY) VALUES(?,?,?,?) "); 
			for(int counter=0;counter<kodes.length;counter++){
				ResultSet currentResultSet=getKod.executeQuery();
				currentResultSet.next();
				int id=currentResultSet.getInt(1);
				currentResultSet.close();
				
				ps.setInt(1, id);
				ps.setInt(2, jobberOrdersId);
				ps.setString(3, kodes[counter]);
				ps.setInt(4, quantities[counter]);
				ps.executeUpdate();
				connection.commit();
			}
			return Integer.toString(jobberOrdersId);
		}catch(Exception ex){
			System.err.println("DatabaseOrderListSave#saveOrderListAndGetNumber Exception:"+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

}
