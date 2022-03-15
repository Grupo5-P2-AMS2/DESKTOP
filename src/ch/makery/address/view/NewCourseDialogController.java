package ch.makery.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import ch.makery.address.model.Course;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class NewCourseDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField oidField;
    @FXML
    private TextField suscribersField;
    @FXML
    private TextField elementsField;
    @FXML
    private TextField tasksField;
    @FXML
    private TextField vrTasksField;

    private Stage dialogStage;
    private Course course;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param person
     */
    public void setCourse(Course course) {
        this.course = course;

        titleField.setText(course.getTitle());
        descriptionField.setText(course.getDescription());
        oidField.setText(null);
        suscribersField.setText(null);
        elementsField.setText(null);
        tasksField.setText(null);
        vrTasksField.setText(null);
        
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            course.setTitle(titleField.getText());
            course.setDescription(descriptionField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n"; 
        }
        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) {
            errorMessage += "No valid description!\n"; 
        }
        

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
        	Dialogs.create()
		        .title("Invalid Fields")
		        .masthead("Please correct invalid fields")
		        .message(errorMessage)
		        .showError();
            return false;
        }
    }
}