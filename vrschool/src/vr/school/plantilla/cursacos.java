package vr.school.plantilla;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

public class cursacos {
	
	private final StringProperty titulo= new StringProperty();;
	private final StringProperty desripcion = new StringProperty();

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } 
    }
    
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

	public StringProperty getDesripcion() {
		return desripcion;
	}

	public StringProperty getTitulo() {
		return titulo;
	}
	
	
}
