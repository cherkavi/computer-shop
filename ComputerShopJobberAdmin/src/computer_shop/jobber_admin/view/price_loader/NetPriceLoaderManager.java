package computer_shop.jobber_admin.view.price_loader;

import org.codehaus.xfire.XFire;

import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.AdminIdentifier;

/** класс для загрузки удаленно состояния закачки прайс-листа в базу данных */
public class NetPriceLoaderManager implements PriceLoaderManager{
	private String pathToService;
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	
	
	/** объект, который может узнавать у сервера о состоянии загрузки прайс-листа самим же сервером */
	public NetPriceLoaderManager(String url){
		String controlValue=url.trim();
		if(!controlValue.endsWith("/")){
			this.pathToService=controlValue+"/";
		}else{
			this.pathToService=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(PriceLoaderManager.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}
	
	@Override
	public int getPercentLoad() throws Exception{
        String serviceUrl = this.pathToService+"services/PriceLoaderManager";
        
        PriceLoaderManager client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (PriceLoaderManager)object; 
               
        //Invoke the service
        int returnValue=client.getPercentLoad();
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}

	@Override
	public boolean downloadPriceAndWriteIt(AdminIdentifier adminIdentifier) throws Exception{
        String serviceUrl = this.pathToService+"services/PriceLoaderManager";
        
        PriceLoaderManager client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (PriceLoaderManager)object; 
               
        //Invoke the service
        boolean returnValue=client.downloadPriceAndWriteIt(adminIdentifier);
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}

	@Override
	public String getPriceLoadUrl(AdminIdentifier adminIdentifier) throws Exception {
        String serviceUrl = this.pathToService+"services/PriceLoaderManager";
        
        PriceLoaderManager client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (PriceLoaderManager)object; 
               
        //Invoke the service
        String returnValue=client.getPriceLoadUrl(adminIdentifier);
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}

	@Override
	public String setPriceLoadUrl(AdminIdentifier adminIdentifier,String url) throws Exception {
        String serviceUrl = this.pathToService+"services/PriceLoaderManager";
        
        PriceLoaderManager client = null;

        Object object=factory.create(serviceModel, serviceUrl);
    	//Proxy proxy=(Proxy)object;
    	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
        client = (PriceLoaderManager)object; 
               
        //Invoke the service
        String returnValue=client.setPriceLoadUrl(adminIdentifier,url);
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}
	

}
