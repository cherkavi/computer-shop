package computer_shop.jobber.view.order.list_view;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import computer_shop.jobber.common_objects.RowElement;

/** панель для получения кол-ва товара по RowElement */
public class PanelQuantity extends JPanel{
	
	private final static long serialVersionUID=1L;
	private RowElement element;
	private JSpinner spinner;
	private JDialog dialogParent; 
	/** 
	 * панель для получения кол-ва товара 
	 * @param element - элемент, который необходимо отобразить  
	 * */
	public PanelQuantity(RowElement element){
		this.element=element;
		initComponents(null);
	}
	
	/** 
	 * панель для получения кол-ва товара 
	 * @param element - элемент, который необходимо отобразить
	 * @param dialog - диалоговое окно данного элемента   
	 * */
	public PanelQuantity(RowElement element, JDialog dialog){
		this.element=element;
		this.dialogParent=dialog;
		initComponents(null);
	}
	
	/** 
	 * панель для получения кол-ва товара 
	 * @param element - элемент, который необходимо отобразить
	 * @param dialog - диалоговое окно данного элемента   
	 * @param quantity - кол-во элементов 
	 * */
	public PanelQuantity(RowElement element, JDialog dialog,Integer quantity){
		this.element=element;
		this.dialogParent=dialog;
		initComponents(quantity);
	}
	
	
	private final static DecimalFormat priceFormat=new DecimalFormat("#.00");
	private final static DecimalFormat warrantyFormat=new DecimalFormat("#");
	
	private void initComponents(Integer quantity){
		// create components
		spinner=new JSpinner(new SpinnerNumberModel(1,1,100,1));
		if(quantity!=null){
			spinner.setValue(quantity);
		};
		JPanel panelSpinner=this.getTitledPanel(spinner, "");
		
		JButton buttonOk=new JButton("Заказать");
		JButton buttonCancel=new JButton("Отменить");

		JTextField fieldName=new JTextField();
		fieldName.setText(element.getName());
		fieldName.setHorizontalAlignment(SwingConstants.LEFT);
		fieldName.setEditable(false);
		fieldName.setBorder(javax.swing.BorderFactory.createTitledBorder("Наименование:"));
		
		JTextField fieldPrice=new JTextField();
		fieldPrice.setText(priceFormat.format(element.getPrice()));
		fieldPrice.setEditable(false);
		fieldPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		fieldPrice.setBorder(javax.swing.BorderFactory.createTitledBorder("Цена:"));
		
		JTextField fieldWarranty=new JTextField();
		fieldWarranty.setText(warrantyFormat.format(element.getWarranty()));
		fieldWarranty.setEditable(false);
		fieldWarranty.setHorizontalAlignment(SwingConstants.CENTER);
		fieldWarranty.setBorder(javax.swing.BorderFactory.createTitledBorder("Гарантия:"));
		
		// add actionListener
		buttonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonOk();
			}
		});
		buttonCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonCancel();
			}
		});
		// placing component's
		JPanel panelManager=new JPanel(new BorderLayout());
		panelManager.add(buttonOk,BorderLayout.CENTER);
		panelManager.add(buttonCancel,BorderLayout.EAST);
		
		GroupLayout groupLayout=new GroupLayout(this);
		this.setLayout(groupLayout);
		GroupLayout.SequentialGroup groupLayoutHorizontal=groupLayout.createSequentialGroup();
		GroupLayout.SequentialGroup groupLayoutVertical=groupLayout.createSequentialGroup();
		groupLayout.setHorizontalGroup(groupLayoutHorizontal);
		groupLayout.setVerticalGroup(groupLayoutVertical);

		groupLayoutHorizontal.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							 .addComponent(fieldName)
							 .addGroup(groupLayout.createSequentialGroup()
									   .addComponent(fieldPrice)
									   .addGap(10)
									   .addComponent(fieldWarranty)
									   .addGap(10)
									   .addComponent(panelSpinner)
									   )
							 .addComponent(panelManager)
							 );
		
		groupLayoutVertical
							 .addComponent(fieldName,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
							 .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									   .addComponent(fieldPrice,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
									   .addComponent(fieldWarranty,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
									   .addComponent(panelSpinner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
									   )
							 .addComponent(panelManager,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
	}
	
	private boolean flagButtonOk=false;
	
	private void onButtonOk(){
		this.flagButtonOk=true;
		if(this.dialogParent!=null){
			this.dialogParent.setVisible(false);
		}
	}
	
	private void onButtonCancel(){
		this.flagButtonOk=false;
		if(this.dialogParent!=null){
			this.dialogParent.setVisible(false);
		}
	}
	
	public boolean isButtonOk(){
		return this.flagButtonOk;
	}
	
	public Integer getQuantity(){
		return ((SpinnerNumberModel)this.spinner.getModel()).getNumber().intValue();
	}
	
	private JPanel getTitledPanel(JComponent component, String title){
		JPanel returnValue=new JPanel(new GridLayout(1,1));
		returnValue.add(component);
		returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
		return returnValue;
	}
}
