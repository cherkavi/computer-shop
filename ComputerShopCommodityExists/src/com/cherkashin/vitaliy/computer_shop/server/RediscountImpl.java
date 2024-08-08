package com.cherkashin.vitaliy.computer_shop.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point.IRediscount;
import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point.RediscountElement;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import database.ConnectWrap;
import database.StaticConnector;

public class RediscountImpl extends RemoteServiceServlet implements IRediscount{
	private final static long serialVersionUID=1L;
	
	@Override
	public boolean createRediscount(int pointKod) {
		boolean returnValue=false;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			// ���� ���������� �������� - �������
			if(isRediscountExists(connector, pointKod)){
				PreparedStatement ps=connector.getConnection().prepareStatement("delete from rediscount where rediscount.kod_point=? and rediscount.date_rediscount=?");
				ps.setInt(1, pointKod);
				ps.setDate(2, new java.sql.Date( new Date().getTime()));
				ps.executeUpdate();
				connector.getConnection().commit();
				ps.close();
			}
			// �������� ��� ������ �� ��������� �������� ����� - �������� ��� ������ � ������� REDISCOUNT � ������������� ��������� IS_SOURCE
			StringBuffer query=new StringBuffer();
			query.append("SELECT assortment.kod assortment_kod ,serial.kod serial_kod FROM COMMODITY \n");
			query.append("inner join assortment on assortment.kod=commodity.assortment_kod \n"); 
			query.append("INNER JOIN SERIAL ON SERIAL.KOD=COMMODITY.SERIAL_KOD \n");
			query.append("inner join points on points.kod=commodity.point_kod \n");
			query.append("WHERE COMMODITY.POINT_KOD=? AND COMMODITY.DATE_IN_OUT<=? \n");
			query.append("GROUP BY serial.kod, assortment.kod HAVING SUM(COMMODITY.QUANTITY)>0 \n");
			PreparedStatement psCommodityExists=connector.getConnection().prepareStatement(query.toString());
			psCommodityExists.setInt(1, pointKod);
			psCommodityExists.setDate(2, new java.sql.Date(new Date().getTime()));
			// ������� ������ �� �������� �����
			ResultSet rsCommodityExists=psCommodityExists.executeQuery();
			// ��������������� ������ ��� ������� ������ 
			PreparedStatement insertRediscount=connector.getConnection().prepareStatement("insert into rediscount(is_source, kod_point, date_rediscount, kod_assortment, kod_serial, date_write) values(?,?,?,?,?,?)");
			java.sql.Date dateRediscount=new java.sql.Date(new Date().getTime());
			java.sql.Timestamp dateRediscountWrite=new java.sql.Timestamp(new Date().getTime());
			while(rsCommodityExists.next()){
				System.out.println("Create Rediscount: "+rsCommodityExists.getInt("serial_kod"));
				insertRediscount.clearParameters();
				insertRediscount.setInt(1, 1); // is_source
				insertRediscount.setInt(2, pointKod); // kod_point
				insertRediscount.setDate(3,dateRediscount); // date_rediscount
				insertRediscount.setInt(4, rsCommodityExists.getInt("assortment_kod")); // kod_assortment
				insertRediscount.setInt(5, rsCommodityExists.getInt("serial_kod")); // kod_serial
				insertRediscount.setTimestamp(6, dateRediscountWrite);// date_write
				insertRediscount.executeUpdate();
			}
			System.out.println("commit");
			insertRediscount.getConnection().commit();
			psCommodityExists.close();
			insertRediscount.close();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("RediscountImpl#createRediscount Exception:"+ex.getMessage());
			returnValue=false;
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	private boolean isRediscountExists(ConnectWrap connector, int pointKod) throws SQLException{
		PreparedStatement ps=connector.getConnection().prepareStatement("select count(*) from rediscount where rediscount.kod_point=? and rediscount.date_rediscount=?");
		ps.setInt(1, pointKod);
		ps.setDate(2, new java.sql.Date( new Date().getTime()));
		ResultSet rs=ps.executeQuery();
		rs.next();
		boolean returnValue=rs.getInt(1)>0;
		rs.getStatement().close();
		return returnValue; 
	}
	
	@Override
	public boolean isRediscountExists(int pointKod) {
		boolean returnValue=false;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			returnValue=isRediscountExists(connector, pointKod);
		}catch(Exception ex){
			System.err.println("RediscountImpl#isRediscountExists Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public RediscountElement[] saveBarCode(int kodPoint, String readedCod, int logSize) {
		RediscountElement[] returnValue=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Date tempCurrentDate=new Date();
			java.sql.Date currentDate=new java.sql.Date(tempCurrentDate.getTime());
			java.sql.Timestamp dateWrite=new java.sql.Timestamp(tempCurrentDate.getTime());
			// ����� ����� ���� �������� ����� - ������������
			if(isCommodityIntoRediscount(connector, kodPoint, currentDate, readedCod)==true){
				return null;
			}
			SWriteRecord record=isCommodityOnPoint(connector, kodPoint, currentDate, readedCod);
			if(record!=null){ // ����� ����� ���� �� �����
				// ��������� BarCode � ���� ������ �� ���������
				if(writeRecordIntoRediscount(connector, kodPoint, currentDate, dateWrite, record)){
					returnValue=this.getLastRediscountValue(kodPoint, logSize);
				}else{
					return null;
				}
			}else{
				// ����� ����� ���� �� ������ �����
				// ����� ����� ���� ����������
				// ����� ����� ���� ������
				record=isSerialNumberExists(connector, readedCod);
				if(record!=null){
					// ��������� BarCode � ���� ������ �� ���������
					if(writeRecordIntoRediscount(connector, kodPoint, currentDate, dateWrite, record)==true){
						returnValue=this.getLastRediscountValue(kodPoint, logSize);
					}else{
						// ������ ������ ������ 
						return null;
					}
				}else{
					// ��� �� ������
					returnValue=null;
				}
			}
		}catch(Exception ex){
			System.err.println("RediscountImpl#saveBarCode Exception:"+ex.getMessage());
			returnValue=null;
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** ���������, ���������� �� �������� ��� � ���� ������ � �������� ��� ������������ � ��� �������� - ��� ����������� �� �������� �����  
	 * @param connector
	 * @param serialNumber
	 * @return
	 */
	private SWriteRecord isSerialNumberExists(ConnectWrap connector, String serialNumber) throws SQLException{
		SWriteRecord returnValue=null;
		ResultSet rs=null;
		try{
			PreparedStatement ps=connector.getConnection().prepareStatement("select serial.kod from serial where number like ?");
			ps.setString(1, serialNumber);
			rs=ps.executeQuery();
			if(rs.next()){
				int kodSerial=rs.getInt("KOD");
				rs.getStatement().close();
				rs=connector.getConnection().createStatement().executeQuery("select first 1 skip 0 * from commodity where commodity.serial_kod="+kodSerial+" order by kod desc");
				if(rs.next()){
					returnValue=new SWriteRecord(rs.getInt("ASSORTMENT_KOD"),rs.getInt("SERIAL_KOD"));
				}
			}
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		}
		return returnValue;
	}

	/** ��������� ������� ������ �� �������� ����� 
	 * @param connector
	 * @param kodPoint
	 * @param date
	 * @param searchingKod
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - ����� �� �������� �����</li>
	 * 	<li><b>false</b> - ��� ������ �� �������� ����� </li>
	 * </ul>
	 * @throws SQLException
	 */
	private SWriteRecord isCommodityOnPoint(ConnectWrap connector, int kodPoint, java.sql.Date date, String searchingKod) throws SQLException{
		ResultSet rs=null;
		SWriteRecord returnValue=null;
		try{
			StringBuffer query=new StringBuffer();
			query.append("select rediscount.kod_assortment, rediscount.kod_serial from rediscount \n");
			query.append("inner join serial on serial.kod=rediscount.kod_serial \n");
			query.append("where rediscount.kod_point=? \n");
			query.append("and rediscount.date_rediscount=? \n");
			query.append("and serial.number like ? \n");
			query.append("and rediscount.is_source>0 \n");
			PreparedStatement ps=connector.getConnection().prepareStatement(query.toString());
			ps.setInt(1, kodPoint);
			ps.setDate(2, date);
			ps.setString(3, searchingKod);
			rs=ps.executeQuery();
			rs.next();
			returnValue=new SWriteRecord(rs.getInt("kod_assortment"), rs.getInt("kod_serial"));
		}finally{
			try{
				if(rs!=null)rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** �������� � ������� Rediscount ������������ ����� 
	 * @param connector - ���������� � ����� ������ 
	 * @param kodPoint - ��� �����
	 * @param date - ���� 
	 * @param assortmentKod - �������������� ���
	 * @param serialKod - �������� ��� 
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - ������� �������� </li>
	 * 	<li><b>false</b> - ������ ������ </li>
	 * </
	 * @throws SQLException
	 */
	private boolean writeRecordIntoRediscount(ConnectWrap connector, int kodPoint, java.sql.Date date, java.sql.Timestamp timestamp, SWriteRecord record) throws SQLException{
		boolean returnValue=false;
		PreparedStatement ps=null;
		try{
			ps=connector.getConnection().prepareStatement("insert into rediscount(is_source, kod_point, date_rediscount, kod_assortment, kod_serial, date_write) values(-1,?,?,?,?,?)");
			ps.setInt(1, kodPoint);// kod_point
			ps.setDate(2, date);// date_rediscount
			ps.setInt(3, record.getAssortmentKod());// kod_assortment
			ps.setInt(4, record.getSerialKod());// kod_serial
			ps.setTimestamp(5, timestamp);// date_write
			ps.executeUpdate();
			ps.getConnection().commit();
			returnValue=true;
		}finally{
			if(ps!=null){
				try{
					ps.close();
				}catch(Exception ex){};
			}
		}
		return returnValue;
	}
	
	/** ��������� �� ����� ��� � ������ ��������� - ��� �� ��� ��������� ?*/
	private boolean isCommodityIntoRediscount(ConnectWrap connector, int kodPoint, java.sql.Date date, String searchingKod) throws SQLException{
		ResultSet rs=null;
		boolean returnValue=false;
		try{
			StringBuffer query=new StringBuffer();
			query.append("select rediscount.kod from rediscount \n");
			query.append("inner join serial on serial.kod=rediscount.kod_serial \n");
			query.append("where rediscount.kod_point=? \n");
			query.append("and rediscount.date_rediscount=? \n");
			query.append("and serial.number like ? \n");
			query.append("and rediscount.is_source<0 \n");
			PreparedStatement ps=connector.getConnection().prepareStatement(query.toString());
			ps.setInt(1, kodPoint);
			ps.setDate(2, date);
			ps.setString(3, searchingKod);
			rs=ps.executeQuery();
			returnValue=rs.next();
		}finally{
			try{
				if(rs!=null)rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public RediscountElement[] getLastRediscountValue(int pointKod, int size) {
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Date tempCurrentDate=new Date();
			java.sql.Date currentDate=new java.sql.Date(tempCurrentDate.getTime());
			StringBuffer query=new StringBuffer();
			query.append("select first "+size+" skip 0  assortment.name, serial.number, serial.number_seller, assortment.bar_code, assortment.bar_code_company  from rediscount \n");
			query.append("inner join serial on serial.kod=rediscount.kod_serial \n");
			query.append("inner join assortment on assortment.kod=rediscount.kod_assortment \n");
			query.append("where rediscount.kod_point=? \n");
			query.append("and rediscount.date_rediscount=? \n");
			query.append("and rediscount.is_source<0 \n");
			query.append("order by rediscount.kod desc ");
			PreparedStatement ps=connector.getConnection().prepareStatement(query.toString());
			ps.setInt(1, pointKod);
			ps.setDate(2, currentDate);
			ResultSet rs=ps.executeQuery();
			ArrayList<RediscountElement> returnValue=new ArrayList<RediscountElement>();
			while(rs.next()){
				returnValue.add(new RediscountElement(rs.getString("name"),rs.getString("number"),rs.getString("number_seller"),rs.getString("bar_code"),rs.getString("bar_code_company")));
			}
			return returnValue.toArray(new RediscountElement[]{});
		}catch(Exception ex){
			System.err.println("RediscountImpl#saveBarCode Exception:"+ex.getMessage());
			return null;
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
	}
}

/** ������ ������������ � ��������� ������ ��� ����������  */
class SWriteRecord {
	private int assortmentKod;
	private int serialKod;
	
	/** ������ ������������ � ��������� ������ ��� ����������
	 * @param assortmentKod - ��� ������������
	 * @param serialKod - �������� ���
	 */
	public SWriteRecord(int assortmentKod, int serialKod){
		this.assortmentKod=assortmentKod;
		this.serialKod=serialKod;
	}
	
	/** �������� �������������� ���  */
	public int getAssortmentKod(){
		return this.assortmentKod;
	}
	
	/** �������� �������� ����� */
	public int getSerialKod(){
		return this.serialKod;
	}
}
