package computer_shop.jobber.admin_remote_implementation;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import computer_shop.jobber.admin_remote_interface.PriceLoaderManager;
import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.IConnectWrapAware;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.database.wrap.Admin;
import computer_shop.jobber.database.wrap.Properties;
import computer_shop.jobber.price_loader.PriceLoader;

/** класс, которые реализует интерфейс {@link PriceLoaderManager} */
public class DatabaseAdminPriceLoaderManager implements PriceLoaderManager, IConnectWrapAware{
	// обязательный пустой конструктор 
	public DatabaseAdminPriceLoaderManager(){
	}
	
	@Override
	public boolean downloadPriceAndWriteIt(AdminIdentifier adminIdentifier) throws Exception {
		if(adminIdentifier!=null){
			boolean returnValue=false;
			ConnectWrap connector=StaticConnector.getConnectWrap();
			try{
				Connection connection=connector.getConnection();
				Session session=connector.getSession();
				if(isAdminIdentifier(adminIdentifier, session)){
					// код администратора найден и опознан
					String pathToXls=loadPriceFromUrlToLocal(session);
						// дать команду на загрузку прайса
					PriceLoader loader=new PriceLoader();
					loader.clearTable(connection, true);
					loader.writeListPropertiesToDatabaseFromXls(session, pathToXls, 6, 10);
					// запуск потока, который будет наполнять базу данных данными 
					new ThreadPriceLoader(this, loader,pathToXls,20); 
					returnValue=true;
				}else{
					try{
						session.close();
					}catch(Exception ex2){};
					try{
						connection.close();
					}catch(Exception ex2){};
					// код администратора не найден и не опознан 
					throw new AdminDetectException("DatabaseAdminPriceLoaderManager#getPriceLoadUrl");
				}
			}catch(Exception ex){
				try{
					connector.close();
				}catch(Exception ex2){};
				if(ex instanceof AdminDetectException){
					throw new Exception("Admin is not recognized ");
				}else{
					System.err.println("DatabaseAdminPriceLoaderManager#getPriceLoadUrl: "+ex.getMessage());
				}
			}
			return returnValue;
		}else{
			throw new Exception("");
		}
	}

	private static final SimpleDateFormat timeStamp=new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
	
	/** скачать файл из указанного места в сети и сохранить файл (Microsoft Excel ) 
	 * 
	 * */
	private String loadFromUrlAndGetName(String pathToUrl, String localRepository) throws Exception{
		String destinationName=localRepository+timeStamp.format(new Date())+".xls";
		URL url=new URL(pathToUrl);
		URLConnection connection=url.openConnection();
		InputStream input=null;
		OutputStream output=null;
		int bufferLength=1024;
		byte[] buffer=new byte[bufferLength];
		int readedByteCount=0;
		try{
			input=connection.getInputStream();
			output=new FileOutputStream(new File(destinationName));
			while( (readedByteCount=input.read(buffer))>=0){
				output.write(buffer,0,readedByteCount);
			}
			output.flush();
		}finally{
			try{
				input.close();
			}catch(Exception ex){};
			try{
				output.close();
			}catch(Exception ex){};
		}
		//System.out.println("price destination: "+destinationName);
		return destinationName;
	}
	
	
	private String loadPriceFromUrlToLocal(Session session) throws Exception{
		String remoteUrl=((Properties)session.createCriteria(Properties.class).add(Restrictions.eq("name","PRICE.URL")).uniqueResult()).getValue();
		String tempLocation=((Properties)session.createCriteria(Properties.class).add(Restrictions.eq("name","PRICE.REPOSITORY")).uniqueResult()).getValue();
		//System.out.println("remoteUrl: "+remoteUrl+"    tempLocation:"+tempLocation);
		return this.loadFromUrlAndGetName(remoteUrl, tempLocation); 
	}

