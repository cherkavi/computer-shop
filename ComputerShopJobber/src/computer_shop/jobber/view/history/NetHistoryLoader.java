package computer_shop.jobber.view.history;

import java.net.MalformedURLException;


import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.HistoryOrder;
import computer_shop.jobber.common_objects.HistoryRow;
import computer_shop.jobber.common_objects.JobberIdentifier;

public class NetHistoryLoader implements HistoryLoader {
	/** полный путь к серверу WebService*/
	private String pathToHttp;
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	private HistoryLoader client;	

	public NetHistoryLoader(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(HistoryLoader.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);
        
        String serviceUrl = this.pathToHttp+"services/HistoryLoader"; // !!!!!
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (HistoryLoader)object; // !!!!!
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        
	}
	
	@Override
	public HistoryRow[] getHistoryOrders(JobberIdentifier identifier,
									     int daysBefore) {
               
        //Invoke the service
        HistoryRow[] serviceResponse = null;// !!!!!
        try { 
            serviceResponse = client.getHistoryOrders(identifier, daysBefore);
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public HistoryOrder[] getHistoryElementsByOrder(
			JobberIdentifier identifier, HistoryRow historyRow) {
		return client.getHistoryElementsByOrder(identifier, historyRow);
	}

}
