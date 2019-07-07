package computer_shop.jobber.view.order.list_view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;

public class DatabaseElementsLoader implements ElementsLoader{
	private Connection connection;
	
	public DatabaseElementsLoader(Connection connection){
		this.connection=connection;
	}

	@Override
	public RowElement[] getElementsFromSection(String sectionName, JobberIdentifier jobberIdentifier) {
		ArrayList<RowElement> returnValue=new ArrayList<RowElement>();
		ResultSet rs=null;
		int priceCount=1;
		try{
			String query="select list.* from list where list.kod_section=(select first 1 skip 0 kod from list_section where list_section.name=?) order by kod";
			PreparedStatement ps=this.connection.prepareStatement(query);
			ps.setString(1, sectionName);
			rs=ps.executeQuery();
			while(rs.next()){
				returnValue.add(new RowElement(rs.getString("KOD_KPI"),rs.getString("NAME"),rs.getFloat("PRICE_"+priceCount),rs.getInt("WARRANTY"),rs.getInt("STORE")));
			}
		}catch(Exception ex){
			System.err.println("DatabaseElementsLoader#getElementsFromSection Exception: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new RowElement[]{});
	}
}
