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

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final LongProperty id;
	
	/**
	 * Default constructor.
	 */
	public User() {
		this(null, null, null);
	}
	
	/**
	 * Constructor with some initial data.
	 * @param oid 
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public User(String firstName, String lastName, Long id) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.id = new SimpleLongProperty(id);
		
	}
	
	

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;	
	}
	
	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;	
	}	

	public Long getId() {
		return id.get();
	}

	public void setId(Long id) {
		this.id.set(id);
	}
	
	public LongProperty idProperty() {
		return id;
	}
	

}