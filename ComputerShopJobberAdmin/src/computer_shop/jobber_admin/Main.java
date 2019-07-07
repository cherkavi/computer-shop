package computer_shop.jobber_admin;
import javax.swing.JDesktopPane;


import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import computer_shop.jobber_admin.view.common.IParent;
import computer_shop.jobber_admin.view.login.Login;
import computer_shop.jobber_admin.view.login.NetLoginValidator;

public class Main extends JFrame implements IParent{
	private final static long serialVersionUID=1L;
	private JDesktopPane desktop;
	public static String URL="";
	public Main(String pathToHtml){
		super("Админ ресурс");
		initComponents();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
		URL=pathToHtml;
		// give managment to another window	
		new Login(new NetLoginValidator(Main.URL),"Вход для администратора",this,this.desktop); 
	}
	
	/** инициализация компонентов */
	private void initComponents(){
		// create desktop
		this.desktop=new JDesktopPane();
		this.getContentPane().add(desktop);
	}
	
	public static void main(String[] args){
		if(args.length==0){
			new Main("http://127.0.0.1:8080/ComputerShopJobberResource/");
			//new Main("http://193.201.80.186");
		}else{
			new Main(args[0]);
		}
	}


	@Override
	public void windowWasClosed(JInternalFrame source) {
		this.dispose();
	}


	@Override
	public void selfSetVisible(boolean isVisible) {
		// nothing
	}
}
