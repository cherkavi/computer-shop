package database.wrap;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="question") 
public class Question {
	@Id
	@Column(name="ID")
	@GeneratedValue
    	private Integer id;

	@Column(name="TEL",length=35)
    	private String tel;

	@Column(name="EMAIL",length=35)
    	private String email;

	@Column(name="MESSAGE_TEXT",length=1024)
    	private String message_text;

	@Column(name="STATE")
    	private Integer state;

	@Column(name="NAME")
	private String name;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String messageText) {
		message_text = messageText;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
