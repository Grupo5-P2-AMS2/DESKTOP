package ch.makery.address.connection;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Connection {

	
	
	public MongoCollection<Document> connect() {
		MongoClient client = MongoClients.create(Configuration.getConfiguration().getDBURL());
		
		MongoDatabase database = client.getDatabase("ClassVRroomDB");
		MongoCollection<Document> testCollection = database.getCollection("Courses");
		System.out.println("Collection access granted");
		
		return testCollection;
	}
}
