package containerize.romanbruce.com.mongo;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import containerize.romanbruce.com.Env;
import containerize.romanbruce.com.OptionsFromArgs;


public class MongoConnection {

	String url = "mongodb://cluster0.l17ml.mongodb.net";
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> apps;
	MongoCollection<Document> notify;

	
	/*		APPLICATION SCHEMA 
		    date: String,
		    appID: String,
		    error: Boolean,
		    title: String,
		    body: String,
	 */

	public MongoConnection() {
		try {
			ConnectionString connectionString = new ConnectionString(Env.url);
			MongoClientSettings settings = MongoClientSettings.builder()
			        .applyConnectionString(connectionString)
			        .build();
			mongoClient = MongoClients.create(settings);
			database = mongoClient.getDatabase("test");
		} catch (Exception e) {
			e.printStackTrace();
		}

		apps = database.getCollection("apps");
		notify = database.getCollection("appnotifications");
	}
	
	public void notifyApp(String appID, String title, String body, boolean isError) {
		int time = (int)System.currentTimeMillis();
		Document newApp = new Document()
				.append("date",time)
				.append("appID", appID)
				.append("error", isError)
				.append("title", title)
				.append("body", body);
		notify.insertOne(newApp);
	}

	public void insertApplicationData(OptionsFromArgs args, int livePort) {
		Document newApp = new Document()
                					.append("owner", args.owner)
                					.append("appID", args.appID)
                					.append("appName", args.appName)
                					.append("domain", args.domain)
                					.append("repo", args.repo)
                					.append("branch", args.branch)
                					.append("sourceFolder", args.sourceFolder)
                					.append("port", args.port)
                					.append("runCommand", args.runCommand)
                					.append("created", args.created)
                					.append("lastDeployed", args.lastDeployed);
		apps.insertOne(newApp);
	}
	
	
}
