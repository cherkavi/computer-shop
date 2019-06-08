package window.commons;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.criterion.Restrictions;

import database.ConnectWrap;
import database.wrap.PointSettings;

/** объект, который представляет заказ в целом */
public class GroupOrder implements Serializable{
	private final static long serialVersionUID=1L;
	private int id;
	/** уникальный код потребителя */
	private int idCustomer;
	private String name;
	private String surname;
	private String description;
	private ArrayList<OrderElement> listOfOrder=new ArrayList<OrderElement>();
	/** настройки торговой точки, по которой осуществляется вывод данных */
	private PointSettings pointSettings;
	
	public PointSettings getPointSettings(){
		return this.pointSettings;
	}
	
	/** получить список всех позиций */
	public ArrayList<OrderElement> getListOfOrder(){
		return this.listOfOrder;
	}
	
	/** объект, который представляет заказ в целом */
	public GroupOrder(){
	}
	
	/** загрузка группы на основании одного из элементов 
	 * @param connection - соединение с базой данных
	 * @param elementKod - элемент, который нужно вывести
	 * @param pointId - номер точки с которой осуществляется заказ
	 */
	public void loadByElementCode(ConnectWrap connector, Integer elementKod, Integer pointId){
		ResultSet rs=null;
		try{
			StringBuffer query=new StringBuffer();
			query.append(" select * from get_cartridge_for_customer where id="+elementKod);
			rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			this.id=rs.getInt("id_order_group");
			this.description=rs.getString("description");
			this.name=rs.getString("customer_name");
			this.surname=rs.getString("customer_surname");
			this.idCustomer=rs.getInt("id_customer");
			
			OrderElement orderElement=new OrderElement();
			orderElement.setId(rs.getInt("id"));
			orderElement.setModel(rs.getString("model_name"));
			orderElement.setPrice(rs.getFloat("price"));
			try{
				orderElement.setTimeCreate(new Date(rs.getTimestamp("time_create").getTime()));
			}catch(Exception ex){};
			try{
				orderElement.setTimeReturnToCustomer(new Date(rs.getTimestamp("time_return_to_customer").getTime()));
			}catch(Exception ex){};
			orderElement.setControlNumber(rs.getString("control_number"));
			orderElement.setUniqueNumber(rs.getString("unique_number"));
			orderElement.setVendor(rs.getString("vendor_name"));
			listOfOrder.add(orderElement);
			
			try{
				org.hibernate.Session session=connector.getSession();
				this.pointSettings=(PointSettings)session.createCriteria(PointSettings.class).add(Restrictions.eq("idPoints", pointId)).uniqueResult();
			}catch(Exception ex){};
		}catch(Exception ex){
			System.err.println("GroupOrder#loadByElementCode Exception: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
	}
	
	
	/** объект, который представляет заказ в целом 
	 * @param connection - соединение с базой данных 
	 * @param groupId - код группы
	 * @param pointId - код точки 
	 * @param isTimeToCustomerNull - нужно ли проверять, чтобы товар не был выдан пользователю
	 */
	public GroupOrder(ConnectWrap connector, int groupId, Integer pointId, boolean isTimeToCustomerNull){
		this.id=groupId;
		loadByCode(connector, this.id, pointId, isTimeToCustomerNull);
	}
	
	/** загрузить данные на основании группового кода 
	 * @param connection - соединение с базой данных 
	 * @param id - уникальный групповой номер, по которому нужно загружать данные
	 * @param pointId - уникальный код торговой точки
	 * @param isTimeToCustomerNull - время выдачи пользователю равно null ? ( нужно ли сделать выборку по записям, которые не были возвращены потребителю )
	 */
	private void loadByCode(ConnectWrap connector,int id, Integer pointId, boolean isTimeToCustomerNull){
		ResultSet rs=null;
		try{
			StringBuffer query=new StringBuffer();
			query.append(" select ");
			query.append(" order_group.id, ");
			query.append(" order_group.description, "); 
			query.append(" customer.id customer_id, ");
			query.append(" customer.surname customer_surname, ");
			query.append(" customer.name customer_name, ");
			query.append(" order_list.unique_number,  ");
			query.append(" order_list.control_number, ");
			query.append(" cartridge_model.name model_name, ");
			query.append(" cartridge_vendor.name vendor_name, ");
			query.append(" order_list.id order_list_id, ");
			query.append(" order_list.time_create, ");
			query.append(" order_list.time_return_to_customer, ");
			query.append(" order_list.unique_number unique_number, ");
			query.append(" order_list.control_number control_number, ");
			query.append(" cartridge_model.price ");
			query.append(" from order_list ");
			query.append(" inner join cartridge_model on cartridge_model.id=order_list.id_model "); 
			query.append(" 	inner join cartridge_vendor on cartridge_vendor.id=cartridge_model.id_vendor ");
			query.append(" inner join order_group on order_list.id_order_group=order_group.id ");
			query.append(" 	inner join customer on customer.id=order_group.id_customer ");
			query.append(" where order_group.id="+id);
			if(isTimeToCustomerNull==true){
				query.append(" and order_list.time_return_to_customer is null ");
			}
			rs=connector.getConnection().createStatement().executeQuery(query.toString());
			rs.next();
			this.id=rs.getInt("id");
			this.description=rs.getString("description");
			this.name=rs.getString("customer_name");
			this.surname=rs.getString("customer_surname");
			this.idCustomer=rs.getInt("customer_id");
			do{
				OrderElement orderElement=new OrderElement();
				orderElement.setId(rs.getInt("order_list_id"));
				orderElement.setModel(rs.getString("model_name"));
				orderElement.setPrice(rs.getFloat("price"));
				try{
					orderElement.setTimeCreate(new Date(rs.getTimestamp("time_create").getTime()));
				}catch(Exception ex){};
				try{
					orderElement.setTimeReturnToCustomer(new Date(rs.getTimestamp("time_return_to_customer").getTime()));
				}catch(Exception ex){};
				orderElement.setControlNumber(rs.getString("control_number"));
				orderElement.setUniqueNumber(rs.getString("unique_number"));
				orderElement.setVendor(rs.getString("vendor_name"));
				listOfOrder.add(orderElement);
			}while(rs.next());
			// загрузка данных по торговой точке
			try{
				this.pointSettings=(PointSettings)connector.getSession().createCriteria(PointSettings.class).add(Restrictions.eq("idPoints", pointId)).uniqueResult();
			}catch(Exception ex){};
		}catch(Exception ex){
			System.err.println("GroupOrder#loadByCode Exception: "+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
