package computer_shop.jobber_admin.view.jobber_edit;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;

/** ���� ��� ��������������/���������� ������ */
public class JobberEditor extends AbstractInternalFrame{
	private final static long serialVersionUID=1L;
	/** ����, ������� ���������� � ������� ������� */
	private boolean flagOk=false;
	/** ������������� ������������/�������������� */
	private AdminIdentifier adminIdentifier;
	/** ������ �� ��������� ��������/��������� ������ */
	private IJobberEdit loader;
	/** ������������� ������ */
	private JobberElement jobberElement;
	
	/** ���� ��� ���������� ������ */
	public JobberEditor(AdminIdentifier adminIdentifier,
						IJobberEdit loader, 
						String title, 
						IParent parent, 
						JDesktopPane desktop) {
		super(title, true, false, false, parent, desktop,300,250);
		this.adminIdentifier=adminIdentifier;
		this.loader=loader;
		this.jobberElement=null;
		this.initComponents();
	}

	/** ���� ��� �������������� ������ */
	public JobberEditor(AdminIdentifier adminIdentifier,
						IJobberEdit loader,
						JobberElement jobberElement,
						String title, 
						IParent parent, 
						JDesktopPane desktop) {
		super(title, true, false, false, parent, desktop,300,275);
		this.adminIdentifier=adminIdentifier;
		this.loader=loader;
		this.jobberElement=jobberElement;
		this.initComponents();
		this.pack();
	}

    private void initComponents() {
    	JPanel panelManager=new JPanel(new BorderLayout());
    	JButton buttonOk=new JButton("���������");
    	JButton buttonCancel=new JButton("��������");
    	panelManager.add(buttonOk, BorderLayout.CENTER);
    	panelManager.add(buttonCancel,BorderLayout.EAST);
    	
    	final JTextField fieldSurname=new JTextField(""); 
    	final JTextField fieldName=new JTextField(""); 
    	final JTextField fieldLogin=new JTextField(""); 
    	final JTextField fieldPassword=new JTextField("");
    	final JComboBox fieldPrice=new JComboBox(new String[]{"���","����. ���.","����.","������"});
    	if(this.jobberElement!=null){
    		fieldSurname.setText(this.jobberElement.getSurname());
    		fieldName.setText(this.jobberElement.getName());
    		fieldLogin.setText(this.jobberElement.getLogin());
    		fieldPassword.setText(this.jobberElement.getPassword());
    		fieldPrice.setSelectedIndex(this.jobberElement.getPriceNumber()-1);
    	}
    	buttonOk.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			JobberEditor.this.onButtonOk(fieldSurname.getText(), 
    										 fieldName.getText(), 
    										 fieldLogin.getText(), 
    										 fieldPassword.getText(),
    										 fieldPrice.getSelectedIndex()+1);
    		}
    	});
    	buttonCancel.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			JobberEditor.this.onButtonCancel();
    		}
    	});
    	
    	JPanel panelMain=new JPanel(new GridLayout(6,1));
    	panelMain.add(this.getTitledPanel(fieldSurname, "�������"));
    	panelMain.add(this.getTitledPanel(fieldName, "���"));
    	panelMain.add(this.getTitledPanel(fieldLogin, "�����"));
    	panelMain.add(this.getTitledPanel(fieldPassword, "������"));
    	panelMain.add(this.getTitledPanel(fieldPrice,"����� ������"));
    	panelMain.add(panelManager);
    	this.getContentPane().add(panelMain);
	}
    
    
    /** �������� ������ � ���������� */
    private JPanel getTitledPanel(JComponent component, String title){
    	JPanel returnValue=new JPanel(new GridLayout(1,1));
    	returnValue.add(component);
    	returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
    	return returnValue;
    }

	/** ���� �� ������ ������� OK */
	public boolean isButtonOk(){
    	return this.flagOk;
    }
	
	public void onButtonOk(String surname, String name, String login, String password, int priceNumber){
		try{
			if((surname==null)||(surname.trim().equals(""))){
				throw new Exception("������� �������");
			}
			if((name==null)||(name.trim().equals(""))){
				throw new Exception("������� ���");
			}
			if((login==null)||(login.trim().equals(""))){
				throw new Exception("������� �����");
			}
			if((password==null)||(password.trim().equals(""))){
				throw new Exception("������� ������");
			}
			String returnValue=null;
			if(this.jobberElement!=null){
				//System.out.println("������� ��������������");
				this.jobberElement.setSurname(surname);
				this.jobberElement.setName(name);
				this.jobberElement.setLogin(login);
				this.jobberElement.setPassword(password);
				this.jobberElement.setPriceNumber(priceNumber);
				try{
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					returnValue=this.loader.update(adminIdentifier, this.jobberElement);
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					if(returnValue==null){
						// ������ ������� ��������� - ������� ���� 
						this.flagOk=true;
						this.closeThisWindow();
					}else{
						JOptionPane.showInternalMessageDialog(this, returnValue,"������",JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception ex){
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showInternalMessageDialog(this, "��������� ������ �� �������","������",JOptionPane.ERROR_MESSAGE);
				}
			}else{
				//System.out.println(" ������� ���������� ������"); 
				JobberElement jobberElement=new JobberElement();
				jobberElement.setSurname(surname);
				jobberElement.setName(name);
				jobberElement.setLogin(login);
				jobberElement.setPassword(password);
				jobberElement.setPriceNumber(priceNumber);
				try{
					this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					returnValue=this.loader.add(adminIdentifier, jobberElement);
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					if(returnValue==null){
						// ������ ������� ��������� - ������� ���� 
						this.flagOk=true;
						this.closeThisWindow();
					}else{
						JOptionPane.showInternalMessageDialog(this, returnValue,"������",JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception ex){
					this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showInternalMessageDialog(this, "��������� ������ �� �������","������",JOptionPane.ERROR_MESSAGE);
				}
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}
	
	
	public void onButtonCancel(){
		this.internalFrameClosed(null);
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		// TODO Auto-generated method stub
		
	}

}
