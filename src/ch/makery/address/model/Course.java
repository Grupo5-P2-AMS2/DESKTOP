package ch.makery.address.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {

	private final StringProperty title;
	private final StringProperty description;
	private final StringProperty oid;
	private final Map<String, ArrayList<Integer>> suscribers;
	private final ArrayList<ObjectProperty<Object>> elements;
	private final ArrayList<ObjectProperty<Object>> tasks;
	private final ArrayList<ObjectProperty<Object>> vrTasks;
	
	/**
	 * Default constructor.
	 */
	public Course() {
		this(null, null, null);
	}
	
	/**
	 * Constructor with some initial data.
	 * @param oid 
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Course(String title, String description, String oid) {
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
		this.oid = new SimpleStringProperty(oid);
		
		this.suscribers = null;
		this.elements = null;
		this.tasks = null;
		this.vrTasks = null;
		
	}
	
	public Course(String title, String description, String oid,
			JSONObject suscribers, JSONObject elements, JSONObject tasks,
			JSONObject vrTasks) {
		super();
		//Adding simple strings to the Course object
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
		this.oid = new SimpleStringProperty(oid);
		
		//JSONParser to parse strings and extract information
		JSONParser jsonParser = new JSONParser();
		
		//Checking suscribers exists
		if(suscribers.get("suscribers") != null) {
			//this.suscribers = null;
			//Instantiating the suscribers property of the course
			this.suscribers = new HashMap<>();
			//Getting the string of suscribers
			String suscriberString = ((Document) suscribers.get("suscribers")).toJson();
			
			//Parsing suscribers to extract id list of teachers and students suscribed
			JSONObject suscriberObject;
			try {
				suscriberObject = (JSONObject) jsonParser.parse(suscriberString);
				System.out.println("PrintCourse");
				System.out.println((ArrayList<Integer>) suscriberObject.get("teachers"));
				System.out.println((ArrayList<Integer>) suscriberObject.get("students"));
				
				ArrayList<Integer> teacherIdList = (ArrayList<Integer>) suscriberObject.get("teachers");
				ArrayList<Integer> studentIdList = (ArrayList<Integer>) suscriberObject.get("students");
				
				System.out.println(teacherIdList);
				System.out.println(studentIdList);
				System.out.println("END");
				
				this.suscribers.put("teachers", teacherIdList);
				this.suscribers.put("students", studentIdList);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			this.suscribers = null;
		}
		
		this.elements = null;
		this.tasks = null;
		this.vrTasks = null;
	}
	

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public StringProperty titleProperty() {
		return title;	
	}
	
	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public StringProperty descriptionProperty() {
		return description;	
	}	

	public String getOid() {
		return oid.get();
	}

	public void setOid(String oid) {
		this.oid.set(oid);
	}
	
	public StringProperty oidProperty() {
		return oid;
	}
	
	public ArrayList<Integer> getTeachers() {
		return suscribers.get("teachers");
	}
	
	public ArrayList<Integer> getStudents() {
		return suscribers.get("students");
	}

}