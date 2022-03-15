package ch.makery.address.view;

import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.controlsfx.dialog.Dialogs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;

import ch.makery.address.MainApp;
import ch.makery.address.connection.Connection;
import ch.makery.address.model.Course;
public class CourseController {

	   @FXML
	    private TableView<Course> courseTable;
	    @FXML
	    private TableColumn<Course, String> titleColumn;
	    @FXML
	    private TableColumn<Course, String> descriptionColumn;

	    @FXML
	    private Label titleLabel;
	    @FXML
	    private Label descriptionLabel;
	    @FXML
	    private Label oidLabel;
	    @FXML
	    private Label subscribersLabel;
	    @FXML
	    private Label elementsLabel;
	    @FXML
	    private Label tasksLabel;
	    @FXML
	    private Label vrTasksLabel;

	    // Reference to the main application.
	    private MainApp mainApp;

	    /**
	     * The constructor.
	     * The constructor is called before the initialize() method.
	     */
	    public CourseController() {
	    }

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
	    @FXML
	    private void initialize() {
	        // Initialize the person table with the two columns.
	        titleColumn.setCellValueFactory(
	                cellData -> cellData.getValue().titleProperty());
	        descriptionColumn.setCellValueFactory(
	                cellData -> cellData.getValue().descriptionProperty());

	        // Clear person details.
	        //showCourseDetails(null);

	        // Listen for selection changes and show the person details when changed.
	        //courseTable.getSelectionModel().selectedItemProperty().addListener(
	          //      (observable, oldValue, newValue) -> showCourseDetails(newValue));
	    }

	    /**
	     * Is called by the main application to give a reference back to itself.
	     * 
	     * @param mainApp
	     */
	    public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;

	        // Add observable list data to the table
	        courseTable.setItems(mainApp.getCourseData());
	    }
	    
	    /**
	     * Fills all text fields to show details about the person.
	     * If the specified person is null, all text fields are cleared.
	     * 
	     * @param person the person or null
	     */
	    /*private void showPersonDetails(Course course) {
	        if (course != null) {
	            // Fill the labels with info from the person object.
	            firstNameLabel.setText(person.getFirstName());
	            lastNameLabel.setText(person.getLastName());
	            streetLabel.setText(person.getStreet());
	            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
	            cityLabel.setText(person.getCity());

	            birthdayLabel.setText(DateUtil.format(person.getBirthday()));

	        } else {
	            // Person is null, remove all the text.
	            firstNameLabel.setText("");
	            lastNameLabel.setText("");
	            streetLabel.setText("");
	            postalCodeLabel.setText("");
	            cityLabel.setText("");
	            birthdayLabel.setText("");
	        }
	    }
	    
	    /**
	     * Called when the user clicks the new button. Opens a dialog to edit
	     * details for a new person.
	     */
	    @FXML
	    private void handleNewCourse() {
	    	Course tempCourse = new Course();
	        boolean okClicked = mainApp.showNewCourseDialog(tempCourse);
	        if (okClicked) {
	            mainApp.getCourseData().add(tempCourse);
      
	        }
	    }
	  
	    
	    /**
	     * Called when the user clicks on the delete button.
	     */
    @FXML
    private void handleDeleteCourse() {
        int selectedIndex = courseTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	Bson query = eq("_id", new ObjectId("622794b2acc0dbccea8388b4"));
        	MainApp.collection.deleteOne(query);
        	courseTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No Person Selected")
                .message("Please select a person in the table.")
                .showWarning();
        }
    }

}