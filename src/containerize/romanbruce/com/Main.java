package containerize.romanbruce.com;

import java.util.UUID;

import containerize.romanbruce.com.docker.DockerFactory;
import containerize.romanbruce.com.logging.DebugLogger;
import containerize.romanbruce.com.logging.RuntimeLogger;
import containerize.romanbruce.com.mongo.MongoConnection;

public class Main {
	
	/*
	 * Logging options
	 */
	static DebugLogger debug = new DebugLogger();
	static RuntimeLogger runtime = new RuntimeLogger();

	/*
	 * Core services
	 */
	OptionsFromArgs options = new OptionsFromArgs();
	DockerFactory dockerFactory = new DockerFactory();
	MongoConnection connection = new MongoConnection();
	
	public Main(String[] args) {
		/*
		 * Create options from args[].
		 * Passing by main()
		 */
		try {
			options = options.getArgs(args);
			
			//Detect rebuild
			if(options.valid) {				
		
				create(options);
				
			}else {
				//Invalid options
				runtime.write("");
				runtime.write("Invalid input provided.");
				runtime.write("");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create(OptionsFromArgs a) throws Exception{
		int open = OpenPort.getNextPort();
		connection.insertApplicationData(a, open);
		
		runtime.write("Creating ... "+a.appID);
		
		//connection.updateLog(a.appID, "deployment", "Waiting for build to finish");


		//Directory where project files will be created in.
		String directory = "./"+a.appID;
		
		//Step 1: Clone project
		String[] d = {"git", "clone", "https://"+a.g_username+":"+a.g_token+"@github.com/"+a.g_username+"/"+a.repo+".git", directory};
		Output clone = new ExternalProcess(d).execute();
		//connection.updateLog(a.appID, "build", "Pulled repo "+a.repo);
		//connection.updateLog(a.appID, "build", "Using "+a.srcFolder+" as root directory");

		if(clone.isError()) {
			runtime.write("Failed to clone project: ");
			for(String e : clone.getConsoleLog()) {
				runtime.write(e);
			}
			abort(a.appID, "Failure to clone project.");
			
		}else {
			for(String e : clone.getConsoleLog()) {
				runtime.write(e);
			}
			connection.notifyApp(a.appID, "Clone", "Cloned project successfully", false);
			runtime.write("Cloned project");
		}
	
		if(a.sourceFolder.startsWith("/")) {
			a.dir = ""+a.appID+""+a.sourceFolder;
		}else {
			a.dir = ""+a.appID+"/"+a.sourceFolder;
		}
		
		//Step 2: Create container
		long s1 = System.currentTimeMillis();
		Output output = dockerFactory.createDockerFile(a, directory+"/Dockerfile");
		long s2 = System.currentTimeMillis();
		runtime.write("Took "+(s2-s1)+"ms ");
		
		if(output.isError()) {
			abort(a.appID, "Failure to make Dockerfile");
		}else {
			runtime.write("Created Dockerfile");
		}
		
		s1 = System.currentTimeMillis();

		
		//`docker build --progress=plain -t `+appID+` `+directory+` `
		//Step 3: Build
		String[] buildCommand = {"docker", "build", "--progress=plain", "-t", a.appID, directory};
		Output build = new ExternalProcess(buildCommand).execute();
		
		s2 = System.currentTimeMillis();

		String _tmp  = "";
		
		if(build.isError()) {
			abort(a.appID, "Failure to build");
		}
				
		for(String l : build.getConsoleLog()) {
			_tmp += l;
			_tmp +="\n";
			runtime.write(l);
		}
		
		connection.notifyApp(a.appID, "Build", "All docker operations completed successfully", false);
		connection.notifyApp(a.appID, "Build", "Build took "+(s2-s1)+"ms ", false);

		
		runtime.write("Using port: "+open);
		
		//Step 4: Run 1st time
		//All other runs will be built & run with runBox
		//Enables live monitor
		String[] runCommand = {"docker", "run", "--name", a.appID, "-p", open+":"+a.port, a.appID};
		new Thread() {
			public void run() {
				try {
					//connection.updateLog(a.appID, "deployment", "Starting");
					new ExternalProcess(runCommand, true).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
				
		
		
		Thread.sleep(2000);
		
		
		
		//connection.updateLog(a.appID, "deployment", "Running");
		connection.notifyApp(a.appID, "Live on the web", "Your application has been deployed to the web", false);
		
		
		System.exit(0);
				
		
	}
	
	public void abort(String appID, String reason) {
		runtime.write("Aborting "+appID+" for "+reason);
		connection.notifyApp(appID, "Deployment Aborted", reason, true);
		System.exit(0);
	}
	
	public void rebuild(OptionsFromArgs a) {
		runtime.write("Rebuilding ... "+a.appID);
		/*
		 * We store the live port in the database, make sure its clear before reusing.
		 */
	}
	
	
	public static void main(String[] args) {
		
//		args[10] = "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";

		/*
		 * 	
		 * owner = args[0];
			appID = args[1];
			appName = args[2];
			domain = args[3];
			repo = args[4];
			branch = args[5];
			sourceFolder = args[6];
			runCommand = args[7];
			port = Integer.valueOf(args[8]);
			created = Integer.valueOf(args[9]);
			lastDeployed = Integer.valueOf(args[10]);
			valid = true;
			//
			g_username = args[11];
			g_token = args[12];
		 */
		/*
		String[] args = new String[100];
		args[0] = "romanwbruce";
		args[1] = UUID.randomUUID().toString();
		args[2] = "TestApp_"+UUID.randomUUID().toString();
		args[3] = "demo1.freeapphosting.co";
		args[4] = "test_app";
		args[5] = "master";
		args[6] = "/";
		args[7] = "node app.js";
		args[8] = "8080";
		args[9] = "1000000";
		args[10] = "1000000";
		args[11] = "romanwbruce";
		args[12] = "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";
		*/
		new Main(args);
	}

}
