package window.catalog.tree.core;

import java.io.Serializable;


import wicket_extension.action.IActionExecutor;

public class TreeElement implements Serializable{
	private final static long serialVersionUID=1L;
	private Integer id;
	private String caption;
	private Integer idParent;
	private IActionExecutor actionExecutor;
	private int childCount;
	private int deepIndex;
	
	public TreeElement(){
	}


	public String getCaption() {
		return caption;
	}


	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the actionExecutor
	 */
	public IActionExecutor getActionExecutor() {
		return actionExecutor;
	}

	/**
	 * @param actionExecutor the actionExecutor to set
	 */
	public void setActionExecutor(IActionExecutor actionExecutor) {
		this.actionExecutor = actionExecutor;
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the idParent
	 */
	public Integer getIdParent() {
		return idParent;
	}


	/**
	 * @param idParent the idParent to set
	 */
	public void setIdParent(Integer idParent) {
		this.idParent = idParent;
	}


	/**
	 * @return the deepIndex
	 */
	public int getDeepIndex() {
		return deepIndex;
	}


	/**
	 * @param deepIndex the deepIndex to set
	 */
	public void setDeepIndex(int deepIndex) {
		this.deepIndex = deepIndex;
	}

}
