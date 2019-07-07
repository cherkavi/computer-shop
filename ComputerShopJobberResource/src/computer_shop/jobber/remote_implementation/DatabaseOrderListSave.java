package computer_shop.jobber.remote_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.hibernate.Session;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.database.wrap.Jobber_orders;
import computer_shop.jobber.database.wrap.Orders;
import computer_shop.jobber.remote_interface.OrderListSave;

public class DatabaseOrderListSave implements OrderListSave{

	/** получить номер прайса дл€ данного оптовика */
	private int getJobberPriceNumber(JobberIdentifier jobberIdentifier, Connection connection){
		int returnValue=1;
		PreparedStatement ps=null;
		try{
			ps=connection.prepareStatement("select * from j_jobber where j_jobber.login=? and j_jobber.jobber_password=? ");
			ps.setString(1, jobberIdentifier.getLogin());
			ps.setString(2, jobberIdentifier.getPassword());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=rs.getInt("PRICE_NUMBER");
				if(returnValue==0){
					returnValue=1;
				}
			}else{
				// jobber is not found
			}
		}catch(Exception ex){
			
		}finally{
			try{
				ps.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	@Override
	public String saveOrderListAndGetNumber(JobberIdentifier identifier,
											String[] kodes, 
											int[] quantities, 
											boolean isCurrency) {
		String returnValue=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			// получить необходимые пол€ дл€ сохранени€
			/** уникальный код поставщика */
			int jobberId=1; // identifier
				// получить новый код из таблицы JOBBER_ORDERS дл€ сохранени€ информации о заказе
			/** уникальный код заказа дл€ сохранени€ информации о заказе в таблице JOBBER_ORDERS данных */
			Jobber_orders jobberOrder=new Jobber_orders();
			jobberOrder.setKod_jobber(jobberId);
			jobberOrder.setDate_write(new Date());
			jobberOrder.setIsCurrency((isCurrency==true)?1:0);
			jobberOrder.setKodOrderStatus(0);
			session.beginTransaction();
			session.save(jobberOrder);
			session.getTransaction().commit();
			/** номер прайс-листа дл€ оптовика */
			int price_number=this.getJobberPriceNumber(identifier, connection);
			/** курс, на который следует умножать цены, в случае сохранени€ в √–Ќ */
			float course=1;
			if(isCurrency==true){
				// заказ в долларах
			}else{
				// заказ в грн - получить значение курса 
				DatabaseCurrencyValue currency=new DatabaseCurrencyValue();
				course=currency.getCurrencyValue();
			}
			// записать все данные в таблицу ORDERS
			// PreparedStatement getKod=connection.prepareStatement("select gen_id(gen_orders_id,1) from rdb$database");
			// PreparedStatement psInsert=connection.prepareStatement("INSERT INTO j_orders(KOD_JOBBER_ORDERS,KOD_KPI,QUANTITY,AMOUNT) VALUES(?,?,?,?) ");
			PreparedStatement getAmount=connection.prepareStatement("select price_"+price_number+" from j_list where j_list.kod_kpi=?");
			for(int counter=0;counter<kodes.length;counter++){
				getAmount.setString(1, kodes[counter]);
				ResultSet currentResultSet=getAmount.executeQuery();
				float amount=0;
				if(currentResultSet.next()){
					amount=currentResultSet.getFloat(1);
				}
				amount=amount*course;
				//ps.setInt(1, id);
				Orders orders=new Orders();
				orders.setKod_jobber_orders(jobberOrder.getKod());
				orders.setKod_kpi(kodes[counter]);
				orders.setQuantity(quantities[counter]);
				orders.setAmount(amount);
				session.beginTransaction();
				session.save(orders);
				session.getTransaction().commit();
			}
			return Integer.toString(jobberOrder.getKod());
		}catch(Exception ex){
			System.err.println("DatabaseOrderListSave#saveOrderListAndGetNumber Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

}
