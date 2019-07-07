package computer_shop.jobber.common_objects;

/** POJO element for Jobber add/edit */
public class JobberElement {
	private final static long serialVersionUID=1L;
	private Integer kod;
	private String surname;
	private String name;
	private String login;
	private String password;
	private int priceNumber;

	/** POJO element for Jobber add/edit */
	public JobberElement(){
	}
	
	/**
	 * @return the kod
	 */
	public Integer getKod() {
		return kod;
	}

	/**
	 * @param kod the kod to set
	 */
	public void setKod(Integer kod) {
		this.kod = kod;
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
	 * @return the priceNumber
	 */
	public int getPriceNumber() {
		return priceNumber;
	}

	/**
	 * @param priceNumber the priceNumber to set
	 */
	public void setPriceNumber(int priceNumber) {
		this.priceNumber = priceNumber;
	}

	
}
