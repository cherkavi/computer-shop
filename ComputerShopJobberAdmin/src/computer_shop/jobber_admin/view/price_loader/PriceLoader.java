package computer_shop.jobber_admin.view.price_loader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import computer_shop.jobber_admin.Main;
import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;

public class PriceLoader extends AbstractInternalFrame{
	private static final long serialVersionUID = 1L;
	private final static int processWatcherDelay=5;
	/** ���������� ������������� ��������������, �� ��������� �������� �������������� ����� ������� */
	private AdminIdentifier adminIdentifier;
	/** ��������� ��� ��������� �������� �������� �����-����� */
	private PriceLoaderManager priceLoaderManager;
	/** ������, ������� ���������� ������� �������� �����-����� ��� �� ������ ��� ������ �������� �����-����� */
	private JPanel panelShowProcess;
	/** ��������� ���� ��� ��������/��������� ������� ���� ��� �������� �����-����� */
	private JTextField fieldUrl;
	/** ������, ������� ������ �� ������������� ���������� ������� ���� � ������ �������� ������ */
	private ProcessWatcher processWatcher; 
	private final Container parentContainer;
	
	
	public PriceLoader(IParent parent, 
					   JDesktopPane desktop,
					   AdminIdentifier adminIdentifier) {
		super("�������� �����-�����", 
			  true, 
			  true, 
			  false, 
			  parent, 
			  desktop, 
			  400, 
			  150);
		parentContainer=this.getParent();
		this.adminIdentifier=adminIdentifier;
		priceLoaderManager=new NetPriceLoaderManager(Main.URL);
		initComponents();
	}
	
