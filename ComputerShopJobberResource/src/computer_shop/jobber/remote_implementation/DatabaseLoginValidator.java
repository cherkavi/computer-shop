package computer_shop.jobber.remote_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.remote_interface.LoginValidator;

public class DatabaseLoginValidator implements LoginValidator{
	public DatabaseLoginValidator(){
	}
	@Override
	public JobberIdentifier checkPassword(String login, String password) {
		JobberIdentifier returnValue=null;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			//Session session=connector.getSession();
			PreparedStatement ps=connection.prepareStatement("select * from j_jobber where j_jobber.login=? and j_jobber.jobber_password=?");
			ps.setString(1, login);
			ps.setString(2, password);
			rs=ps.executeQuery();
			if(rs.next()){
				returnValue=this.getJobberFromResultSet(rs);
			}
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
	
	private JobberIdentifier getJobberFromResultSet(ResultSet rs) throws SQLException{
		return new JobberIdentifier(rs.getString("LOGIN"),rs.getString("JOBBER_PASSWORD"),rs.getString("NAME"),rs.getString("SURNAME"));
	}
}
