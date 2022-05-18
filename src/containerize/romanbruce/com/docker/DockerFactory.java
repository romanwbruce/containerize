package containerize.romanbruce.com.docker;

import containerize.romanbruce.com.AppType;
import containerize.romanbruce.com.OptionsFromArgs;
import containerize.romanbruce.com.Output;

public class DockerFactory {

	public Output createDockerFile(OptionsFromArgs args, String path) {
		Output output = null;
		IDockerFileTemplate template = null;
		
		template = new NodeDockerFileTemplate(AppType.NODE, path, args);
		try {
			template.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		output = new Output(null, null, false);
		return output;
	}
	
}
