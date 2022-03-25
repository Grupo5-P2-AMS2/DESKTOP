package ch.makery.address;

import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;
import org.json.simple.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import ch.makery.address.connection.Connection;
import ch.makery.address.model.Course;
import ch.makery.address.view.ConfirmDeletionController;
import ch.makery.address.view.CourseController;
import ch.makery.address.view.NewCourseDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class MainApp extends Application {

	

	private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Course> courseData = FXCollections.observableArrayList();
    private static MongoCollection<Document> courseCollection;
    private static MongoCollection<Document> userCollection;
    
	/**
	 * Constructor
	 */
	public MainApp() {
		// Add some sample data
		Connection connection = new Connection();
		 courseCollection = connection.getCourses();
		 userCollection = connection.getUsers();
		try (MongoCursor<Document> cur = courseCollection.find().iterator()) {

            while (cur.hasNext()) {
           	System.out.println("next");
           	var next = cur.next();
            	Object title = next.get("title");
            	Object description = next.get("description");
            	
            	//JSON Objects
            	JSONObject suscribers = new JSONObject();
            	suscribers.put("suscribers", next.get("suscribers"));
            	JSONObject elements = new JSONObject();
            	elements.put("elements", next.get("elements"));
            	JSONObject tasks = new JSONObject();
            	tasks.put("tasks", next.get("tasks"));
            	JSONObject vrTasks = new JSONObject();
            	vrTasks.put("vrTasks", next.get("vrTasks"));
            	
            	courseData.add(new Course((String)title, (String)description, (String) next.get("_id").toString(),
            		 suscribers, elements, tasks, vrTasks));
            }
		}
	}
  
	/**
	 * Returns the data as an observable list of Persons. 
	 * @return
	 */
	public ObservableList<Course> getCourseData() {
		return courseData;
	}
    
    @Override
    public void start(Stage primaryStage) {
    	System.out.println("Loading stuff");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        
        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();
        System.out.println("Showing stuff");
        showCourseViewer();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showCourseViewer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseViewer.fxml"));
            AnchorPane courseViewer = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(courseViewer);

            // Give the controller access to the main app.
            CourseController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * 
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showNewCourseDialog(Course course) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NewCourseDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Course");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            NewCourseDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCourse(course);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }	
    
    public boolean showConfirmDeletion() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ConfirmDeletion.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirm deletion");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ConfirmDeletionController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }	
    
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public MongoCollection<Document> getCourseCollection() {
		return MainApp.courseCollection;
	}
	
	public MongoCollection<Document> getUserCollection() {
		return MainApp.userCollection;
	}
}
