package computer_shop.jobber.admin_remote_implementation;

import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.IConnectWrapAware;
import computer_shop.jobber.price_loader.PriceLoader;

/** �����, ������� �������� ����������� ������ ��� �������� ������ �� Microsoft Excel � Connector*/
public class ThreadPriceLoader implements Runnable{
	private IConnectWrapAware connectorAware;
	private PriceLoader priceLoader;
	private String pathToXls;
	private int xlsPositionBegin;
	
	/** ������� ��������� �����, � ������� ��������� ��� ����������� ������ � ����� 
	 * @param connection - ���������� � ����� ������
	 * @param session - hibernate ���������� � ����� ������
	 * @param priceLoader - ��������� ������ 
	 * @param pathToXls - ���� � Excel ����� 
	 * @param xlsPositionBegin - ������ ������� ��� �������� ������ �� Excel ����� 
	 */
	public ThreadPriceLoader(IConnectWrapAware connectorAware,
							 PriceLoader priceLoader,
							 String pathToXls,
							 int xlsPositionBegin){
		this.connectorAware=connectorAware;
		this.priceLoader=priceLoader;
		this.pathToXls=pathToXls;
		this.xlsPositionBegin=xlsPositionBegin;
		Thread thread=new Thread(this);
		thread.start();
	}
	
	@Override
	public void run(){
		ConnectWrap connector=this.connectorAware.getConnectWrap();
		try{
			priceLoader.writeListToDatabaseFromXls(connector.getSession(), pathToXls, this.xlsPositionBegin);
		}catch(Exception ex){
			System.err.println("ThreadPriceLoader#run Exception: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
	}
}
