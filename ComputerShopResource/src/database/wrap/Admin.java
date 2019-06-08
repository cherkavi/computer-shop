package database.wrap;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Таблица-обертка для ADMIN_TABLE
 * <table border="1">
 * 	<tr>
 * 		<th> Database </th> <th> POJO </th>
 * 	</tr>
 * 	<tr>
 * 		<td> KOD </td> <td> kod</td>
 * 	</tr>
 * 	<tr>
 * 		<td> SURNAME </td> <td> surname</td>
 * 	</tr>
 * 	<tr>
 * 		<td> NAME </td> <td> name</td>
 * 	</tr>
 * 	<tr>
 * 		<td> LOGIN </td> <td> login</td>
 * 	</tr>
 * 	<tr>
 * 		<td> ADMIN_PASSWORD </td> <td> password</td>
 * 	</tr>
 * </table>
 */
@Entity
@Table(name="j_admin_table") 
public class Admin implements Serializable{
	@Transient
	private final static long serialVersionUID=1L;
	@Id
	@Column(name="KOD")
	//SequenceGenerator(name="generator",sequenceName="GEN_ADMIN_ID")
	//GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@GeneratedValue
    	private Integer kod;

	@Column(name="SURNAME")
    	private String surname;

	@Column(name="NAME")
    	private String name;

	@Column(name="LOGIN")
    	private String login;

	@Column(name="ADMIN_PASSWORD")
    	private String password;

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

	
	
}
