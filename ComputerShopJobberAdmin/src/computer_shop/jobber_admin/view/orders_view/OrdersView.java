package computer_shop.jobber_admin.view.orders_view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber_admin.Main;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;

public class OrdersView extends AbstractInternalFrame{
	private final static long serialVersionUID=1L;
	/** for choice status of orders */
	private JComboBox choiceStatus;
	private Vector<String> choiceValues;
	/** for choice day before  */
	private JComboBox choiceDayBefore;
	/** ������ ������ ��� ������� ����������� ������� �� �������� ������ */
	private TableOrdersModel modelOfTable;
	/** �������, ������� �������� ��� ����������� ������ */
	private JTable table;
	/** ������, ������� ���������� ��� ����������� �������� */
	private AdminOrders loader;
	/** ���������� ������������� �������������� */
	private AdminIdentifier adminIdentifier;
	
	public OrdersView(IParent parent, 
					  JDesktopPane desktop,
					  AdminIdentifier adminIdentifier) {
		super("������", true, true, true, parent, desktop, 500, 300);
		this.loader=new NetAdminOrders(Main.URL);
		this.adminIdentifier=adminIdentifier;
		initComponents();
	}
	
	/** create, add listeners and placing component's */
	private void initComponents(){
		JPanel panelManager=new JPanel(new FlowLayout(FlowLayout.CENTER));
		choiceValues=new Vector<String>();
		choiceValues.add("���");
		choiceValues.add("�����");
		choiceValues.add("������������ ����������");
		choiceValues.add("���������� �� ����������");
		choiceValues.add("���������� ��������");
		choiceStatus=new JComboBox(choiceValues);
		JPanel panelChoiceStatus=this.getTitledPanel(choiceStatus, "������ ������");
		choiceStatus.setSelectedIndex(1);
		panelManager.add(panelChoiceStatus);
		choiceStatus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onChoiceStatusChange();
			}
		});
		choiceDayBefore=new JComboBox(new Object[]{"1 ����", "2 ���", "3 ���", "5 ����", "7 ����", "15 ����", "30 ����", "45 ����"});
		choiceDayBefore.setSelectedIndex(0);
		choiceDayBefore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onChoiceDayBeforeChange();
			}
		});
		JPanel panelDayBefore=this.getTitledPanel(choiceDayBefore, "�������� ��:");
		panelManager.add(new JLabel("   "));
		panelManager.add(panelDayBefore);
		
		table=new JTable();
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent event) {
				if((event.getClickCount()>1)&&(event.getButton()==MouseEvent.BUTTON1)){
					onTableDblClick();
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
		modelOfTable=new TableOrdersModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(this.modelOfTable);

		table.getColumn(table.getColumnName(0)).setHeaderValue("� ������");
		table.getColumn(table.getColumnName(1)).setHeaderValue("�������");
		table.getColumn(table.getColumnName(2)).setHeaderValue("���");
		table.getColumn(table.getColumnName(3)).setHeaderValue("�����");
		table.getColumn(table.getColumnName(4)).setHeaderValue("������");
		table.getColumn(table.getColumnName(5)).setHeaderValue("���-��");
		table.getColumn(table.getColumnName(6)).setHeaderValue("�����");
		table.getColumn(table.getColumnName(7)).setHeaderValue("������");
		
		
		JPanel panelMain=new JPanel(new BorderLayout());
		panelMain.add(panelManager, BorderLayout.NORTH);
		panelMain.add(new JScrollPane(table),BorderLayout.CENTER);
		this.getContentPane().add(panelMain);
		
		// �������� ������ � �������
		this.updateTable(this.getKodFromSelectedChoiceStatus(), this.getSelectedDayBefore());
	}
	
	private void updateTable(int statusKod, int dayBefore){
		Cursor cursor=this.getDesktopPane().getParent().getCursor();
		this.getDesktopPane().getParent().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			AdminTableOrdersRow[] elements=this.loader.getListOfOrders(this.adminIdentifier, statusKod, dayBefore);
			this.modelOfTable.updateModelData(elements);
			this.getDesktopPane().getParent().setCursor(cursor);
		}catch(Exception ex){
			this.getDesktopPane().getParent().setCursor(cursor);
			JOptionPane.showInternalMessageDialog(this, "��������� ������ �� �������","������",JOptionPane.ERROR_MESSAGE);
		}
	}

	/** ������� �� ��������� ������������� �������� */
	private void onTableDblClick(){
		int rowIndex=this.table.convertRowIndexToModel(this.table.getSelectedRow());
		AdminTableOrdersRow element=this.modelOfTable.getElementByRow(rowIndex);
		new OrderDetail(this, this.getDesktopPane(),adminIdentifier, this.loader, element);
	}
	
	private void onChoiceStatusChange(){
		this.updateTable(this.getKodFromSelectedChoiceStatus(), this.getSelectedDayBefore());
	}
	
	private void onChoiceDayBeforeChange(){
		this.updateTable(this.getKodFromSelectedChoiceStatus(), this.getSelectedDayBefore());
	}

	/**  �������� ��� ����������� � ������ ������ �������� ������ � ������� ��������� ���, ������ �� �������:
	 * <table border="1">
	 * 	<tr>
	 * 		<td> ���  </td> <td> -1 </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> ����� </td> <td> 0 </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> ������������ ���������� </td> <td> 1 </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> ���������� �� ���������� </td> <td> 2 </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> ���������� �������� </td> <td> 3 </td> 
	 * 	</tr>
	 * </table>
	 * 
	 * 
	 * */
	private int getKodFromSelectedChoiceStatus(){
		String value=(String)this.choiceStatus.getSelectedItem();
		return this.choiceValues.indexOf(value)-1;
	}
	
	private int getSelectedDayBefore(){
		String value=(String)this.choiceDayBefore.getSelectedItem();
		return Integer.parseInt(value.substring(0, value.indexOf(" ")));
	}
	
	private JPanel getTitledPanel(JComponent component, String title){
		JPanel returnValue=new JPanel(new GridLayout(1,1));
		returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
		returnValue.add(component);
		return returnValue;
	}

	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

}
