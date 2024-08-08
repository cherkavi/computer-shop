package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRediscountAsync {

	/** ��������� ������� ���������� ��������� �� ��������� ����� 
	 * @param pointKod - ��� �������� �����
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - �������� ������ </li>
	 * 	<li> <b>false</b> - ��� ��������� �� �������� ��� </li>
	 * </ul>
	 */
	void isRediscountExists(int pointKod, AsyncCallback<Boolean> callback);

	/** ������� ��������� ����� ��� ��������� ( ��������� ����� ������� ���� ��������� � ������� ) 
	 * @param pointKod - ��� �������� �����
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - �������� ������ </li>
	 * 	<li> <b>false</b> - ������ �������� ��������� </li>
	 * </ul>
	 */
	void createRediscount(int pointKod, AsyncCallback<Boolean> callback);

	/**
	 * ��������� ����������� ��� ( ����������� �������� �����-����� )
	 * @param pointKod - ��� �������� ����� 
	 * @param readedKod - ����������� ��� 
	 * @param size - ������ ������������� �������
	 * @return
	 * <ul>
	 * 	<li> <b>Array of element</b> - ��� ������� � �������� </li>
	 * 	<li> <b>null</b> - ��� �� ������� </li>
	 * </ul>
	 */
	void saveBarCode(int pointKod, String readedCod, int size, AsyncCallback<RediscountElement[]> callback);

	void getLastRediscountValue(int pointKod, int size, AsyncCallback<RediscountElement[]> callback);

}
