package computer_shop.jobber.admin_remote_interface;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;

/** интерфейс, который служит для редактирования Jobber-ов */
public interface IJobberEdit {
	
	/** получить весь список Jobber-ов 
	 * @param adminIdentifier - уникальный идентификатор администратора 
	 * @return список всех Jobber-ов в системе 
	 */
	public JobberElement[] getAllJobbers(AdminIdentifier adminIdentifier) throws Exception;
	
	
	/** запрос на изменение данные по определенному Jobber 
	 * @param adminIdentifier - уникальный идентификатор администратора
	 * @param jobberElement - элемент, который необходимо обновить, ключевое поле - JobberElement.kod
	 * @return
	 * <li> <b>null</b> запись успешно обновлена </li> 
	 * <li> <b>String</b>ошибка обновления записи - описание ошибки</li> 
	 */
	public String update(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;
	
	
	/** запрос на добавление нового элемента  - Jobber-а
	 * @param adminIdentifier - уникальный идентификатор администратора 
	 * @param jobberElement - элемент, который необходимо добавить, поле JobberElement.kod может отсутствовать
	 * @return
	 * <li> <b>null</b> запись успешно добавлена </li> 
	 * <li> <b>String</b>ошибка добавления записи описание ошибки </li> 
	 */
	public String add(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;

	
	/** запрос на удаление элемента 
	 * @param adminIdentifier - идентификатор администратора 
	 * @param jobberElement - элемент, который следуем удалить 
	 * @return 
	 * <li> <b>true</b> запись успешно удалена </li> 
	 * <li> <b>false</b>запись не может быть удалена </li> 
	 */
	public String remove(AdminIdentifier adminIdentifier, JobberElement jobberElement) throws Exception;
}
