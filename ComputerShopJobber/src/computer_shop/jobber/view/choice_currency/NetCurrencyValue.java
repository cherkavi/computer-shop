package computer_shop.jobber.view.choice_currency;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

public class NetCurrencyValue implements CurrencyValue {

	private String pathToHttp;
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;


	public NetCurrencyValue(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(CurrencyValue.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}

	
	@Override
	public float getCurrencyValue() throws Exception{
        String serviceUrl = this.pathToHttp+"services/CurrencyValue";

        CurrencyValue client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (CurrencyValue)object; 
               
        //Invoke the service
        float returnValue = 1;
        returnValue = client.getCurrencyValue();
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}

}
