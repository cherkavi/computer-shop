package computer_shop.jobber_admin.view.jobber_edit;

import org.codehaus.xfire.XFire;

import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;

public class NetJobberEdit implements IJobberEdit {
	private Service serviceModel;
	private XFire xfire;
	private XFireProxyFactory factory;
	private String serviceUrl;
	private String pathToHttp;
	private IJobberEdit client;
	
	public NetJobberEdit(String pathToHttp){
		String controlValue=pathToHttp.trim();
		if(!controlValue.endsWith("/")){
			this.pathToHttp=controlValue+"/";
		}else{
			this.pathToHttp=controlValue;
		}
        //Create a metadata of the service      
        serviceModel= new ObjectServiceFactory().create(IJobberEdit.class);
        serviceUrl = this.pathToHttp+"services/IJobberEdit";
        //Create a proxy for the deployed service
        xfire=XFireFactory.newInstance().getXFire();
        factory = new XFireProxyFactory(xfire);
        try{
            Object object=factory.create(serviceModel, serviceUrl);
            client = (IJobberEdit)object;
        }catch(Exception ex){
        	System.err.println("NetJobberEdit Exception:"+ex.getMessage());
        }
	}
	
	@Override
	public String add(AdminIdentifier adminIdentifier,
					   JobberElement jobberElement) throws Exception{
		return client.add(adminIdentifier, jobberElement);
	}

	@Override
	public JobberElement[] getAllJobbers(AdminIdentifier adminIdentifier) throws Exception{
		return client.getAllJobbers(adminIdentifier);
	}

	@Override
	public String update(AdminIdentifier adminIdentifier,
						  JobberElement jobberElement) throws Exception{
		return client.update(adminIdentifier, jobberElement);
	}

	@Override
	public String remove(AdminIdentifier adminIdentifier,
						  JobberElement jobberElement) throws Exception {
		return client.remove(adminIdentifier, jobberElement);
	}

}
