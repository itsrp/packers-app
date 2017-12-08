package com.rp.packers.packersapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.rp.packers.packersapp.actions.ActionEnum;
import com.rp.packers.packersapp.actions.ActionPerformer;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.Model;
import com.rp.packers.packersapp.service.CrudService;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Controller
public abstract class BaseController<T extends Model> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	protected Parent rootNode;
	
	@Autowired
	protected ConfigurableApplicationContext springContext;
	
	@FXML
	protected Label message;
	
	@FXML
	protected Button editButton;

	@FXML
	protected Button deleteButton;

	@FXML
	protected Button searchButton;

	@FXML
	protected ToggleGroup searchByGroup;

	@FXML
	protected RadioButton searchById;

	@FXML
	protected RadioButton searchByName;

	@FXML
	protected TextField searchText;

	@FXML
	protected TableView<T> table;
	
	protected String searchByCriteria;
	
	//protected List<T> allObj = new ArrayList<>();
	protected Map<Long, T> allObj;

	protected T selectedObj;
	
	/*protected ActionPerformer actionPerformer;*/
	
	protected static final String HOME_SCREEN_PATH = "/fxml/HomeView.fxml";
	
	@FXML
	protected void goToHome(MouseEvent event) throws IOException {
		LOGGER.info("Home button pressed.");
		dispatch(ActionEnum.GO_TO_HOME, event, null);
	}
	
	@FXML
	protected void save(MouseEvent event) {
		LOGGER.info("Save button pressed.");
		dispatch(ActionEnum.SAVE, event, null);
	}
	
	@FXML
	protected void createNew(MouseEvent event) {
		LOGGER.info("Add new:");
		dispatch(ActionEnum.ADD, event, null);
	}
	
	@FXML
	protected void searchBy(MouseEvent event) {
		dispatch(ActionEnum.SEARCH, event, null);
	}
	
	@FXML
	protected void edit(MouseEvent event) {
		LOGGER.info("Edit : " + selectedObj.getId());
		dispatch(ActionEnum.EDIT, event, null);
	}
	
	@FXML
	protected void delete(MouseEvent event) {
		LOGGER.info("Delete : " + selectedObj.getId());
		dispatch(ActionEnum.DELETE, event, null);
		
	}
	
	protected Stage getStage(MouseEvent event) {
		Button source = (Button) event.getSource();
		return (Stage) source.getScene().getWindow();
	}
	
	protected void intializeData() {
		searchByGroup.selectedToggleProperty()
				.addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					searchByCriteria = ((RadioButton) newValue).getText();
					searchButton.setDisable(false);
				});
		
		table.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						selectedObj = table.getSelectionModel().getSelectedItem();
						editButton.setDisable(false);
						deleteButton.setDisable(false);
					}
				});
		//allObj = getService().getAll();
		allObj = (Map<Long, T>) getService().getAll().stream().collect(Collectors.toMap(T::getId, c -> c));
		initializeTableData();
	}
	
	public void delete() {
		getService().delete(selectedObj.getId());
	}

	protected abstract void initializeTableData(); 
	
	protected abstract CrudService getService();
	
	protected abstract void dispatch(ActionEnum action, MouseEvent event, Stage stage);
	
	protected abstract void loadScreen(Stage stage);
}
