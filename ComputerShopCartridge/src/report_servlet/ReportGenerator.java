package report_servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.beanutils.PropertyUtils;
import window.commons.GroupOrder;
import window.commons.OrderElement;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
/** объект, который отвечает за создание отчетов на уровне приложения */
public class ReportGenerator {
	public static String dir;
	public static String patternDir;
	//private static String fileNameReceipt;
	//private static String fileNameBarcode;
	private static JasperReport jasperReportReceipt;
	private static JasperReport jasperReportBarCode;
	private static JasperReport jasperReportCheque;


	/**
	 * @param dir - путь ко временному хранилищу 
	 * @param dirPattern - путь к каталогу, в котором лежат шаблоны 
	 * @param fileNameReceipt - шаблон Квитанции 
	 * @param fileNameBarcode - шаблон BarCode
	 * @param fileNameCommodity - шаблон Товарного чека
	 */
	public static void init(String dir, String dirPattern, String fileNameReceipt, String fileNameBarcode, String fileNameCommodity) {
    	ReportGenerator.dir=dir;
    	ReportGenerator.patternDir=dirPattern;
    	//ReportGenerator.fileNameReceipt=fileNameReceipt;
    	//ReportGenerator.fileNameBarcode=fileNameBarcode;
    	try{
    		jasperReportReceipt=JasperCompileManager.compileReport(patternDir+fileNameReceipt);
    	}catch(Exception ex){
    		System.err.println("init jasperReportReceipt Exception: "+ex.getMessage());
    	};
    	try{
    		jasperReportBarCode=JasperCompileManager.compileReport(patternDir+fileNameBarcode);
    	}catch(Exception ex){
    		System.err.println("init jasperReportBarCode Exception: "+ex.getMessage());
    	};
    	try{
    		jasperReportCheque=JasperCompileManager.compileReport(patternDir+fileNameCommodity);
    	}catch(Exception ex){
    		System.err.println("init jasperReportBarCode Exception: "+ex.getMessage());
    	};
	}
	
	private ReportGenerator(){
	}
	
	/** получить новый объект данного класса, проконтролировав вызов init*/
	public static ReportGenerator getInstance(){
		if(jasperReportReceipt==null){
			return null;
		}else{
			return new ReportGenerator();
		}
	}

