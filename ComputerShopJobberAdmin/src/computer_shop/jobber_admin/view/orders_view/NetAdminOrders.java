package computer_shop.jobber_admin.view.orders_view;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber.common_objects.ElementOfOrder;

public class NetAdminOrders implements AdminOrders{
	private String pathToHttp;
	
	public NetAdminOrders(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(AdminOrders.class);
        serviceUrl = this.pathToHttp+"services/AdminOrders";
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);
	}

	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	private String serviceUrl;
	
	
	@Override
	public boolean changeStatus(AdminIdentifier adminIdentifier, int orderKod, int statusForSet) throws Exception {
        Object object=factory.create(serviceModel, serviceUrl);
        AdminOrders client = (AdminOrders)object;
        return client.changeStatus(adminIdentifier, orderKod,statusForSet);
	}

	@Override
	public ElementOfOrder[] getElementsForOrder(AdminIdentifier adminIdentifier, int orderKod) throws Exception {
        Object object=factory.create(serviceModel, serviceUrl);
        AdminOrders client = (AdminOrders)object;
        return client.getElementsForOrder(adminIdentifier, orderKod);
	}

	@Override
	public AdminTableOrdersRow[] getListOfOrders(AdminIdentifier adminIdentifier, int status, int dayBefore) throws Exception {
        Object object=factory.create(serviceModel, serviceUrl);
        AdminOrders client = (AdminOrders)object;
        return client.getListOfOrders(adminIdentifier, status, dayBefore);
	}

}
