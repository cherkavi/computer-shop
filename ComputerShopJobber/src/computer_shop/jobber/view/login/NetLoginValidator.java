package computer_shop.jobber.view.login;

//import java.lang.reflect.Proxy;
import computer_shop.jobber.common_objects.JobberIdentifier;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

public class NetLoginValidator implements LoginValidator{
	private String pathToHttp;
	public NetLoginValidator(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(LoginValidator.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}

	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	
	@Override
	public JobberIdentifier checkPassword(String login, String password) throws Exception{
    
        String serviceUrl = this.pathToHttp+"services/LoginValidator";
        
        LoginValidator client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (LoginValidator)object; 
               
        //Invoke the service
        JobberIdentifier serviceResponse = null;
        serviceResponse = client.checkPassword(login, password);
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

}
