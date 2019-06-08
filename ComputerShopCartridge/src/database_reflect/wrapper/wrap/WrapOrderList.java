package database_reflect.wrapper.wrap;

import java.sql.ResultSet;
import java.util.Date;
import database.wrap.OrderList;
import database_reflect.wrapper.TableWrap;


public class WrapOrderList extends TableWrap{

	public WrapOrderList() {
		super("order_list", "for_send", "id");
	}

	@Override
	public Object getObjectFromResultSet(ResultSet rs) {
		OrderList returnValue=new OrderList();
		try{
			returnValue.setId(rs.getInt("id"));
			returnValue.setIdModel(rs.getInt("id_model"));
			if(rs.getTimestamp("time_create")!=null){
				returnValue.setTimeCreate(new Date(rs.getTimestamp("time_create").getTime()));
			}
			if(rs.getTimestamp("time_get_to_process")!=null){
				returnValue.setTimeGetToProcess(new Date(rs.getTimestamp("time_get_to_process").getTime()));
			}
			if(rs.getTimestamp("time_return_from_process")!=null){
				returnValue.setTimeReturnFromProcess(new Date(rs.getTimestamp("time_return_from_process").getTime()));
			}
			if(rs.getTimestamp("time_return_to_customer")!=null){
				returnValue.setTimeReturnToCustomer(new Date(rs.getTimestamp("time_return_to_customer").getTime()));
			}
			returnValue.setUniqueNumber(rs.getInt("unique_number"));
			returnValue.setControlNumber(rs.getString("control_number"));
			returnValue.setIdOrderGroup(rs.getInt("id_order_group"));
			returnValue.setForSend(rs.getInt("for_send"));
		}catch(Exception ex){
			System.err.println("WrapOrderList#getObjectFromResultSet Exception: "+ex.getMessage());
		}
		return returnValue;
	}

}
