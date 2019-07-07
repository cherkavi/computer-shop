package computer_shop.jobber.remote_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.remote_interface.ElementsLoader;


public class DatabaseElementsLoader implements ElementsLoader{
	public DatabaseElementsLoader(){
	}

	/** получить номер прайса для данного оптовика */
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
	public RowElement[] getElementsFromSection(String sectionName, 
											   JobberIdentifier jobberIdentifier,
											   boolean isCurrency) {
		ArrayList<RowElement> returnValue=new ArrayList<RowElement>();
		float currency=1;
		if(isCurrency==false){
			DatabaseCurrencyValue currencyValue=new DatabaseCurrencyValue();
			try{
				currency=currencyValue.getCurrencyValue();
			}catch(Exception ex){};
		};
		ResultSet rs=null;
		
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			//Session session=connector.getSession();
			int priceCount=getJobberPriceNumber(jobberIdentifier,connection);
			String query="select j_list.kod_kpi, j_list.price_1*"+currency+" price_1, j_list.price_2*"+currency+" price_2, j_list.price_3*"+currency+" price_3, j_list.price_4*"+currency+" price_4, j_list.name, j_list.warranty, j_list.store from j_list where j_list.kod_section=(select kod from j_list_section where j_list_section.name=? limit 1) order by kod";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1, sectionName);
			rs=ps.executeQuery();
			while(rs.next()){
				returnValue.add(new RowElement(rs.getString("KOD_KPI"),
											   rs.getString("NAME"),
											   rs.getFloat("PRICE_"+priceCount),
											   rs.getInt("WARRANTY"),
											   rs.getInt("STORE")));
			}
		}catch(Exception ex){
			System.err.println("DatabaseElementsLoader#getElementsFromSection Exception: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new RowElement[]{});
	}
}
