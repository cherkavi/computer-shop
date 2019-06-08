package report_servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import database.ConnectWrap;
import database.Connector;

/**
 * Servlet implementation class Reporter
 */
public class Reporter extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connector connectFactory;
	//private ReportGenerator reportGenerator;
	
	@Override
	public void init() throws ServletException {
	}
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Reporter() {
        super();
    	try{
    		this.connectFactory=new Connector("computer_shop_cartridge");
    		//this.reportGenerator=ReportGenerator.getInstance();
    	}catch(Exception ex){
    		System.err.println("Reporter#constructor Exception: "+ex.getMessage());
    	}
    }

	@Override
	public void service(ServletRequest request, 
						ServletResponse response) throws ServletException, IOException {
		try{
			// прочесть из потока объект String
			ObjectInputStream input=new ObjectInputStream(request.getInputStream());
			Object readedObject=input.readObject();
			if(readedObject instanceof String){
				input.close();
				// передать данный объект на обработчик
				ObjectOutputStream output=new ObjectOutputStream(response.getOutputStream());
				// выдать ответ
				output.writeObject(this.getFilePath((String)readedObject));
			}else{
				// Object is not string;
			}
		}catch(Exception ex){
			System.err.println("Reporter Exception: "+ex.getMessage());
		}
	}

	/** разобрать строку и получить ответ */
	private String getFilePath(String readedObject){
		String returnValue=null;
		try{
			ArrayList<String> parameters=this.parseString(readedObject);
			if(parameters.get(0).equals("RECEIPT")){
				ConnectWrap connector=this.connectFactory.getConnector();
				try{
					//returnValue=this.reportGenerator.createReceiptPdf(connector.getConnection(),parameters.get(1), parameters.get(2));
				}finally{
					connector.close();
				}
			}
			if(parameters.get(0).equals("BARCODE")){
				ConnectWrap connector=this.connectFactory.getConnector();
				try{
					//returnValue=this.reportGenerator.createBarCodePdf(connector.getConnection(),parameters.get(1), parameters.get(2));
				}finally{
					connector.close();
				}
			}
			if(parameters.get(0).equals("CHEQUE")){
				ConnectWrap connector=this.connectFactory.getConnector();
				try{
					//returnValue=this.reportGenerator.createChequePdf(connector.getConnection(),parameters.get(1), parameters.get(2));
				}finally{
					connector.close();
				}
			}
		}catch(Exception ex){
			System.err.println("Reporter: "+ex.getMessage());
		}
		return returnValue;
	}
	
	
	private ArrayList<String> parseString(String value){
		StringTokenizer token=new StringTokenizer(value, ";");
		ArrayList<String> parameters=new ArrayList<String>();
		while(token.hasMoreTokens()){
			parameters.add(token.nextToken());
		}
		return parameters;
	}
}