	/** создать Квитанцию 
	 * @param connection соединение с базой данных
	 * @param uniqueNumber уникальный номер 
	 * @param controlNumber номер для контроля получения данных 
	 * @return имя файла в котором сохранен отчет
	 */
	public String createReceiptPdf(GroupOrder groupOrder){
		// INFO место создания отчёта
		String returnValue=null;
		//String returnValueHtml=null;
		try{
			JasperPrint jasper_print=null;
			synchronized(jasperReportReceipt){
				//System.out.println("First Vendor:"+PropertyUtils.getProperty(groupOrder.getListOfOrder().get(0), "vendor"));
		        jasper_print=JasperFillManager.fillReport(jasperReportReceipt,
		        										  this.getParameters(groupOrder),
		        										  new BeanDataSource(groupOrder.getListOfOrder())//new JRBeanCollectionDataSource(groupOrder.getListOfOrder())
		        										 ); 
			}
	        returnValue="receipt_"+this.getTimeStamp()+this.getRandomString(3)+".pdf";
	        JasperExportManager.exportReportToPdfFile(jasper_print,dir+returnValue);
	        //returnValueHtml="receipt_"+this.getTimeStamp()+this.getRandomString(3)+".html";
	        //JasperExportManager.exportReportToHtmlFile(jasper_print,dir+returnValueHtml);
	        //JRAbstractExporter exporter=new JRGraphics2DExporter();
	        //exporter.setParameter(JRExporterParameter., value)
		}catch(Exception ex){
			System.err.println("Reporter#createReceiptPdf Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/** создать BarCode 
	 * @param connection соединение с базой данных
	 * @param uniqueNumber уникальный номер 
	 * @return имя файла в котором сохранен отчет 
	 */
	public String createBarCodePdf(String uniqueNumber){
		String returnValue=null;
		try{
			JasperPrint jasper_print=null;
			HashMap<String,String> parameters=new HashMap<String,String>();
			parameters.put("UNIQUE_NUMBER", uniqueNumber);
			synchronized(jasperReportBarCode){
		        jasper_print=JasperFillManager.fillReport(jasperReportBarCode,
		        										  parameters,
		        										  new JREmptyDataSource()
		        										  ); 
			}
	        returnValue="barcode_"+this.getTimeStamp()+this.getRandomString(3)+".pdf";
	        JasperExportManager.exportReportToPdfFile(jasper_print,dir+returnValue);
		}catch(Exception ex){
			System.err.println("Reporter#createBarCodePdf Exception:"+ex.getMessage());
		}
		return returnValue;
	}

	/** создать Товарный чек 
	 * @param connection соединение с базой данных
	 * @param uniqueNumber уникальный номер 
	 * @param controlNumber номер для контроля получения данных 
	 * @return имя файла в котором сохранен отчет 
	 */
	public String createChequePdf(GroupOrder groupOrder){
		// INFO место создания отчёта
		String returnValue=null;
		//String returnValueHtml=null;
		try{
			JasperPrint jasper_print=null;
			synchronized(jasperReportCheque){
		        jasper_print=JasperFillManager.fillReport(jasperReportCheque,this.getParameters(groupOrder),new BeanDataSource(groupOrder.getListOfOrder())); //,new JRResultSetDataSource(resultset));
			}
	        returnValue="cheque_"+this.getTimeStamp()+this.getRandomString(3)+".pdf";
	        JasperExportManager.exportReportToPdfFile(jasper_print,dir+returnValue);
	        //returnValueHtml="receipt_"+this.getTimeStamp()+this.getRandomString(3)+".html";
	        //JasperExportManager.exportReportToHtmlFile(jasper_print,dir+returnValueHtml);
	        //JRAbstractExporter exporter=new JRGraphics2DExporter();
	        //exporter.setParameter(JRExporterParameter., value)
		}catch(Exception ex){
			System.err.println("Reporter#createCheque Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	
	
	/** получить параметры для заполнения на основании необходимых кодов */
	private HashMap<String,String> getParameters(GroupOrder groupOrder){
		HashMap<String,String> returnValue=new HashMap<String,String>();
		returnValue.put("CUSTOMER_NAME", groupOrder.getName());
		returnValue.put("CUSTOMER_SURNAME", groupOrder.getSurname());
		returnValue.put("DESCRIPTION", groupOrder.getDescription());
		returnValue.put("DATE_CREATE",sdfPrint.format(new Date()));
		try{
			returnValue.put("RECEIPT_TITLE",groupOrder.getPointSettings().getReceiptTitle());
			System.out.println("RECEIPT_TITLE: "+groupOrder.getPointSettings().getReceiptTitle());
		}catch(NullPointerException ex){
			System.err.println("ReportGenerator#getParameters Exception:"+ex.getMessage());
		};
		return returnValue;
	}
	
	private SimpleDateFormat sdfPrint=new SimpleDateFormat("dd.MM HH:mm:ss");
	private SimpleDateFormat sdf=new SimpleDateFormat("dd_MM__HH_mm_ss");
	/** */
	private String getTimeStamp(){
		return sdf.format(new Date());
	}
	
	private String getRandomString(int size){
        StringBuffer return_value=new StringBuffer();
        Random random=new java.util.Random();
        int temp_value;
        for(int counter=0;counter<size;counter++){
            temp_value=random.nextInt(10);
            return_value.append(temp_value);
        }
        return return_value.toString();
	}
	
}


class BeanDataSource implements JRDataSource{
	private ArrayList<OrderElement> list;
	private int position=(-1);
	public BeanDataSource(ArrayList<OrderElement> list){
		this.list=list;
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		try{
			return PropertyUtils.getProperty(list.get(position), field.getName());
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public boolean next() throws JRException {
		position++;
		if(position<this.list.size()){
			return true;
		}else{
			return false;
		}
	}
	
}