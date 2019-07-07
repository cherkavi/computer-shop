package computer_shop.jobber.view.order.order_list;

import java.awt.Component;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import computer_shop.jobber.common_objects.RowElement;

public class OrderTableModel extends AbstractTableModel{
	private final static long serialVersionUID=1L;
	private ArrayList<OrderTableModelRow> list=new ArrayList<OrderTableModelRow>();
	/** общая сумма заказа */
	private float amount;
	/** обее кол-во единиц в заказе */
	private int quantity;
	
	
	/** 
	 * @return 
	 * <table>
	 * 	<tr>
	 * 		<th> # </th><th>Description</th>
	 * 	</tr>
	 * <tr>
	 * 		<th> 0</th> <th> Кол-во </th>
	 * </tr>
	 * <tr>
	 * 		<th> 1</th> <th> Наименование </th>
	 * </tr>
	 * <tr>
	 * 		<th> 2</th> <th> Цена </th>
	 * </tr>
	 * 
	 * </table>
	 * */
	@Override
	public int getColumnCount() {
		return 3;
	}

	/** установить в указанную таблицу имена колонок и значения размеров ширины для этих колонок */
	public void setColumnWidthAndName(JTable table){
		try{
			table.getColumn(table.getColumnName(0)).setPreferredWidth(30);
			table.getColumn(table.getColumnName(0)).setPreferredWidth(30);
			table.getColumn(table.getColumnName(0)).setHeaderRenderer(this.getHeaderRendered("Кол-во"));
			
			table.getColumn(table.getColumnName(1)).setPreferredWidth(400);
			table.getColumn(table.getColumnName(1)).setPreferredWidth(400);
			table.getColumn(table.getColumnName(1)).setHeaderRenderer(this.getHeaderRendered("Наименование"));
			
			table.getColumn(table.getColumnName(2)).setPreferredWidth(70);
			table.getColumn(table.getColumnName(2)).setPreferredWidth(70);
			table.getColumn(table.getColumnName(2)).setHeaderRenderer(this.getHeaderRendered("Цена"));
		}catch(Exception ex){
			//System.err.println("OrderTableModel#setColumnWidthAndName: "+ex.getMessage());
		}
	}

	
	private TableCellRenderer getHeaderRendered(final String tableCaption){
		return new TableCellRenderer(){
			private JPanel label; 
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if(this.label==null){
					this.label=new JPanel(new GridLayout(1,1));
					this.label.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
					this.label.add(new JLabel(tableCaption,JLabel.CENTER));
				}
				return this.label;
			}
		};
	}
	
	
	@Override
	public int getRowCount() {
		if(list==null){
			return 0;
		}else{
			return list.size();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, 
							 int columnIndex) {
		OrderTableModelRow element=this.list.get(rowIndex);
		switch(columnIndex){
			case 0: return element.getQuantity();
			case 1: return element.getElement().getName();			
			case 2: return element.getElement().getPrice();
		}
		return null;
	}

	
	/** добавить еще один элемент в список 
	 * @param element - элемент, который должен быть добавлен 
	 * @param quantity - кол-во данных элементов в корзине 
	 */
	public void addRowToFirst(RowElement element, int quantity){
		int rowIndex=(-1);
		for(int counter=0;counter<list.size();counter++){
			if(list.get(counter).getElement().equals(element)){
				rowIndex=counter;
				break;
			}
		}
		if(rowIndex>=0){
			// найден элемент в списке - добавить сумму и переместить вверх
			OrderTableModelRow removedElement=this.list.remove(rowIndex);
			removedElement.setQuantity(removedElement.getQuantity()+quantity);
			this.list.add(0,removedElement);
		}else{
			list.add(0,new OrderTableModelRow(element, quantity));
		}
		
		this.fireTableDataChanged();
	}

	
	/** пересчитать сумму заказа и кол-во заказанных элементов 
	 * @param listeners */
	public void recalculateQuantityAndAmount(ArrayList<IAmountChangeListener> listeners){
		this.quantity=0;
		this.amount=0;
		for(int counter=0;counter<this.list.size();counter++){
			OrderTableModelRow currentElement=this.list.get(counter);
			this.quantity+=currentElement.getQuantity();
			this.amount+=currentElement.getElement().getPrice()*currentElement.getQuantity();
		}
		this.notifyAmountChangeListener(listeners);
	}

	/** оповестить всех слушателей об изменениях в данных */
	private void notifyAmountChangeListener(ArrayList<IAmountChangeListener> listeners) {
		for(int counter=0;counter<listeners.size();counter++){
			listeners.get(counter).amountChanged(this.amount, this.quantity);
		}
	}

	/** получить указанный элемент по индексу */
	public OrderTableModelRow getElement(int rowIndex){
		return this.list.get(rowIndex);
	}
	
	/** удалить указанный элемент из модели */
	public OrderTableModelRow removeElement(OrderTableModelRow element,ArrayList<IAmountChangeListener> listeners){
		if(this.list.remove(element)){
			this.fireTableDataChanged();
			this.recalculateQuantityAndAmount(listeners);
			return element;
		}else{
			return null;
		}
	}
	
	/** обновить данные в модели */
	public void refreshData(ArrayList<IAmountChangeListener> listeners){
		this.fireTableDataChanged();
		this.recalculateQuantityAndAmount(listeners);
	}
	
	
	/** получить кол-во элементов в заказе */
	public int getOrderQuantity() {
		return this.quantity;
	}

	/** получить сумму по элементам в заказе */
	public float getOrderAmount() {
		return amount;
	}

	public void removeAllElements() {
		this.list.clear();
	}
}


