package computer_shop.jobber.view.order.section_tree;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import computer_shop.jobber.common_objects.StringArrayElement;

/** ������ �� ������ � ��������� */
public class SectionTree extends JPanel implements TreeExpansionListener, TreeSelectionListener{
	private final static long serialVersionUID=1L;
	private JTree tree=null;
	private ArrayList<ISectionSelected> sectionSelectedListener=new ArrayList<ISectionSelected>();
	private DefaultMutableTreeNode rootElement=null;
	
	/** ������ �� ������ � ���������*/
	public SectionTree(SectionLoader sectionLoader){
		// ��������� ��������� ��� ������ ������ ��������, ���� ���������
		Cursor currentCursor=this.getCursor();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			this.rootElement=getTreeNodeFromStringArrayElement(sectionLoader.getTreeRootNode());
			tree=new JTree(this.rootElement);
			//this.fillAllTreeBenchFromLoader(this.rootNode, sectionLoader);
			this.setCursor(currentCursor);
		}catch(Exception ex){
			this.setCursor(currentCursor);
			JOptionPane.showMessageDialog(this, "��������� ������ �� �������","������",JOptionPane.ERROR_MESSAGE);
		}
		// ������� ���������� �������� � ����������� �� ��������� ���������� ��������
		initComponents();
	}

	
	private DefaultMutableTreeNode getTreeNodeFromStringArrayElement(StringArrayElement element){
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(element.getName());
		if((element.getElements()!=null)&&(element.getElements().length>0)){
			for(int counter=0;counter<element.getElements().length;counter++){
				node.add(getTreeNodeFromStringArrayElement(element.getElements()[counter]));
			}
		}
		return node;
	}
	
	/* �������� � ��������� �������� ��� ����� �� ��������� ���������� 
	 * @param rootNode - �������� ������� ������, � �������� ����� �������� ��� �����
	 * @param loader - ��������� ���������
	 *
	private void fillAllTreeBenchFromLoader(DefaultMutableTreeNode rootNode, SectionLoader loader){
		String[] rootElements=loader.getSubRootElements();
		for(int counter=0;counter<rootElements.length;counter++){
			rootNode.add(getAllBenchFromSubRootElement(rootElements[counter],loader));
		}
	}*/
	
	/**  
	 * �� ����� ���-��������� �������� �������� ��������� ��� ����� � ������ ���-��� ��������
	 *
	private DefaultMutableTreeNode getAllBenchFromSubRootElement(String string,
																 SectionLoader loader) {
		DefaultMutableTreeNode returnValue=new DefaultMutableTreeNode(string);
		String[] elements=loader.getSubElements(string);
		if(elements!=null){
			// ������ ������� �������� �������� �������� - ������ ��� ��������
			for(int counter=0;counter<elements.length;counter++){
				returnValue.add(getAllBenchFromSubRootElement(elements[counter],loader));
			}
		}else{
			// isLeaf - ������ ������� �������� ������
		}
		return returnValue;
	}*/

	/** ������������� �������� */
	private void initComponents(){
		//tree.setRootVisible(false);
		tree.addTreeExpansionListener(this);
		tree.addTreeSelectionListener(this);
		/*String[] elements=null;
		Cursor currentCursor=this.getCursor();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			elements=this.sectionLoader.getSubRootElements();
			this.setCursor(currentCursor);
		}catch(Exception ex){
			this.setCursor(currentCursor);
			elements=new String[]{};
			JOptionPane.showConfirmDialog(this, "��������� ������ �� ��������", "������", JOptionPane.ERROR_MESSAGE);
		}
		this.addElementsToNode(this.rootNode, elements,this.sectionLoader);
		*/
		//tree.setRootVisible(false);
		this.setLayout(new GridLayout(1,1));
		this.add(new JScrollPane(tree));
		//tree.expandPath(new TreePath(new DefaultMutableTreeNode("root")));
	}

/*	private void addElementsToNode(DefaultMutableTreeNode node, String[] elements,SectionLoader loader){
		for(int counter=0;counter<elements.length;counter++){
			node.add(new SectionNode(elements[counter],loader));
		}
	}
*/	
	
	/** �������� ���������, ������� �� ���������� �� ��������� Leaf */
	public void addSectionSelectedListener(ISectionSelected sectionSelected){
		if(this.sectionSelectedListener.indexOf(sectionSelected)<0){
			this.sectionSelectedListener.add(sectionSelected);
		}
		//this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void removeSectionSelectedListener(ISectionSelected sectionSelected){
		this.sectionSelectedListener.remove(sectionSelected);
	}
	
	private void notifyAllSectionSelected(String sectionName){
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		for(ISectionSelected currentValue : this.sectionSelectedListener){
			currentValue.sectionSelected(sectionName);
		}
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		/*System.out.println("treeCollapsed");
		System.out.println("Source:"+event.getSource());
		System.out.println("Path:"+event.getPath().getPath());
		*/
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		/*System.out.println("treeExpanded");
		System.out.println("Source:"+event.getSource());
		System.out.println("Path:"+event.getPath().getPath());
		*/
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		notifyAllSectionSelected(event.getPath().getLastPathComponent().toString());
	}

}
