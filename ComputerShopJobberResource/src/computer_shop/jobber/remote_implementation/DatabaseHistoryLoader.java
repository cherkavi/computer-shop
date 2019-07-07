package computer_shop.jobber.remote_implementation;

import java.sql.Connection;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import computer_shop.jobber.common_objects.HistoryOrder;
import computer_shop.jobber.common_objects.HistoryRow;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.database.wrap.Jobber;
import computer_shop.jobber.remote_interface.HistoryLoader;

public class DatabaseHistoryLoader implements HistoryLoader {
	private SimpleDateFormat sqlFormat=new SimpleDateFormat("yyyy.MM.dd");
	
	
	/** получить объект Jobber на основании присланного идентификатора */
	private Jobber getJobberByIdentifier(JobberIdentifier identifier, Session session){
		try{
			return (Jobber)session.createCriteria(Jobber.class)
	        .add(Restrictions.eq("login", identifier.getLogin()))
	        .add(Restrictions.eq("password", identifier.getPassword()))
	        .uniqueResult();
		}catch(Exception ex){
			System.err.println("DatabaseHistoryLoader#getJobberByIdentifier: jobber is not recognized ");
			return null;
		}
	}
	
	@Override
	public HistoryRow[] getHistoryOrders(JobberIdentifier identifier,
										 int daysBefore) {
		ArrayList<HistoryRow> list=new ArrayList<HistoryRow>();
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			Jobber jobber=this.getJobberByIdentifier(identifier, session);
			//System.out.println("KodJobber: "+jobber.getKod());
			//SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy ");
			Calendar dateBefore=Calendar.getInstance();
			dateBefore.setTime(new Date());
			dateBefore.add(Calendar.DAY_OF_MONTH, (-1)*daysBefore);
			//System.out.println("Date before: "+sdf.format(dateBefore.getTime()));
			StringBuffer query=new StringBuffer();
 		    query.append("	select  j_jobber_orders.kod,	 ");
		    query.append("	        j_jobber_orders.date_write,	 ");
		    query.append("	        j_jobber_orders.is_currency,	 ");
		    query.append("	        (select sum(j_orders.quantity)	 ");
		    query.append("	         from j_orders	 ");
		    query.append("	         where j_orders.kod_jobber_orders=j_jobber_orders.kod) quantity,	 ");
		    query.append("	        (select sum(amount*j_orders.quantity)	 ");
		    query.append("	         from j_orders	 ");
		    query.append("	         inner join j_list on j_list.kod_kpi=j_orders.kod_kpi	 ");
		    query.append("	         where j_orders.kod_jobber_orders=j_jobber_orders.kod	 ");
		    query.append("	         ) amount	 ");
		    query.append("	from j_jobber_orders	 ");
		    query.append("	where kod_jobber="+jobber.getKod());
		    query.append("	and date_write>='"+sqlFormat.format(dateBefore.getTime())+"' ");
		    query.append("  order by j_jobber_orders.kod desc ");
		    ResultSet rs=connection.createStatement().executeQuery(query.toString());
		    while(rs.next()){
		    	try{
		    		list.add(new HistoryRow(rs.getString(1),rs.getDate(2),rs.getInt(3),rs.getInt(4),rs.getFloat(5)));
		    	}catch(SQLException ex){};
		    }
			//System.out.println("JobberOrders: size:"+list.size());
		}catch(Exception ex){
			System.err.println("DatabaseHistoryLoader#getHistoryOrders Exception: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return list.toArray(new HistoryRow[]{});
	}
	
	
	@Override
	public HistoryOrder[] getHistoryElementsByOrder(JobberIdentifier identifier, 
													HistoryRow historyRow) {
		ResultSet rs=null;
		ArrayList<HistoryOrder> list=new ArrayList<HistoryOrder>();
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			// проверить идентификатор
			Jobber jobber=this.getJobberByIdentifier(identifier, session);
			if(jobber==null){
				throw new Exception("Jobber is not recognized");
			}
			StringBuffer query=new StringBuffer();
			query.append("	select j_orders.kod kod,	");
			query.append("	       j_list_section.name section_name,	");
			query.append("	       j_list.name name,	");
			query.append("	       j_orders.quantity quantity,	");
			query.append("	       j_orders.amount amount	");
			query.append("	from j_orders	");
			query.append("	left join j_list on j_list.kod_kpi=j_orders.kod_kpi	");
			query.append("	    inner join j_list_section on j_list_section.kod=j_list.kod_section	");
			query.append("	where j_orders.kod_jobber_orders="+historyRow.getNumber());
			query.append("  order by j_orders.kod");
			rs=connection.createStatement().executeQuery(query.toString());
				// получить по указанной позиции заказа все элементы заказа
			while(rs.next()){
				list.add(new HistoryOrder(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getFloat(5)));
			}
		}catch(Exception ex){
			System.err.println("DatabaseHistoryLoader#getHistoryOrders Exception: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		// преобразовать все элементы заказа в массив 
		return list.toArray(new HistoryOrder[]{});
	}

}
