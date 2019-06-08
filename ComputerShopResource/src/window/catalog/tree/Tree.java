package window.catalog.tree;

import org.apache.wicket.markup.html.panel.Panel;


import database.ConnectWrap;
import wicket_extension.UserApplication;
import wicket_extension.action.IActionExecutor;
import window.catalog.tree.core.DatabaseNodeAttributesAware;
import window.catalog.tree.core.NodeElement;
import window.catalog.tree.core.TreeNode;

public class Tree extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor actionExecutor;
	private NodeElement rootNode;
	
	public Tree(String id,IActionExecutor actionExecutor){
		super(id);
		this.actionExecutor=actionExecutor;
		initComponents();
	}
	
	private void initComponents(){
		addOrReplaceTree();
	}
	
	/** задать выделенный узел, родительские узлы которого должны быть Expand */
	public void setSelected(Integer selectedId){
		//System.out.println("Tree set selected");
		this.rootNode.setSelected(selectedId,null);
	}
	
	/** добавить или обновить дерево (основная задача - обновление измененного состояния )*/
	public void addOrReplaceTree(){
		if(this.get("tree")!=null){
			this.remove("tree");
		}
		ConnectWrap connector=((UserApplication)this.getApplication()).getConnector();
		
		DatabaseNodeAttributesAware nodeAware=new DatabaseNodeAttributesAware(connector.getConnection(), "KOD", "KOD_PARENT", "NAME", "any_value","j_list_section");
		TreeNode treeNode=new TreeNode(nodeAware,this.actionExecutor,null,null, "root",0);
		this.rootNode=new NodeElement("tree",treeNode,null,this.actionExecutor,false);
		this.add(this.rootNode);
		this.rootNode.setExpandThisNode();
		nodeAware.close();
		connector.close();
	}
}


