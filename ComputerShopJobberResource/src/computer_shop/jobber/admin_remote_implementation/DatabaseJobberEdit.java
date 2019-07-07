package computer_shop.jobber.admin_remote_implementation;

import java.sql.Connection;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import computer_shop.jobber.admin_remote_interface.IJobberEdit;
import computer_shop.jobber.common_objects.AdminIdentifier;
import computer_shop.jobber.common_objects.JobberElement;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.database.wrap.Admin;
import computer_shop.jobber.database.wrap.Jobber;

public class DatabaseJobberEdit implements IJobberEdit {

	public DatabaseJobberEdit(){
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
	
	/** проверка на повторение логина  - получить кол-во логинов в системе */
	private int getLoginCount(Session session, String login){
		int returnValue=0;
		try{
			List<?> list=session.createCriteria(Jobber.class).add(Restrictions.eq("login", login)).list();
			if((list==null)||(list.size()==0)){
				returnValue=0;
			}else{
				returnValue=list.size();
			}
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit.isLoginExists Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	@Override
	public String add(AdminIdentifier adminIdentifier,
					  JobberElement jobberElement) throws Exception {
		String returnValue=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			//Connection connection=connector.getConnection();
			Session session=connector.getSession();
			if(this.isAdminIdentifier(adminIdentifier, session)){
				// implementation
				if(this.getLoginCount(session, jobberElement.getLogin())!=0){
					returnValue="Такой Login уже существует";
				}else{
					Jobber jobber=new Jobber();
					jobber.setSurname(jobberElement.getSurname());
					jobber.setName(jobberElement.getName());
					jobber.setLogin(jobberElement.getLogin());
					jobber.setPassword(jobberElement.getPassword());
					jobber.setPriceNumber(jobberElement.getPriceNumber());
					session.beginTransaction();
					session.save(jobber);
					session.getTransaction().commit();
					returnValue=null;
				}
			}else{
				throw new AdminDetectException("DatabaseJobberEdit.add");
			}
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit.add Exception: "+ex.getMessage());
			throw new Exception("Ошибка");
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	private JobberElement getJobberElementFromResultSet(ResultSet rs){
		JobberElement element=new JobberElement();
		try{
			element.setKod(rs.getInt("KOD"));
			element.setSurname(rs.getString("SURNAME"));
			element.setName(rs.getString("NAME"));
			element.setLogin(rs.getString("LOGIN"));
			element.setPassword(rs.getString("JOBBER_PASSWORD"));
			int priceNumber=rs.getInt("PRICE_NUMBER");
			priceNumber=(priceNumber==0)?4:priceNumber;
			System.out.println(priceNumber);
			element.setPriceNumber(priceNumber);
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit#getJobberElementFromResultSet: "+ex.getMessage());
		}
		return element;
	}
	
	@Override
	public JobberElement[] getAllJobbers(AdminIdentifier adminIdentifier)
			throws Exception {
		JobberElement[] returnValue=new JobberElement[]{};
		ArrayList<JobberElement> list=new ArrayList<JobberElement>();
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			if(this.isAdminIdentifier(adminIdentifier, session)){
				// implementation
				rs=connection.createStatement().executeQuery("select * from j_jobber order by kod desc");
				while(rs.next()){
					list.add(this.getJobberElementFromResultSet(rs));
				}
				returnValue=list.toArray(returnValue);
			}else{
				throw new AdminDetectException("DatabaseJobberEdit.getAllJobbers");
			}
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit.getAllJobbers Exception: "+ex.getMessage());
			throw new Exception("Ошибка");
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public String remove(AdminIdentifier adminIdentifier,
			JobberElement jobberElement) throws Exception {
		String returnValue=null;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			Session session=connector.getSession();
			if(this.isAdminIdentifier(adminIdentifier, session)){
				// implementation
				if((jobberElement.getKod()!=null)&&(jobberElement.getKod().intValue()!=0)){
					// проверить заказы по данному оптовику, если они есть - удалить 
					rs=connection.createStatement().executeQuery("select * from j_jobber_orders where kod_jobber="+jobberElement.getKod().intValue());
					if(rs.next()){
						returnValue="Оптовик не может быть удален - есть заказы";
					}else{
						connection.createStatement().executeUpdate("delete from j_jobber where j_jobber.kod="+jobberElement.getKod());
						connection.commit();
						returnValue=null;
					}
				}else{
					returnValue="Оптовик не опознан";
				}
			}else{
				throw new AdminDetectException("DatabaseJobberEdit.remove");
			}
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit.remove Exception: "+ex.getMessage());
			throw new Exception("Ошибка");
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public String update(AdminIdentifier adminIdentifier,
			JobberElement jobberElement) throws Exception {
		String returnValue=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			//Connection connection=connector.getConnection();
			Session session=connector.getSession();
			if(this.isAdminIdentifier(adminIdentifier, session)){
				if(this.getLoginCount(session, jobberElement.getLogin())<=1){
					// implementation
					Jobber jobber=(Jobber)session.get(Jobber.class, jobberElement.getKod());
					jobber.setLogin(jobberElement.getLogin());
					jobber.setPassword(jobberElement.getPassword());
					jobber.setSurname(jobberElement.getSurname());
					jobber.setName(jobberElement.getName());
					jobber.setPriceNumber(jobberElement.getPriceNumber());
					session.beginTransaction();
					session.update(jobber);
					session.getTransaction().commit();
				}else{
					returnValue="Такой логин уже существует";
				}
			}else{
				throw new AdminDetectException("DatabaseJobberEdit.update");
			}
		}catch(Exception ex){
			System.err.println("DatabaseJobberEdit.update Exception: "+ex.getMessage());
			throw new Exception("Ошибка");
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

}
