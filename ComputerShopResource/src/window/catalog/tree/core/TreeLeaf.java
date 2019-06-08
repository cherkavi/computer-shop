package window.catalog.tree.core;

import wicket_extension.action.IActionExecutor;


/** листок - элемент, который не имеет потомков*/
public class TreeLeaf extends TreeElement{
	private final static long serialVersionUID=1L;

	public TreeLeaf(int id, int idParent, String caption, IActionExecutor actionExecutor, int deepIndex){
		super();
		this.setId(id);
		this.setIdParent(idParent);
		this.setCaption(caption);
		this.setActionExecutor(actionExecutor);
		this.setChildCount(0);
		this.setDeepIndex(deepIndex);
	}
	
}