package com.cherkashin.vitaliy.computer_shop.client.view.main_menu.rediscount.trade_point;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(value="rediscount")
public interface IRediscount extends RemoteService{
	/** проверить наличие созданного переучета по указанной точке 
	 * @param pointKod - код торговой точки
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - переучет создан </li>
	 * 	<li> <b>false</b> - нет переучета по текущему дню </li>
	 * </ul>
	 */
	public boolean isRediscountExists(int pointKod);
	
	/** создать стартовую точку для переучета ( загрузить товар наличия всех элементов в таблицу )<br />
	 *  удалить данные предыдущих переучетов, если они были
	 * @param pointKod - код торговой точки
	 * @return 
	 * <ul>
	 * 	<li> <b>true</b> - переучет создан </li>
	 * 	<li> <b>false</b> - ошибка создания переучета </li>
	 * </ul>
	 */
	public boolean createRediscount(int pointKod);
	
	
	/**
	 * Сохранить прочитанный код ( сосчитанный сканером штрих-кодов )
	 * @param pointKod - код торговой точки 
	 * @param readedKod - прочитанный код 
	 * @param size - размер возвращаемого массива
	 * @return
	 * <ul>
	 * 	<li> <b>Array of element</b> - код опознан и сохранен </li>
	 * 	<li> <b>null</b> - код не опознан </li>
	 * </ul>
	 */
	public RediscountElement[] saveBarCode(int pointKod, String readedCod, int size);
	
	
	/** получить последние n значений по проведенному переучету  
	 * @param pointKod - код торговой точки
	 * @param size - кол-во элементов, которые необходимо вернуть
	 * @return
	 */
	public RediscountElement[] getLastRediscountValue(int pointKod, int size);
}
