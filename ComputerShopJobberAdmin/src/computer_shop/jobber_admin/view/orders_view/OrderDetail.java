package computer_shop.jobber_admin.view.orders_view;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.AdminTableOrdersRow;
import computer_shop.jobber.common_objects.ElementOfOrder;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;

/** ������ ��� ���������� � ������, ������� ������������� ���������� ��� ��������� � ���������� ������ */
public class OrderDetail extends AbstractInternalFrame{
	private final static long serialVersionUID=1L;
	private AdminOrders loader;
	private AdminTableOrdersRow element;
	private boolean flagUpdate=false;
	private Vector<String> choiceValues;
	private JComboBox choiceStatus;
	private AdminIdentifier adminIdentifier;
	
	/** ������ ��� ���������� � ������, ������� ������������� ���������� ��� ��������� � ���������� ������ 
	 * @param parent - ����������� ������ (����) �� ������� ������������ ������ ������ 
	 * @param adminIdentifier - ���������� ������������� �������������� 
	 * @param loader - ������������� ������� 
	 * @param element - ������� ��� �������������� 
	 */
	public OrderDetail(IParent parent, 
			  				JDesktopPane desktop, 
							AdminIdentifier adminIdentifier, 
							AdminOrders loader, 
							AdminTableOrdersRow element){
		super("����� �"+element.getKodOrder(), true, true, false, parent, desktop, 260, 175);
		this.adminIdentifier=adminIdentifier;
		this.loader=loader;
		this.element=element;
		initComponents();
	}
	
	private void initComponents(){
		// ChangeStatus
		choiceValues=new Vector<String>();
		choiceValues.add("�����");
		choiceValues.add("������������ ����������");
		choiceValues.add("���������� �� ����������");
		choiceValues.add("���������� ��������");
		choiceStatus=new JComboBox(choiceValues);
		choiceStatus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onChoiceStatus();
			}
		});
		choiceStatus.setSelectedIndex(this.element.getStatus());
		JPanel panelChoiceStatus=this.getTitledPanel(choiceStatus, "������ ������");
		
		JButton buttonSave=new JButton("��������� ����� �� ������� ���� ");
		buttonSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonSave();
			}
		});
		
		JButton buttonClose=new JButton("������� ����");
		buttonClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonClose();
			}
		});
		
		JPanel panelMain=new JPanel(new GridLayout(3,1));
		panelMain.add(panelChoiceStatus);
		panelMain.add(buttonSave);
		panelMain.add(buttonClose);
		this.getContentPane().add(panelMain);
	}

	/** �������� ������ � ���������� */
	private JPanel getTitledPanel(JComponent component, String title){
		JPanel returnValue=new JPanel(new GridLayout(1,1));
		returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
		returnValue.add(component);
		return returnValue;
	}

	private void onChoiceStatus(){
		int selectedStatus=this.choiceStatus.getSelectedIndex();
		if(selectedStatus!=this.element.getStatus()){
			Cursor cursor=this.getCursor();
			try{
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				this.loader.changeStatus(this.adminIdentifier, element.getKodOrder(), selectedStatus);
				element.setStatus(selectedStatus);
				this.flagUpdate=true;
				this.setCursor(cursor);
			}catch(Exception ex){
				this.setCursor(cursor);
				JOptionPane.showMessageDialog(this, "��������� ������ �� �������","������", JOptionPane.ERROR_MESSAGE);
			}
		}else{
			// status is not change
		}
	}
	
	public boolean needUpdate(){
		return this.flagUpdate;
	}
	
	public void onButtonClose(){
		this.internalFrameClosed(null);
	}
	
	private final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd-HH_mm");
	
	public void onButtonSave(){
		// �������� ������ �� ����������
		Cursor cursor=this.getCursor();
		try{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			ElementOfOrder[] orderElements=this.loader.getElementsForOrder(adminIdentifier, this.element.getKodOrder());
			this.setCursor(cursor);
			if(orderElements!=null){
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setSelectedFile(new File(element.getJobberName()+" "+sdf.format(element.getTimeWrite())+".txt"));
				if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
					if(saveElementsToFile(fileChooser.getSelectedFile(),orderElements)==true){
						// ������ ������� ��������� 
						JOptionPane.showMessageDialog(this, "������ ������� ���������");
					}else{
						// ������ ���������� ������ 
						JOptionPane.showMessageDialog(this, "������ �� ���������","������",JOptionPane.ERROR_MESSAGE);
					}
				}else{
					// ������ ����������
				}
			}else{
				JOptionPane.showMessageDialog(this, "��� ������ ��� ����������");
			}
		}catch(Exception ex){
			this.setCursor(cursor);
			JOptionPane.showMessageDialog(this, "��������� ������ �� �������","������", JOptionPane.ERROR_MESSAGE);
		}
		// �������� ���� ���������� �� ������� ���� 
	}

	private boolean saveElementsToFile(File selectedFile, ElementOfOrder[] orderElements) {
		boolean returnValue=false;
		try{
			// ��������� ������
			BufferedWriter writer=null;
			try{
				writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
				for(int counter=0;counter<orderElements.length;counter++){
					writer.write(orderElements[counter].getKod()+" - "+orderElements[counter].getQuantity()+"; ");
					writer.write("\n");
				}
				returnValue=true;
			}catch(Exception ex){
				System.err.println("Error in save to File: ");
			}finally{
				try{
					writer.close();
				}catch(Exception ex){};
			}
		}catch(Exception ex){
			returnValue=false;
		}
		return returnValue;
	}

	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}
	
	
}
