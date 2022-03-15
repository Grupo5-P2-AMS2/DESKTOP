package ch.makery.address;

import java.io.IOException;

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

	private Stage primaryStage;
    private AnchorPane courseViewer;	

    public MainApp() {
    	System.out.println("Starting app");
    }
    
	@Override
	public void start(Stage primaryStage) {
		System.out.println("Loading stuff");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
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
	}

	
}
