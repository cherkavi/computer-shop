package computer_shop.jobber.view.order.list_view;

import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import computer_shop.jobber.Main;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;
import computer_shop.jobber.view.order.order_list.IAddRowElement;
import computer_shop.jobber.view.order.section_tree.ISectionSelected;

public class ListView extends JPanel implements ISectionSelected{
	private final static long serialVersionUID=1L;
	private RowElementTableModel tableModel;
	private ElementsLoader elementsLoader;
	private JobberIdentifier jobberIdentifier;
	private JTable table;
	private JPopupMenu popupMenu;
	/** */
	private boolean isCurrency;
	
	/** отображение списка товаров по заданной категории 
	 * @param jobberIdentifier - уникальный идентификатор клиента
	 * @param isCurrency - 
	 * <li><b>true </b> - цены в долларах по курсу </li>
	 * <li><b>false </b> - цены в грн </li>
	 */
	public ListView(JobberIdentifier jobberIdentifier,
					boolean isCurrency){
		this.jobberIdentifier=jobberIdentifier;
		this.isCurrency=isCurrency;
		initComponents();
	}
	
	private void initComponents(){
		tableModel=new RowElementTableModel();
		table=new JTable(tableModel);
		elementsLoader=new NetElementsLoader(Main.URL);
		popupMenu=new JPopupMenu();
		JMenuItem menuAdd=new JMenuItem("Добавить");
		popupMenu.add(menuAdd);
		
		table.setDefaultRenderer(Object.class, new RowElementTableCellRender());
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent event) {
				if((event.getClickCount()>1)&&(event.getButton()==MouseEvent.BUTTON1)){
					onTableClickSelection();
				}
				if(event.getButton()==MouseEvent.BUTTON3){
					onTableRightClick(event);
				}
			}
		});
		menuAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onTableClickSelection();
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setColumnNameAndSize(table, 0, "Наименование", 400);
		this.setColumnNameAndSize(table, 1, "Цена", 50);
		this.setColumnNameAndSize(table, 2, "Гарантия", 50);
		this.setColumnNameAndSize(table, 3, "Наличие", 50);
		this.setLayout(new GridLayout(1,1));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder("Ассортимент товара по выделенной категории"));
		this.add(new JScrollPane(table));
	}

	@Override
	public void sectionSelected(String sectionName) {
		try{
			this.tableModel.updateDataIntoModel(this.elementsLoader.getElementsFromSection(sectionName, 
																						   this.jobberIdentifier,
																						   isCurrency)
												);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "Удаленный сервер не ответил","Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void setColumnNameAndSize(JTable table, int columnNumber, String columnTitle, int columnWidth){
		table.getColumn(table.getColumnName(columnNumber)).setPreferredWidth(columnWidth);
		table.getColumn(table.getColumnName(columnNumber)).setHeaderValue(columnTitle);
	}
	
	/** click on table selection */
	private void onTableClickSelection(){
		int selectedPosition=table.convertRowIndexToModel(table.getSelectedRow());
		RowElement element=this.tableModel.getRowElement(selectedPosition);
		JDialog dialog=new JDialog();
		PanelQuantity panelQuantity=new PanelQuantity(element,dialog);
		dialog.getContentPane().setLayout(new GridLayout(1,1));
		dialog.getContentPane().add(panelQuantity);
		dialog.setModal(true);
		dialog.setSize(300,135);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setLocationRelativeTo(this);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setVisible(true);
		if(panelQuantity.isButtonOk()){
			this.notifyRowElementListeners(element, panelQuantity.getQuantity());
		}else{
		}
	}
	
	/** слушатели, которых необходимо оповещать о необходимости добавления новых элементов */
	private ArrayList<IAddRowElement> rowElementList=new ArrayList<IAddRowElement>();

	private void notifyRowElementListeners(RowElement element, int quantity){
		for(IAddRowElement currentElement: rowElementList){
			currentElement.addRowElement(element, quantity);
		}
	}
	
	/** добавить слушателя для оповещения о необходимости добавления данных */
	public void addRowElementListener(IAddRowElement element){
		if(rowElementList.indexOf(element)<0){
			rowElementList.add(element);
		}
	}
	
	/** удалить слушателя для оповещения о необходимости добавления данных */
	public void removeRowElementListener(IAddRowElement element){
		this.rowElementList.remove(element);
	}
	
	private void onTableRightClick(MouseEvent event){
		this.popupMenu.show(this.table, event.getX(), event.getY());
	}
}