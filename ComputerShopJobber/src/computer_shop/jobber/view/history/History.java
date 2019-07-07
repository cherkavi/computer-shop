package computer_shop.jobber.view.history;

import java.awt.BorderLayout;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.csvreader.CsvWriter;

import computer_shop.jobber.common_objects.HistoryOrder;
import computer_shop.jobber.common_objects.HistoryRow;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.view.common.AbstractIntrnalFrame;
import computer_shop.jobber.view.common.IParent;

public class History extends AbstractIntrnalFrame{
	private static final long serialVersionUID = 1L;
	private JobberIdentifier jobberIdentifier;
	private JComboBox comboboxRange;
	private final HashMap<String, Integer> historyRange=new HashMap<String,Integer>();
	private TableHistory tableHistory;
	private HistoryLoader loader;
	private JPopupMenu popup;
	private SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
	
	/** история заказов по указанному оптовику */
	public History(String title, 
				   IParent parent, 
				   JDesktopPane desktop,
				   JobberIdentifier jobberIdentifier,
				   HistoryLoader loader) {
		super(title, true, true, true, parent, desktop, 500, 400);
		this.jobberIdentifier=jobberIdentifier;
		this.loader=loader;
		initComponents();
	}
	
	private void initComponents(){
		// create elements
		historyRange.put("1 день", new Integer(1));
		historyRange.put("3 дня", new Integer(3));
		historyRange.put("7 дней", new Integer(7));
		historyRange.put("14 дней", new Integer(14));
		historyRange.put("30 дней", new Integer(14));
		String[] array=historyRange.keySet().toArray(new String[]{});
		Arrays.sort(array,new Comparator<String>(){
			@Override
			public int compare(String arg0, String arg1) {
				int spacePosition0=arg0.indexOf(" ");
				int spacePosition1=arg1.indexOf(" ");
				int int0=Integer.parseInt(arg0.substring(0, spacePosition0));
				int int1=Integer.parseInt(arg1.substring(0, spacePosition1));
				if(int0>int1){
					return 1;
				}else{
					if(int1>int0){
						return -1;
					}else{
						return 0;
					}
				}
			}
		});
		this.tableHistory=new TableHistory();
		
		this.popup=new JPopupMenu();
		Action showSave=new AbstractAction("Сохранить список"){
			private static final long serialVersionUID=1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				onShowList(event);
			}
		};
		this.popup.addSeparator();
		this.popup.add(showSave);
		this.popup.addSeparator();
		
		// add listener's
		comboboxRange=new JComboBox(array);
		comboboxRange.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onComboboxChange();
			}
		});
		this.tableHistory.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton()==MouseEvent.BUTTON3){
					onShowPopup(event);
				}
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
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		
		// placing components
		JPanel panelManager=new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelManager.add(comboboxRange);
		JPanel panelMain=new JPanel(new BorderLayout());
		panelMain.add(panelManager,BorderLayout.NORTH);
		panelMain.add(new JScrollPane(this.tableHistory));
		this.getContentPane().add(panelMain);
		comboboxRange.setSelectedIndex(0);
		this.onComboboxChange();
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

	/** нажата правая кнопка мыши на таблице - отобразить дополнительное меню*/
	private void onShowPopup(MouseEvent event){
		if(event.getSource() instanceof JTable){
			this.popup.show(((JTable)event.getSource()), event.getX(), event.getY());
		}
	}
	
	private void onShowList(ActionEvent event){
		// получить выделенную строку из таблицы
		int selectedRow=this.tableHistory.convertRowIndexToModel(this.tableHistory.getSelectedRow());
		HistoryRow historyRow=this.tableHistory.getModelHistoryRow(selectedRow);
		if(historyRow!=null){
			// отправить запрос на базу по получению данных
			HistoryOrder[] historyOrders=null;
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try{
				historyOrders=this.loader.getHistoryElementsByOrder(this.jobberIdentifier, historyRow);
				if(historyOrders.length==0){
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));					
					JOptionPane.showInternalMessageDialog(this, "Нет данных для отображения");
				}else{
					JFileChooser fileChooser=new JFileChooser();
					fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files","csv"));
					if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
						// file is selected
						File selectedFile=fileChooser.getSelectedFile();
						String filePath=selectedFile.getAbsolutePath();
						if(filePath.toLowerCase().endsWith(".csv")==false){
							filePath=selectedFile.getAbsolutePath()+".csv";
						}
						CsvWriter writer=null;
						try{
							writer=new CsvWriter(filePath,';',Charset.forName("WINDOWS-1251"));
							writer.writeRecord(new String[]{"","","Заказ №:"+historyRow.getNumber(),"    Дата:"+sdf.format(historyRow.getDate()),"  Валюта:"+((historyRow.getCurrency()==0)?"грн.":"USD")});
							writer.writeRecord(new String[]{});
							writer.writeRecord(new String[]{"№","Секция","Наименование","Кол-во","Цена","Сумма"});
							for(int counter=0;counter<historyOrders.length;counter++){
								writer.writeRecord(new String[]{Integer.toString(counter),
																historyOrders[counter].getSectionName(),
																historyOrders[counter].getName(),
																Integer.toString(historyOrders[counter].getQuantity()),
																Float.toString(historyOrders[counter].getAmount()).replace('.', ','),
																Float.toString(historyOrders[counter].getQuantity()*historyOrders[counter].getAmount()).replace('.', ',')
																}
								                   );
								if(counter%10==0){
									writer.flush();
								}
							}
							writer.close();
							this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							JOptionPane.showInternalMessageDialog(this, "Данные успешно сохранены");
						}catch(Exception ex){
							this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));							
							JOptionPane.showInternalMessageDialog(this, "Удаленный сервер не ответил","Ошибка",JOptionPane.ERROR_MESSAGE);
						}finally{
							try{
								writer.close();
							}catch(Exception ex){};
						}
					}else{
						// file is not selected 
					}
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}catch(Exception ex){
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));				
				JOptionPane.showInternalMessageDialog(this, "Удаленный сервер не ответил\n"+ex.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}else{
			// нет выделенного объекта
		}
	}
	
	/** произошла смена элемента Combobox */
	private void onComboboxChange(){
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.comboboxRange.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//System.out.println("Combobox: "+this.comboboxRange.getSelectedItem()+"   "+this.historyRange.get(this.comboboxRange.getSelectedItem()));
		this.tableHistory.updateModel(this.loader.getHistoryOrders(this.jobberIdentifier, this.historyRange.get(this.comboboxRange.getSelectedItem())));
		this.comboboxRange.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
}
