package window.main_menu.create_order.panel_order;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.ConnectWrap;

/** ������, ������� �������� ����������� �������� ������ */
public class OrderPanelElement implements Serializable {
	private final static long serialVersionUID=1L;
	
	private String vendor;
	private String model;
	private Integer idModel;
	
	/** ������, ������� �������� ����������� �������� ������ */
	public OrderPanelElement(Connection connection){
		// ���������� ������� ����������� Vendor
		this.vendor=this.getRandomVendor(connection);
		// ���������� ������ ���������� ������ �� Vendor
		this.setRandomModel(connection, this.vendor);
	}

	/** �������� ��������� (������ �� ������) Vendor*/
	private String getRandomVendor(Connection connection){
		String returnValue=null;
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select * from cartridge_vendor");
			rs.next();
			returnValue=rs.getString("NAME");
		}catch(Exception ex){
			System.err.println("OrderPanelElement#getRandomVendor Message:"+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** ������, ������� �������� ����������� �������� ������ */
	public OrderPanelElement(Connection connection, String vendor){
		this.vendor=vendor;
		// ���������� ������ ���������� ������ �� Vendor
		this.setRandomModel(connection, vendor);
	}

	/** �������� ���������(������ �� ������) ������ �� ������������� */
	private void setRandomModel(Connection connection, String vendor){
		this.model=null;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			ps=connection.prepareStatement("select cartridge_model.id, cartridge_model.name from cartridge_model inner join cartridge_vendor on cartridge_model.id_vendor=cartridge_vendor.id and cartridge_vendor.name=?");
			ps.setString(1, vendor);
			rs=ps.executeQuery();
			rs.next();
			this.idModel=rs.getInt("ID");
			this.model=rs.getString("NAME");
		}catch(Exception ex){
			System.err.println("OrderPanelElement#getRandomModel Message: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				ps.close();
			}catch(Exception ex){};
		}
	}
	
	/** ������, ������� �������� ����������� �������� ������ */
	public OrderPanelElement(String vendor,String model){
		this.vendor=vendor;
		this.model=model;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model, ConnectWrap connector) {
		this.model = model;
		if((this.vendor!=null)&&(this.model!=null)){
			// ������� ���������� ��� � ��� ������ ������ 
			try{
				Connection connection=connector.getConnection();
				String query=" select cartridge_model.id from cartridge_model "+
							 " inner join cartridge_vendor on cartridge_vendor.id=cartridge_model.id_vendor and cartridge_vendor.name=? "+
							 " where cartridge_model.name=? ";
				PreparedStatement ps=connection.prepareStatement(query);
				ps.setString(1, this.vendor);
				ps.setString(2, this.model);
				ResultSet rs=ps.executeQuery();
				if(rs.next()){
					this.idModel=rs.getInt("ID");
				}
				ps.close();
			}catch(Exception ex){
				System.err.println("OrderPanelElement#setModel Exception: "+ex.getMessage());
			}
		}
	}

	public Integer getIdModel(){
		return this.idModel;
	}
}
