package computer_shop.jobber.remote_interface;

import computer_shop.jobber.common_objects.JobberIdentifier;

/** получение уникального идентификатора Jobber по логину и паролю */
public interface LoginValidator {
	/** получить уникальный объект-идентификатор по Jobber
	 * @param login - логин
	 * @param password - пароль
	 * @return объект-идентификатор
	 * */
	public JobberIdentifier checkPassword(String login, String password);
}
