package computer_shop.jobber.view.choice_currency;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.view.JobberOrder;
import computer_shop.jobber.view.common.AbstractIntrnalFrame;
import computer_shop.jobber.view.common.IParent;

public class ChoiceCurrency extends AbstractIntrnalFrame{
	private final static long serialVersionUID=1L;
	/** уникальный идентификатор клиента */
	private JobberIdentifier jobberIdentifier;
	/** в качестве валюты - USD */
	private JRadioButton radioUsd;
	/** в качестве валюты - гривна */
	private JRadioButton radioHrn;
	/** удаленный загрузчик курса валют */
	private CurrencyValue currencyValue;
	
	public ChoiceCurrency( IParent parent, 
						  JDesktopPane desktop,
						  JobberIdentifier jobberIdentifier,
						  CurrencyValue currencyValue) {
		super("Выбор валюты", false, true, false, parent, desktop, 200, 100);
		this.jobberIdentifier=jobberIdentifier;
		this.currencyValue=currencyValue;
		initComponents();
	}

	private void initComponents(){
		String addString=" (???) ";
		try{
			addString=" ("+this.currencyValue.getCurrencyValue()+")";
		}catch(Exception ex){};
		radioUsd=new JRadioButton("USD",true);
		radioHrn=new JRadioButton("Грн."+addString);
		ButtonGroup group=new ButtonGroup();
		group.add(radioUsd);
		group.add(radioHrn);
		
		JPanel panelManager=new JPanel(new BorderLayout());
		JButton buttonOk=new JButton("Заказ");
		buttonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonOk();
			}
		});
		JButton buttonCancel=new JButton("Отмена");
		buttonCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonCancel();
			}
		});
		panelManager.add(buttonOk, BorderLayout.CENTER);
		panelManager.add(buttonCancel,BorderLayout.EAST);
		
		JPanel panelMain=new JPanel(new GridLayout(3,1));
		panelMain.add(radioUsd);
		panelMain.add(radioHrn);
		panelMain.add(panelManager);
		this.getContentPane().add(panelMain);
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		// child window was closed
		this.setVisible(true);
	}

	private void onButtonOk(){
		this.getDesktopPane().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new JobberOrder(this.jobberIdentifier,"Добро пожаловать : "+jobberIdentifier.getName(),this,this.desktop,this.isCurrency());	
	}
	
	/** выбран ли в качестве валюты USD*/
	private boolean isCurrency(){
		return this.radioUsd.isSelected();
	}
	
	private void onButtonCancel(){
		this.internalFrameClosed(null);
	}
}
