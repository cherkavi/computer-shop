package database_reflect.wrapper.wrap;
import java.sql.ResultSet;
import database.wrap.OrderGroup;
import database_reflect.wrapper.TableWrap;


public class WrapOrderGroup extends TableWrap{

	public WrapOrderGroup() {
		super("order_group", "for_send", "id");
	}

	@Override
	public Object getObjectFromResultSet(ResultSet rs) {
		OrderGroup returnValue=new OrderGroup();
		try{
			returnValue.setDescription(rs.getString("description"));
			returnValue.setForSend(rs.getInt("for_send"));
			returnValue.setId(rs.getInt("id"));
			returnValue.setIdCustomer(rs.getInt("id_customer"));
		}catch(Exception ex){
			System.err.println("WrapOrderGroup#getObjectFromResultSet Exception:"+ex.getMessage());
		}
		return returnValue;
	}

}
