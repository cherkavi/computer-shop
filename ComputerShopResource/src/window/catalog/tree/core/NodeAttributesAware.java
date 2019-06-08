package window.catalog.tree.core;

import java.io.Serializable;
import java.util.ArrayList;

/** ������, ������� �������� ���������� ��� ���� �����, ������� ������������ ��� �������� ������ */
public abstract class NodeAttributesAware implements Serializable{
	private final static long serialVersionUID=1L;
	
	/** �������� ����������� ������ ���� ���������  */
	public abstract ArrayList<NodeAttributes> getNodeAttributes(Integer id);
	
}
