package com.cherkashyn.vitalii.utility.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TelnetInstance {
	
	public static void main(String[] args) {
		if(args.length<3){
			System.out.println("Parameters:  <host>   <port>   <data> ");
			System.exit(1);
		}
		
		String host=args[0];
		int portNumber=Integer.parseInt(args[1]);
		String data=args[2];
		try{
			Socket socket=new Socket(host, portNumber);
			sendData(socket, data);
			socket.close();
		}catch(IOException ex){
			System.err.println("Error, can't read/write to remote socket: "+ex.getMessage());
		}
		
	}
	
	private static void sendData(Socket socket, String data) throws IOException{
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write(data);
		writer.flush();
	}
	
}
