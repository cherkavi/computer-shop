package computer_shop.jobber.database.connector;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import computer_shop.jobber.database.wrap.*; 

/** �����, ������� ���������� ���������� � ����� ������, ���������� Hibernate � Connection */
public class Connector {
	// INFO - ����� ������������� ���� ������� ��� ��������� ����������� �������� ���� ������ �� ������� ���������
	private Class<?>[] classesOfDatabase=new Class[]{
												   Admin.class,
												   Jobber_orders.class,
												   Jobber.class,
												   List_properties.class,
												   List_section.class,
												   List.class,
												   Orders.class,
												   Properties.class
												   };
	private IConnector connector=null;
	private HibernateConnection hibernateConnection;
	
	public Connector() throws Exception{
		this("computer_shop_resource","technik","technik");
	}
	
	
	/** ���������� ���������� � ����� ������ ����������� �����
	public Connector(File file) throws Exception {
		while(true){
			if(file.exists()==false){
				throw new IOException("file is not exists:"+file.getAbsolutePath());
			}
			// ������� ����������� Firebird 
			if(file.getName().toUpperCase().endsWith(".GDB")){
				System.out.println("Connector created:");
				try{
					this.connector=new FirebirdConnection(file);
				}catch(Exception ex){
					
				}
				hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
				break;
			}
			throw new Exception("algorithm is not found");
		}
	}

	���������� ���������� � ����� ������ ����������� �����
	public Connector(String path) throws Exception {
		System.out.println("Connector created:");
		this.connector=new FirebirdConnection(path);
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
	}*/
	
	/** ���������� ���������� � ����� ������ ����������� �����*/
	private Connector(String path) throws Exception {
		System.out.println("Connector created:");
		/*this.connector=new FirebirdConnection(path);
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
		*/
		this.connector=new MySqlConnection(null,null,path,null,null);
		//org.hibernate.dialect.MySQLDialect
		//org.hibernate.dialect.MySQL5Dialect
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.MySQL5Dialect", classesOfDatabase);
	}

	/** ���������� ���������� � ����� ������ ����������� �����*/
	private Connector(String path, String userName, String password) throws Exception {
		System.out.println("Connector created:");
		/*this.connector=new FirebirdConnection(path);
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.FirebirdDialect",classOfDatabase);
		*/
		this.connector=new MySqlConnection(null,null,path,userName,password);
		//org.hibernate.dialect.MySQLDialect
		//org.hibernate.dialect.MySQL5Dialect
		hibernateConnection=new HibernateConnection("org.hibernate.dialect.MySQL5Dialect", classesOfDatabase);
	}
	
	
	/** �������� Hibernate Session */
	private Session openSession(Connection connection){
		return this.hibernateConnection.openSession(connection);
	}
	
	/** �������� ���������� � ����� ������ */
	private Connection getConnection(){
		return this.connector.getConnection();
	}
	
	public ConnectWrap getConnector(){
		Connection connection=this.getConnection();
		Session session=this.openSession(connection);
		return new ConnectWrap(connection,session);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("-- begin --");
		Connector connector=new Connector("computer_shop_cartridge");
		System.out.println("Connector: "+connector.getConnector());
		System.out.println("-- end --");
		Connection connection=connector.getConnection();
		DatabaseMetaData metaData=connection.getMetaData();
		ResultSet rs=metaData.getClientInfoProperties();
		
		int columnCount=rs.getMetaData().getColumnCount();
		while(rs.next()){
			for(int counter=0;counter<columnCount;counter++){
				System.out.print(counter+" : "+rs.getString(counter));
			}
			System.out.println();
		}
	}
}
