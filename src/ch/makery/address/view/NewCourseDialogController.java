package ch.makery.address.view;

import java.io.FileNotFoundException;
import java.io.IOException;

import ch.makery.address.connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewCourseDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
  

    private Stage dialogStage;
    private boolean okClicked = false;


    @FXML
    private void initialize() {
    }

    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() throws FileNotFoundException, IOException {
        if (isInputValid()) {
        	Connection.newCourse(titleField.getText(), descriptionField.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

 
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    //Check if any of the strings entered is empty
	private boolean isInputValid() {
		String errorMessage = "";

		if (titleField.getText() == null || titleField.getText().replaceAll(" ", "").length() == 0) {
			errorMessage += "No valid title!";
		}	
		if (descriptionField.getText() == null || descriptionField.getText().replaceAll(" ", "").length() == 0) {
			errorMessage += "No valid description!";
		}
		

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}