	@Override
	public int getPercentLoad() throws Exception {
		int returnValue=0;
		// получить из последней записи LIST_PROPERTIES.LOAD_PERCENT
		ConnectWrap connector=StaticConnector.getConnectWrap();
		//Session session=null;
		try{
			Connection connection=connector.getConnection();
			ResultSet rs=connection.createStatement().executeQuery("select * from j_list_properties order by kod desc limit 1");
			if(rs.next()){
				returnValue=rs.getInt("LOAD_PERCENT");
			}else{
				returnValue=100;
			}
			rs.close();
		}catch(Exception ex){
			System.err.println("DatabaseAdminPriceLoaderManager#getPercentLoad: "+ex.getMessage());
		}finally{
			/*
			try{
				session.close();
			}catch(Exception ex){};
			*/
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	/** проверить уникальный идентификатор администратора на валидность, то есть есть ли такой в базе данных */
	private boolean isAdminIdentifier(AdminIdentifier adminIdentifier, Session session){
		boolean returnValue=false;
		try{
			Object adminObject=session.createCriteria(Admin.class)
				   .add(Restrictions.eq("login", adminIdentifier.getLogin()))
				   .add(Restrictions.eq("password", adminIdentifier.getPassword())).uniqueResult();
			if(adminObject!=null){
				// объект найден 
				returnValue=true;
			}else{
				// объект не найден - ошибка регистрации 
				returnValue=false;
			}
					               
		}catch(Exception ex){
			System.err.println("DatabaseAdminPriceLoaderManager#isAdminIdentifier: "+ex.getMessage());
		}
		return returnValue;
	}
	
	@Override
	public String getPriceLoadUrl(AdminIdentifier adminIdentifier) throws Exception {
		if(adminIdentifier!=null){
			String returnValue=null;
			ConnectWrap connector=StaticConnector.getConnectWrap();
			try{
				//Connection connection=connector.getConnection();
				Session session=connector.getSession();
				if(isAdminIdentifier(adminIdentifier, session)){
					// код администратора найден и опознан
					Properties properties=(Properties)session.createCriteria(Properties.class).add(Restrictions.eq("name", "PRICE.URL")).uniqueResult();
					returnValue=properties.getValue();
				}else{
					// код администратора не найден и не опознан 
					throw new AdminDetectException("DatabaseAdminPriceLoaderManager#getPriceLoadUrl");
				}
			}catch(Exception ex){
				if(ex instanceof AdminDetectException){
					throw new Exception("Admin is not recognized ");
				}else{
					System.err.println("DatabaseAdminPriceLoaderManager#getPriceLoadUrl: "+ex.getMessage());
				}
			}finally{
				try{
					connector.close();
				}catch(Exception ex){};
			}
			return returnValue;
		}else{
			throw new Exception("");
		}
	}

	@Override
	public String setPriceLoadUrl(AdminIdentifier adminIdentifier, String url)
			throws Exception {
		if(adminIdentifier!=null){
			String returnValue="set error";
			ConnectWrap connector=StaticConnector.getConnectWrap();
			try{
				//Connection connection=connector.getConnection();
				Session session=connector.getSession();
				if(isAdminIdentifier(adminIdentifier, session)){
					// код администратора найден и опознан
					Properties properties=(Properties)session.createCriteria(Properties.class).add(Restrictions.eq("name", "PRICE.URL")).uniqueResult();
					returnValue=properties.getValue();
					// сохранить значение 
					session.beginTransaction();
					properties.setValue(url);
					session.update(properties);
					session.getTransaction().commit();
					returnValue=null;
				}else{
					// код администратора не найден и не опознан 
					throw new AdminDetectException("DatabaseAdminPriceLoaderManager#getPriceLoadUrl");
				}
			}catch(Exception ex){
				if(ex instanceof AdminDetectException){
					throw new Exception("Admin is not recognized ");
				}else{
					System.err.println("DatabaseAdminPriceLoaderManager#getPriceLoadUrl: "+ex.getMessage());
				}
			}finally{
				try{
					connector.close();
				}catch(Exception ex){};
			}
			return returnValue;
		}else{
			throw new Exception("");
		}
	}

	@Override
	public ConnectWrap getConnectWrap() {
		return StaticConnector.getConnectWrap();
	}

}
