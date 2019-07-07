package computer_shop.jobber.view.history;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import computer_shop.jobber.common_objects.HistoryRow;

public class ModelHistory extends AbstractTableModel{
	private final static long serialVersionUID=1L;
	private ArrayList<HistoryRow> list=new ArrayList<HistoryRow>();

	/** обновить модель на основании данных */
	public void updateModel(HistoryRow[] elements){
		list.clear();
		if(elements!=null){
			for(int counter=0;counter<elements.length;counter++){
				list.add(elements[counter]);
			}
		}
		this.fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return 5;
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		HistoryRow element=this.list.get(rowIndex);
		switch(columnIndex){
		case 0:return element.getNumber();
		case 1:return element.getDate();
		case 2:return element.getCurrency();
		case 3:return element.getQuantity();
		case 4:return element.getAmount();
		}
		return null;
	}

	public HistoryRow getElementByIndex(int index){
		try{
			return this.list.get(index);
		}catch(Exception ex){
			return null;
		}
	}
}
