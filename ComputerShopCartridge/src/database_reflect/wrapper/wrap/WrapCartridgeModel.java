package database_reflect.wrapper.wrap;

import java.sql.ResultSet;
import database.wrap.CartridgeModel;
import database_reflect.wrapper.TableWrap;


public class WrapCartridgeModel extends TableWrap {

	public WrapCartridgeModel() {
		super("cartridge_model", "for_send", "id");
	}

	@Override
	public Object getObjectFromResultSet(ResultSet rs) {
		CartridgeModel returnValue=new CartridgeModel();
		try{
			returnValue.setForSend(rs.getInt("for_send"));
			returnValue.setId(rs.getInt("id"));
			returnValue.setIdVendor(rs.getInt("id_vendor"));
			returnValue.setName(rs.getString("name"));
			returnValue.setPrice(rs.getFloat("price"));
		}catch(Exception ex){
			System.err.println("WrapCartridgeModel#getObjectFromResultSet: "+ex.getMessage());
		}
		return returnValue;
	}


}
