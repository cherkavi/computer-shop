package computer_shop.jobber.common_objects;

/**
	 * ”никальный объект - идентификатор клиента 
	 * @param login
	 * @param password
	 * @param name
	 * @param surname
 */
public class JobberIdentifier {
	private String login;
	private String password;
	private String name;
	private String surname;
	
	
	public JobberIdentifier(){
	}
	
	/**
	 * ”никальный объект - идентификатор клиента 
	 * @param login
	 * @param password
	 * @param name
	 * @param surname
	 */
	public JobberIdentifier(String login, 
							String password,
							String name,
							String surname){
		this.login=login;
		this.password=password;
		this.name=name;
		this.surname=surname;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	
}
