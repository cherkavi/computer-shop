package com.cherkashyn.vitalii.utility.os;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.cherkashyn.vitalii.utility.net.HttpRequest;
import com.cherkashyn.vitalii.utility.os.linux.KillProgram;
import com.cherkashyn.vitalii.utility.os.linux.Run;

public class RestartProgram {
	
	public static void main(String[] args) throws IOException, InterruptedException{
		if(args.length<3){
			System.out.println("First parameter - path to program \nSecond parameter - name of process for kill it \nThird - delay time ( seconds ) \nFourth - get request to server ");
			System.exit(0);
		}
		String pathToProgram=args[0];
		String processName=args[1];
		Long delay=Long.parseLong(args[2]);
		String url=args[3];
		
		// start tomcat
		Run.commandByNameLinux(pathToProgram);
		// wait for upload
		TimeUnit.SECONDS.sleep(delay);
		// execute GET
		HttpRequest.executeGet(url);
		// stop tomcat
		KillProgram.byName(processName);
		// start tomcat
		TimeUnit.SECONDS.sleep(1);
		Run.commandByNameLinux(pathToProgram);
	}
	
}
