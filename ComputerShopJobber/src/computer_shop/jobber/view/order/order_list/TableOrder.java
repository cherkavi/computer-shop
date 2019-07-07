package computer_shop.jobber.view.order.order_list;

import java.awt.Cursor;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;

/** таблица, которая содержит все добавленные заказы */
public class TableOrder extends JTable implements IAddRowElement{
	private final static long serialVersionUID=1L;
	/** модель данных для таблицы */
	private OrderTableModel model;
	/** объект, который сохраняет данные и выдает ответ в виде строки текста, либо же null, если сохранение не удалось */
	private OrderListSave saver;
	/** являются ли цены в заказе в USD (екгу) или же в Грн(false) */
	private boolean isCurrency;
	
	/** Таблица, которая содержит весь список заказа 
	 * @param saver - объект, который содержит функции сохранения данных на сервере 
	 * @param isCurrency - является ли заказы выполненном в валюте (true), либо же в гривнах (false)
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

	/** добавить в модель данные еще одно значение 
	 * @param element - элемент ассортимента, который следует добавить в корзину  
	 * @param quantity - кол-во элементов по данному ассортименту  
	 * */
	public void addRow(RowElement element, int quantity){
		this.model.addRowToFirst(element, quantity);
		this.model.recalculateQuantityAndAmount(this.list);
	}
	
	/** получить кол-во товаров в корзине */
	public int getOrderQuantity(){
		return this.model.getOrderQuantity();
	}

	/** получить сумму товаров в корзине */
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
	
	/** получить объект по номеру строки в модели */
	public OrderTableModelRow getObjectFromModel(int rowModelIndex){
		return this.model.getElement(rowModelIndex);
	}
	
	/** удалить объект из модели */
	public void removeElementFromModel(OrderTableModelRow element){
		this.model.removeElement(element, list);
	}
	
	/** обновить элементы из списка */
	public void replaceElement(OrderTableModelRow element){
		/*if(this.model.removeElement(element,list)!=null){
			this.model.addRowToFirst(element.getElement(), element.getQuantity());
		}*/
		this.model.refreshData(list);
	}
	
	/** удалить объект из модели */
	public void removeElementFromModel(int rowModelIndex){
		this.model.removeElement(this.getObjectFromModel(rowModelIndex), list);
	}
	
	private ArrayList<IAmountChangeListener> list=new ArrayList<IAmountChangeListener>();

	/** добавить слушателя для оповещения об изменениях в сумме и кол-ве элементов заказа */
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

	/** сохранить список заказа по данному поставщику <br> 
	 * (необходимо ставить временную задержку)
	 * @param identifier - уникальный идентификатор оптовика 
	 * @return код заказа либо null - если что-то прошло неудачно
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
			JOptionPane.showMessageDialog(this, "Ошибка ответа удаленного сервера","Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		return remoteResult;
	}

	public void clearAllData() {
		this.model.removeAllElements();
		this.model.refreshData(list);
	}
}
