package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount;

import java.util.HashMap;
import java.util.List;

import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.find_commodity.TradePoint;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("get_points")
public interface IGetPoints extends RemoteService{
	/** get all trade points list */
	public HashMap<Integer,String> getPoints();
	/** get all trade points as {@link TradePoint}*/
	public List<TradePoint> getTradePoints();

}
