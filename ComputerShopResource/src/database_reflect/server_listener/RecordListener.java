package database_reflect.server_listener;

import org.hibernate.Session;

import database.ConnectWrap;
import database.StaticConnector;
import database.wrap.*;
import database_reflect.marshalling.TransportObject;

public class RecordListener implements ITableRecordListener {
	
	public RecordListener(){
		System.out.println("RecordListener was created ");
	}
	
	@Override
	public String getRecord(TransportObject transport) {
		CartridgeModel cartridgeModel=transport.getCartridgeModel();
		//System.out.println(cartridgeModel);
		CartridgeVendor cartridgeVendor=transport.getCartridgeVendor();
		//System.out.println(cartridgeVendor);
		Customer customer=transport.getCustomer();
		//System.out.println(customer);
		OrderGroup orderGroup=transport.getOrderGroup();
		//System.out.println(orderGroup);
		OrderList orderList=transport.getOrderList();
		//System.out.println(orderList);

		ConnectWrap connector=StaticConnector.getConnector();
		Session session=connector.getSession();
		try{
			session.beginTransaction();
			if(cartridgeModel!=null){
				session.saveOrUpdate(cartridgeModel);
			}
			if(cartridgeVendor!=null){
				session.saveOrUpdate(cartridgeVendor);
			}
			if(customer!=null){
				session.saveOrUpdate(customer);
			}
			if(orderGroup!=null){
				session.saveOrUpdate(orderGroup);
			}
			if(orderList!=null){
				session.saveOrUpdate(orderList);
			}
			session.getTransaction().commit();
			System.out.println("RecordListener SaveOk ");
		}catch(Exception ex){
			try{
				session.getTransaction().rollback();
			}catch(Exception exInner){};
			System.err.println("RecordListener#getRecord: Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return "OK";
	}

}
