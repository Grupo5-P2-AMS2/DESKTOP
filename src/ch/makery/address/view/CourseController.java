package ch.makery.address.view;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.controlsfx.dialog.Dialogs;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import ch.makery.address.MainApp;
import ch.makery.address.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class CourseController {

		@FXML
	    private TableView<Course> courseTable;
	    @FXML
	    private TableColumn<Course, String> titleColumn;
	    @FXML
	    private TableColumn<Course, String> descriptionColumn;

	    @FXML
	    private TextField titleField = new TextField();
	    @FXML
	    private TextArea descriptionArea = new TextArea();
	    @FXML
	    private ListView<String> userList;
	    @FXML
	    private ListView<String> teacherList;
	    @FXML
	    private ListView<String> studentList;
	    
	    //attributes for new courses
	    Map<String, ArrayList<Integer>> suscribers = new HashMap<>();
    	ArrayList<Integer> teachers = new ArrayList<Integer>();
    	ArrayList<Integer> students = new ArrayList<Integer>();
    	ArrayList<Map<String, Object>> elements = new ArrayList<Map<String, Object>>();
    	Map<String, Object> element;
    	ArrayList<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
    	Map<String,Object> upload1;
    	Map<String,Object> upload2;
    	ArrayList<Map<String, Object>> vrTasks = new ArrayList<Map<String, Object>>();
    	
    	//Lists for users
    	ArrayList<Integer> teacherIdList = new ArrayList<Integer>();
        ArrayList<Integer> studentIdList = new ArrayList<Integer>();
        ArrayList<Integer> userIdList = new ArrayList<Integer>();
    	
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
	    	//Initialize data for template new course
	    	initializeTemplate();
	    	
	    	// Initialize the person table with the two columns.
	        titleColumn.setCellValueFactory(
	                cellData -> cellData.getValue().titleProperty());
	        descriptionColumn.setCellValueFactory(
	                cellData -> cellData.getValue().descriptionProperty());

	        
	        
	        // Clear person details.
	        showCourseDetails(null);

	        // Listen for selection changes and show the person details when changed.
	        courseTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showCourseDetails(newValue));
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
	     * Fills all text fields to show details about the course.
	     * If the specified course is null, all text fields are cleared.*/

	    private void showCourseDetails(Course course) {
	        if (course != null) {
	            // Fill the labels with info from the course object.
	            titleField.setText(course.getTitle());
	            descriptionArea.setText(course.getDescription());
	            
	         // Performing a read operation on the collection.
	            FindIterable<Document> fi = this.mainApp.getUserCollection().find();
	            MongoCursor<Document> cursor = fi.iterator();
	            
	            //Clear user lists
	            userList.getItems().clear();
            	teacherList.getItems().clear();
            	studentList.getItems().clear();
	            
	            //Get users suscribed to the course
	            ArrayList<Integer> teacherIDs = course.getTeachers();
	            ArrayList<Integer> studentIDs = course.getStudents();
	            System.out.println("PrintController");
	            System.out.println(teacherIDs);
	            System.out.println(studentIDs);
	            
	            //JSONParser to get info from strings
	            JSONParser jsonParser = new JSONParser();
	            String userString;
	            JSONObject userObject;

	            
	            try {
	                while(cursor.hasNext()) {          
	                	userString = cursor.next().toJson();
	                	try {
							userObject = (JSONObject) jsonParser.parse(userString);
							System.out.println(userObject.get("ID"));
							//Check which list to add the user to
							if (teacherIDs.contains(userObject.get("ID"))) {
								//Add teacher name to interface list
								teacherList.getItems().add((userObject.get("first_name").toString())+
										" "/*+(userObject.get("last_name").toString())*/);
								//Add Id to IdList
								teacherIdList.add(((Long)userObject.get("ID")).intValue());
							} else if (studentIDs.contains(userObject.get("ID"))) {
								//Add student name to interface list
								studentList.getItems().add((userObject.get("first_name").toString())+
										" "/*+(userObject.get("last_name").toString())*/);
								//Add Id to IdList

								studentIdList.add(((Long)userObject.get("ID")).intValue());
							} else {
								//Add unsuscribed user name to interface list
								userList.getItems().add((userObject.get("first_name").toString())+
										" "/*+(userObject.get("last_name").toString())*/);
								//Add Id to IdList
								userIdList.add(((Long)userObject.get("ID")).intValue());
							}
							
						} catch (ParseException e) {
							e.printStackTrace();
						} 	
	                }
	            } finally {
	                cursor.close();
	            }

	        } else {
	            // Course is null, remove all the text.
	        	titleField.setText("");
	        	descriptionArea.setText("");
	        }
	    }
	    
	    /**
	     * Called when the user clicks the new button. Opens a dialog to edit
	     * details for a new course.
	     */
	    @FXML
	    private void handleNewCourse() {
	    	Course tempCourse = new Course();
	        boolean okClicked = mainApp.showNewCourseDialog(tempCourse);
	        if (okClicked) {
	        	
	            mainApp.getCourseData().add(tempCourse);
	            mainApp.getCourseCollection().insertOne(new Document()
	                    .append("_id", tempCourse.getOid())
	                    .append("title", tempCourse.getTitle())
	                    .append("description", tempCourse.getDescription())
	            		.append("suscribers", suscribers)
	            		.append("elements", elements)
	            		.append("tasks", tasks)
	            		.append("VRtasks", vrTasks));
	        }
	    }
	  
	    
	    /**
	     * Called when the user clicks on the delete button.
	     */

	    @FXML
	    private void handleDeleteCourse() {
	        boolean okClicked = mainApp.showConfirmDeletion();
	        if (okClicked) {
		        int selectedIndex = courseTable.getSelectionModel().getSelectedIndex();
		        //delete from mongo db based on oid from element
		        if (selectedIndex >= 0) {
		        	String oid = courseTable.getItems().get(selectedIndex).getOid();
		        	Bson query = eq("_id", oid);
		        	mainApp.getCourseCollection().deleteOne(query);
		        	courseTable.getItems().remove(selectedIndex);
		        } else {
		            // Nothing selected.
		            Dialogs.create()
		                .title("No Selection")
		                .masthead("No Course Selected")
		                .message("Please select a course in the table.")
		                .showWarning();
		        }
	        }
	    }
    
    //User handling button functions
    @FXML
    private void addTeacher() {
    	int selectedUserIndex = userList.getSelectionModel().getSelectedIndex();
        int selectedCourseIndex = courseTable.getSelectionModel().getSelectedIndex();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedUserIndex >= 0) {
        	//Get course
        	String oid = courseTable.getItems().get(selectedCourseIndex).getOid();
        	
        	Map<String, ArrayList<Integer>> suscriberMap = new HashMap<>();
        	//Add user Id from list as teacher
        	teacherIdList.add(userIdList.get(selectedUserIndex));
        	suscriberMap.put("teachers", teacherIdList);
        	suscriberMap.put("students", studentIdList);
        	
        	Bson filter = eq("_id", oid);
        	Bson query = Updates.set("suscribers", suscriberMap);    	
        	
        	UpdateResult result = mainApp.getCourseCollection().updateOne(filter, query);
        	System.out.println(result);
        	
        	showCourseDetails(selectedCourse);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No Course Selected")
                .message("Please select a course in the table.")
                .showWarning();
        }
    }
    
    @FXML
    private void removeTeacher() {
    	int selectedTeacherIndex = teacherList.getSelectionModel().getSelectedIndex();
        int selectedCourseIndex = courseTable.getSelectionModel().getSelectedIndex();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedTeacherIndex >= 0) {
        	//Get course
        	String oid = courseTable.getItems().get(selectedCourseIndex).getOid();
        	
        	Map<String, ArrayList<Integer>> suscriberMap = new HashMap<>();
        	//Delete teacher Id from list
        	teacherIdList.remove(studentIdList.get(selectedTeacherIndex));
        	suscriberMap.put("teachers", teacherIdList);
        	suscriberMap.put("students", studentIdList);
        	
        	Bson filter = eq("_id", oid);
        	Bson query = Updates.set("suscribers", suscriberMap);    	
        	
        	UpdateResult result = mainApp.getCourseCollection().updateOne(filter, query);
        	System.out.println(result);
        	
        	showCourseDetails(selectedCourse);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No Course Selected")
                .message("Please select a course in the table.")
                .showWarning();
        }
    }
    
    @FXML
    private void addStudent() {
    	int selectedUserIndex = userList.getSelectionModel().getSelectedIndex();
        int selectedCourseIndex = courseTable.getSelectionModel().getSelectedIndex();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        
        if (selectedUserIndex >= 0) {
        	//Get course
        	String oid = courseTable.getItems().get(selectedCourseIndex).getOid();
        	
        	Map<String, ArrayList<Integer>> suscriberMap = new HashMap<>();
        	//Add user Id from list as student
        	studentIdList.add(userIdList.get(selectedUserIndex));
        	suscriberMap.put("teachers", teacherIdList);
        	suscriberMap.put("students", studentIdList);
        	
        	Bson filter = eq("_id", oid);
        	Bson query = Updates.set("suscribers", suscriberMap);    	
        	
        	UpdateResult result = mainApp.getCourseCollection().updateOne(filter, query);
        	System.out.println(result);
        	
        	showCourseDetails(selectedCourse);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No Course Selected")
                .message("Please select a course in the table.")
                .showWarning();
        }
    }
    
    @FXML
    private void removeStudent() {
        int selectedStudentIndex = studentList.getSelectionModel().getSelectedIndex();
        int selectedCourseIndex = courseTable.getSelectionModel().getSelectedIndex();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedStudentIndex >= 0) {
        	//Get course
        	String oid = courseTable.getItems().get(selectedCourseIndex).getOid();
        	
        	Map<String, ArrayList<Integer>> suscriberMap = new HashMap<>();
        	//Delete student Id from list
        	studentIdList.remove(studentIdList.get(selectedStudentIndex));
        	suscriberMap.put("teachers", teacherIdList);
        	suscriberMap.put("students", studentIdList);
        	
        	Bson filter = eq("_id", oid);
        	Bson query = Updates.set("suscribers", suscriberMap);    	
        	
        	UpdateResult result = mainApp.getCourseCollection().updateOne(filter, query);
        	
        	showCourseDetails(selectedCourse);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No Course Selected")
                .message("Please select a course in the table.")
                .showWarning();
        }
    }
    
    //Template initialization
    
    private void initializeTemplate() {

    	//Suscribers
    	teachers.add(101);
    	teachers.add(102);
    	suscribers.put("teachers", teachers);
    	students.add(1);
    	students.add(2);
    	students.add(3);
    	students.add(4);
    	suscribers.put( "students", students);
    	
    	//Elements
    	fillElementOrTask(elements, 1, "HTML", "Traslado de pacientes", "Información sobre el traslado de pacientes", 
    			1, "contents", "<h1>Apuntes de traslado de pacientes</h1><p>El traslado ...</p>");
    	fillElementOrTask(elements, 2, "file", "Primeros auxilios", "Información sobre primeros auxilios",
    			2, "file", "file:///media/apuntes.pdf");
    	
    	//Tasks
    	upload1 = new HashMap<>();
    	upload2 = new HashMap<>();
    	fillUpload(upload1, 11, "no está acabado pero lo subo ya", "file:///media/Ejercicio1-nacho.pdf", 
    			8, "Buen trabajo");
    	fillUpload(upload2, 12, "Entrega del ejercicio 1", "Ejercicio1-pepe.pdf", 
    			6, "Buen trabajo");
    	
    	ArrayList<Map<String, Object>> uploads = new ArrayList<Map<String, Object>>();
    	uploads.add(upload1);
    	uploads.add(upload2);
    	
    	fillElementOrTask(tasks, 13, "file", "Cambio a postura lateral", "Inmovilización de pacientes en cama",
    			1, "uploads", uploads);
    	
    	upload1 = new HashMap<>();
    	upload2 = new HashMap<>();
    	fillUpload(upload1, 13, "loren ipsum dolo sit amet...", null, 
    			5, "Hay que mejorar");
    	fillUpload(upload2, 12, "lorem ipsum chiquito de la calzada...", null, 
    			3, "Hay que mejorar");
    	
    	uploads = new ArrayList<Map<String, Object>>();
    	uploads.add(upload1);
    	uploads.add(upload2);
    	
    	fillElementOrTask(tasks, 14, "HTML", "Cambio a postura frontal", "Inmovilización de pacientes en cama de manera frontal",
    			2, "uploads", uploads);
    	//VRTasks
    	
    	
    }
 
    //Template Course Complimentary Functions
    
    private void fillElementOrTask(ArrayList<Map<String, Object>> map, int id, String type, String title, String description, int order, 
    		String lastKey, Object lastValue) {
    	element = new HashMap<>();
    	element.put("ID", id);
    	element.put("type", type);
    	element.put("title", title);
    	element.put("description", description);
    	element.put(lastKey, lastValue);
    	map.add(element);
    }
    
    private void fillUpload(Map<String, Object> upload, int studentID, String text, String file, int grade, String feedback) {
    	upload.put("studentID", studentID);
    	upload.put("text", text);
    	if (file != null) {
    		upload.put("file", file);	
    	}
    	upload.put("grade", grade);
    	upload.put("feedback", feedback);
    };
    
    private void fillVRTaks() {
    	
    }
    
    
}