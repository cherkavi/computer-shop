package computer_shop.jobber.admin_remote_implementation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import computer_shop.jobber.admin_remote_interface.AdminOrders;
import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber.common_objects.ElementOfOrder;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.database.wrap.Admin;

public class DatabaseAdminOrders implements AdminOrders{
	public DatabaseAdminOrders(){
	}

	/** проверить уникальный идентификатор администратора на валидность, то есть есть ли такой в базе данных */
	private boolean isAdminIdentifier(AdminIdentifier adminIdentifier, Session session){
		boolean returnValue=false;
		try{
			Object adminObject=session.createCriteria(Admin.class)
				   .add(Restrictions.eq("login", adminIdentifier.getLogin()))
				   .add(Restrictions.eq("password", adminIdentifier.getPassword())).uniqueResult();
			if(adminObject!=null){
				// объект найден 
				returnValue=true;
			}else{
				// объект не найден - ошибка регистрации 
				returnValue=false;
			}
					               
		}catch(Exception ex){
			System.err.println("DatabaseAdminPriceLoaderManager#isAdminIdentifier: "+ex.getMessage());
		}
		return returnValue;
	}

	/** получить код администратора по данному идентификатору */
	private int getAdminKod(AdminIdentifier adminIdentifier, Session session){
		int returnValue=(-1);
		try{
			Object adminObject=session.createCriteria(Admin.class)
				   .add(Restrictions.eq("login", adminIdentifier.getLogin()))
				   .add(Restrictions.eq("password", adminIdentifier.getPassword())).uniqueResult();
			if(adminObject!=null){
				// объект найден
				returnValue=((Admin)adminObject).getKod();
			}else{
				// объект не найден - ошибка регистрации 
				returnValue=(-1);
			}
		}catch(Exception ex){
			System.err.println("DatabaseAdminPriceLoaderManager#isAdminIdentifier: "+ex.getMessage());
		}
		return returnValue;
	}
	
	@Override
	public boolean changeStatus(AdminIdentifier adminIdentifier, int orderKod, int statusForSet) throws Exception {
		boolean returnValue=false;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Session session=connector.getSession();
			Connection connection=connector.getConnection();
			if(isAdminIdentifier(adminIdentifier, session)){
				// function: 
				connection.createStatement().executeUpdate("update j_jobber_orders SET KOD_ORDER_STATUS="+statusForSet+"  WHERE KOD="+orderKod);
				connection.commit();
				returnValue=true;
			}else{
				throw new AdminDetectException("DatabaseAdminOrders#changeStatus");
			}
		}catch(Exception ex){
			System.err.println("DatabaseEdmitOrders.changeStatus Exception: ");
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public ElementOfOrder[] getElementsForOrder(AdminIdentifier adminIdentifier, int orderKod) throws Exception {
		ElementOfOrder[] returnValue=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		ResultSet rs=null;
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			if(isAdminIdentifier(adminIdentifier, session)){
				// function:
				rs=connection.createStatement().executeQuery("SELECT * FROM j_orders WHERE KOD_JOBBER_ORDERS="+orderKod+" order by KOD");
				ArrayList<ElementOfOrder> list=new ArrayList<ElementOfOrder>();
				while(rs.next()){
					try{
						list.add(new ElementOfOrder(rs.getString("KOD_KPI"),rs.getInt("QUANTITY")));
					}catch(Exception ex){};
				}
				returnValue=list.toArray(new ElementOfOrder[]{});
			}else{
				throw new AdminDetectException("DatabaseAdminOrders#getElementsForOrder");
			}
		}catch(Exception ex){
			System.err.println("DatabaseAdminOrders.getElementsForOrder Exception: ");
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	private SimpleDateFormat sqlDate=new SimpleDateFormat("yyyy.MM.dd");
	
	@Override
	public AdminTableOrdersRow[] getListOfOrders( AdminIdentifier adminIdentifier, int status, int dayBefore) throws Exception {
		AdminTableOrdersRow[] returnValue=null;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			int kodAdmin=this.getAdminKod(adminIdentifier, session);
			if(kodAdmin>=0){
				// function:
				ArrayList<AdminTableOrdersRow> list=new ArrayList<AdminTableOrdersRow>();
				Calendar date=Calendar.getInstance();
				date.add(Calendar.DAY_OF_YEAR, (-1)*dayBefore);
				StringBuffer query=new StringBuffer();
				query.append("SELECT j_jobber_orders.KOD KOD_ORDER, \n");
				query.append("	j_jobber.NAME JOBBER_NAME, \n");
				query.append("	j_jobber.SURNAME JOBBER_SURNAME, \n");
				query.append("	j_jobber_orders.DATE_WRITE DATE_WRITE, \n");
				query.append("	j_jobber_orders.IS_CURRENCY CURRENCY, \n");
				query.append("	(SELECT SUM(j_orders.QUANTITY) \n");
				query.append("		FROM j_orders \n");
				query.append("		WHERE j_orders.KOD_JOBBER_ORDERS=j_jobber_orders.KOD) QUANTITY, \n");
				query.append("	(SELECT SUM(j_list.PRICE_3*j_orders.QUANTITY)  \n");
				query.append("		FROM j_orders \n");
				query.append("		INNER JOIN j_list ON j_list.KOD_KPI=j_orders.KOD_KPI \n");
				query.append("		WHERE j_orders.KOD_JOBBER_ORDERS=j_jobber_orders.KOD) AMOUNT, \n");
				query.append(" j_jobber_orders.KOD_ORDER_STATUS STATUS \n");
				query.append(" FROM j_jobber_orders \n");
				query.append(" INNER JOIN j_jobber ON j_jobber.KOD=j_jobber_orders.KOD_JOBBER \n");
				query.append("WHERE  \n");
				if(status>=0){
					query.append("j_jobber_orders.KOD_ORDER_STATUS="+status+"  AND \n");
				}else{
					// all statuses
				}
				query.append(" j_jobber_orders.DATE_WRITE>='"+sqlDate.format(date.getTime())+"' \n");
				query.append("ORDER BY j_jobber_orders.KOD DESC \n");
				rs=connection.createStatement().executeQuery(query.toString());
				while(rs.next()){
					try{
						AdminTableOrdersRow element=new AdminTableOrdersRow();
						element.setKodOrder(rs.getInt("KOD_ORDER"));
						element.setJobberName(rs.getString("JOBBER_NAME"));
						element.setJobberSurname(rs.getString("JOBBER_SURNAME"));
						element.setTimeWrite(rs.getDate("DATE_WRITE"));
						element.setCurrency((rs.getInt("CURRENCY")==0)?false:true);
						element.setQuantity(rs.getInt("QUANTITY"));
						element.setAmount(rs.getFloat("AMOUNT"));
						element.setStatus(rs.getInt("STATUS"));
						list.add(element);
					}catch(Exception ex){
						System.err.println("getList");
					}
				}
				//System.out.println("DatabaseAdminOrders Count: "+list.size());
				returnValue=list.toArray(new AdminTableOrdersRow[]{});
			}else{
				throw new AdminDetectException("DatabaseAdminOrders#getListOfOrders");
			}
		}catch(Exception ex){
			System.err.println("DatabaseAdminOrders.getListOfOrders Exception: "+ex.getMessage());
			throw ex;
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
}
