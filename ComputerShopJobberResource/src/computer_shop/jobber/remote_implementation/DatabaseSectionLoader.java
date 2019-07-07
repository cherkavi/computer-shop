package computer_shop.jobber.remote_implementation;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.tree.TreePath;

import computer_shop.jobber.common_objects.StringArrayElement;
import computer_shop.jobber.database.connector.ConnectWrap;
import computer_shop.jobber.database.connector.StaticConnector;
import computer_shop.jobber.remote_interface.SectionLoader;

public class DatabaseSectionLoader implements SectionLoader{
	public DatabaseSectionLoader(){
		
	}

	
	
	@Override
	public boolean elementHasChild(TreePath path) {
		boolean returnValue=false;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			// Session session=connector.getSession();
			if(path.getPathCount()>=2){
				String lastElement=path.getPathComponent(path.getPathCount()-1).toString();
				PreparedStatement statement=connection.prepareStatement("select * from j_list_section where kod_parent=(select  kod from j_list_section where j_list_section.name=? limit 1)");
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
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	
	@Override
	public String[] getSubElements(TreePath path) {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			//Session session=connector.getSession();
			if(path.getPathCount()>=2){
				String lastElement=path.getPathComponent(path.getPathCount()-1).toString();
				PreparedStatement statement=connection.prepareStatement("select * from j_list_section where kod_parent=(select kod from j_list_section where j_list_section.name=? limit 1)");
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
			try{
				connector.close();
			}catch(Exception ex){};
			
		}
		return returnValue.toArray(new String[]{});
	}

	@Override
	public String[] getSubRootElements() {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			// Session session=connector.getSession();
			rs=connection.createStatement().executeQuery("select * from j_list_section where j_list_section.kod_parent is null");
			while(rs.next()){
				returnValue.add(rs.getString("NAME"));
			}
		}catch(Exception ex){
			System.out.println("DatabaseSectionLoader#getSubRootElements Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new String[]{});
	}

	@Override
	public boolean elementHasChild(String lastElement) {
		boolean returnValue=false;
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			// Session session=connector.getSession();
			PreparedStatement statement=connection.prepareStatement("select * from j_list_section where kod_parent=(select kod from j_list_section where j_list_section.name=? limit 1)");
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
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	@Override
	public String[] getSubElements(String lastElement) {
		ArrayList<String> returnValue=new ArrayList<String>();
		ResultSet rs=null;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			// Session session=connector.getSession();
			PreparedStatement statement=connection.prepareStatement("select * from j_list_section where kod_parent=(select kod from j_list_section where j_list_section.name=? limit 1)");
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
			try{
				connector.close();
			}catch(Exception ex){};
		}
		return returnValue.toArray(new String[]{});
	}



	@Override
	public StringArrayElement getTreeRootNode() {
		return this.fillAllTreeBenchFromLoader();
	}
	
	private StringArrayElement fillAllTreeBenchFromLoader(){
		StringArrayElement returnValue=new StringArrayElement("root");
		String[] rootElements=this.getSubRootElements();
		ArrayList<StringArrayElement> elements=new ArrayList<StringArrayElement>();
		for(int counter=0;counter<rootElements.length;counter++){
			//System.out.println("SubRoot element:"+rootElements[counter]);
			elements.add(getAllBenchFromSubRootElement(rootElements[counter]));
		}
		returnValue.setElements(elements.toArray(new StringArrayElement[]{}));
		return returnValue;
	}
	
	/**  
	 * по имени под-корневого элемента получить полностью всю ветку с полным кол-вом вложений
	 * */
	private StringArrayElement getAllBenchFromSubRootElement(String string) {
		StringArrayElement returnValue=new StringArrayElement(string);
		String[] elements=this.getSubElements(string);
		if(elements!=null){
			// данный элемент содержит дочерние элементы - читаем эти элементы
			ArrayList<StringArrayElement> subList=new ArrayList<StringArrayElement>();
			for(int counter=0;counter<elements.length;counter++){
				subList.add(getAllBenchFromSubRootElement(elements[counter]));
			}
			returnValue.setElements(subList.toArray(new StringArrayElement[]{}));
		}else{
			// isLeaf - данный элемент является листом
		}
		return returnValue;
	}
	
}
