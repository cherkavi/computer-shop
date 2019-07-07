package computer_shop.jobber.view.history;

import java.awt.Color;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import computer_shop.jobber.common_objects.HistoryRow;

public class TableHistory extends JTable{
	private static final long serialVersionUID = 1L;
	private ModelHistory model;
	
	public TableHistory(){
		super();
		this.model=new ModelHistory();
		this.setModel(this.model);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.initColumns();
	}
	
	private void initColumns(){
		final Color selectedColor=new Color(204,204,250);
		final SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");
		//JLabel label=new JLabel("");
		//final Font font=label.getFont();
		//font.deriveFont(font.getStyle() | Font.BOLD);
		final Font font=new Font("Arial", Font.PLAIN,12); 

		
		this.getColumn(this.getColumnName(0)).setWidth(30);
		this.getColumn(this.getColumnName(0)).setCellRenderer(new TableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
														   Object value, 
														   boolean isSelected, 
														   boolean hasFocus, 
														   int row, 
														   int column) {
				JPanel panelResult=new JPanel(new GridLayout(1,1));
				String labelValue=(value==null)?"":(String)value;
				JLabel label=new JLabel(labelValue,JLabel.CENTER);
				label.setFont(font);
				if(hasFocus||isSelected){
					panelResult.setBackground(selectedColor);
				}else{
					panelResult.setBackground(Color.white);
				}
				panelResult.add(label);
				return panelResult;
			}
			
		});
		this.getColumn(this.getColumnName(0)).setHeaderValue("Номер заказа");
		
		this.getColumn(this.getColumnName(1)).setCellRenderer(new TableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
														   Object value, 
														   boolean isSelected, 
														   boolean hasFocus, 
														   int row, 
														   int column) {
				JPanel panelResult=new JPanel(new GridLayout(1,1));
				String labelValue=(value==null)?"":sdf.format((Date)value);
				JLabel label=new JLabel(labelValue,JLabel.CENTER);
				label.setFont(font);
				if(hasFocus||isSelected){
					panelResult.setBackground(selectedColor);
				}else{
					panelResult.setBackground(Color.white);
				}
				panelResult.add(label);
				return panelResult;
			}
		});
		this.getColumn(this.getColumnName(1)).setWidth(75);
		this.getColumn(this.getColumnName(1)).setHeaderValue("Дата заказа");
		
		this.getColumn(this.getColumnName(2)).setWidth(50);
		this.getColumn(this.getColumnName(2)).setCellRenderer(new TableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
														   Object value, 
														   boolean isSelected, 
														   boolean hasFocus, 
														   int row, 
														   int column) {
				JPanel panelResult=new JPanel(new GridLayout(1,1));
				String labelValue=((Integer)value==0)?"грн.":"$";
				JLabel label=new JLabel(labelValue,JLabel.CENTER);
				label.setFont(font);
				if(hasFocus||isSelected){
					panelResult.setBackground(selectedColor);
				}else{
					panelResult.setBackground(Color.white);
				}
				panelResult.add(label);
				return panelResult;
			}
		});
		this.getColumn(this.getColumnName(2)).setHeaderValue("Валюта");
		
		this.getColumn(this.getColumnName(3)).setWidth(50);
		this.getColumn(this.getColumnName(3)).setCellRenderer(new TableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
														   Object value, 
														   boolean isSelected, 
														   boolean hasFocus, 
														   int row, 
														   int column) {
				JPanel panelResult=new JPanel(new GridLayout(1,1));
				String labelValue=(value==null)?"":((Integer)value).toString();
				JLabel label=new JLabel(labelValue,JLabel.CENTER);
				label.setFont(font);
				if(hasFocus||isSelected){
					panelResult.setBackground(selectedColor);
				}else{
					panelResult.setBackground(Color.white);
				}
				panelResult.add(label);
				return panelResult;
			}
		});
		this.getColumn(this.getColumnName(3)).setHeaderValue("Кол-во");
		
		this.getColumn(this.getColumnName(4)).setWidth(75);
		this.getColumn(this.getColumnName(4)).setCellRenderer(new TableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable arg0,
														   Object value, 
														   boolean isSelected, 
														   boolean hasFocus, 
														   int row, 
														   int column) {
				JPanel panelResult=new JPanel(new GridLayout(1,1));
				String labelValue=(value==null)?"":((Float)value).toString();
				JLabel label=new JLabel(labelValue,JLabel.CENTER);
				label.setFont(font);
				if(hasFocus||isSelected){
					panelResult.setBackground(selectedColor);
				}else{
					panelResult.setBackground(Color.white);
				}
				panelResult.add(label);
				return panelResult;
			}
		});
		this.getColumn(this.getColumnName(4)).setHeaderValue("Сумма заказа");
	}
	
	public HistoryRow getModelHistoryRow(int index){
		return this.model.getElementByIndex(index);
	}
	
	public void updateModel(HistoryRow[] elements){
		this.model.updateModel(elements);
	}

	
}
