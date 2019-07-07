package computer_shop.jobber.view.order.section_tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class SectionNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private String name;
	private SectionLoader sectionLoader;
	private boolean childrenWasFilled=false;
	
	public SectionNode(String name, SectionLoader loader){
		super(name);
		this.name=name;
		this.sectionLoader=loader;
	}
	
	@Override
	public boolean isLeaf() {
		try{
			return !this.sectionLoader.elementHasChild(this.name);
		}catch(Exception ex){
			//System.err.println("Remote server is not response");
			return true;
		}
		
	}

	@Override
	public int getChildCount(){
		if(this.childrenWasFilled==false){
			this.childrenWasFilled=true; // обязательно, для избежания ухода в бесконечность стека
			String[] elements=this.sectionLoader.getSubElements(this.name);
			for(int counter=0;counter<elements.length;counter++){
				this.add(new SectionNode(elements[counter],this.sectionLoader));
			}
		}
		return super.getChildCount();
	}
}
