package ch.makery.address;

import java.io.IOException;

import ch.makery.address.view.CourseController;
import ch.makery.address.view.NewCourseDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start (Stage primaryStage) {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Course Viewer");
			this.primaryStage.setResizable(false);
			initRootLayout();
			showCourseViewer();

	}
	
	public void initRootLayout() {
		try {
			// Load root Layout from fxml file.
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout );
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (IOException e) {
			e.printStackTrace () ;
		}
	}
	
	//Show courses
	public void showCourseViewer() {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/CourseViewer.fxml"));
	        AnchorPane courseViewer = (AnchorPane) loader.load();
	        rootLayout.setCenter(courseViewer);
	        CourseController controller = loader.getController();
	        controller.setMainApp(this);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	//Show course creation dialog
	public boolean showNewCourseDialog() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	    	primaryStage.hide();
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
	        dialogStage.setResizable(false);
	        dialogStage.setOnCloseRequest(event -> event.consume());
	        
	        NewCourseDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        dialogStage.showAndWait();
	        primaryStage.show();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public Stage getPrimaryStage () {
		return primaryStage;
	}
	
	public static void main (String[] args) {
		launch (args);
	}

}
