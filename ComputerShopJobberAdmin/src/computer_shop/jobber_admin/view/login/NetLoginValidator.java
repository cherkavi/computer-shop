package computer_shop.jobber_admin.view.login;

import computer_shop.jobber.common_objects.AdminIdentifier;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

public class NetLoginValidator implements AdminLoginValidator{
	private String pathToHttp;
	public NetLoginValidator(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(AdminLoginValidator.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}

	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	
	@Override
	public AdminIdentifier checkPassword(String login, String password) throws Exception{
    
        String serviceUrl = this.pathToHttp+"services/AdminLoginValidator";
        
        AdminLoginValidator client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (AdminLoginValidator)object; 
               
        //Invoke the service
        AdminIdentifier serviceResponse = null;
        serviceResponse = client.checkPassword(login, password);
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

}
