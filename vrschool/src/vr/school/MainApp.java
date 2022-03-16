package vr.school;

import java.awt.color.CMMException;
import java.io.IOException;

import javax.swing.text.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import conexionMongo.Mongo;
import vr.school.plantilla.cursacos;
//import vr.school.conexion.MongoCliente;
import vr.school.vistas.cursoVistas;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane curso;
    
    public MainApp() {


    	
    }
    

    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("VRSCHOOL7");

      
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("vistas/curso.fxml"));
            curso = (BorderPane) loader.load();

            Scene scene = new Scene(curso);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("vistas/rootVistas.fxml"));
            AnchorPane cursoVistas = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            curso.setCenter(cursoVistas);

            // Give the controller access to the main app.
            cursoVistas controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
        MongoDBJDBC();
}



	private static void MongoDBJDBC() {
		// TODO Auto-generated method stub
	}



	public Object getPersonData() {
		// TODO Auto-generated method stub
		return null;
	}



	public boolean showPersonEditDialog(cursacos tempPerson) {
		// TODO Auto-generated method stub
		return false;
	}



	public boolean showPersonEditDialog(cursacos tempPerson) {
		// TODO Auto-generated method stub
		return false;
	}
	}