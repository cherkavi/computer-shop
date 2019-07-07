package computer_shop.jobber.view.order.order_list;

import java.awt.Cursor;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;

/** �������, ������� �������� ��� ����������� ������ */
public class TableOrder extends JTable implements IAddRowElement{
	private final static long serialVersionUID=1L;
	/** ������ ������ ��� ������� */
	private OrderTableModel model;
	/** ������, ������� ��������� ������ � ������ ����� � ���� ������ ������, ���� �� null, ���� ���������� �� ������� */
	private OrderListSave saver;
	/** �������� �� ���� � ������ � USD (����) ��� �� � ���(false) */
	private boolean isCurrency;
	
	/** �������, ������� �������� ���� ������ ������ 
	 * @param saver - ������, ������� �������� ������� ���������� ������ �� ������� 
	 * @param isCurrency - �������� �� ������ ����������� � ������ (true), ���� �� � ������� (false)
	 * */
	public TableOrder(OrderListSave saver,boolean isCurrency){
		super();
		this.saver=saver;
		this.isCurrency=isCurrency;
		this.initComponents();
	}
	
	private void initComponents(){
		this.model=new OrderTableModel();
		this.setModel(model);
		//this.setDefaultRenderer(Object.class, new RowElementTableCellRender());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.model.setColumnWidthAndName(this);
		
	}

	/** �������� � ������ ������ ��� ���� �������� 
	 * @param element - ������� ������������, ������� ������� �������� � �������  
	 * @param quantity - ���-�� ��������� �� ������� ������������  
	 * */
	public void addRow(RowElement element, int quantity){
		this.model.addRowToFirst(element, quantity);
		this.model.recalculateQuantityAndAmount(this.list);
	}
	
	/** �������� ���-�� ������� � ������� */
	public int getOrderQuantity(){
		return this.model.getOrderQuantity();
	}

	/** �������� ����� ������� � ������� */
	public float getOrderAmount(){
		return this.model.getOrderAmount();
	}

	public OrderTableModelRow getSelectedObject(){
		try{
			return this.getObjectFromModel(this.convertRowIndexToModel(this.getSelectedRow()));
		}catch(Exception ex){
			return null;
		}
		
	}
	
	/** �������� ������ �� ������ ������ � ������ */
	public OrderTableModelRow getObjectFromModel(int rowModelIndex){
		return this.model.getElement(rowModelIndex);
	}
	
	/** ������� ������ �� ������ */
	public void removeElementFromModel(OrderTableModelRow element){
		this.model.removeElement(element, list);
	}
	
	/** �������� �������� �� ������ */
	public void replaceElement(OrderTableModelRow element){
		/*if(this.model.removeElement(element,list)!=null){
			this.model.addRowToFirst(element.getElement(), element.getQuantity());
		}*/
		this.model.refreshData(list);
	}
	
	/** ������� ������ �� ������ */
	public void removeElementFromModel(int rowModelIndex){
		this.model.removeElement(this.getObjectFromModel(rowModelIndex), list);
	}
	
	private ArrayList<IAmountChangeListener> list=new ArrayList<IAmountChangeListener>();

	/** �������� ��������� ��� ���������� �� ���������� � ����� � ���-�� ��������� ������ */
	public void addAmountChangeListener(IAmountChangeListener object) {
		if(this.list.indexOf(object)<0){
			this.list.add(object);
		}
	}
	public void removeAmountChangeListener(IAmountChangeListener object){
		this.list.remove(object);
	}

	@Override
	public void addRowElement(RowElement element, int quantity) {
		this.addRow(element, quantity);
	}

	/** ��������� ������ ������ �� ������� ���������� <br> 
	 * (���������� ������� ��������� ��������)
	 * @param identifier - ���������� ������������� �������� 
	 * @return ��� ������ ���� null - ���� ���-�� ������ ��������
	 * */
	public String saveOrderList(JobberIdentifier identifier){
		String[] kodes=new String[this.model.getRowCount()];
		int[] quantities=new int[this.model.getRowCount()];
		for(int counter=0;counter<this.model.getRowCount();counter++){
			OrderTableModelRow element=this.model.getElement(counter);
			kodes[counter]=element.getElement().getKpiKod();
			quantities[counter]=element.getQuantity();
		}
		String remoteResult=null;
		Cursor currentCursor=this.getCursor();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			remoteResult=this.saver.saveOrderListAndGetNumber(identifier, kodes, quantities,isCurrency);
			this.setCursor(currentCursor);
		}catch(Exception ex){
			this.setCursor(currentCursor);
			JOptionPane.showMessageDialog(this, "������ ������ ���������� �������","������",JOptionPane.ERROR_MESSAGE);
		}
		return remoteResult;
	}

	public void clearAllData() {
		this.model.removeAllElements();
		this.model.refreshData(list);
	}
}
