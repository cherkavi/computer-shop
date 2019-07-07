package computer_shop.jobber.view.order.list_view;

import java.net.MalformedURLException;


import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;


public class NetElementsLoader implements ElementsLoader {
	private String pathToHttp;
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	
	public NetElementsLoader(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(ElementsLoader.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}

	@Override
	public RowElement[] getElementsFromSection(String sectionName,
											   JobberIdentifier jobberIdentifier,
											   boolean isCurrency) {
        String serviceUrl = this.pathToHttp+"services/ElementLoader"; // !!!!!
        
        ElementsLoader client = null;// !!!!!
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (ElementsLoader)object; // !!!!!
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
               
        //Invoke the service
        RowElement[] serviceResponse = null;// !!!!!
        try { 
            serviceResponse = client.getElementsFromSection(sectionName, jobberIdentifier,isCurrency);// !!!!!
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

}
