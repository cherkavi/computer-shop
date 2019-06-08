package wicket_extension;

import java.io.Serializable;

/** уникальный код пользователя для идентификации его в HTTP сессии */
public class User implements Serializable{
	private final static long serialVersionUID=1L;
	/** код в базе данных */
	private Integer kod;
	/** код роли данного пользователя из таблицы USERS_ROLE */
	private Integer kodRole;
	/** имя пользователя  */
	private String userName;
	/** класс, который точно идентифицирует данного пользователя в HTTP сессии*/
	public User(){
	}
	
	/** уникальный код пользователя для идентификации его в HTTP сессии 
	 * 
	 * */
	/**
	 * @param kod
	 * @param userName
	 */
	public User(Integer kod, String userName){
		this.kod=kod;
		this.userName=userName;
	}

	/** уникальный код пользователя для идентификации его в HTTP сессии */
	public User(Integer kod, String userName, Integer role){
		this.kod=kod;
		this.kodRole=role;
		this.userName=userName;
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
	
	/** является ли данный пользователь суперпользователем, которому доступны все режимы  */
	public boolean isRoot(){
		if(this.kod==0){
			return true;
		}else{
			return false;
		}
	}

	/** получить уникальный код роли данного пользователя  (USERS.USER_ROLE -> USERS_ROLE.KOD) */
	public Integer getRole(){
		return this.kodRole;
	}

	/** получить имя пользователя  */
	public String getName() {
		return this.userName;
	}
}
