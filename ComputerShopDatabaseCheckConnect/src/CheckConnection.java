import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CheckConnection {
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		System.out.println("begin");
		Parameter parameter=readParameter(args);
		Connection connection=getConnection(parameter);
		printResult(connection, parameter);
		System.out.println("end");
	}

	 
	
	private static void printResult(Connection connection, Parameter parameter) {
		ResultSet rs=null;
		try {
			rs=connection.createStatement().executeQuery(parameter.query);
			while(rs.next()){
				List<String> result=new ArrayList<String>();
				for(int index=1;index<=rs.getMetaData().getColumnCount();index++){
					safeAdd(rs.getObject(index), result);
				}
				printList(result);
				result.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){
			}
		}
	}

	private final static String DELIMITER="    ";

	private static void printList(List<String> result) {
		for(String eachString:result){
			System.out.print(eachString);
			System.out.print(DELIMITER);
		}
		System.out.println();
	}

	private final static String EMPTY="";

	private static void safeAdd(Object object, List<String> result) {
		if(object==null){
			result.add(EMPTY);
		}else{
			result.add(object.toString());
		}
	}



	private static Connection getConnection(Parameter parameter)  {
		try{
			Class.forName(parameter.driver);
		}catch(ClassNotFoundException ex){
			throw new RuntimeException("can't find class for name: "+parameter.driver);
		}
		
		try {
			return DriverManager.getConnection(parameter.url, parameter.login, parameter.password);
		} catch (SQLException e) {
			throw new RuntimeException("can't create connection to Database: "+parameter, e);
		}
	}



	private static Parameter readParameter(String[] args) throws FileNotFoundException, IOException {
		if(args.length==0){
			throw new RuntimeException("need to set path to file as first parameter ");
		}
		Properties properties=new Properties();
		properties.load(new FileInputStream(args[0].trim()));
		
		Parameter returnValue=new Parameter();
		returnValue.url=properties.getProperty("url");
		returnValue.login=properties.getProperty("login");
		returnValue.password=properties.getProperty("password");
		returnValue.driver=properties.getProperty("driver");
		returnValue.query=properties.getProperty("query");
		Parameter.checkForValid(returnValue);
		return returnValue;
	}
	
	
	
	
}

class Parameter{
	String url;
	String login;
	String password;
	String driver;
	String query;
	
	
	public static void checkForValid(Parameter parameter){
		checkProperty(parameter.url, "url");
		checkProperty(parameter.login, "login");
		checkProperty(parameter.password, "password");
		checkProperty(parameter.password, "driver");
		checkProperty(parameter.password, "query");
	}
	
	private static void checkProperty(String value, String parameterName){
		if(value==null || value.trim().length()==0){
			throw new RuntimeException("please, fill property: url");
		}
	}

	@Override
	public String toString() {
		return "Parameter [url=" + url + ", login=" + login + ", password="
				+ password + ", driver=" + driver + ", query=" + query + "]";
	}
	
	
	
}