package computer_shop.jobber.view.order.section_tree;

import java.net.MalformedURLException;
import javax.swing.tree.TreePath;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.StringArrayElement;

public class NetSectionTree implements SectionLoader {
	private String pathToHttp;
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	
	public NetSectionTree(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(SectionLoader.class);        
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);      
	}
	
	@Override
	public boolean elementHasChild(TreePath path) {
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//java.lang.reflect.Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        boolean serviceResponse = false;
        try { 
            serviceResponse = client.elementHasChild(path);
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public boolean elementHasChild(String lastElement) {
        //Create a metadata of the service      
        Service serviceModel = new ObjectServiceFactory().create(SectionLoader.class);        
        //log.debug("callSoapServiceLocal(): got service model." );
   
        //Create a proxy for the deployed service
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);      
    
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        boolean serviceResponse = false;
        try { 
            serviceResponse = client.elementHasChild(lastElement);
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public String[] getSubElements(TreePath path) {
        //Create a metadata of the service      
        Service serviceModel = new ObjectServiceFactory().create(SectionLoader.class);        
        //log.debug("callSoapServiceLocal(): got service model." );
   
        //Create a proxy for the deployed service
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);      
    
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        String[] serviceResponse = new String[]{};
        try { 
            serviceResponse = client.getSubElements(path);
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public String[] getSubElements(String lastElement) {
        //Create a metadata of the service      
        Service serviceModel = new ObjectServiceFactory().create(SectionLoader.class);        
        //log.debug("callSoapServiceLocal(): got service model." );
   
        //Create a proxy for the deployed service
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);      
    
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        String[] serviceResponse = new String[]{};
        try { 
            serviceResponse = client.getSubElements(lastElement);
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public String[] getSubRootElements() {
        //Create a metadata of the service      
        Service serviceModel = new ObjectServiceFactory().create(SectionLoader.class);        
        //log.debug("callSoapServiceLocal(): got service model." );
   
        //Create a proxy for the deployed service
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);      
    
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        String[] serviceResponse = new String[]{};
        try { 
            serviceResponse = client.getSubRootElements();
       } catch (Exception e){
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return serviceResponse;
	}

	@Override
	public StringArrayElement getTreeRootNode() {
        String serviceUrl = this.pathToHttp+"services/SectionLoader";
        SectionLoader client = null;
        try {
        	Object object=factory.create(serviceModel, serviceUrl);
        	//java.lang.reflect.Proxy proxy=(Proxy)object;
        	//System.out.println("Class:"+object.getClass()+"   toString:"+object.toString());
            client = (SectionLoader)object; 
        } catch (MalformedURLException e) {
            //System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
        //Invoke the service
        StringArrayElement returnValue= null;
        try { 
        	returnValue = client.getTreeRootNode();
       } catch (Exception e){
            System.err.println("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
        }        
        //System.out.println("WsClient.callWebService(): status=" + serviceResponse);            
        //Return the response
        return returnValue;
	}

}
