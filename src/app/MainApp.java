 package app;

import java.io.File;
import java.io.IOException;

import app.view.CourseCreationDialogController;
import app.view.CourseOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application{
	
	private Stage primaryStage;
	private BorderPane rootLayout;	
	@Override
	public void start (Stage primaryStage) {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("VRSCHOOL7");
			this.primaryStage.getIcons().add(new Image("file:resources" + File.separator + "images" + File.separator + "icon.png"));
			initRootLayout();
			showCourseOverview();
	}
	
	public void initRootLayout() {
		try {
			// Load root Layout from fxml file.
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view" + File.separator + "RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout );
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch (IOException e) {
			e.printStackTrace () ;
		}
	}
	
	public boolean showCourseCreateDialog() {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	    	primaryStage.hide();
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view" + File.separator + "CourseCreationDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Create Course");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.setResizable(false);
	        dialogStage.setOnCloseRequest(evt -> evt.consume());
	        
	        CourseCreationDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);

	        dialogStage.showAndWait();
	        primaryStage.show();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public void showCourseOverview() {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view" + File.separator + "CourseOverview.fxml"));
	        AnchorPane personOverview = (AnchorPane) loader.load();
	        rootLayout.setCenter(personOverview);
	        CourseOverviewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public Stage getPrimaryStage () {
		return primaryStage;
	}
	
	public static void main (String[] args) {
		launch (args);
	}

	
}