package computer_shop.jobber.common_objects;

/**
	 * ”никальный объект - идентификатор администратора 
	 * @param login
	 * @param password
	 * @param name
	 * @param surname
 */
public class AdminIdentifier {
	private String login;
	private String password;
	private String name;
	private String surname;
	

	public AdminIdentifier(){
	}
	/**
	 * ”никальный объект - идентификатор администратора
	 * @param login
	 * @param password
	 * @param name
	 * @param surname
	 */
	public AdminIdentifier(String login, 
							String password,
							String name,
							String surname){
		this.login=login;
		this.password=password;
		this.name=name;
		this.surname=surname;
	}
	
	@Override
	public String toString(){
		return "Name:"+name+"   Surname:"+surname+"   Login:"+login+"   Password:"+password;
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
