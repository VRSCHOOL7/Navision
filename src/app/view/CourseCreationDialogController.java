package app.view;


import java.io.FileNotFoundException;
import java.io.IOException;
import app.database.MongoDBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CourseCreationDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
  

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
        	MongoDBConnection.insertCourse(titleField.getText(), descriptionArea.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

 
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


	private boolean isInputValid() {
		String errorMessage = "";

		if (titleField.getText() == null || titleField.getText().replaceAll(" ", "").length() == 0) {
			errorMessage += "Titulo no puede ser vacio!\n";
		}
		if (descriptionArea.getText() == null || descriptionArea.getText().replaceAll(" ", "").length() == 0) {
			errorMessage += "Descripcion no puede ser vacia!";
		}
		

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Corrige los datos introducidos");
			alert.setContentText(errorMessage);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("DarkTheme.css").toExternalForm());

			alert.showAndWait();

			return false;
		}
	}
}