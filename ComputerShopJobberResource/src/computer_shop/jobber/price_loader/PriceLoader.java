package computer_shop.jobber.price_loader;

import java.io.File;
import java.sql.Connection;

import jxl.Sheet;
import jxl.Workbook;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import computer_shop.jobber.database.wrap.List;
import computer_shop.jobber.database.wrap.List_properties;
import computer_shop.jobber.database.wrap.List_section;

/** */
public class PriceLoader {
	private final static String tableList="j_list";
	
	/** очистить таблицу в базе данных, в которую будет записана очередна€ верси€ прайса 
	 * @param connection - соединение с базой данных 
	 * @param commit - нужно ли делать Commit базе данных
	 * @return true - если удаление данных прошло успешно   
	 * */
	public boolean clearTable(Connection connection,boolean commit){
		boolean returnValue=false;
		try{
			if(commit){
				try{
					connection.commit();
				}catch(Exception ex){};
			}
			connection.createStatement().executeUpdate("DELETE FROM "+tableList);
			
			if(commit){
				connection.commit();			
			}
			returnValue=true;
		}catch(Exception ex){
			System.err.println("PriceLoader#clearTable Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/** 
	 * «аписать в базу данных информацию о загружаемом файле 
	 * @param session 
	 * @param pathToXls
	 * @param positionBegin 0
	 * 
	 * 
	 */
	public String writeListPropertiesToDatabaseFromXls(Session session, 
			 										   String pathToXls,
			 										   int rowBegin,
			 										   int columnBegin){
		String returnValue=null;
		Workbook workBook=null;
		try{
			// открыть книгу Excel
			workBook=Workbook.getWorkbook(new File(pathToXls));
			Sheet sheet=workBook.getSheet(0);
			List_properties listProperties=new List_properties();
			listProperties.setCourse_cash(this.getFloatFromString( sheet.getCell(columnBegin-1,rowBegin-1).getContents()));
			listProperties.setCourse_account(this.getFloatFromString( sheet.getCell(columnBegin-1,rowBegin).getContents()));
			listProperties.setCourse_jobber_cash(this.getFloatFromString( sheet.getCell(columnBegin-1,rowBegin+1).getContents()));
			listProperties.setCourse_jobber_account(this.getFloatFromString( sheet.getCell(columnBegin-1,rowBegin+2).getContents()));
			listProperties.setDate_write(new java.util.Date());
			listProperties.setLoadPercent(0);
			session.beginTransaction();
			session.save(listProperties);
			session.getTransaction().commit();
			// прочесть данные и записать их в базу 
		}catch(Exception ex){
			returnValue=ex.getMessage();
			System.err.println("PriceLoader#writeListPropertiesToDatabaseFromXls Exception:"+ex.getMessage());
		}finally{
			try{
				workBook.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	private Integer listPropertiesKod=null;
	
	/** обновить значение процентов в загрузке прайса */
	private void updatePercentIntoListProperties(int percent, Session session){
		try{
			if(listPropertiesKod==null){
				listPropertiesKod=((List_properties)session.createCriteria(List_properties.class).addOrder(Order.desc("kod")).setMaxResults(1).uniqueResult()).getKod();
			}
			List_properties listProperties=(List_properties)session.get(List_properties.class, listPropertiesKod);
			session.beginTransaction();
			listProperties.setLoadPercent(percent);
			session.update(listProperties);
			session.getTransaction().commit();
			System.out.println("Loaded: "+percent+"%");
		}catch(Exception ex){
			System.err.println("PriceLoader#updateListProperties: "+ex.getMessage());
		}
	}
	
	
	/** записать данные из файла XLS в базу данных 
	 * @param session - HibernateSession
	 * @param pathToXls - путь к файлу Excel
	 * @param positionBegin - позици€ в прайсе, котора€ указывает на шапку (Header) прайса  
	 * @return null - если все прошло успешно или же текст ошибки  
	 */
	public String writeListToDatabaseFromXls(Session session, 
											 String pathToXls,
											 int positionBegin){
		// обновить значение записи в ListProperties
		listPropertiesKod=null;
		int loadPercentValue=0;
		String returnValue=null;
		try{
			// ќткрыть файл Excel 
			System.out.println("Path to file for load price: "+pathToXls);
			Workbook workbook=Workbook.getWorkbook(new File(pathToXls));
			Sheet sheet=workbook.getSheet(0);
			// пробежка по всем строкам в прайсе
			int columnCount=10;
			int columnIndex=0;
			int rowCount=sheet.getRows(); // 30
			int sectionKod=0;
			int subSectionKod=0;
			String[] rowValues=new String[columnCount];
			for(int rowIndex=positionBegin;rowIndex<rowCount;rowIndex++){
				try{
					for(columnIndex=0;columnIndex<columnCount;columnIndex++){
						rowValues[columnIndex]=null;
					};
					for(columnIndex=0;columnIndex<columnCount;columnIndex++){
						rowValues[columnIndex]=sheet.getCell(columnIndex,rowIndex).getContents();
					}
				}catch(Exception ex){
					System.err.println("PriceLoader#writeListToDatabaseFromXls Exception:"+ex.getMessage());
				}
				if((rowValues[1].equals(rowValues[2]))&&(rowValues[2].equals(rowValues[3]))&&(rowValues[3].equals(rowValues[4]))){
					// find new Section
					sectionKod=this.getSection(session, rowValues[2]);
					subSectionKod=sectionKod;
					//System.out.println("Section:"+rowValues[2]);
				}else{
					if((!rowValues[1].equals(rowValues[2]))&&(rowValues[2].equals(rowValues[3]))&&(rowValues[3].equals(rowValues[4]))){
						// find new SubSection
						subSectionKod=this.getSubSection(session, sectionKod, rowValues[2]);
						//System.out.println("	SubSection:"+rowValues[2]);
					}else{
						// find Data
						/*if(rowValues[1].equals("25340")){
							System.out.println("control");
						}*/
						
						try{
							List list=new List();
							list.setKod_kpi(rowValues[1]);
							list.setKod_producer(rowValues[2]);
							list.setKod_section(subSectionKod);
							list.setName(rowValues[3]);
							list.setPrice_1(this.getFloatFromString(rowValues[4]));
							list.setPrice_2(this.getFloatFromString(rowValues[5]));
							list.setPrice_3(this.getFloatFromString(rowValues[6]));
							list.setPrice_4(this.getFloatFromString(rowValues[7]));
							list.setWarranty(this.getIntegerFromString(rowValues[8]));
							list.setStore(this.getStoreFromString(rowValues[9]));
							session.beginTransaction();
							session.save(list);
							session.getTransaction().commit();
							//System.out.println("        list:"+list.getKod_kpi());
							if((((float)rowIndex/(float)rowCount)*100)>(loadPercentValue+5)){
								loadPercentValue+=5;
								this.updatePercentIntoListProperties(loadPercentValue, session);
							}else{
								//System.out.println( ((float)rowIndex/(float)rowCount)*100+"%  > "+loadPercentValue);
							}
						}catch(Exception ex){
							System.err.println("        ROW Exception:"+rowIndex);
							try{
								session.getTransaction().rollback();
							}catch(Exception ex2){};
						}finally{
						}
					}
				}
			}
			this.updatePercentIntoListProperties(100,session);
			workbook.close();
			returnValue=null;
		}catch(Exception ex){
			System.out.println("Exception "+ex.getClass());
			ex.printStackTrace();
			returnValue=ex.getMessage();
			System.err.println("writeListToDatabaseFromXls Exception:"+returnValue);
		}finally{
			// закрыть файл Excel
		}
		return returnValue;
	}
	
	private Integer getStoreFromString(String value){
		Integer returnValue=null;
		while(true){
			if(value.equals("+")){
				returnValue=1;
				break;
			}
			if(value.equals("-")){
				returnValue=(-1);
				break;
			}
			if(value.equals("резерв")){
				returnValue=0;
				break;
			}
			break;
		}
		return returnValue;
	}
	
	private Float getFloatFromString(String value){
		Float returnValue=null;
		try{
			returnValue=Float.parseFloat(value.replace(',', '.'));
		}catch(Exception ex){
			returnValue=null;
		}
		return returnValue;
	}
	
	private Integer getIntegerFromString(String value){
		Integer returnValue=null;
		try{
			returnValue=Integer.parseInt(value);
		}catch(Exception ex){
			returnValue=null;
		}
		return returnValue;
	}
	
	/** получить им€ секции, или создать секцию, если не будет найдена в прайс-листе 
	 * @param session - Hibernate Session 
	 * @param section - section name
	 * */
	private Integer getSection(Session session, String section){
		Integer returnValue=null;
		// попытка прочитать значение из базы
		try{
			Object objectFromBase=null;
			try{
				objectFromBase=session.createCriteria(List_section.class)
				 .add(Restrictions.eq("name", section))
				 .add(Restrictions.isNull("kod_parent")
						 		 ).uniqueResult();
			}catch(Exception ex){
				System.err.println("PriceLoader#getSection Exception: "+ex.getMessage());
			}
			if(objectFromBase!=null){
				// object is finded
				// значение найдено - вернуть код
				returnValue=((List_section)objectFromBase).getKod();
			}else{
				// create object
				// значение не найдно - создать и вернуть код
				session.beginTransaction();
				List_section baseObject=new List_section();
				baseObject.setKod_parent(null);
				baseObject.setName(section);
				session.save(baseObject);
				session.getTransaction().commit();
				returnValue=baseObject.getKod();
			}
		}catch(Exception ex){
			System.err.println("PriceLoader#getSection Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	/** получить подсекцию или создать подсекцию, котора€ имеет родител€ 
	 * @param session Hibernate Session
	 * @param parentKod родительский код 
	 * @param subSection им€ под-элемента
	 * */
	private int getSubSection(Session session, Integer parentKod, String subSection){
		Integer returnValue=null;
		// найти объект в базе данных
		try{
			Object objectFromBase=session.createCriteria(List_section.class)
			 .add(Restrictions.eq("name", subSection))
			 .add(Restrictions.eq("kod_parent", parentKod)
					 		 ).uniqueResult();
			if(objectFromBase!=null){
				returnValue=((List_section)objectFromBase).getKod();
			}else{
				// create Element
				session.beginTransaction();
				List_section baseObject=new List_section();
				baseObject.setKod_parent(parentKod);
				baseObject.setName(subSection);
				session.save(baseObject);
				session.getTransaction().commit();
				returnValue=baseObject.getKod();
			}
		}catch(Exception ex){
			System.err.println("PriceLoader#getSubSection Exception:"+ex.getMessage());
		}
		// 
		return returnValue;
	}
	
}
