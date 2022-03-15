package ch.makery.address.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {

	private final StringProperty courseName;
	private final StringProperty description;
	private final ObjectProperty<LocalDate> datePropertyMaybe;

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
	public Course(String courseName, String description) {
		this.courseName = new SimpleStringProperty(courseName);
		this.description = new SimpleStringProperty(description);
		
		// Some initial dummy data, just for convenient testing.
		this.datePropertyMaybe = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
	}
	
	public String getCourseName() {
		return courseName.get();
	}

	public void setCourseName(String courseName) {
		this.courseName.set(courseName);
	}
	
	public StringProperty courseNameProperty() {
		return courseName;	
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

	public LocalDate getThis() {
		return datePropertyMaybe.get();
	}

	public void setThis(LocalDate date) {
		this.datePropertyMaybe.set(date);
	}
	
	public ObjectProperty<LocalDate> thisProperty() {
		return datePropertyMaybe;
	}
}