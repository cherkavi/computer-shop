package window.catalog.tree.core;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_extension.action.IActionExecutor;

public class LeafElement extends Panel{
	private final static long serialVersionUID=1L;
	private IActionExecutor actionExecutor;
	private Integer uniqueId;
	private WebMarkupContainer leaf;
	
	
	public LeafElement(String id, TreeLeaf treeLeaf, String rootNode, IActionExecutor actionExecutor){
		super(id);
		this.actionExecutor=actionExecutor;
		this.uniqueId=treeLeaf.getId();
		leaf=new WebMarkupContainer("leaf");
		Link<Object> link=new Link<Object>("link_leaf"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				LeafElement.this.actionExecutor.action("TREE", LeafElement.this.uniqueId);
			}
		};
		leaf.add(link);
		
		link.add(new Label("caption",treeLeaf.getCaption()));
		// разблокировать, если нужно выделять лист дерева 
		//leaf.add(new SimpleAttributeModifier("onclick", "setLeafSelected('"+rootNode+"',this)"));

		WebMarkupContainer spacer=new WebMarkupContainer("spacer");
		spacer.add(new SimpleAttributeModifier("style","float:left;margin-left:"+treeLeaf.getDeepIndex()*10+"px"));
		link.add(spacer);
		this.add(leaf);
	}
	
	/** получить уникальный номер/идентификатор данного листа*/
	public Integer getUniqueId(){
		return uniqueId;
	}

	public void setSelected() {
		this.leaf.add(new SimpleAttributeModifier("class", "leaf_selected"));
	}

	public void setUnSelected() {
		this.leaf.add(new SimpleAttributeModifier("class", "leaf"));
	}
}
