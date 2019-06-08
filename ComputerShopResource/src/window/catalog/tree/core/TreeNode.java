package window.catalog.tree.core;

import java.util.ArrayList;


import wicket_extension.action.IActionExecutor;


public class TreeNode extends TreeElement{
	private final static long serialVersionUID=1L;
	private ArrayList<TreeElement> list=new ArrayList<TreeElement>();
	private String rootId;
	/** 
	 * @param nodeAware - объект, который генерирует ветки дерева
	 * @param actionExecutor - кому передавать управление
	 * @param id - уникальный номер 
	 * @param idParent - номер родителя
	 * @param caption - заголовок 
	 */
	public TreeNode(NodeAttributesAware nodeAware, IActionExecutor actionExecutor, Integer id, Integer idParent, String caption, int deepIndex){
		super();
		this.setId(id);
		this.setIdParent(idParent);
		this.setCaption(caption);
		this.setActionExecutor(actionExecutor);
		this.setDeepIndex(deepIndex);
		initChilds(nodeAware, id,deepIndex);
	}
	
	private void initChilds(NodeAttributesAware nodeAware, Integer idParent, int deepIndex){
		ArrayList<NodeAttributes> nodes=nodeAware.getNodeAttributes(idParent);
		this.setChildCount(nodes.size());
		for(int counter=0;counter<nodes.size();counter++){
			this.list.add(this.convertToTreeElement(nodeAware, nodes.get(counter),deepIndex));
		}
	}
	
	private TreeElement convertToTreeElement(NodeAttributesAware nodeAware, NodeAttributes element, int deepIndex){
		
		if(element.getChildCount()==0){
			return new TreeLeaf(element.getId(),element.getIdParent(), element.getName(),this.getActionExecutor(),deepIndex+1);
		}else{
			return new TreeNode(nodeAware,this.getActionExecutor(),element.getId(), element.getIdParent(), element.getName(),deepIndex+1);
		}
	}
	
	public int getChildCount(){
		return list.size();
	}
	
	public void addChild(TreeElement element){
		this.list.add(element);
	}
	
	public boolean removeChild(TreeElement element){
		return this.list.remove(element);
	}

	public TreeElement getElement(int index){
		return this.list.get(index);
	}
	
	public ArrayList<TreeElement> getChildren(){
		return this.list;
	}

	/**
	 * @return the rootId
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * @param rootId the rootId to set
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	
}
