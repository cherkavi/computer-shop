package computer_shop.jobber_admin.view.price_loader;

import computer_shop.jobber.common_objects.AdminIdentifier;

/** интерфейс, который:
 * <li> оповещает о проценте загрузки прайс-листа в базу данных </li> 
 * <li>дает команду серверу на загрузку прайс-листа </li>
 * <li>получает строку на основании которой происходит загрузка данных в базу - путь к файлу-прайсу в сети</li>
 * <li>устанавливает строку на основании которой происходит загрузка данных в базу - путь к файлу-прайсу в сети</li>
 * */
public interface PriceLoaderManager {
	/** 
	 * @return процент загрузки прайс-листа в базу данных
	 * <table style="border-style:solid; border-width:1px; border-color:gray">
	 * 	<tr >
	 * 		<th style="border-bottom-style:solid; border-bottom-width:1px; border-bottom-color:black"> Значение процента </th> <th style="border-bottom-style:solid; border-bottom-width:1px; border-bottom-color:black"> Description </th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold"> 0</td> <td> Прайс в состоянии закачки файла Excel </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold"> 1..99</td> <td> Прайс в состоянии загрузки данных в таблицы базы данных </td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td align="center" style="font-weight:bold "> 100</td> <td> Данные полность загружены - конец загрузки </td>
	 * 	</tr>
	 * </table>
	 * @throws Exception  - если произошла ошибка соединения с сервером 
	 * */
	public int getPercentLoad() throws Exception;
	
	/** послать команду серверу на начало операции загрузки данных с удаленного сервера 
	 * @param adminIdentifier - уникальный идентификатор администратора для доступа к ресурсу
	 * @return 
	 * <li> true - процесс загрузки начался </li>
	 * <li> false - процесс загрузки не начат </li>
	 * @throws Exceptin ошибка ответа сервера, или сервер не доступен
	 * */
	public boolean downloadPriceAndWriteIt(AdminIdentifier adminIdentifier) throws Exception;
	
	
	/** получить URL в текстовом виде для загрузки прайс-листа с удаленного сервера
	 * @param adminIdentifier - уникальный идентификатор администратора  
	 * @return URL -место загрузки прайс-листа
	 * @throws Exceptin ошибка ответа сервера, или сервер не доступен
	 * */
	public String getPriceLoadUrl(AdminIdentifier adminIdentifier)throws Exception;

	/** установить URL в текстовом виде для загрузки прайс-листа с удаленного сервера
	 * @param adminIdentifier - уникальный идентификатор администратора  
	 * @param url - строка URL, которая ссылается на необходимый файл 
	 * @return 
	 * <li> <b>null</b> - строка URL успешно установлена </li>
	 * <li> <b>String</b> - текстовое описание <i>ошибки установки</i> </li>  
	 * @throws Exceptin ошибка ответа сервера, или сервер не доступен
	 * */
	public String setPriceLoadUrl(AdminIdentifier adminIdentifier, String url)throws Exception;
	
}
