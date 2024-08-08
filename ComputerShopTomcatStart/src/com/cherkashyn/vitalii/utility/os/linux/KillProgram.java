package com.cherkashyn.vitalii.utility.os.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KillProgram {
	
	public static void byName(String name) throws IOException{
		Process process=Runtime.getRuntime().exec(new String[]{"bash", "-c", "ps aux | grep "+name});
		BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		List<String> outputList=new ArrayList<String>();
		String line=null;
		while((line=reader.readLine())!=null){
			outputList.add(line);
		};
		String programId=getProgramId(outputList, name);
		Run.commandByNameLinux("kill -9 "+programId);
	}

	private final static int PROCESS_ID_POSITION=1;
	private final static int PROCESS_NAME_POSITION=10;
	
	private static String getProgramId(List<String> outputList, String processName) {
		if(outputList.size()>0){
			for(String eachLine:outputList){
				String[] values=omitNulls(eachLine.split(" "));
				if(values[PROCESS_NAME_POSITION].equals(processName)){
					return values[PROCESS_ID_POSITION];
				}
			}
		}
		return null;
	}
	
	private static String[] omitNulls(String[] split) {
		List<String> returnValue=new ArrayList<String>();
		for(int index=0;index<split.length;index++){
			String nextValue=split[index].trim();
			if(nextValue.length()>0){
				returnValue.add(nextValue);
			}
		}
		return returnValue.toArray(new String[returnValue.size()]);
	}

	public static void main(String[] args) throws IOException{
		KillProgram.byName("gedit");
	}
}
