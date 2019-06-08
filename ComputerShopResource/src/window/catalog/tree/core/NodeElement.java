package window.catalog.tree.core;

import java.util.ArrayList;


import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;

public class NodeElement extends Panel {
	private final static long serialVersionUID=1L;
	private boolean showRoot=true;
	
	/*public NodeElement(String id, TreeNode treeNode, String nodeRoot, ActionExecutor actionExecutor){
		super(id);
		System.out.println("NodeElement#Constructor: "+treeNode.getId()+" "+treeNode.getCaption());
		initComponents(treeNode, nodeRoot, actionExecutor);
	}*/

	public NodeElement(String id, TreeNode treeNode, String nodeRoot, IActionExecutor actionExecutor, boolean showRoot){
		super(id);
		this.showRoot=showRoot;
		initComponents(treeNode, nodeRoot, actionExecutor,showRoot);
	}
	
	private WebMarkupContainer icon;
	private WebMarkupContainer node;
	
	private void initComponents(TreeNode treeNode, String nodeRoot,final IActionExecutor actionExecutor, final boolean showRoot){
		node=new WebMarkupContainer("node");
		node.setOutputMarkupId(true);
		this.add(node);
		
		WebMarkupContainer nodeHead=new WebMarkupContainer("node_head");
		node.add(nodeHead);
		
		WebMarkupContainer spacer=new WebMarkupContainer("spacer");
		spacer.add(new SimpleAttributeModifier("style","float:left;margin-left:"+treeNode.getDeepIndex()*10+"px"));
		nodeHead.add(spacer);

		icon=new WebMarkupContainer("icon");
		nodeHead.add(icon);

		if(nodeRoot!=null){
			this.setCollapseThisNode();
		}
		
		nodeHead.add(new Label("caption",treeNode.getCaption()));
		
		final String tempNodeRoot;
		// проверить данный элемент на принадлежность к корневому 
		if(nodeRoot==null){
			// данный элемент является корневым
			tempNodeRoot=node.getMarkupId();
			if(this.showRoot==false){
				nodeHead.add(new SimpleAttributeModifier("style", "display:none"));
				nodeHead.add(new SimpleAttributeModifier("onclick", ""));
				nodeHead.add(new SimpleAttributeModifier("class", ""));
			}else{
			}
		}else{
			tempNodeRoot=nodeRoot;
		}
		
		ArrayList<TreeElement> list=treeNode.getChildren();
		childPanels.clear();
		for(int counter=0;counter<list.size();counter++){
			if(list.get(counter).getChildCount()==0){
				childPanels.add(new LeafElement("child",(TreeLeaf)list.get(counter),tempNodeRoot,actionExecutor));
			}else{
				childPanels.add(new NodeElement("child",(TreeNode)list.get(counter), tempNodeRoot, actionExecutor,showRoot));
			}
		}
		node.add(new ListView<Panel>("children",childPanels) {
			private static final long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<Panel> item) {
				item.add(item.getModelObject());
			}
		});
	}

	/** назначить данному Node статус развернутого */
	public void setExpandThisNode(){
		//System.out.println("Expand:"+this.getId());
		node.add(new SimpleAttributeModifier("class","node_expand"));
		icon.add(new SimpleAttributeModifier("class","icon_expand"));
	}
	
	/** назначить данному Node статус свернутого */
	public void setCollapseThisNode(){
		//System.out.println("Collapse:"+this.getId());
		node.add(new SimpleAttributeModifier("class","node_collapse"));
		icon.add(new SimpleAttributeModifier("class","icon_collapse"));
	}
	
	/** снять любое выделение из дочерних элементов */
	public void setUnSelectedLeaf(){
		for(int counter=0;counter<this.childPanels.size();counter++){
			if(this.childPanels.get(counter) instanceof LeafElement){
				((LeafElement)this.childPanels.get(counter)).setUnSelected();
			}else{
				((NodeElement)this.childPanels.get(counter)).setUnSelectedLeaf();
			}
		}
	}
	
	private ArrayList<Panel> childPanels=new ArrayList<Panel>();
	
	public boolean setSelected(Integer selectedId, NodeElement parentNode) {
		boolean returnValue=false;
		// полностью снять выделение со всех элементов 
		this.setUnSelectedLeaf();
		int controlValue=selectedId.intValue();
		// проверка на существующих child - если найден - открыть только этот Node
		for(int counter=0;counter<this.childPanels.size();counter++){
			if(this.childPanels.get(counter) instanceof LeafElement){
				if( ((LeafElement)this.childPanels.get(counter) ).getUniqueId().intValue()==controlValue){
					((LeafElement)this.childPanels.get(counter)).setSelected(); 
					returnValue=true;
					break;
				}
			}else{
				// this is Node, not Leaf
			}
		}
		if(returnValue==false){
			// this node not consists selected Id - check into ChildNode
			for(int counter=0;counter<this.childPanels.size();counter++){
				if(this.childPanels.get(counter) instanceof NodeElement){
					if(((NodeElement)this.childPanels.get(counter)).setSelected(selectedId,this)){
						returnValue=true;
						break;
					}
				}
			}
		}
		if(returnValue==true){
			this.setExpandThisNode();
		}else{
			this.setCollapseThisNode();
		}
		return returnValue;
	}
}
