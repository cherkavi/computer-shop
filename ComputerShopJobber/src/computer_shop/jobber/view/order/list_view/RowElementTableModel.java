package computer_shop.jobber.view.order.list_view;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import computer_shop.jobber.common_objects.RowElement;


public class RowElementTableModel extends DefaultTableModel{
	private final static long serialVersionUID=1L;
	
	private ArrayList<RowElement> elements=new ArrayList<RowElement>();
	
	public RowElementTableModel(){
	}
	
	public void updateDataIntoModel(RowElement[] rowElements){
		elements.clear();
		if(rowElements!=null){
			for(int counter=0;counter<rowElements.length;counter++){
				this.elements.add(rowElements[counter]);
			}
		}
		this.fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(columnIndex==0){
			return "name";
		};
		if(columnIndex==1){
			return "price";
		};
		if(columnIndex==2){
			return "warranty";
		};
		if(columnIndex==3){
			return "quantity";
		}
		return "";
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		/*if(columnIndex==0){
			return String.class;
		};
		if(columnIndex==1){
			return Float.class;
		};
		if(columnIndex==2){
			return Integer.class;
		};
		if(columnIndex==3){
			return Integer.class;
		}*/
		return Object.class;
	}	
	@Override
	public Object getValueAt(int row, int column) {
		switch(column){
		case 0: return elements.get(row).getName();
		case 1: return elements.get(row).getPrice();
		case 2: return elements.get(row).getWarranty();
		case 3: return elements.get(row).getQuantity();
		default: return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	public RowElement getRowElement(int row){
		return this.elements.get(row);
	}
	
	@Override
	public int getRowCount() {
		if(this.elements==null){
			return 0;
		}else{
			return elements.size();
		}
	}
	
	public int getColumnCount(){
		return 4;
	}
}
