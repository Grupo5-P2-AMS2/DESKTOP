package ch.makery.address;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import ch.makery.address.connection.Connection;
import ch.makery.address.view.CourseController;
import ch.makery.address.model.Course;
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

	/*private Stage primaryStage;
    private AnchorPane courseViewer;	

    public MainApp() {
    	System.out.println("Starting app");
    }
    
	@Override
	public void start(Stage primaryStage) {
		System.out.println("Loading stuff");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ClassVRroom Desktop");
        // Set the application icon.
        //this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initCourseViewer();
        System.out.println("Showing stuff");
	}

	public void initCourseViewer() {
        try {
            // Load root layout from fxml file.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseViewer.fxml"));
            courseViewer = (AnchorPane) loader.load();
            CourseController controller = loader.getController();
            
            System.out.println("Loading scene");
            // Show the scene containing the root layout.
            Scene scene = new Scene(courseViewer);
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("Scene loaded");
            
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}*/

	private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Course> courseData = FXCollections.observableArrayList();

    
	/**
	 * Constructor
	 */
	public MainApp() {
		// Add some sample data
		 MongoCollection<Document> collection = new Connection().connect();
		try (MongoCursor<Document> cur = collection.find().iterator()) {

            while (cur.hasNext()) {
           	System.out.println("next");
            	Object docId = cur.next().get("_id");
            	Object title = ((Document) docId).get("title");
            	Object description = ((Document) docId).get("description");
            	courseData.add(new Course((String)title, (String)description));

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
     *//*
    public boolean showCourseEditDialog(Course course) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CourseController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(course);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }*/	
    
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
