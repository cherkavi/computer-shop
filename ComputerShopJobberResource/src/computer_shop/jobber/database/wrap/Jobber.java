package computer_shop.jobber.database.wrap;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Wrap for JOBBER
 * <li> kod</li>
 * <li> surname</li>
 * <li> name</li>
 * <li> login</li>
 * <li> password</li>
 */
@Entity
@Table(name="j_jobber") 
public class Jobber implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	@Id
	@Column(name="KOD")
	//SequenceGenerator(name="generator",sequenceName="GEN_JOBBER_ID")
	//GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
    	private Integer kod;

	@Column(name="SURNAME")
    	private String surname;

	@Column(name="NAME")
    	private String name;

	@Column(name="LOGIN")
    	private String login;

	@Column(name="JOBBER_PASSWORD")
    	private String password;

	@Column(name="PRICE_NUMBER")
	private Integer priceNumber;
	
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
	public Integer getPriceNumber() {
		return priceNumber;
	}

	/**
	 * @param priceNumber the priceNumber to set
	 */
	public void setPriceNumber(Integer priceNumber) {
		this.priceNumber = priceNumber;
	}
	
}
