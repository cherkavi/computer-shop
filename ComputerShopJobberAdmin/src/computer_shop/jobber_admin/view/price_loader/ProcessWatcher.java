package computer_shop.jobber_admin.view.price_loader;

import java.util.concurrent.TimeUnit;

/** ������, ������� ������� �� ��������� ������, � ��������� ��������� ������ �� ���������� */
public class ProcessWatcher implements Runnable{
	private PriceLoader priceLoader;
	private PriceLoaderManager priceLoaderManager;
	private int secondsDelay;
	private boolean flagRun=false;
	
	/** ������, ������� ������� �� ��������� ������, � ��������� ��������� ������ �� ���������� 
	 * @param priceLoader - ������, ������� ���������� ���������
	 * @param secondsDelay - 
	 * */
	public ProcessWatcher(PriceLoader priceLoader,int secondsDelay, PriceLoaderManager priceLoaderManager){
		this.priceLoader=priceLoader;
		this.priceLoaderManager=priceLoaderManager;
		this.secondsDelay=secondsDelay;
		this.flagRun=true;
		new Thread(this).start();
	}
	
	public void stop(){
		this.flagRun=false;
	}
	
	public void run(){
		while(this.flagRun==true){
			int percentValue=0;
			synchronized(priceLoaderManager){
				try{
					percentValue=priceLoaderManager.getPercentLoad();
				}catch(Exception ex){
				}
			}
			try{
				//System.out.println("Process Watcher set Value:"+percentValue);
				priceLoader.setPercentToShowProcessPanel(percentValue);
			}catch(Exception ex){
				// NullPointerException 
			};
			

			if(percentValue==100){
				this.flagRun=false;
			}
			try{
				TimeUnit.SECONDS.sleep(this.secondsDelay);
			}catch(Exception ex){
			}
		}
	}
}
