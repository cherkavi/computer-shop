package computer_shop.jobber_admin.view.orders_view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import computer_shop.jobber.common_objects.AdminTableOrdersRow;

public class TableOrdersModel extends AbstractTableModel{
	private final static long serialVersionUID=1L;
	private ArrayList<AdminTableOrdersRow> list=new ArrayList<AdminTableOrdersRow>();
	
	/** получить элемент модели на основании номера строки 
	 * @param rowIndex - номер строки 
	 * */
	public AdminTableOrdersRow getElementByRow(int rowIndex){
		return list.get(rowIndex);
	}
	
	public void updateModelData(AdminTableOrdersRow[] elements){
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
		return 8;
	}

	@Override
	public int getRowCount() {
		if(list==null){
			return 0;
		}else{
			return list.size();
		}
	}

	private final SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH.mm.sss");
	private final DecimalFormat amountFormat=new DecimalFormat("#.00");
	
	/** получить наименование валюты,на основании Валюта/Местные деньги */
	private String getCurrency(boolean value){
		if(value){
			return "$";
		}else{
			return "грн.";
		}
	}

	/** получить статус данного заказа, на основании кода из базы данных */
	private String getStatus(int status){
		switch(status){
			case 0: return "Новый"; 
			case 1: return "Отправленный поставщику"; 
			case 2: return "Полученный от поставщика"; 
			case 3: return "Переданный оптовику"; 
			default: return "";
		}
	}
	
	/** заменяет необходимость в TableRender - сразу же выдает готовые строки для таблицы */
	private String getStringFromElement(AdminTableOrdersRow element, int columnIndex){
		try{
			switch(columnIndex){
				case 0:return Integer.toString(element.getKodOrder());
				case 1:return element.getJobberSurname();
				case 2:return element.getJobberName();
				case 3:return sdf.format(element.getTimeWrite());
				case 4:return this.getCurrency(element.isCurrency());
				case 5:return Integer.toString(element.getQuantity());
				case 6:return this.amountFormat.format(element.getAmount());
				case 7:return this.getStatus(element.getStatus());
				default: return null;
			}
		}catch(Exception ex){
			return null;
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(list!=null){
			AdminTableOrdersRow element=list.get(rowIndex);
			return this.getStringFromElement(element, columnIndex);
		}else{
			return null;
		}
	}

}
