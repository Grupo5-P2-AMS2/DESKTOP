package ch.makery.address.connection;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Connection {

	
	
	public MongoCollection<Document> getCourses() {
		MongoClient client = MongoClients.create(Configuration.getConfiguration().getDBURL());
		
		MongoDatabase database = client.getDatabase("ClassVRroomDB");
		MongoCollection<Document> courseCollection = database.getCollection("Courses");
		System.out.println("Collection access granted");
		
		return courseCollection;
	}
	
	public MongoCollection<Document> getUsers() {
		MongoClient client = MongoClients.create(Configuration.getConfiguration().getDBURL());
		
		MongoDatabase database = client.getDatabase("ClassVRroomDB");
		MongoCollection<Document> userCollection = database.getCollection("Users");
		System.out.println("Collection access granted");
		
		return userCollection;
	}
}
