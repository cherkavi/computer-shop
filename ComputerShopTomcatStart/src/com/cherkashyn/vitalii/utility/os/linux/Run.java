package com.cherkashyn.vitalii.utility.os.linux;

import java.io.IOException;

public class Run {
	public static void programByName(String name) throws IOException{
		new ProcessBuilder(name).start();
	}

	public static void commandByNameLinux(String name) throws IOException{
		Runtime.getRuntime().exec(new String[]{"bash", "-c", name});
	}

	public static void commandByNameWindows(String name) throws IOException{
		Runtime.getRuntime().exec(new String[]{"cmd", "-c", name});
	}
	
	public static void main(String[] args) throws IOException{
		Run.programByName("gedit");
	}
}
