package ch.makery.address.model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {

	private final StringProperty title;
	private final StringProperty description;
	private final StringProperty oid;
	private final ObjectProperty<Object>[] suscribers;
	private final ObjectProperty<Object>[] elements;
	private final ObjectProperty<Object>[] tasks;
	private final ObjectProperty<Object>[] vrTasks;
	
	/**
	 * Default constructor.
	 */
	public Course() {
		this(null, null);
	}
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Course(String title, String description) {
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
		
		this.oid = null;
		this.suscribers = null;
		this.elements = null;
		this.tasks = null;
		this.vrTasks = null;
		
	}
	
	public Course(StringProperty title, StringProperty description, StringProperty oid,
			ObjectProperty<Object>[] suscribers, ObjectProperty<Object>[] elements, ObjectProperty<Object>[] tasks,
			ObjectProperty<Object>[] vrTasks) {
		super();
		this.title = title;
		this.description = description;
		this.oid = oid;
		this.suscribers = suscribers;
		this.elements = elements;
		this.tasks = tasks;
		this.vrTasks = vrTasks;
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

}