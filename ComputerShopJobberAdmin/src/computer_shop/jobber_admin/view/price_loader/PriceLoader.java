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
	/** уникальный идентификатор администратора, на основании которого осуществляется обмен данными */
	private AdminIdentifier adminIdentifier;
	/** интерфейс для получения процента загрузки прайс-листа */
	private PriceLoaderManager priceLoaderManager;
	/** панель, которая отображает процесс загрузки прайс-листа или же кнопку для начала загрузки прайс-листа */
	private JPanel panelShowProcess;
	/** текстовое поле для загрузки/установки полного пути для загрузки прайс-листа */
	private JTextField fieldUrl;
	/** объект, который следит за необходимость оповещения данного окна о полной загрузке прайса */
	private ProcessWatcher processWatcher; 
	private final Container parentContainer;
	
	
	public PriceLoader(IParent parent, 
					   JDesktopPane desktop,
					   AdminIdentifier adminIdentifier) {
		super("Загрузка прайс-листа", 
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
		// послать запрос на сервер, может быть в данный момент прайс находится в состоянии загрузки
			// панель для установки URL для принятия прайс-листа
		JPanel panelSetUrl=new JPanel(new BorderLayout());
		panelSetUrl.setBorder(javax.swing.BorderFactory.createTitledBorder("Установить URL для загрузки прайса"));
		fieldUrl=new JTextField();
		JButton buttonSetUrl=new JButton("Установить");
		panelSetUrl.add(fieldUrl,BorderLayout.CENTER);
		panelSetUrl.add(buttonSetUrl,BorderLayout.EAST);
		buttonSetUrl.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonSetUrl();
			}
		});

			// панель отображения процесса загрузки 
		panelShowProcess=new JPanel(new GridLayout(1,1));
		panelShowProcess.setBorder(javax.swing.BorderFactory.createTitledBorder("Процесс загрузки прайс-листа на сервере"));
		
		JProgressBar progressBar=new JProgressBar(SwingConstants.HORIZONTAL, 0,100);
		panelShowProcess.add(progressBar);
			// основная панель 
		JPanel panelMain=new JPanel(new GridLayout(2,1));
		panelMain.add(panelSetUrl);
		panelMain.add(panelShowProcess);
		this.getContentPane().add(panelMain);
		
		Cursor cursor=this.getCursor();
		//System.out.println(cursor.getName());
		try{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			// проверить состояние загрузки текущего прайс-листа - возможно прайс сейчас в состоянии загрузки
			int percentLoad=this.getPercentLoad();
			if(percentLoad==100){
				this.setToShowProcessButton();
			}else{
				this.setPercentToShowProcessPanel(percentLoad);
			}
			// установить строку полного URL для получения файла 
			synchronized(priceLoaderManager){
				this.fieldUrl.setText(this.priceLoaderManager.getPriceLoadUrl(adminIdentifier));
			}
			this.setCursor(cursor);
		}catch(Exception ex){
			this.setCursor(cursor);
			JOptionPane.showMessageDialog(this, "Удаленный сервер не ответил", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	
	/** установить на панель загрузки JButton вместо JProgressBar */
	private void setToShowProcessButton(){
		this.panelShowProcess.remove(0);
		JButton buttonLoad=new JButton("Начать загрузку прайс-листа");
		buttonLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonLoadClick();
			}
		});
		this.panelShowProcess.add(buttonLoad);
	}
	
	/** установить на панель загрузки вместо JButton JProgressBar */
	private void setToShowProcessProgressBar(){
		this.panelShowProcess.remove(0);
		JProgressBar progressBar=new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
		this.panelShowProcess.add(progressBar);
	}
	
	/** установить в панель загрузки значение процента загрузки данных 
	 * @param percent - процент, который необходимо отобразить пользователю 
	 * */
	public void setPercentToShowProcessPanel(int percent){
		if(percent==100){
			this.setToShowProcessButton();
			/*
			if(this.processWatcher!=null){
				try{
					this.processWatcher.stop();
				}catch(NullPointerException npe){}; // сборщик может успеть убрать
				this.processWatcher=null;
			}*/
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					JOptionPane.showInternalMessageDialog(PriceLoader.this.parentContainer, "Данные успешно загружены ");					
				}
			});
			
		}else{
			if(this.panelShowProcess.getComponent(0) instanceof JProgressBar){
				// в панели установле JProgressBar
			}else{
				// в панели установлена кнопка - заменить на ProgressBar
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
	
	/** процесс загрузки прайс-листа */
	private void onButtonLoadClick(){
		int returnValue=JOptionPane.showInternalConfirmDialog(this,"Вы уверены в перезаписи","Внимание",JOptionPane.YES_NO_OPTION);
		System.out.println(returnValue);
		if(returnValue==JOptionPane.YES_OPTION){
			boolean serverReturnValue=false;
			Cursor cursor=this.getCursor();
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try{
				this.setCursor(cursor);
				// послать на сервер команду на загрузку прайс-листа
				synchronized(priceLoaderManager){
					serverReturnValue=this.priceLoaderManager.downloadPriceAndWriteIt(adminIdentifier);
				}
				if(serverReturnValue==false){
					JOptionPane.showInternalMessageDialog(this, "Прайс не может быть загружен");
				}else{
					if(this.processWatcher==null){
						this.processWatcher=new ProcessWatcher(this,processWatcherDelay, priceLoaderManager);
					}
					this.setToShowProcessProgressBar();
				}
			}catch(Exception ex){
				this.setCursor(cursor);
				JOptionPane.showInternalMessageDialog(this, "Сервер не ответил","Ошибка", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/** нажата кнопка по установке полного пути для загрузки прайс-листа */
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
				JOptionPane.showInternalMessageDialog(this, "URL успешно установлен");
			}else{
				this.setCursor(cursor);
				JOptionPane.showInternalMessageDialog(this, setResult,"Ошибка установки URL",JOptionPane.ERROR_MESSAGE);
			}
		}catch(Exception ex){
			this.setCursor(cursor);
			JOptionPane.showInternalMessageDialog(this, "Удаленный сервер не ответил", "Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
	/** отправить запрос на сервер о проценте состояния загрузки прайс-листа */
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