	private void initComponents(){
		// ������� ������ �� ������, ����� ���� � ������ ������ ����� ��������� � ��������� ��������
			// ������ ��� ��������� URL ��� �������� �����-�����
		JPanel panelSetUrl=new JPanel(new BorderLayout());
		panelSetUrl.setBorder(javax.swing.BorderFactory.createTitledBorder("���������� URL ��� �������� ������"));
		fieldUrl=new JTextField();
		JButton buttonSetUrl=new JButton("����������");
		panelSetUrl.add(fieldUrl,BorderLayout.CENTER);
		panelSetUrl.add(buttonSetUrl,BorderLayout.EAST);
		buttonSetUrl.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonSetUrl();
			}
		});

			// ������ ����������� �������� �������� 
		panelShowProcess=new JPanel(new GridLayout(1,1));
		panelShowProcess.setBorder(javax.swing.BorderFactory.createTitledBorder("������� �������� �����-����� �� �������"));
		
		JProgressBar progressBar=new JProgressBar(SwingConstants.HORIZONTAL, 0,100);
		panelShowProcess.add(progressBar);
			// �������� ������ 
		JPanel panelMain=new JPanel(new GridLayout(2,1));
		panelMain.add(panelSetUrl);
		panelMain.add(panelShowProcess);
		this.getContentPane().add(panelMain);
		
		Cursor cursor=this.getCursor();
		//System.out.println(cursor.getName());
		try{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			// ��������� ��������� �������� �������� �����-����� - �������� ����� ������ � ��������� ��������
			int percentLoad=this.getPercentLoad();
			if(percentLoad==100){
				this.setToShowProcessButton();
			}else{
				this.setPercentToShowProcessPanel(percentLoad);
			}
			// ���������� ������ ������� URL ��� ��������� ����� 
			synchronized(priceLoaderManager){
				this.fieldUrl.setText(this.priceLoaderManager.getPriceLoadUrl(adminIdentifier));
			}
			this.setCursor(cursor);
		}catch(Exception ex){
			this.setCursor(cursor);
			JOptionPane.showMessageDialog(this, "��������� ������ �� �������", "������", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	
	/** ���������� �� ������ �������� JButton ������ JProgressBar */
	private void setToShowProcessButton(){
		this.panelShowProcess.remove(0);
		JButton buttonLoad=new JButton("������ �������� �����-�����");
		buttonLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonLoadClick();
			}
		});
		this.panelShowProcess.add(buttonLoad);
	}
	
	/** ���������� �� ������ �������� ������ JButton JProgressBar */
	private void setToShowProcessProgressBar(){
		this.panelShowProcess.remove(0);
		JProgressBar progressBar=new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		this.panelShowProcess.add(progressBar);
	}
	
	/** ���������� � ������ �������� �������� �������� �������� ������ 
	 * @param percent - �������, ������� ���������� ���������� ������������ 
	 * */
	public void setPercentToShowProcessPanel(int percent){
		if(percent==100){
			this.setToShowProcessButton();
			/*
			if(this.processWatcher!=null){
				try{
					this.processWatcher.stop();
				}catch(NullPointerException npe){}; // ������� ����� ������ ������
				this.processWatcher=null;
			}*/
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					JOptionPane.showInternalMessageDialog(PriceLoader.this.parentContainer, "������ ������� ��������� ");					
				}
			});
			
		}else{
			if(this.panelShowProcess.getComponent(0) instanceof JProgressBar){
				// � ������ ��������� JProgressBar
			}else{
				// � ������ ����������� ������ - �������� �� ProgressBar
				this.setToShowProcessProgressBar();
				
			}
			if(this.processWatcher==null){
				this.processWatcher=new ProcessWatcher(this,processWatcherDelay, priceLoaderManager);
			}
			((JProgressBar)this.panelShowProcess.getComponent(0)).setValue(percent);
			//System.out.println("set percent");
			SwingUtilities.updateComponentTreeUI(this.panelShowProcess);
		}
	}
	
	/** ������� �������� �����-����� */
	private void onButtonLoadClick(){
		int returnValue=JOptionPane.showInternalConfirmDialog(this,"�� ������� � ����������","��������",JOptionPane.YES_NO_OPTION);
		System.out.println(returnValue);
		if(returnValue==JOptionPane.YES_OPTION){
			boolean serverReturnValue=false;
			Cursor cursor=this.getCursor();
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try{
				this.setCursor(cursor);
				// ������� �� ������ ������� �� �������� �����-�����
				synchronized(priceLoaderManager){
					serverReturnValue=this.priceLoaderManager.downloadPriceAndWriteIt(adminIdentifier);
				}
				if(serverReturnValue==false){
					JOptionPane.showInternalMessageDialog(this, "����� �� ����� ���� ��������");
				}else{
					if(this.processWatcher==null){
						this.processWatcher=new ProcessWatcher(this,processWatcherDelay, priceLoaderManager);
					}
					this.setToShowProcessProgressBar();
				}
			}catch(Exception ex){
				this.setCursor(cursor);
				JOptionPane.showInternalMessageDialog(this, "������ �� �������","������", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/** ������ ������ �� ��������� ������� ���� ��� �������� �����-����� */
	private void onButtonSetUrl(){
		Cursor cursor=this.getCursor();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try{
			String setResult=null;
			synchronized(priceLoaderManager){
				setResult=this.priceLoaderManager.setPriceLoadUrl(adminIdentifier, this.fieldUrl.getText());
			}
			if(setResult==null){
				this.setCursor(cursor);
				JOptionPane.showInternalMessageDialog(this, "URL ������� ����������");
			}else{
				this.setCursor(cursor);
				JOptionPane.showInternalMessageDialog(this, setResult,"������ ��������� URL",JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception ex){
			this.setCursor(cursor);
			JOptionPane.showInternalMessageDialog(this, "��������� ������ �� �������", "������",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
	/** ��������� ������ �� ������ � �������� ��������� �������� �����-����� */
	private int getPercentLoad() throws Exception {
		synchronized(priceLoaderManager){
			return this.priceLoaderManager.getPercentLoad();
		}
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

	@Override
	protected void onBeforeCloseThisWindow() {
		try{
			this.processWatcher.stop();
		}catch(NullPointerException ex){};
	}
	
}
