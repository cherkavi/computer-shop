package computer_shop.jobber.view.login;

import java.awt.BorderLayout;


import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import computer_shop.jobber.common_objects.JobberIdentifier;
import computer_shop.jobber.view.choice_action.ChoiceAction;
import computer_shop.jobber.view.choice_currency.CurrencyValue;
import computer_shop.jobber.view.common.AbstractIntrnalFrame;
import computer_shop.jobber.view.common.IParent;

public class Login extends AbstractIntrnalFrame{
	private static final long serialVersionUID = 1L;
	/** удаленный checker логина и пароля */
	private LoginValidator loginValidator;
	/** удаленный загрузчик курса валют */
	private CurrencyValue currencyValue;

	public Login(LoginValidator loginValidator,
				 CurrencyValue currencyValue,
				 String title, 
				 IParent parent, 
				 JDesktopPane desktop) {
		super(title, false, true, false, parent, desktop, 200, 150);
		this.loginValidator=loginValidator;
		this.currencyValue=currencyValue;
		initComponents();
		this.revalidate();
	}

	private void initComponents(){
		// create element's
		final JLabel labelLogin=new JLabel("Имя пользователя:");
		final JTextField fieldLogin=new JTextField();
		final JLabel labelPassword=new JLabel("Пароль: ");
		final JPasswordField fieldPassword=new JPasswordField();
		JButton buttonOk=new JButton("Вход");
		JButton buttonCancel=new JButton("Отмена");
		// add listener's
		buttonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonOk(fieldLogin,fieldPassword);
			}
		});
		buttonCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonCancel();
			}
		});
		// placing components
		JPanel panelInput=new JPanel(new GridLayout(4,1));
		panelInput.add(labelLogin);
		panelInput.add(fieldLogin);
		panelInput.add(labelPassword);
		panelInput.add(fieldPassword);
		
		JPanel panelManager=new JPanel(new BorderLayout());
		panelManager.add(buttonOk, BorderLayout.CENTER);
		panelManager.add(buttonCancel, BorderLayout.EAST);
		
		JPanel panelMain=new JPanel(new BorderLayout());
		panelMain.add(panelInput,BorderLayout.CENTER);
		panelMain.add(panelManager,BorderLayout.SOUTH);
		panelMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(panelMain);
	}

	private void onButtonOk(JTextField fieldLogin, JTextField fieldPassword){
		//System.out.println("Login:"+fieldLogin.getText()+"    Password: "+fieldPassword.getText());
		JobberIdentifier jobberIdentifier=null;
		Cursor currentCursor=this.getCursor();
		//this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.getDesktopPane().getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try{
			if((jobberIdentifier=checkPassword(fieldLogin.getText(),fieldPassword.getText()))!=null){
				this.getDesktopPane().getParent().setCursor(currentCursor);				
				//this.setCursor(currentCursor);
				// password is valid
				new ChoiceAction(this,this.getDesktopPane(),jobberIdentifier,currencyValue);
			}else{
				this.getDesktopPane().getParent().setCursor(currentCursor);
				// password is not valid
				JOptionPane.showInternalMessageDialog(this, "Логин и/или пароль недействительны","Предупреждение",JOptionPane.WARNING_MESSAGE);
			}
		}catch(Exception ex){
			this.getDesktopPane().getParent().setCursor(currentCursor);
			JOptionPane.showMessageDialog(this, "Удаленный сервер не ответил","Ошибка",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void onButtonCancel(){
		this.internalFrameClosed(null);
	}
	
	@Override
	public void windowWasClosed(JInternalFrame source) {
		source.setVisible(false);
		// child window was closed
		this.setVisible(true);
	}

	
	private JobberIdentifier checkPassword(String login, String password)throws Exception{
		return this.loginValidator.checkPassword(login, password);		
	}

}
