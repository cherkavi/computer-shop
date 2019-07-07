package computer_shop.jobber.admin_remote_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import computer_shop.jobber.admin_remote_interface.AdminLoginValidator;
import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;

public class DatabaseAdminLoginValidator implements AdminLoginValidator{
	public DatabaseAdminLoginValidator(){
	}
	@Override
	public AdminIdentifier checkPassword(String login, String password) {
		ConnectWrap connector=StaticConnector.getConnectWrap();
		AdminIdentifier returnValue=null;
		ResultSet rs=null;
		try{
			Connection connection=connector.getConnection();
			PreparedStatement ps=connection.prepareStatement("select * from j_admin_table where j_admin_table.login=? and j_admin_table.admin_password=?");
			ps.setString(1, login);
			ps.setString(2, password);
			rs=ps.executeQuery();
			if(rs.next()){
				returnValue=this.getJobberFromResultSet(rs);
			}
			//System.out.println("#checkPassword: "+returnValue);
		}catch(Exception ex){
			System.err.println("DatabaseLoginValidator Exception:"+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	private AdminIdentifier getJobberFromResultSet(ResultSet rs) throws SQLException{
		return new AdminIdentifier(rs.getString("LOGIN"),
								   rs.getString("ADMIN_PASSWORD"),
								   rs.getString("NAME"),
								   rs.getString("SURNAME"));
	}
}
