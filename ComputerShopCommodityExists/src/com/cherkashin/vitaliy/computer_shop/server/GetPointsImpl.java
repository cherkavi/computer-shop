package com.cherkashin.vitaliy.computer_shop.server;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.find_commodity.TradePoint;
import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.IGetPoints;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import database.ConnectWrap;
import database.StaticConnector;

/** get all existings Points */
public class GetPointsImpl extends RemoteServiceServlet implements IGetPoints{
	private final static long serialVersionUID=1L;
	
	@Override
	public HashMap<Integer,String> getPoints() {
		HashMap<Integer, String> returnValue=new HashMap<Integer, String>();
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from points where kod>0 and kod<1000 and name is not null and trim(name)<>'' order by kod");
			while(rs.next()){
				returnValue.put(rs.getInt("KOD"), rs.getString("NAME"));
				// System.out.println("KOD:"+rs.getInt("KOD")+"    "+rs.getString("NAME"));
			}
		}catch(Exception ex){
			System.err.println("PasswordManager#checkPassword Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public List<TradePoint> getTradePoints() {
		List<TradePoint> returnValue=new ArrayList<TradePoint>();
		returnValue.add(new TradePoint(0,""));
		returnValue.add(new TradePoint(1,"point #1"));
		returnValue.add(new TradePoint(2,"point #2"));
		returnValue.add(new TradePoint(3,"point #3"));
		/*
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from points where kod>0 and kod<1000 and name is not null and trim(name)<>'' order by kod");
			while(rs.next()){
				returnValue.add(new TradePoint(rs.getInt("KOD"), rs.getString("NAME")));
				// System.out.println("KOD:"+rs.getInt("KOD")+"    "+rs.getString("NAME"));
			}
		}catch(Exception ex){
			System.err.println("PasswordManager#checkPassword Exception:"+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		*/
		return returnValue;
	}
	
}
