package com.cherkashyn.vitalii.utility.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.cherkashyn.vitalii.utility.os.linux.Run;


/**
 * listen port, check input string, execute logic ( if string equals with input parameter )
 */
public class SocketListener {
	
	static class Parameter{
		int portNumber;
		String password;
		String executeProcess;
		
		public Parameter(int portNumber, String password, String executeProcess) {
			super();
			this.portNumber = portNumber;
			this.password = password;
			this.executeProcess=executeProcess;
		}
		
		public boolean isPasswordValid(String inputData) {
			if(inputData==null){
				return false;
			}
			return this.password.trim().equalsIgnoreCase(inputData.trim());
		}
		
		public void executeProcess() throws IOException{
			Run.programByName(executeProcess);
		}
		
	}
	
	private final static int CLIENT_TIMEOUT=30000;
	
	private static void debug(String info){
		System.out.println(info);
	}
	
	private static void err(String info){
		System.err.println(info);
	}
	
	public static void main(String[] args){
		Parameter parameter=readArguments(args);
		
		ServerSocket serverSocket=registerSocket(parameter);
		
		debug("start listener");
		// working cycle
		while(true){
			Socket socket=null;
			try {
				socket=serverSocket.accept();
				socket.setSoTimeout(CLIENT_TIMEOUT); // 30 sec as time for input password
			} catch (IOException e) {
				err("can't accept socket connection");
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e1) {
					}
				}
				continue;
			}

			try{
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				String inputData=input.readLine();
				debug("read input data: "+inputData);
				
				if(inputData!=null){
					PrintWriter output = new PrintWriter(socket.getOutputStream(),true); 
					if( parameter.isPasswordValid(inputData)){
						output.println("executing");
						socket.close();
						break;
					}else{
						output.println("wrong password");
						socket.close();
					}
				}
			}catch(IOException ex){
				err("can't read/write from/to socket: "+ex.getMessage());
				try {
					if(socket!=null){
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			if(serverSocket!=null){
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			parameter.executeProcess();
		} catch (IOException e) {
			err("can't execute process");
		}
		debug("finish");
	}
	
	
	
	private static Parameter readArguments(String[] args) throws RuntimeException{
		// check arguments
		if(args.length<3){
			System.err.println("Arguments: <port number> <password for execute logic>  <path to execute program>");
			System.exit(1);
		}
		Integer portNumber=Integer.parseInt(args[0]);
		if(portNumber==null || portNumber<1024 || portNumber>65535){
			System.err.println("port number should be more than 1024 and less than 65535");
		}
		String password=args[1].trim().toLowerCase();
		String executeProgram=args[2];
		return new Parameter(portNumber, password, executeProgram);
	}
	
	

	private static ServerSocket registerSocket(Parameter parameter) {
		// start listener
		ServerSocket serverSocket=null;
		try{
			serverSocket=new ServerSocket(parameter.portNumber);
			// serverSocket.setSoTimeout(0);
		}catch(Exception ex){
			throw new RuntimeException("can't register listener: "+ex.getMessage());
		}
		return serverSocket;
	}

}
