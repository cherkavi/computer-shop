package computer_shop.jobber.price_loader;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.hibernate.Session;

import computer_shop.jobber.database.connector.*;

public class Test extends JFrame{
	private final static long serialVersionUID=1L;
	
	public static void main(String[] args){
		new Test();
	}
	
	/** путь к файлу XLS */
	private String pathToXls;
	public Test(){
		super("Тестовый вариант загрузки данных в базу данных ");
		this.pathToXls="V:\\eclipse_workspace\\ComputerShopJobberResource\\Information\\Price\\price.xls";
		initComponents();
		this.setSize(200,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/** инициализация компонентов */
	private void initComponents(){
		// create component's
		JButton buttonExecute=new JButton("Execute");
		
		// add listener's
		buttonExecute.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonExecute();
			}
		});
		
		// placing component's
		JPanel panelMain=new JPanel(new FlowLayout());
		panelMain.add(buttonExecute);
		this.getContentPane().add(panelMain);
	}
	
	private void onButtonExecute(){
		System.out.println("begin");
		ConnectWrap connectWrap=null;
		try{
			Connector connector=new Connector();
			connectWrap=connector.getConnector();
			//Connector connector=new Connector("V:\\eclipse_workspace\\ComputerShopJobberResource\\Database\\computershopjobberresource.gdb","SYSDBA","masterkey",20);
			Connection connection=connectWrap.getConnection();
			Session session=connectWrap.getSession();
			PriceLoader loader=new PriceLoader();
			loader.clearTable(connection, true);
			loader.writeListPropertiesToDatabaseFromXls(session, pathToXls, 6, 10);
			loader.writeListToDatabaseFromXls(session, pathToXls, 20);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "Error\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}finally{
			try{
				connectWrap.close();
			}catch(Exception ex){};
		}
		System.out.println("end");
	}
	
	
}
