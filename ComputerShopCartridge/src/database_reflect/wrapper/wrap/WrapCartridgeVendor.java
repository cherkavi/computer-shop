package database_reflect.wrapper.wrap;
import java.sql.ResultSet;
import database.wrap.CartridgeVendor;
import database_reflect.wrapper.TableWrap;


public class WrapCartridgeVendor extends TableWrap{

	public WrapCartridgeVendor() {
		super("cartridge_vendor", "for_send", "id");
	}

	@Override
	public Object getObjectFromResultSet(ResultSet rs) {
		CartridgeVendor returnValue=new CartridgeVendor();
		try{
			returnValue.setForSend(rs.getInt("for_send"));
			returnValue.setId(rs.getInt("id"));
			returnValue.setName(rs.getString("name"));
		}catch(Exception ex){
			System.err.println("WrapCartridgeVendor#getObjectFromResultSet: "+ex.getMessage());
		}
		return returnValue;
	}

}
