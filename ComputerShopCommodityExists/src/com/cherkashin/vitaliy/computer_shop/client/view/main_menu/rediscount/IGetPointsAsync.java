package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount;

import java.util.HashMap;
import java.util.List;

import com.cherkashin.vitaliy.computer_shop.client.view.main_menu.find_commodity.TradePoint;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IGetPointsAsync {
	
	/** get all trade points list */
	void getPoints(AsyncCallback<HashMap<Integer,String>> callback);

	void getTradePoints(AsyncCallback<List<TradePoint>> callback);

}
