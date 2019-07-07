package computer_shop.jobber.view;

import java.awt.Cursor;

import javax.swing.JDesktopPane;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

import computer_shop.jobber.Main;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.view.common.AbstractIntrnalFrame;
import computer_shop.jobber.view.common.IParent;
import computer_shop.jobber.view.order.list_view.ListView;
import computer_shop.jobber.view.order.order_list.OrderList;
import computer_shop.jobber.view.order.section_tree.NetSectionTree;
import computer_shop.jobber.view.order.section_tree.SectionTree;

public class JobberOrder extends AbstractIntrnalFrame{
	private final static long serialVersionUID=1L;
	/** дерево разделов/подразделов */
	private SectionTree sectionTree;
	/** список товаров по выделенному листу */
	private ListView listView;
	/** уникальный идентификатор клиента */
	private JobberIdentifier jobberIdentifier;
	/** список заказанных товаров */
	private OrderList orderList;
	
	public JobberOrder(JobberIdentifier jobberIdentifier,
					   String title,
					   IParent parent, 
					   JDesktopPane desktop,
					   boolean isCurrency) {
		super(title, true, true, true, parent, desktop, 750, 500);
		this.jobberIdentifier=jobberIdentifier;
		initComponents(isCurrency);
		this.getDesktopPane().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void initComponents(boolean isCurrency){
		// create element's
		this.sectionTree=new SectionTree(new NetSectionTree(Main.URL));
		this.listView=new ListView(this.jobberIdentifier,isCurrency);
		this.orderList=new OrderList(this.jobberIdentifier,isCurrency);
		// add listener's
		this.sectionTree.addSectionSelectedListener(this.listView);
		this.listView.addRowElementListener(this.orderList);
		// placing component's
		JSplitPane splitList=new JSplitPane();
		splitList.setLeftComponent(this.listView);
		splitList.setRightComponent(this.orderList);
		splitList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitList.setResizeWeight(0.5);
		
		JSplitPane splitMain=new JSplitPane();
		splitMain.setLeftComponent(this.sectionTree);
		splitMain.setRightComponent(splitList);
		splitMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitMain.setResizeWeight(0.25);
		this.getContentPane().add(splitMain);
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

}
