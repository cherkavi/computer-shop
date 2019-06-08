package report_servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetFile
 */
public class GetFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request,response);
	}


	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName=request.getParameter("name");
		String fullPath=ReportGenerator.dir+fileName;
		try{
			File file=new File(fullPath);
			if(fileName.toLowerCase().endsWith(".pdf")){
		    	response.setContentType("application/pdf");
		    	response.addHeader("MIME-type", "application/pdf");
		    	response.addHeader("Content-Disposition", "attachment; filename=report.pdf");
		    	response.setContentLength((int)file.length());
			}else{
		    	//response.setContentType("application/pdf");
		    	//response.addHeader("MIME-type", "application/pdf");
		    	//response.addHeader("Content-Disposition", "attachment; filename=report.html");
			}
	    	FileInputStream fis=new FileInputStream(fullPath);
	    	BufferedInputStream buffer=new BufferedInputStream(fis);    
            ServletOutputStream servlet_out=response.getOutputStream();
	    	
	    	this.BufferedInputStreamToStream(buffer, servlet_out);
	    	buffer.close();
	    	servlet_out.flush();
	    	servlet_out.close();
		}catch(Exception ex){
			System.err.println("GetFile doAction Exception: "+ex.getMessage());
		}
	}

	/**
	 * Копирование из буфера ввода в вывод сервлета
	 * @param buffer
	 * @param servlet_out
	 */
	private void BufferedInputStreamToStream(BufferedInputStream buffer,
											 ServletOutputStream servlet_out) throws ServletException,IOException {
        byte[] read_bytes=new byte[1000];
        int byte_count=0;
        while( (byte_count=buffer.read(read_bytes))!=-1){
            servlet_out.write(read_bytes,0,byte_count);
        }
	}
	
	
}

