package database.wrap;

import javax.persistence.*;


@Entity
@Table(name="USERS_ROLE")
public class UsersRole {
	@Id
	@GeneratedValue(generator="generator", strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator", sequenceName="GEN_USERS_ROLE_ID")
	@Column(name="ID")
	private int id;
	@Column(name="NAME", length=50)
	private String name ;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
