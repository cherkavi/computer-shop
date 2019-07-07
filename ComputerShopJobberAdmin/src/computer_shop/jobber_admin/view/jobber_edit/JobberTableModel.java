package computer_shop.jobber_admin.view.jobber_edit;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import computer_shop.jobber.common_objects.JobberElement;

public class JobberTableModel extends AbstractTableModel{
	private final static long serialVersionUID=1L;
	
	private ArrayList<JobberElement> list=new ArrayList<JobberElement>();
	
	public JobberTableModel(){
	}
	
	/** �������� ������ � ������ */
	public void updateData(JobberElement[] elements){
		this.list.clear();
		if(elements!=null){
			for(int counter=0;counter<elements.length;counter++){
				this.list.add(elements[counter]);
			}
		};
		this.fireTableDataChanged();
	}
	
	/** �������� ������ �� ������ 
	 * @param - ������ � ������ ������
	 * @return null - ���� Out of range
	 * */
	public JobberElement getJobberElement(int index){
		return this.list.get(index);
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
	public Object getValueAt(int row, int column) {
		switch (column){
			case 0: {
					int priceNumber=list.get(row).getPriceNumber();
					switch(priceNumber){
						case 1: return "���";
						case 2: return "����. ���.";
						case 3: return "����.";
						case 4: return "������";
						default: return "";
					}
			}
			case 1: return list.get(row).getSurname();
			case 2: return list.get(row).getName();
			case 3: return list.get(row).getLogin();
			case 4: return list.get(row).getPassword();
			default: return null;
		}
	}

}
