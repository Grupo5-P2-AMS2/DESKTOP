package ch.makery.address.view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import ch.makery.address.MainApp;
import ch.makery.address.connection.Connection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CourseController {


	@FXML
	private GridPane courses;
    @FXML
	private TextField titleField;
	@FXML
	private TextArea descriptionArea;
	@FXML
	private ListView<Document> userList;
	@FXML
	private ListView<Document> teacherList;
	@FXML
	private ListView<Document> studentList;
	
	private String courseId;
	private MainApp mainApp;
	
    	public CourseController() {
    		
    	}
    	
    	@FXML
    	private void initialize() throws FileNotFoundException {
    		showCourses();
    	}
    	
    	public void setMainApp(MainApp mainApp) {
    		this.mainApp = mainApp;
    	}
    	
    	public void showCourses() throws FileNotFoundException{
    		//Get courses from connection class
    		ArrayList<Document> courseList = Connection.getAll("Courses");
    		for (int i = 0; i < courseList.size(); i++) {
    			//Set course values
	    		String descriptionText = courseList.get(i).getString("description");
				String titleText = courseList.get(i).getString("title");
				ObjectId id = courseList.get(i).getObjectId("_id");
				
				//Set course as a button with a delete button
				Button courseTitleButton = new Button(titleText);
				Button deleteButton = new Button("Delete");
							
				//Set size for the course button
				courseTitleButton.setMaxWidth(Double.MAX_VALUE);
				courseTitleButton.setMaxHeight(Double.MAX_VALUE);
				courseTitleButton.setAlignment(Pos.BASELINE_LEFT);
	
				//Set size for delete button
				deleteButton.setId(id.toString());
				deleteButton.setMaxWidth(Double.MAX_VALUE);
				deleteButton.setMaxHeight(Double.MAX_VALUE);	
	
				//Add buttons to the courses GridPane
				courses.add(courseTitleButton, 0, i);
				courses.add(deleteButton, 1, i);
				courses.setAlignment(Pos.CENTER);
				
				//Set actions to show course details if the course button is clicked
				courseTitleButton.setOnMouseClicked(e ->{
					titleField.setText(titleText);
					descriptionArea.setText(descriptionText);
					showUsers(id);
					courseId = id.toString();
				});
				
				//Set course deletion method for the delete button
				deleteButton.setOnMouseClicked(e -> {			
					boolean answer = handleDeleteCourse();
					if(answer == true) {
						Connection.delete("Courses", id.toString());
						mainApp.showCourseViewer();
					}
				});
    		}
    		
    	}
    	
    	public void showUsers(ObjectId courseId) {
    		//Set teacher list
    		ArrayList<Integer> teacherIdArray = Connection.getTeachers(courseId.toString());
    		ArrayList<Document> teacherArray = Connection.getUserList(teacherIdArray);
    		System.out.println(teacherArray);
    		teacherList.getItems().clear();
    		teacherList.getItems().addAll(teacherArray);
    		teacherList.setCellFactory(lv -> new ListCell<Document>(){
    			@Override
    			public void updateItem(Document user, boolean empty) {
    				super.updateItem(user, empty);

    				if (user == null || empty) {
    	                setText(null);
    	                setId(null);
    	            } else {
    	            	setText(user.getString("first_name"));
    					setId(user.getObjectId("_id").toString());
    	            }
    			}
    		});
    		
    		//Set student list
    		ArrayList<Integer> studentIdArray = Connection.getStudents(courseId.toString());
    		ArrayList<Document>studentArray = Connection.getUserList(studentIdArray);
    		System.out.println(studentArray);
    		studentList.getItems().clear();
    		studentList.getItems().addAll(studentArray);
    		studentList.setCellFactory(lv -> new ListCell<Document>(){
    			@Override
    			public void updateItem(Document user, boolean empty) {
    				super.updateItem(user, empty);

    				if (user == null || empty) {
    	                setText(null);
    	                setId(null);
    	            } else {
    	            	setText(user.getString("first_name"));
    					setId(user.getObjectId("_id").toString());
    	            }
    			}
    		});
    		
    		//Set unsuscribed user list
    		ArrayList<Integer> suscriberIds = new ArrayList<Integer>();
    		suscriberIds.addAll(teacherIdArray);
    		suscriberIds.addAll(studentIdArray);
    		ArrayList<Document> userArray = Connection.getUsers(suscriberIds);
    		System.out.println(userArray);
    		userList.getItems().clear();
    		userList.getItems().addAll(userArray);
    		userList.setCellFactory(param -> new ListCell<Document>(){
    			@Override
    			protected void updateItem(Document user, boolean empty) {
    				super.updateItem(user, empty);

    				if (user == null || empty) {
    	                setText(null);
    	                setId(null);
    	            } else {
    	            	setText(user.getString("first_name"));
    					setId(user.getObjectId("_id").toString());
    	            }
    			}
    		});
    	}

		//Confirmation for deletion of course
    	@FXML
    	private boolean handleDeleteCourse() {    
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this course?");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

            Optional<ButtonType> result = alert.showAndWait();
    		 if (result.isPresent() && result.get() == ButtonType.OK) {
    		     return true;
    		 }
    		 return false;    
    	}
    	
    	//Calls new dialog for course creation
    	@FXML
    	private void handleNewCourse() {
    	    boolean okClicked = mainApp.showNewCourseDialog();
    	    //Show courses if 'ok' was clicked
    	    if (okClicked) {
    	        mainApp.showCourseViewer();
    	    }
    	}
    
    	//Subscribe or unsuscribe users to a course
    
    	@FXML
    	private void addStudent() {
    		System.out.println("add student");
    		if(userList.getSelectionModel().getSelectedIndex() != -1) {
    	        Connection.addUserToCourse("students",courseId, 
    	        		userList.getSelectionModel().getSelectedItem().getInteger("ID"));
    			showUsers(new ObjectId(courseId));
    		}
    	}
    	
    	@FXML
    	private void removeStudent() {
    		if(studentList.getSelectionModel().getSelectedIndex() != -1) {
    			Connection.removeUserFromCourse("students",courseId, 
    					studentList.getSelectionModel().getSelectedItem().getInteger("ID"));
    			showUsers(new ObjectId(courseId));
    		}

    	}
    	
    	@FXML
    	private void addTeacher() {
    		System.out.println("add teacher");
    		if(userList.getSelectionModel().getSelectedIndex() != -1) {
    	        Connection.addUserToCourse("teachers",courseId, 
    	        		userList.getSelectionModel().getSelectedItem().getInteger("ID"));
    			showUsers(new ObjectId(courseId));
    		}   
    	}
    	
    	@FXML
    	private void removeTeacher() {
    		System.out.println("remove teacher");
    		if(teacherList.getSelectionModel().getSelectedIndex() != -1) {
    			Connection.removeUserFromCourse("teachers",courseId, 
    					teacherList.getSelectionModel().getSelectedItem().getInteger("ID"));
    			showUsers(new ObjectId(courseId));
    		}  
    	}
    	
    	
}