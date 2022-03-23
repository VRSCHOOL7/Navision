package app.view;

import javafx.fxml. FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx. scene.control. Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import app.MainApp;
import app.database.MongoDBConnection;

public class CourseOverviewController {
	
	@FXML
	private GridPane courses ;
	@FXML
	private Label title;
	@FXML
	private Label description;
	@FXML
	private MainApp mainApp;
	@FXML
	private ListView<Document> users;
	@FXML
	private ListView<Document> teachers;
	@FXML
	private ListView<Document> students;
	
	private String courseId;
	
	public CourseOverviewController() {
		
	}
	
	@FXML
	private void initialize() throws FileNotFoundException {		
		showCourses();
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void showCourses() throws FileNotFoundException{
		ArrayList<Document> courseList = MongoDBConnection.getAll("Courses");
		for (int i = 0; i < courseList.size(); i++) {

			String descriptionText = courseList.get(i).getString("description");
			String titleText = courseList.get(i).getString("title");
			ObjectId id = courseList.get(i).getObjectId("_id");
			
			Button courseTitleBut = new Button(titleText);
			Button deleteBut = new Button("Delete");
						
			courseTitleBut.setMaxWidth(Double.MAX_VALUE);
			courseTitleBut.setMaxHeight(Double.MAX_VALUE);
			courseTitleBut.setAlignment(Pos.BASELINE_LEFT);
			courseTitleBut.setStyle("-fx-border-width:0 0 3 0;"
					+ "-fx-font-size: 18;");

			deleteBut.setId(id.toString());
			deleteBut.setMaxWidth(Double.MAX_VALUE);
			deleteBut.setMaxHeight(Double.MAX_VALUE);
			deleteBut.setStyle("-fx-border-width:2 2 3 2");


			courses.add(courseTitleBut, 0, i);
			courses.add(deleteBut, 1, i);
			courses.setAlignment(Pos.CENTER);
			
			courseTitleBut.setOnMouseClicked(e ->{
				title.setText(titleText);
				description.setText(descriptionText);
				showUsers(id);
				courseId = id.toString();
			});
			
			deleteBut.setOnMouseClicked(e -> {
				
				boolean answer = handleDeleteCourse(titleText);
				if(answer == true) {
					MongoDBConnection.delete("Courses", id.toString());
					mainApp.showCourseOverview();
				}
			});
		}
	}
	
	public void showUsers(ObjectId id) {
		ArrayList<Integer> teachersIdArray = MongoDBConnection.getTeacher(id.toString());
		ArrayList<Document> teachersArray = MongoDBConnection.getUserDetails(teachersIdArray);
		teachers.getItems().clear();
		teachers.getItems().addAll(teachersArray);
		teachers.setCellFactory(lv -> new ListCell<Document>(){
			@Override
			public void updateItem(Document user, boolean empty) {
				super.updateItem(user, empty);
				setStyle("    -fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;\r\n"
						+ "    -fx-font-size: 18pt;\r\n");
				if (user != null){
					setText(user.get("name").toString());
					setId(user.getObjectId("_id").toString());
				}
			}
		});
		
		ArrayList<Integer> studentsIdArray = MongoDBConnection.getStudents(id.toString());
		ArrayList<Document>studentsArray = MongoDBConnection.getUserDetails(studentsIdArray);
		students.getItems().clear();
		students.getItems().addAll(studentsArray);
		students.setCellFactory(lv -> new ListCell<Document>(){
			@Override
			public void updateItem(Document user, boolean empty) {
				super.updateItem(user, empty);
				setStyle("    -fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;\r\n"
						+ "    -fx-font-size: 18pt;\r\n");
				if (user != null){
					setText(user.get("name").toString());
					setId(user.getObjectId("_id").toString());
					

				}
			}
		});
		
		ArrayList<Integer> asignedUsersId = new ArrayList<Integer>();
		asignedUsersId.addAll(teachersIdArray);
		asignedUsersId.addAll(studentsIdArray);
		ArrayList<Document> nonAsignedArray = MongoDBConnection.getNotAsignedUsers(asignedUsersId);
		users.getItems().clear();
		users.getItems().addAll(nonAsignedArray);
		users.setCellFactory(lv -> new ListCell<Document>(){
			@Override
			public void updateItem(Document user, boolean empty) {
				super.updateItem(user, empty);
				setStyle("    -fx-font-family: \"Segoe UI\", Helvetica, Arial, sans-serif;\r\n"
						+ "    -fx-font-size: 18pt;\r\n");
				if (user != null){
					setText(user.get("name").toString());
					setId(user.getObjectId("_id").toString());
				}
			}
		});
	}

	
	@FXML
	private boolean handleDeleteCourse(String name) {
		    
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Course Elimination");
        alert.setHeaderText("¿Eliminar el curso " + name + "?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Aceptar");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("DarkTheme.css").toExternalForm());
		Optional<ButtonType> result = alert.showAndWait();
		 if (result.isPresent() && result.get() == ButtonType.OK) {
		     return true;
		 }
		 return false;    
	}
	
	@FXML
	private void handleInsertCourse() {
	    boolean okClicked = mainApp.showCourseCreateDialog();
	    if (okClicked) {
	        mainApp.showCourseOverview();
	    }
	}
	
	@FXML
	private void handleDeleteStudent() {
		if(students.getSelectionModel().getSelectedIndex() != -1) {
			int studentDelete = students.getSelectionModel().getSelectedItem().getInteger("id");
			MongoDBConnection.deleteUserFromCourse("students",courseId, studentDelete);
			showUsers(new ObjectId(courseId));
		}

	}
	
	@FXML
	private void handleInsertStudent() {
		if(users.getSelectionModel().getSelectedIndex() != -1) {
			int studentInsert = users.getSelectionModel().getSelectedItem().getInteger("id");
	        MongoDBConnection.insertUserToCourse("students",courseId, studentInsert);
			showUsers(new ObjectId(courseId));
		}
	}
	
	@FXML
	private void handleDeleteTeacher() {
		if(teachers.getSelectionModel().getSelectedIndex() != -1) {
			int teacherDelete = teachers.getSelectionModel().getSelectedItem().getInteger("id");
			MongoDBConnection.deleteUserFromCourse("teachers",courseId, teacherDelete);
			showUsers(new ObjectId(courseId));
		}  
	}
	
	@FXML
	private void handleInsertTeacher() {
		if(users.getSelectionModel().getSelectedIndex() != -1) {
			int teacherInsert = users.getSelectionModel().getSelectedItem().getInteger("id");
	        MongoDBConnection.insertUserToCourse("teachers",courseId, teacherInsert);
			showUsers(new ObjectId(courseId));
		}   
	}
}