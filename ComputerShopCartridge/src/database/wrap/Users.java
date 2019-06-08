package database.wrap;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="USERS")
public class Users implements Serializable {
	@Transient
	private final static long serialVersionUID=1L;
	
	@Id
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator", sequenceName="GEN_USERS_ID")
	@Column(name="ID")
	private int id;
	@Column(name="USER_LOGIN", length=50)
	private String userLogin;
	@Column(name="USER_PASSWORD",length=50)
	private String userPassword;
	@Column(name="USER_ROLE")
	private int userRole;
	@Column(name="USER_NAME", length=100)
	private String userName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getUserRole() {
		return userRole;
	}
	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
