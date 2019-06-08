package window.assembly.chooser;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import wicket_utility.IConnectorAware;
import window.assembly.element.Element;
import database.ConnectWrap;

/** объект, который выдает данные */
public class ElementDataProvider extends SortableDataProvider<Serializable>{
	private final static long serialVersionUID=1L;
	private IConnectorAware connectorAware;
	private String currentTableName;
	private ArrayList<Element> listOfElement;

	public ElementDataProvider(IConnectorAware connectorAware, 
							   String currentTable,
							   ArrayList<Element> listOfElement){
		this.listOfElement=listOfElement;
		this.connectorAware=connectorAware;
		this.currentTableName=currentTable;
	}
	
	/** получить строку, которая выдаст условие на выборку, согласно заданным критериям
	 * INFO главный алгоритм сборки по комплектующим  
	 * */
	private String getQuery(){
		Integer motherId=this.getUniqueIdByFromElementTableName("mb");
		Integer processorId=this.getUniqueIdByFromElementTableName("processor");
		Integer memoryId=this.getUniqueIdByFromElementTableName("memory");
		Integer videoId=this.getUniqueIdByFromElementTableName("video");
		Integer hddId=this.getUniqueIdByFromElementTableName("hdd");
		
		if(this.currentTableName.equals("mb")){
			// выбираем материнскую плату
			if((processorId==null)&&(memoryId==null)&&(videoId==null)&&(hddId==null)){
				// нет ни одного выделенного объекта
				StringBuffer query=new StringBuffer();
				query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
				query.append("inner join "+this.currentTableName+"_describe on "+this.currentTableName+"_describe.id_mb="+this.currentTableName+".id \n");
				query.append("inner join "+this.currentTableName+"_describe_name on "+this.currentTableName+"_describe_name.id="+this.currentTableName+"_describe.id_describe_name \n");
				query.append(" order by "+this.currentTableName+".id" );
				return query.toString();
			}else{
				// есть выделенные элементы, получить их
				StringBuffer query=new StringBuffer();
				query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
				int joinCounter=0;
				if(processorId!=null){
					// 451
					ArrayList<String> value_1=this.getStringValueFromTableById("processor", processorId, 1);
					if(value_1.size()>0){
						StringBuffer returnValue=new StringBuffer();
						returnValue.append("(");
						for(int counter=0;counter<value_1.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("mb", 451, value_1.get(counter),joinCounter));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe  "+this.currentTableName+"_describe"+joinCounter+" on "+this.currentTableName+"_describe"+joinCounter+".id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
						joinCounter++;
					}
					String processorName=this.getNameFromBase("processor",processorId);
					boolean isAmd=false;
					if(processorName.indexOf("AMD")>0){
						// this is amd processor
						isAmd=true;
					}
					// 469
					ArrayList<String> value_3=this.getStringValueFromTableById("processor", processorId, 3);
					if((isAmd==false)&&(value_3.size()>0)){
						StringBuffer returnValue=new StringBuffer();
						returnValue.append("(");
						for(int counter=0;counter<value_3.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("mb", 469, value_3.get(counter),joinCounter));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe  "+this.currentTableName+"_describe"+joinCounter+" on "+this.currentTableName+"_describe"+joinCounter+".id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
						joinCounter++;
					}
				}
				if(memoryId!=null){
					// 458
					ArrayList<String> value_29=this.getStringValueFromTableById("memory", processorId, 29);
					if(value_29.size()>0){
						StringBuffer returnValue=new StringBuffer();
						returnValue.append("(");
						for(int counter=0;counter<value_29.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("mb", 458, value_29.get(counter),joinCounter));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe  "+this.currentTableName+"_describe"+joinCounter+" on "+this.currentTableName+"_describe"+joinCounter+".id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
						joinCounter++;
					}
				}
				if(videoId!=null){
					// 459
					ArrayList<String> value_2=this.getStringValueFromTableById("video", processorId, 2);
					if(value_2.size()>0){
						StringBuffer returnValue=new StringBuffer();
						returnValue.append("(");
						for(int counter=0;counter<value_2.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("mb", 459, "%"+value_2.get(counter),joinCounter));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe  "+this.currentTableName+"_describe"+joinCounter+" on "+this.currentTableName+"_describe"+joinCounter+".id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
						joinCounter++;
					}
				}
				if(hddId!=null){
					// 453
					ArrayList<String> value_1=this.getStringValueFromTableById("hdd", processorId, 1);
					if(value_1.size()>0){
						StringBuffer returnValue=new StringBuffer();
						returnValue.append("(");
						for(int counter=0;counter<value_1.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("mb", 453, "%"+value_1.get(counter),joinCounter));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe  "+this.currentTableName+"_describe"+joinCounter+" on "+this.currentTableName+"_describe"+joinCounter+".id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
						joinCounter++;
					}
				}
				query.append(" order by "+this.currentTableName+".id" );
				return query.toString();
			}
		}
		if(this.currentTableName.equals("processor")){
			// выбираем процессор
			StringBuffer query=new StringBuffer();
			query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
			
			//query.append(" inner join "+this.currentTableName+"_describe_name on "+this.currentTableName+"_describe_name.id="+this.currentTableName+"_describe.id_describe_name \n");
			String whereString=null;			
			if(motherId==null){
				whereString=null;
			}else{
				
				// сопряжение материнская плата-процессор
				ArrayList<String> value_451=this.getStringValueFromTableById("mb", motherId, 451);
				ArrayList<String> value_469=this.getStringValueFromTableById("mb", motherId, 469);
				if((value_451.size()>0)||(value_469.size()>0)){
					StringBuffer returnValue=new StringBuffer();
					if(value_451.size()>0){
						returnValue.append("(");
						for(int counter=0;counter<value_451.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("processor", 1, value_451.get(counter)));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe on "+this.currentTableName+"_describe.id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
					}
					
					returnValue=new StringBuffer();
					// получить имя процессора
					boolean isAmd=this.isMotherAMD(motherId);
					if((isAmd==false)&&(value_469.size()>0)){
						if(returnValue.length()>0){
							returnValue.append(" and ");
						}
						returnValue.append("(");
						for(int counter=0;counter<value_469.size();counter++){
							if(counter>0){
								returnValue.append(" or \n");
							}
							returnValue.append(getStringForWhere("processor", 3, value_469.get(counter),2));
						}
						returnValue.append(" ) ");
						if(returnValue.length()>0){
							query.append("inner join "+this.currentTableName+"_describe "+this.currentTableName+"_describe2 on "+this.currentTableName+"_describe2.id_mb="+this.currentTableName+".id \n");
							query.append(" and "+returnValue);
						}
					}
					
				}
				return query.toString();
			}
			if(whereString!=null){
				query.append("where ");
				query.append(whereString);
			}
			query.append(" order by "+this.currentTableName+".id" );
			//System.out.println("Processor: "+query.toString());
			return query.toString();
		}
		if(this.currentTableName.equals("video")){
			// выбираем видео 
			StringBuffer query=new StringBuffer();
			query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
			query.append("inner join "+this.currentTableName+"_describe on "+this.currentTableName+"_describe.id_mb="+this.currentTableName+".id \n");
			query.append("inner join "+this.currentTableName+"_describe_name on "+this.currentTableName+"_describe_name.id="+this.currentTableName+"_describe.id_describe_name \n");
			String whereString=null;			
			if(motherId==null){
				whereString=null;
			}else{
				// сопряжение материнская плата-video
				StringBuffer returnValue=null;
				ArrayList<String> value_459=this.getStringValueFromTableById("mb", motherId, 459);
				if(value_459.size()>0){
					for(int counter=0;counter<value_459.size();counter++){
						try{
							value_459.set(counter, value_459.get(counter).substring(4).trim());
						}catch(Exception ex){};
					}
					returnValue=new StringBuffer();
					returnValue.append("(");
					for(int counter=0;counter<value_459.size();counter++){
						if(counter>0){
							returnValue.append(" or \n");
						}
						returnValue.append(getStringForWhere("video", 2, value_459.get(counter)));
					}
					returnValue.append(" ) ");
				}
				whereString=(returnValue==null)?null:returnValue.toString();
			}
			if(whereString!=null){
				query.append("where ");
				query.append(whereString);
			}
			query.append(" order by "+this.currentTableName+".id" );
			return query.toString();
		}
		if(this.currentTableName.equals("memory")){
			// выбираем модуль памяти
			StringBuffer query=new StringBuffer();
			query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
			query.append("inner join "+this.currentTableName+"_describe on "+this.currentTableName+"_describe.id_mb="+this.currentTableName+".id \n");
			query.append("inner join "+this.currentTableName+"_describe_name on "+this.currentTableName+"_describe_name.id="+this.currentTableName+"_describe.id_describe_name \n");
			String whereString=null;			
			if(motherId==null){
				whereString=null;
			}else{
				StringBuffer returnValue=null;
				// сопряжение материнская плата-память
				ArrayList<String> value_458=this.getStringValueFromTableById("mb", motherId, 458);
				if(value_458.size()>0){
					returnValue=new StringBuffer();
					returnValue.append("(");
					for(int counter=0;counter<value_458.size();counter++){
						if(counter>0){
							returnValue.append(" or \n");
						}
						returnValue.append(getStringForWhere("memory", 29, value_458.get(counter)));
					}
					returnValue.append(" ) ");
				}
				whereString=(returnValue==null)?null:returnValue.toString();
			}
			if(whereString!=null){
				query.append("where ");
				query.append(whereString);
			}
			query.append(" order by "+this.currentTableName+".id" );
			return query.toString();
		}
		if(this.currentTableName.equals("hdd")){
			// выбираем жесткий диск
			StringBuffer query=new StringBuffer();
			query.append("select distinct "+this.currentTableName+".id, "+this.currentTableName+".name  from "+this.currentTableName+" \n");
			query.append("inner join "+this.currentTableName+"_describe on "+this.currentTableName+"_describe.id_mb="+this.currentTableName+".id \n");
			query.append("inner join "+this.currentTableName+"_describe_name on "+this.currentTableName+"_describe_name.id="+this.currentTableName+"_describe.id_describe_name \n");
			String whereString=null;			
			if(motherId==null){
				whereString=null;
			}else{
				// сопряжение материнская плата-hdd
				ArrayList<String> value_453=this.getStringValueFromTableById("mb", motherId, 453);
				for(int counter=0;counter<value_453.size();counter++){
					String value=value_453.get(counter);
					value_453.set(counter, value.substring(2).trim());
				}
				int index=0;
				while(index<value_453.size()){
					//System.out.println("hdd: "+value_453.get(index));
					if (  (value_453.get(index).indexOf("USB")>=0)
						||(value_453.get(index).indexOf("usb")>=0)){
							value_453.remove(index);
							continue;
						}
					if (  (value_453.get(index).indexOf("LPT")>=0)
							||(value_453.get(index).indexOf("lpt")>=0)){
								value_453.remove(index);
								continue;
							}
					index++;
				}
				StringBuffer returnValue=new StringBuffer();
				/*
				String temp=null;
				temp=getStringForWhere("hdd", 5, "%Внутренний%");
				if(temp!=null){
					if(returnValue.length()>0){
						returnValue.append(" and ");
					}
					returnValue.append(temp);
				}*/
				if(value_453.size()>0){
					//returnValue.append(" and \n");
					returnValue.append(" ( ");
					for(int counter=0;counter<value_453.size();counter++){
						if(counter>0){
							returnValue.append(" or ");
						}
						returnValue.append(getStringForWhere("hdd",1,value_453.get(counter)));
					}
					returnValue.append(" ) ");
				}
				whereString=returnValue.toString();
			}
			if(whereString!=null){
				query.append("where ");
				query.append(whereString);
			}
			query.append(" order by "+this.currentTableName+".id" );
			return query.toString();
			
		}
		return null;
	}
	
