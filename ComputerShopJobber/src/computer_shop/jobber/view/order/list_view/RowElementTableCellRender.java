package computer_shop.jobber.view.order.list_view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import computer_shop.jobber.common_objects.RowElement;

public class RowElementTableCellRender extends DefaultTableCellRenderer{
	private final static long serialVersionUID=1L;
	private Color focusColor=Color.lightGray;
	private Color selectedColor=Color.lightGray;
	private Color defaultColor=new Color(238,238,238);
	private final static DecimalFormat priceFormat=new DecimalFormat("#.00");
	private final static DecimalFormat warrantyFormat=new DecimalFormat("#");
	private Color lessColor=Color.orange;
	private Color plentyColor=Color.blue;
	private Color reservColor=Color.red;
	private final Font font=new Font("Arial", Font.PLAIN,12);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, 
												   Object value,
												   boolean isSelected, 
												   boolean hasFocus, 
												   int row, 
												   int columnIndex) {
		RowElement element=(RowElement) ((RowElementTableModel)table.getModel()).getRowElement(row);
		JPanel panelResult=new JPanel(new GridLayout(1,1));
		JLabel label=new JLabel();
		label.setFont(font);
		if(columnIndex==0){
			label.setText(element.getName());
		};
		if(columnIndex==1){
			label.setText(priceFormat.format(element.getPrice()));
		};
		if(columnIndex==2){
			label.setText(warrantyFormat.format(element.getWarranty()));
		};
		
		panelResult.setBackground(defaultColor);
		if(isSelected){
			panelResult.setBackground(selectedColor);
		}
		if(hasFocus){
			panelResult.setBackground(focusColor);
		}

		if(columnIndex==3){
			switch(element.getQuantity()){
				case 1:label.setForeground(plentyColor);label.setText("есть");break;
				case 0:label.setForeground(reservColor);label.setText("резерв");break;
				case -1:label.setForeground(lessColor);label.setText("мало");break;
			}
		}
		panelResult.add(label);
		return panelResult;
	}
}
