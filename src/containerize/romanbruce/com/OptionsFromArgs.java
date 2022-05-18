package containerize.romanbruce.com;

public class OptionsFromArgs {


	/*
    owner: String,
    appID: String,
    appName: String,
    domain: String,
    repo: String,
    branch: String,
    sourceFolder: String,
    port: Number, 
    runCommand: String,
    created: Number,
    lastDeployed: Number
    */
	
	
	public String owner, appName, appID, domain, repo, branch, sourceFolder, runCommand, g_username, g_token, dir;
	public int port, created, lastDeployed;
	public boolean valid;
	
	public OptionsFromArgs getArgs(String[] args) {
		try {
			owner = args[0];
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
			g_username = args[11];
			g_token = args[12];
			valid = true;
		}catch(Exception e) {
			System.out.println(e);
			valid = false;
		}
		return this;
	}
}
