package computer_shop.jobber.view.login;

import computer_shop.jobber.common_objects.JobberIdentifier;

public interface LoginValidator {
	/** получить уникальный объект-идентификатор по Jobber
	 * @param login - логин
	 * @param password - пароль
	 * @return объект-идентификатор
	 * @throws Exception 
	 * */
	public JobberIdentifier checkPassword(String login, String password) throws Exception;
}
