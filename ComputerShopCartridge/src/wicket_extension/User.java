package wicket_extension;

import java.io.Serializable;

/** ���������� ��� ������������ ��� ������������� ��� � HTTP ������ */
public class User implements Serializable{
	private final static long serialVersionUID=1L;
	/** ��� � ���� ������ */
	private Integer kod;
	/** ��� ���� ������� ������������ �� ������� USERS_ROLE */
	private Integer kodRole;
	/** ��� ������������  */
	private String userName;
	/** �����, ������� ����� �������������� ������� ������������ � HTTP ������*/
	public User(){
	}
	
	/** ���������� ��� ������������ ��� ������������� ��� � HTTP ������ 
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

	/** ���������� ��� ������������ ��� ������������� ��� � HTTP ������ */
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
	
	/** �������� �� ������ ������������ ������������������, �������� �������� ��� ������  */
	public boolean isRoot(){
		if(this.kod==0){
			return true;
		}else{
			return false;
		}
	}

	/** �������� ���������� ��� ���� ������� ������������  (USERS.USER_ROLE -> USERS_ROLE.KOD) */
	public Integer getRole(){
		return this.kodRole;
	}

	/** �������� ��� ������������  */
	public String getName() {
		return this.userName;
	}
}
