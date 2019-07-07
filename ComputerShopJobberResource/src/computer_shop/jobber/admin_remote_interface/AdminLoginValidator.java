package computer_shop.jobber.admin_remote_interface;

import computer_shop.jobber.common_objects.AdminIdentifier;

/** интерфейс для получения объекта-валидатора для администратора */
public interface AdminLoginValidator {
	/** получить уникальный объект-идентификатор для администратора
	 * @param login - логин
	 * @param password - пароль
	 * @return объект-идентификатор администратора 
	 * @throws Exception если не удалось получить ответ от удаленного сервера
	 * */
	public AdminIdentifier checkPassword(String login, String password) throws Exception; 
}
