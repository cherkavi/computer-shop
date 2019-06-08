package window.catalog.tree.core;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseNodeAttributesAware extends NodeAttributesAware{
	private final static long serialVersionUID=1L;
	private PreparedStatement preparedStatementNull;
	private PreparedStatement preparedStatementId;
	private Connection connection;
	private String fieldId;
	private String fieldIdParent;
	private String fieldName;
	private String fieldChildCount;

	/**
	 * 
	 * @param connection - соединение с базой данных
	 * @param fieldId - имя поля с кодом  
	 * @param fieldIdParent - имя поля с кодом родителя
	 * @param fieldName - поле имени 
	 * @param childCount - любое название, отличное от fieldId, fieldIdParent, fieldName
	 * @param tableName - имя таблицы
	 */
	public DatabaseNodeAttributesAware(Connection connection, String fieldId, String fieldIdParent, String fieldName, String childCount, String tableName){
		this.connection=connection;
		this.fieldId=fieldId;
		this.fieldIdParent=fieldIdParent;
		this.fieldName=fieldName;
		this.fieldChildCount=childCount;
		String queryNull="select "+fieldId+", "+fieldIdParent+", "+fieldName+", (select count(*) from "+tableName+" inner_section where inner_section."+fieldIdParent+"="+tableName+"."+fieldId+") "+fieldChildCount+" from "+tableName+" where "+fieldIdParent+" is null order by "+fieldName;
		String queryId="select "+fieldId+", "+fieldIdParent+", "+fieldName+", (select count(*) from "+tableName+" inner_section where inner_section."+fieldIdParent+"="+tableName+"."+fieldId+") "+fieldChildCount+" from "+tableName+" where "+fieldIdParent+"=? order by "+fieldName;
		try{
			this.preparedStatementId=connection.prepareStatement(queryId);
			this.preparedStatementNull=connection.prepareStatement(queryNull);
		}catch(Exception ex){
			System.err.println("DatabaseNodeAttributesAware Exception: "+ex.getMessage());
		}
	}

	public void close(){
		try{
			this.preparedStatementId.close();
		}catch(Exception ex){
		}
		try{
			this.preparedStatementNull.close();
		}catch(Exception ex){
		}
		try{
			this.connection.close();
		}catch(Exception ex){
		}
	}
	
	/** получить набор данных, на основании уникального родительского элемента */
	private ResultSet getResultSet(Integer parentId) throws SQLException{
		if(parentId==null){
			return preparedStatementNull.executeQuery();
		}else{
			preparedStatementId.setInt(1, parentId);
			return preparedStatementId.executeQuery();
		}
	}
	
	private ArrayList<NodeAttributes> getFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<NodeAttributes> returnValue=new ArrayList<NodeAttributes>();
		while(rs.next()){
			returnValue.add(new NodeAttributes(rs.getInt(this.fieldId),rs.getInt(this.fieldIdParent),rs.getString(this.fieldName),rs.getInt(this.fieldChildCount)));
		}
		return returnValue;
	}
	
	@Override
	public ArrayList<NodeAttributes> getNodeAttributes(Integer parentId) {
		try{
			
			return this.getFromResultSet(this.getResultSet(parentId));
		}catch(Exception ex){
			//System.err.println("DatabaseNodeAttributes Exception: "+ex.getMessage());
			return new ArrayList<NodeAttributes>();
		}
	}

}
