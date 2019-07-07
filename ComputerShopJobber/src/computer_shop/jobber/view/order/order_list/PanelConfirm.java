package computer_shop.jobber.view.order.order_list;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelConfirm extends JPanel{
	private final static long serialVersionUID=1L;
	private JDialog dialog;
	
	/** Панель запроса */
	public PanelConfirm(JDialog dialog, 
						String message, 
						String buttonOkCaption, 
						String buttonCancelCaption){
		this.dialog=dialog;
		initComponents(message,buttonOkCaption, buttonCancelCaption);
	}
	
	private void initComponents(String message,String buttonOkCaption, String buttonCancelCaption){
		// create element's
		JLabel labelMessage=new JLabel(message,SwingConstants.CENTER);
		JButton buttonOk=new JButton(buttonOkCaption);
		JButton buttonCancel=new JButton(buttonCancelCaption);
		// add listener's
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
		// placing 
		this.setLayout(new BorderLayout());
		JPanel panelManager=new JPanel(new BorderLayout());
		panelManager.add(buttonOk,BorderLayout.CENTER);
		panelManager.add(buttonCancel, BorderLayout.EAST);
		this.add(labelMessage, BorderLayout.CENTER);
		this.add(panelManager,BorderLayout.SOUTH);
	}
	
	private boolean flagIsOk=false;
	
	private void onButtonOk(){
		this.flagIsOk=true;
		if(this.dialog!=null){
			this.dialog.setVisible(false);
		}
	}
	private void onButtonCancel(){
		this.flagIsOk=false;
		if(this.dialog!=null){
			this.dialog.setVisible(false);
		}
	}
	
	/** была ли нажата кнопка OK */
	public boolean isButtonOk(){
		return this.flagIsOk;
	}
}
