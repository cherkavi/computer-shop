package computer_shop.jobber.view.order.section_tree;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

import database.Connector;

public class DatabaseSectionLoader implements SectionLoader{
	private Connection connection;

	public DatabaseSectionLoader(){
		this.connection=Connector.getConnection();
	}

	@Override
	public void finalize(){
		try{
			this.connection.close();
		}catch(Exception ex){
			System.err.println("DatabaseSectionLoader Exception: "+ex.getMessage());
		}
	}

	
	
	@Override
	public boolean elementHasChild(TreePath path) {
		boolean returnValue=false;
		ResultSet rs=null;
		try{
			if(path.getPathCount()>=2){
				String lastElement=path.getPathComponent(path.getPathCount()-1).toString();
				PreparedStatement statement=connection.prepareStatement("select * from list_section where kod_parent=(select first 1 skip 0 kod from list_section where list_section.name=?)");
				statement.setString(1, lastElement);
				rs=statement.executeQuery();
				if(rs.next()){
					returnValue=true;
				}
			}
		}catch(Exception ex){
			System.err.println("DatabaseSectionLoader#getSubElements: Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	
	@Override
	public String[] getSubElements(TreePath path) {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		try{
			if(path.getPathCount()>=2){
				String lastElement=path.getPathComponent(path.getPathCount()-1).toString();
				PreparedStatement statement=connection.prepareStatement("select * from list_section where kod_parent=(select first 1 skip 0 kod from list_section where list_section.name=?)");
				statement.setString(1, lastElement);
				rs=statement.executeQuery();
				while(rs.next()){
					returnValue.add(rs.getString("NAME"));
				}
			}
		}catch(Exception ex){
			System.err.println("DatabaseSectionLoader#getSubElements: Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new String[]{});
	}

	@Override
	public String[] getSubRootElements() {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select * from list_section where list_section.kod_parent is null");
			while(rs.next()){
				returnValue.add(rs.getString("NAME"));
			}
		}catch(Exception ex){
			System.out.println("DatabaseSectionLoader#getSubRootElements Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new String[]{});
	}

	@Override
	public boolean elementHasChild(String lastElement) {
		boolean returnValue=false;
		ResultSet rs=null;
		try{
			PreparedStatement statement=connection.prepareStatement("select * from list_section where kod_parent=(select first 1 skip 0 kod from list_section where list_section.name=?)");
			statement.setString(1, lastElement);
			rs=statement.executeQuery();
			if(rs.next()){
				returnValue=true;
			}
		}catch(Exception ex){
			System.err.println("DatabaseSectionLoader#getSubElements: Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public String[] getSubElements(String lastElement) {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		try{
			PreparedStatement statement=connection.prepareStatement("select * from list_section where kod_parent=(select first 1 skip 0 kod from list_section where list_section.name=?)");
			statement.setString(1, lastElement);
			rs=statement.executeQuery();
			while(rs.next()){
				returnValue.add(rs.getString("NAME"));
			}
		}catch(Exception ex){
			System.err.println("DatabaseSectionLoader#getSubElements: Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new String[]{});
	}
	
}