	/** проверяет, является ли данная материнская плата носителем AMD процессора */
	private boolean isMotherAMD(Integer motherId){
		boolean returnValue=false;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("SELECT distinct name  FROM mb_describe where id_mb="+motherId+" and id_describe_name=448");
			while(rs.next()){
				if(rs.getString("name").toUpperCase().indexOf("AMD")>=0){
					returnValue=true;
					break;
				}
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("elementDataProvider#isMotherAMD Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** получить значение поля NAME на основании имени таблицы и значения ID ( уникального идентификатора из этой таблицы )
	 * @param tableName - имя таблицы, из которой следует делать выборку
	 * @param uniqueId - значение уникального поля (id)
	 * @return null, если значение не найдно 
	 */
	private String getNameFromBase(String tableName, Integer uniqueId) {
		String returnValue=null;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from "+tableName+" where id="+uniqueId);
			if(rs.next()){
				returnValue=rs.getString("name");
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("ElementDataProvider#getNameFromBase Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		//System.out.println("getNameFromBase: "+returnValue);
		return returnValue;
	}

	/** получить строку для добавления в блок where запроса, которой получает необходимые позиции товара*/
	private String getStringForWhere(String table, Integer describeId, String value){
		if((table!=null)&&(describeId!=null)&&(value!=null)){
			return "("+table+"_describe.id_describe_name="+describeId+" and "+table+"_describe.name like '"+value.replaceAll("'", "''")+"')";
		}else{
			return null;
		}
	}
	
	/** получить строку для добавления в блок where запроса, которой получает необходимые позиции товара*/
	private String getStringForWhere(String table, Integer describeId, String value, Integer aliasNumber){
		if((table!=null)&&(describeId!=null)&&(value!=null)){
			if(aliasNumber!=null){
				return "("+table+"_describe"+aliasNumber+".id_describe_name="+describeId+" and "+table+"_describe"+aliasNumber+".name like '"+value.replaceAll("'", "''")+"')";
			}else{
				return "("+table+"_describe.id_describe_name="+describeId+" and "+table+"_describe.name like '"+value.replaceAll("'", "''")+"')";
			}
		}else{
			return null;
		}
	}
	
	/** получить на основании имени таблицы, уникального значения и кода описания, само описание*/
	private ArrayList<String> getStringValueFromTableById(String table, Integer id, Integer describeId){
		ArrayList<String> returnValue=new ArrayList<String>();
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			StringBuffer query=new StringBuffer();
			query.append(" select "+table+"_describe.name from "+table+" \n");
			query.append(" inner join "+table+"_describe on "+table+"_describe.id_mb="+table+".id and "+table+".id="+id+" and "+table+"_describe.id_describe_name="+describeId+" \n");
			query.append(" inner join "+table+"_describe_name on "+table+"_describe_name.id="+table+"_describe.id_describe_name ");
			ResultSet rs=connector.getConnection().createStatement().executeQuery(query.toString());
			while(rs.next()){
				try{
					//System.out.println(">>> "+rs.getString("name").trim());
					returnValue.add(rs.getString("name").trim());
				}catch(Exception ex){};
			}
			rs.getStatement().close();
		}catch(Exception ex){
			System.err.println("Chooser.ElementDataProvider#getStringValueFromTableById Exception: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(NullPointerException npe){};
		}
		return returnValue;
	}
	
	
	/** по имени таблицы получить уникальный номер, если данные по таблицы выбраны из списка товаров */
	private Integer getUniqueIdByFromElementTableName(String tableName){
		Element element=this.getElementFromListByTableName(tableName);
		if(element!=null){
			return element.getTableId();
		}else{
			return null;
		}
	}
	
	/**  
	 * @param имя таблицы 
	 * @return элемент с данным именем таблицы (или null, если не найден ) 
	 * */
	private Element getElementFromListByTableName(String tableName){
		Element returnValue=null;
		for(int counter=0;counter<this.listOfElement.size();counter++){
			if(this.listOfElement.get(counter).getTableName().equals(tableName)){
				returnValue=this.listOfElement.get(counter);
				break;
			}
		}
		return returnValue;
	}
	
	
	@SuppressWarnings("unchecked")
	public Iterator<? extends Serializable> iterator(int first, int count) {
		Iterator<? extends Serializable> returnValue=null;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			Session session=connector.getSession();
			returnValue=(Iterator<? extends Serializable>)session.createSQLQuery(this.getQuery()).addScalar("id").addScalar("name").setFirstResult(first).setMaxResults(count).list().iterator();
		}catch(Exception ex){
			System.err.println("Chooser.ElementDataProvider#iterator Exception:"+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}

	@Override
	public IModel<Serializable> model(Serializable element) {
		return new Model<Serializable>(element);
	}

	@Override
	public int size() {
		int returnValue=0;
		ConnectWrap connector=this.connectorAware.getConnector();
		try{
			Connection connection=connector.getConnection();
			System.out.println("Query size: \n"+this.getQuery());
			ResultSet rs=connection.createStatement().executeQuery(this.getQuery());
			while(rs.next()){
				returnValue++;
			}
		}catch(Exception ex){
			System.err.println("ElementDataProvider#size() Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
}
