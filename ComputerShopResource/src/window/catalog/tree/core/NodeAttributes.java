package window.catalog.tree.core;

import java.io.Serializable;

/** аттрибуты, необходимые для элемента дерева */
public class NodeAttributes implements Serializable{
	private final static long serialVersionUID=1L;
	
	private int id;
	private int idParent;
	private String name;
	private int childCount;
	/**
	 * @param id - идентификатор уникального поля
	 * @param idParent - идентификатор родительского поля
	 * @param name - идентификатор отображаемого имени
	 * @param childCount - идентификатор числа дочерних элементов
	*/
	public NodeAttributes(int id, int idParent, String name, int childCount){
		this.id=id;
		this.idParent=idParent;
		this.name=name;
		this.childCount=childCount;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the idParent
	 */
	public int getIdParent() {
		return idParent;
	}
	/**
	 * @param idParent the idParent to set
	 */
	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}
	/**
	 * @return the childCount
	 */
	public int getChildCount() {
		return childCount;
	}
	/**
	 * @param childCount the childCount to set
	 */
	public void setChildCount(int childCount) {
		this.childCount = childCount;
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
	
	
}
