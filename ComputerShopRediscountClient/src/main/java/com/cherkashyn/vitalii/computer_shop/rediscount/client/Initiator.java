package com.cherkashyn.vitalii.computer_shop.rediscount.client;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cherkashyn.vitalii.computer_shop.rediscount.client.output.OutputHelper;

/**
 * Servlet implementation class Initiator
 */
public class Initiator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	Object parameterRemoteServer=config.getServletContext().getInitParameter("path_to_server");
    	if(parameterRemoteServer!=null){
    		OutputHelper.SERVER_URL=parameterRemoteServer.toString();
    	}
    }

}
