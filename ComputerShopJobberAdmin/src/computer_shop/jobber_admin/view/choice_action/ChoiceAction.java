package computer_shop.jobber_admin.view.choice_action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber_admin.view.common.AbstractInternalFrame;
import computer_shop.jobber_admin.view.common.IParent;
import computer_shop.jobber_admin.view.jobber_edit.JobberView;
import computer_shop.jobber_admin.view.orders_view.OrdersView;
import computer_shop.jobber_admin.view.price_loader.PriceLoader;


public class ChoiceAction extends AbstractInternalFrame{
	private static final long serialVersionUID = 1L;
	private AdminIdentifier adminIdentifier;
	
	public ChoiceAction(IParent parent, 
						JDesktopPane desktop, 
						AdminIdentifier adminIdentifier) {
		super("Выбор действия ", false, true, false, parent, desktop, 250, 165);
		this.adminIdentifier=adminIdentifier;
		initComponents();
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void initComponents(){
		// create element's
		JButton buttonLoadPrice=new JButton("Загрузка прайс-листа");
		JButton buttonShowOrders=new JButton("Полученные заказы");
		JButton buttonEditJobbers=new JButton("Редактирование оптовиков");
		// add action listener's
		buttonLoadPrice.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonLoadPrice();
			}
		});
		buttonShowOrders.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonShowOrders();
			}
		});
		buttonEditJobbers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonShowJobbers();
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
		
		int width=220;
		groupLayoutHorizontal.addGap(10);
		groupLayoutHorizontal.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									   .addGap(10)
									   .addComponent(buttonLoadPrice,width,width,width)
									   .addGap(5)
									   .addComponent(buttonShowOrders,width,width,width)
									   .addGap(5)
									   .addComponent(buttonEditJobbers,width,width,width)
									   );
		groupLayoutVertical.addGap(15);
		groupLayoutVertical.addComponent(buttonLoadPrice);
		groupLayoutVertical.addGap(10);
		groupLayoutVertical.addComponent(buttonShowOrders);
		groupLayoutVertical.addGap(10);
		groupLayoutVertical.addComponent(buttonEditJobbers);
		this.getContentPane().add(panelMain);
	}

	private void onButtonLoadPrice(){
		new PriceLoader(this,this.getDesktopPane(),adminIdentifier);
	}
	
	private void onButtonShowOrders(){
		//new History("Добро пожаловать : "+jobberIdentifier.getName(),this,this.getDesktopPane(),jobberIdentifier,new NetHistoryLoader(Main.URL));
		new OrdersView(this, this.getDesktopPane(),adminIdentifier);
	}

	private void onButtonShowJobbers(){
		//new History("Добро пожаловать : "+jobberIdentifier.getName(),this,this.getDesktopPane(),jobberIdentifier,new NetHistoryLoader(Main.URL));
		new JobberView(adminIdentifier, this, this.getDesktopPane());
	}

	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		this.setVisible(true);
	}

}
