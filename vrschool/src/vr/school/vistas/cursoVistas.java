package vr.school.vistas;

import ch.makery.address.model.Person;
import javafx.fxml.FXML;
import vr.school.MainApp;
import vr.school.plantilla.cursacos;


public class cursoVistas {
 
    private MainApp mainApp;


    public cursoVistas() {
    }


    @FXML
    private void initialize() {
}
    
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } 
    }
    
    
    @FXML
    private void handleNewPerson() {
        cursacos tempPerson = new cursacos();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;


    }
}