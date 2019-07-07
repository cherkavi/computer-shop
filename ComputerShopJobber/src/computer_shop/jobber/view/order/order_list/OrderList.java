package computer_shop.jobber.view.order.order_list;
import java.awt.BorderLayout;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import computer_shop.jobber.Main;
import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.common_objects.RowElement;
import computer_shop.jobber.view.order.list_view.PanelQuantity;


public class OrderList extends JPanel implements IAmountChangeListener,IAddRowElement{
	private final static long serialVersionUID=1L;
	/** главная таблица с заказами */
	private TableOrder table;
	private JTextField fieldAmount;
	private JTextField fieldQuantity;
	private JobberIdentifier identifier;
	private JPopupMenu popupMenu;
	private boolean isCurrency=false;
	
	public OrderList(JobberIdentifier identifier,boolean isCurrency){
		this.identifier=identifier;
		this.isCurrency=isCurrency;
		initComponents();
	}
	
	private void initComponents(){
		// create element's
		table=new TableOrder(new NetOrderListSave(Main.URL),this.isCurrency);
		table.addAmountChangeListener(this);
		JButton buttonRemove=new JButton("удалить");
		JButton buttonEdit=new JButton("редактировать");
		JButton buttonSend=new JButton("Заказать");
		this.fieldAmount=new JTextField();
		this.fieldAmount.setEditable(false);
		this.fieldQuantity=new JTextField();
		this.fieldQuantity.setEditable(false);
		this.popupMenu=new JPopupMenu();
		JMenuItem menuEdit=new JMenuItem("Редактировать");
		JMenuItem menuRemove=new JMenuItem("Удалить");
		this.popupMenu.add(menuEdit);
		this.popupMenu.addSeparator();
		this.popupMenu.add(menuRemove);
		this.popupMenu.addSeparator();
		// add listener's
		menuEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonEdit();
			}
		});
		menuRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonRemove();
			}
		});
		
		buttonRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonRemove();
			}
		});
		buttonEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonEdit();
			}
		});
		buttonSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonSend();
			}
		});
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
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
			public void mouseReleased(MouseEvent event) {
				if((event.getClickCount()>1)&&(event.getButton()==MouseEvent.BUTTON1)){
					onButtonEdit();
				}
				if(event.getButton()==MouseEvent.BUTTON3){
					showPopupMenu(event);
				}
			}
		});
		// placing 
		JPanel panelInformation=new JPanel(new GridLayout(1,3));
		panelInformation.add(this.getTitledPanel(this.fieldQuantity, "Кол-во"));
		panelInformation.add(this.getTitledPanel(this.fieldAmount, "Сумма"+((this.isCurrency==true)?" (USD) ":" (грн.)")));
		panelInformation.add(buttonSend);
		
		JPanel panelEdit=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelEdit.add(buttonEdit);
		panelEdit.add(new JLabel("      "));
		panelEdit.add(buttonRemove);

		JPanel panelManager=new JPanel(new GridLayout(2,1));
		panelManager.add(panelEdit);
		panelManager.add(panelInformation);
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table),BorderLayout.CENTER);
		this.add(panelManager, BorderLayout.SOUTH);
	}
	
	private final static DecimalFormat amountFormat=new DecimalFormat("#.00");
	private final static DecimalFormat quantityFormat=new DecimalFormat("#");
	
	@Override
	public void amountChanged(float amount, int quantity) {
		this.fieldAmount.setText(amountFormat.format(amount));
		this.fieldQuantity.setText(quantityFormat.format(quantity));
	}
	
	private JPanel getTitledPanel(JComponent component, String title){
		JPanel returnValue=new JPanel(new GridLayout(1,1));
		returnValue.add(component);
		returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
		return returnValue;
	}

	@Override
	public void addRowElement(RowElement element, int quantity) {
		this.table.addRowElement(element, quantity);
	}

	/** reaction on striking button Remove*/
	private void onButtonRemove(){
		OrderTableModelRow element=this.table.getSelectedObject();
		if(element!=null){
			JDialog dialog=new JDialog();
			PanelConfirm panelConfirm=new PanelConfirm(dialog,"<html><center>Уверены в удалении <br>"+element.getElement().getName()+"<br>?</center></html>", "Да", "Нет");
			dialog.getContentPane().add(panelConfirm);
			dialog.setModal(true);
			dialog.setSize(300,150);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setLocationRelativeTo(this);
			dialog.setResizable(false);
			dialog.setVisible(true);
			if(panelConfirm.isButtonOk()){
				// remove
				this.table.removeElementFromModel(element);
			}else{
				// cancel
			}
		}else{
			// Выбор не сделан
		}
	}
	/** reaction on striking button Remove*/	
	private void onButtonEdit(){
		OrderTableModelRow element=this.table.getSelectedObject();
		if(element!=null){
			JDialog dialog=new JDialog();
			PanelQuantity panelQuantity=new PanelQuantity(element.getElement(),dialog,element.getQuantity());
			dialog.getContentPane().setLayout(new GridLayout(1,1));
			dialog.getContentPane().add(panelQuantity);
			dialog.setModal(true);
			dialog.setSize(300,135);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setLocationRelativeTo(this);
			dialog.setResizable(false);
			dialog.setVisible(true);
			if(panelQuantity.isButtonOk()){
				element.setQuantity(panelQuantity.getQuantity());
				this.table.replaceElement(element);
			}else{
				//System.out.println("Cancel");
			}
		}else{
			// user not select element
		}
	}
	
	/** reaction on striking button Remove*/	
	private void onButtonSend(){
		//System.out.println("Send ");
		float currentAmount=0;
		try{
			currentAmount=Float.parseFloat(this.fieldAmount.getText().replace(',', '.'));
		}catch(Exception ex){
			//System.out.println("OrderList#onButtonSend Exception:"+ex.getMessage());
		};
		if(currentAmount!=0){
			JDialog dialog=new JDialog();
			PanelConfirm panelConfirm=new PanelConfirm(dialog,"<html><center>Заказ на общую сумму <br>"+amountFormat.format(this.table.getOrderAmount())+((this.isCurrency==true)?"$":"грн.")+"<br>будет отправлен</center></html>", "Заказать", "Отмена");
			dialog.getContentPane().add(panelConfirm);
			dialog.setModal(true);
			dialog.setSize(300,150);
			dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			dialog.setLocationRelativeTo(this);
			dialog.setResizable(false);
			dialog.setVisible(true);
			if(panelConfirm.isButtonOk()){
				// remove
				Cursor currentCursor=this.getCursor();
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				String returnValue=null;
				try{
					returnValue=this.table.saveOrderList(this.identifier);
					this.setCursor(currentCursor);
				}catch(Exception ex){
					this.setCursor(currentCursor);
					//System.err.println(ex.getMessage());
				}
				if(returnValue!=null){
					JOptionPane.showMessageDialog(this, "Код Вашего заказа:"+returnValue,"Данные отправлены",JOptionPane.INFORMATION_MESSAGE);
					this.table.clearAllData();
				}else{
					JOptionPane.showMessageDialog(this, "Данные не отправлены","Ошибка ",JOptionPane.ERROR_MESSAGE);
				}
			}else{
				// cancel
			}
		}
	}
	
	private void showPopupMenu(MouseEvent event){
		this.popupMenu.show(this.table, event.getX(), event.getY());
	}
	
}
