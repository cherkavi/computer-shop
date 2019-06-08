package window.catalog.tree.core;

import java.io.Serializable;
import java.util.ArrayList;

/** объект, который содержит информацию обо всех нодах, которые используются для создания дерева */
public abstract class NodeAttributesAware implements Serializable{
	private final static long serialVersionUID=1L;
	
	/** получить необходимый список всех элементов  */
	public abstract ArrayList<NodeAttributes> getNodeAttributes(Integer id);
	
}
