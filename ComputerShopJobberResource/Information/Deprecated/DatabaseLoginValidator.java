package computer_shop.jobber.view.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import computer_shop.jobber.common_objects.JobberIdentifier;

public class DatabaseLoginValidator implements LoginValidator{
	private Connection connection;
	public DatabaseLoginValidator(Connection connection){
		this.connection=connection;
	}
	@Override
	public JobberIdentifier checkPassword(String login, String password) {
		JobberIdentifier returnValue=null;
		ResultSet rs=null;
		try{
			PreparedStatement ps=this.connection.prepareStatement("select * from jobber where jobber.login=? and jobber.jobber_password=?");
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
		}
		return returnValue;
	}
	
	private JobberIdentifier getJobberFromResultSet(ResultSet rs) throws SQLException{
		return new JobberIdentifier(rs.getString("LOGIN"),rs.getString("JOBBER_PASSWORD"),rs.getString("NAME"),rs.getString("SURNAME"));
	}
}
