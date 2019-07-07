package computer_shop.jobber_admin.view.jobber_edit;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;
import computer_shop.jobber_admin.Main;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;

public class JobberView extends AbstractInternalFrame{
	private static final long serialVersionUID = 1L;
	/** ������� � ������� */
	final private JTable table; 
	/** ��������� ��������� */
	private IJobberEdit loader;
	/** ������ ������ */
	private JobberTableModel model;
	/** ���������� ������������� ������������-�������������� */
	private AdminIdentifier adminIdentifier;
	/** ���� ��� �������������� ������ */
	private JobberEditor jobberEditor;

	
	/** �������������� ���� ��������� � ������� */
	public JobberView(AdminIdentifier adminIdentifier,
					  IParent parent, 
					  JDesktopPane desktop) {
		super("����������/�������������� ������ �� ���������", 
			  true, 
			  true, 
			  true, 
			  parent, 
			  desktop, 
			  450, 
			  250);
		table=new JTable();
		this.adminIdentifier=adminIdentifier;
		this.loader=new NetJobberEdit(Main.URL);
		initComponents();
	}

	/** create and place components */
	private void initComponents() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// create element's
		model=new JobberTableModel();
		table.setModel(model);
		table.getColumn(table.getColumnName(0)).setHeaderValue("� price");
		table.getColumn(table.getColumnName(1)).setHeaderValue("�������");
		table.getColumn(table.getColumnName(2)).setHeaderValue("���");
		table.getColumn(table.getColumnName(3)).setHeaderValue("�����");
		table.getColumn(table.getColumnName(4)).setHeaderValue("������");
		
		final JPopupMenu popupMenu=new JPopupMenu();
		JMenuItem menuItemEdit=new JMenuItem("�������������");
		JMenuItem menuItemAdd=new JMenuItem("��������");
		JMenuItem menuItemRemove=new JMenuItem("�������");
		popupMenu.add(menuItemEdit);
		popupMenu.addSeparator();
		popupMenu.add(menuItemAdd);
		popupMenu.addSeparator();
		popupMenu.add(menuItemRemove);
		popupMenu.addSeparator();

		
		// add listener's
		final Action actionEdit=new AbstractAction("�������������"){
			private final static long serialVersionUID=1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JobberView.this.onActionEditElement();
			}
		};
		final Action actionRemove=new AbstractAction("�������"){
			private final static long serialVersionUID=1L;
			@Override
			public void actionPerformed(ActionEvent event){
				JobberView.this.onActionRemoveElement();
			}
		};
		
		final Action actionAdd=new AbstractAction("��������"){
			private final static long serialVersionUID=1L;
			@Override
			public void actionPerformed(ActionEvent event){
				JobberView.this.onActionAddElement();
			}
		};
		JButton buttonAdd=new JButton(actionAdd);
		JButton buttonEdit=new JButton(actionEdit);
		JPanel panelManager=new JPanel(new GridLayout(1,2));
		panelManager.add(buttonAdd);
		panelManager.add(buttonEdit);
		
		menuItemEdit.setAction(actionEdit);
		menuItemAdd.setAction(actionAdd);
		menuItemRemove.setAction(actionRemove);
		
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent event) {
				// check for double click
				if((event.getClickCount()>1)&&(event.getButton()==MouseEvent.BUTTON1)){
					actionEdit.actionPerformed(null);
				}
				// check for right click
				if(event.getButton()==MouseEvent.BUTTON3){
					popupMenu.show(table, event.getX(), event.getY());
				}
			}
			@Override
			public void mouseEntered(MouseEvent event) {
			}
			@Override
			public void mouseExited(MouseEvent event) {
			}
			@Override
			public void mousePressed(MouseEvent event) {
			}
			@Override
			public void mouseReleased(MouseEvent event) {
			}
		});
		// placing
		JPanel panelMain=new JPanel(new BorderLayout());
		panelMain.add(new JScrollPane(table),BorderLayout.CENTER);
		panelMain.add(panelManager,BorderLayout.SOUTH);
		this.getContentPane().add(panelMain);
		this.updateList();
	}

	protected void onActionAddElement() {
		this.jobberEditor=new JobberEditor(this.adminIdentifier,loader, "���������� ��������",this,this.desktop);
	}

	private void onActionEditElement(){
		JobberElement selectedElement=getCurrentSelection();
		if(selectedElement!=null){
			this.jobberEditor=new JobberEditor(this.adminIdentifier,loader,selectedElement,"���������� ��������",this,this.desktop);
		}else{
			JOptionPane.showInternalMessageDialog(this, "�������� ���� �����");
		}
	}
	/** ������ �� �������� ������� */
	private void onActionRemoveElement(){
		JobberElement selectedElement=getCurrentSelection();
		if(selectedElement!=null){
			if(JOptionPane.showConfirmDialog(this, "�� ������� � �������� �������� ?", "��������������", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
				// ������� � �������� �������� - ������ �� ��������
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try{
					String returnValue=loader.remove(adminIdentifier, selectedElement);
					if(returnValue==null){
						this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						// ������ ������� �������
						JOptionPane.showMessageDialog(this, "������ ������� �������");
						this.updateList();
					}else{
						this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						// ������ ������� �������
						JOptionPane.showMessageDialog(this, returnValue, "��������������", JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception ex){
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(this, "��������� ������ �� ������� ","������ �����",JOptionPane.ERROR_MESSAGE);
				}
			}else{
				// ������ �������� 
			}
		}else{
			JOptionPane.showInternalMessageDialog(this, "�������� ���� �����");
		}
	}

	/** �������� ������ ��������� */
	private void updateList(){
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try{
			this.model.updateData(loader.getAllJobbers(this.adminIdentifier));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}catch(Exception ex){
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			JOptionPane.showMessageDialog(this, "��������� ������ �� ������� ","������ �����",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** �������� ������� ���������� ������ */
	private JobberElement getCurrentSelection(){
		int selectedRow=table.convertRowIndexToModel(table.getSelectedRow());
		if(selectedRow>=0){
			return this.model.getJobberElement(selectedRow);
		}else{
			return null;
		}
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
		if(this.jobberEditor!=null){
			if(this.jobberEditor.isButtonOk()){
				this.updateList();
			}
		}
	}

}
