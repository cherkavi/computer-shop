package database.wrap;
import java.io.Serializable;

public abstract class Base implements Serializable{
	private final static long serialVersionUID=1L;
	
	public abstract int getId();
	public abstract void setId(int id);
	public abstract String getName();
	public abstract void setName(String name);
}
