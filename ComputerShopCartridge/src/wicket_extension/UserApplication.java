package wicket_extension;

import java.util.HashMap;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import database.ConnectWrap;
import database.IConnectorAware;

public abstract class UserApplication extends WebApplication implements IConnectorAware{
	private HashMap<String,Object> properties=new HashMap<String,Object>();
	
	@Override
	public Session newSession(Request request, Response response) {
		return new UserSession(request);
	}
	
	public void setProperties(String key, Object value){
		this.properties.put(key, value);
		System.out.println("Set key:"+key);
	}
	
	public Object getPropertis(String key){
		return this.properties.get(key);
	}
	
	public abstract ConnectWrap getConnector();
	
}
