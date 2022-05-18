package containerize.romanbruce.com.docker;

import java.io.File;
import java.io.PrintWriter;

import containerize.romanbruce.com.AppType;
import containerize.romanbruce.com.OptionsFromArgs;

public abstract class IDockerFileTemplate {

	protected AppType type;
	protected PrintWriter writer;
	protected String path;
	protected OptionsFromArgs args;

	 public IDockerFileTemplate(AppType type, String path, OptionsFromArgs args) {
		this.type = type;
		this.path = path;
		this.args = args;
		
	}
	
	protected void open() throws Exception{
		File f = new File(path);
		f.createNewFile();
		
		writer = new PrintWriter(path, "UTF-8");
	}
		
	protected void writeCMD(String cmd) {
		String[] splited = cmd.split(" ");
	    String string = "";
	    for(int x = 0; x < splited.length; x++){
	    	String char_ = splited[x];
	        string += "\""+char_+"\","; 
	    }
	    string = string.substring(0, string.length()-1);
	    write("CMD ["+string+"]");
	}
	
	protected void write(String text) {
		writer.println(text);
	}
	
	protected void close() {
		writer.close();
	}
	
	public abstract boolean create() throws Exception;
}
