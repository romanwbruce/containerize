package containerize.romanbruce.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ExternalProcess {
	
	private String[] command;
	boolean live;
	
	public ExternalProcess(String[] command) {
		this.command = command;
	}
	public ExternalProcess(String[] command, boolean live) {
		this.command = command;
		this.live = live;
	}
	
	public Output execute() throws Exception{
		ArrayList<String> errLog = new ArrayList<String>();
		
		boolean error = false;
		Output out = null;
		Runtime rt = Runtime.getRuntime();
		String[] commands = command;
		Process proc = rt.exec(commands);

		BufferedReader stdInput = new BufferedReader(new 
		     InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
		     InputStreamReader(proc.getErrorStream()));


		String e = null;
		while ((e = stdError.readLine()) != null) {
			if(e.contains("fatal") || !e.contains("Cloning into") && !e.contains("") && !e.contains("#")) {
			    errLog.add(e);
			    error = true;
			    //System.out.println("error: "+e);
			}else {
				if(live) {
					System.out.println(e);
				}
				errLog.add(e);
			}
		}
		
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			if(live) {
				System.out.println(s); 
			}
			errLog.add(s);
		}
			
		out = new Output(commands, errLog, error);
		return out;
	}
}
