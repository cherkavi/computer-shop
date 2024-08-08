package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(value="rediscount")
public interface IRediscount extends RemoteService{
	/** ��������� ������� ���������� ��������� �� ��������� ����� 
	 * @param pointKod - ��� �������� �����
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - �������� ������ </li>
	 * 	<li> <b>false</b> - ��� ��������� �� �������� ��� </li>
	 * </ul>
	 */
	public boolean isRediscountExists(int pointKod);
	
	/** ������� ��������� ����� ��� ��������� ( ��������� ����� ������� ���� ��������� � ������� )<br />
	 *  ������� ������ ���������� ����������, ���� ��� ����
	 * @param pointKod - ��� �������� �����
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - �������� ������ </li>
	 * 	<li> <b>false</b> - ������ �������� ��������� </li>
	 * </ul>
	 */
	public boolean createRediscount(int pointKod);
	
	
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
	public RediscountElement[] saveBarCode(int pointKod, String readedCod, int size);
	
	
	/** �������� ��������� n �������� �� ������������ ���������  
	 * @param pointKod - ��� �������� �����
	 * @param size - ���-�� ���������, ������� ���������� �������
	 * @return
	 */
	public RediscountElement[] getLastRediscountValue(int pointKod, int size);
}
