package ch.makery.address.connection;

/*import org.bson.Document;

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
}*/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

public class Connection {
	private static MongoClient client = MongoClients.create(Configuration.getConfiguration().getDBURL());
	private static MongoDatabase database = client.getDatabase("ClassVRroomDB");
	
	public static void main(String[] args) {
		
		Connection.getAll("Users");
	}
	
	public static ArrayList<Document> getAll(String collectionName) {	
		try{
			MongoCollection<Document> collection = database.getCollection(collectionName);
			FindIterable<Document> query = collection.find();
			ArrayList<Document> result = new ArrayList<Document>();
			for (Document doc : query) {
				result.add(doc);
 			};
			return result;
		}catch (Exception e) {
			System.out.println(e);
		}
		return null;
		
	}
	
	public static void insert(String collectionName, Document doc) {

		try {
			MongoCollection<Document> collection = database.getCollection(collectionName);
			collection.insertOne(doc);
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void delete(String collectionName, String ID) {

		try {
			MongoCollection<Document> collection = database.getCollection(collectionName);
			collection.deleteOne(new Document("_id", new ObjectId(ID)));
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void newCourse(String title, String description) {
        JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("src/ch/makery/address/connection/course_data.json"))
        {
            //Read JSON file for template
            Object obj = jsonParser.parse(reader);
 
            //Set values entered by the user
            JSONObject course = (JSONObject) obj;
            course.put("title", title);
            course.put("description", description);
            
            //Insert custom data + template to courses collection
            Connection.insert("Courses", new Document().parse(course.toString()));
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    
	}
	
	//Get all unsuscribed users
	public static ArrayList<Document> getUsers(ArrayList<Integer> suscribersId) {
    		MongoCollection<Document> collection = database.getCollection("Users");
    		Bson firstFilter = Filters.nin("ID", suscribersId);
    		FindIterable<Document> query = collection.find(firstFilter);
    		ArrayList<Document> result = new ArrayList<Document>();
    		for (Document doc : query) {
    			result.add(doc);
    		};
    		return result;
	}
	
	//Get users suscribed as teachers
	public static ArrayList<Integer> getTeachers(String courseId) {
		ArrayList<Integer> result = getSubscribers(courseId, "teachers");
		return result;
	}
	
	//Get users suscribed as students
	public static ArrayList<Integer> getStudents(String courseId) {
		ArrayList<Integer> result = getSubscribers(courseId, "students");
		return result;
	}
	
	//Get teachers or students according to the method that called it
	private static ArrayList<Integer> getSubscribers(String courseId, String name) {
		MongoCollection<Document> collection = database.getCollection("Courses");
		Bson firstFilter = Filters.eq("_id", new ObjectId( courseId));
		Bson projection = Projections.fields(Projections.include("subscribers."+name));
		FindIterable<Document> query = collection.find(firstFilter).projection(projection);
		ArrayList<Integer> result = new ArrayList<Integer>();
		result = query.first().get("subscribers", Document.class).get(name, result.getClass());
		return result;
	}
	 
	//Get list of users according to the list of userIds
	public static ArrayList<Document> getUserList(ArrayList<Integer> userIds) {
		MongoCollection<Document> collection = database.getCollection("Users");
		Bson firstFilter = Filters.in("ID", userIds);
		FindIterable<Document> query = collection.find(firstFilter);
		ArrayList<Document> result = new ArrayList<Document>();
		for (Document doc : query) {
			result.add(doc);
		};
		return result;
	}
	
	//Update course subscriber list with new user Id
	public static void addUserToCourse(String listName, String courseId, int userId) {
		MongoCollection<Document> collection = database.getCollection("Courses");
		Bson query = new Document().append("_id", new ObjectId(courseId));
		Bson fields = new Document().append("subscribers." + listName, userId);
		Bson update = new Document("$push",fields);
		collection.updateOne(query, update);
	}

	//Remove user Id from course subscriber list
	public static void removeUserFromCourse(String listName, String courseId, int userId) {
		MongoCollection<Document> collection = database.getCollection("Courses");
		Bson query = new Document().append("_id", new ObjectId(courseId));
		Bson fields = new Document().append("subscribers."+listName, new Document().append( "$eq", userId));
		Bson update = new Document("$pull",fields);
		collection.updateOne( query, update );
	}
	
}
