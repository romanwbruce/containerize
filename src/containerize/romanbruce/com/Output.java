package containerize.romanbruce.com;

import java.util.ArrayList;

public class Output {
	private String[] srcCommand;
	private ArrayList<String> consoleLog;
	private boolean isError;
	
	public Output(String[] srcCommand, ArrayList<String> consoleLog, boolean isError) {
		this.srcCommand = srcCommand;
		this.consoleLog = consoleLog;
		this.isError = isError;
	}

	public String[] getSrcCommand() {
		return srcCommand;
	}

	public ArrayList<String> getConsoleLog() {
		return consoleLog;
	}
	
	public boolean isError() {
		return isError;
	}
}
