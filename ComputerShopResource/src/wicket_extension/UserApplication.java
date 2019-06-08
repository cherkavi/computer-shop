package wicket_extension;

import java.util.HashMap;
import java.util.Properties;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import database.ConnectWrap;

public abstract class UserApplication extends WebApplication{
	private Properties properties=new Properties();
	
	private HashMap<String,Object> objects=new HashMap<String, Object>();
	
	@Override
	public Session newSession(Request request, Response response) {
		return new UserSession(request);
	}
	
	/** положить свойство в хранилище */
	public void setProperties(String key, String value){
		this.properties.setProperty(key, value);
	}
	
	/** получить свойство из хранилища */
	public String getPropertis(String key){
		return this.properties.getProperty(key);
	}
	
	/** положить объект в хранилище */
	public void putObject(String key, Object value){
		this.objects.put(key, value);
	}

	/** получить объект из хранилища */
	public Object getObject(String key){
		return this.objects.get(key);
	}
	
	public abstract ConnectWrap getConnector();
	
}
