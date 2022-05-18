package containerize.romanbruce.com.docker;

import containerize.romanbruce.com.AppType;
import containerize.romanbruce.com.OptionsFromArgs;

public class NodeDockerFileTemplate extends IDockerFileTemplate{

	public NodeDockerFileTemplate(AppType type, String path, OptionsFromArgs args) {
		super(type, path, args);
	}

	@Override
	public boolean create() throws Exception{
		boolean created = true;
	
		this.open();
		
		this.write("FROM node:lts ");
		
		this.write("WORKDIR ./"+args.sourceFolder);
		this.write("COPY package*.json ./");
		
		this.write("RUN npm install");
		
		this.write("COPY . . ");
		this.write("WORKDIR ./"+args.sourceFolder);
		this.write("COPY *.js ./ ");
		
		//if(args.buildCommand.length()>3) {
		//	this.write("RUN "+args.buildCommand);
		//}
		
		this.write("EXPOSE "+args.port);
		this.writeCMD(args.runCommand);

		this.close();
		
		return created;
	}

	
}
