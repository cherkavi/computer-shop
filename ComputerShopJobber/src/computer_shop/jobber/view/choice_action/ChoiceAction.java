package computer_shop.jobber.view.choice_action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import computer_shop.jobber.Main;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.view.choice_currency.ChoiceCurrency;
import computer_shop.jobber.view.choice_currency.CurrencyValue;
import computer_shop.jobber.view.common.AbstractIntrnalFrame;
import computer_shop.jobber.view.common.IParent;
import computer_shop.jobber.view.history.History;
import computer_shop.jobber.view.history.NetHistoryLoader;

public class ChoiceAction extends AbstractIntrnalFrame{
	private static final long serialVersionUID = 1L;
	private JobberIdentifier jobberIdentifier;
	private CurrencyValue currencyValue;
	
	public ChoiceAction(IParent parent, 
						JDesktopPane desktop, 
						JobberIdentifier jobberIdentifier,
						CurrencyValue currencyValue) {
		super("Выбор действия ", false, true, false, parent, desktop, 200, 125);
		this.jobberIdentifier=jobberIdentifier;
		this.currencyValue=currencyValue;
		initComponents();
	}
	
	private void initComponents(){
		// create element's
		JButton buttonOrder=new JButton("Заказ");
		JButton buttonHistory=new JButton("История заказов");
		// add action listener's
		buttonOrder.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonOrder();
			}
		});
		buttonHistory.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonHistory();
			}
		});
		// placing component's
		JPanel panelMain=new JPanel();
		GroupLayout groupLayout=new GroupLayout(panelMain);
		panelMain.setLayout(groupLayout);
		GroupLayout.SequentialGroup groupLayoutHorizontal=groupLayout.createSequentialGroup();
		GroupLayout.SequentialGroup groupLayoutVertical=groupLayout.createSequentialGroup();
		groupLayout.setHorizontalGroup(groupLayoutHorizontal);
		groupLayout.setVerticalGroup(groupLayoutVertical);
		
		int width=170;
		groupLayoutHorizontal.addGap(10);
		groupLayoutHorizontal.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									   .addGap(10)
									   .addComponent(buttonOrder,width,width,width)
									   .addGap(5)
									   .addComponent(buttonHistory,width,width,width)
									   );
		groupLayoutVertical.addGap(15);
		groupLayoutVertical.addComponent(buttonOrder);
		groupLayoutVertical.addGap(10);
		groupLayoutVertical.addComponent(buttonHistory);
		this.getContentPane().add(panelMain);
	}

	private void onButtonOrder(){
		new ChoiceCurrency(this,this.getDesktopPane(),jobberIdentifier,currencyValue);
	}
	
	private void onButtonHistory(){
		new History("Добро пожаловать : "+jobberIdentifier.getName(),this,this.getDesktopPane(),jobberIdentifier,new NetHistoryLoader(Main.URL));
	}

	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

}
