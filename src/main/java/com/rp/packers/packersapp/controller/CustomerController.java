package com.rp.packers.packersapp.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.actions.ActionEnum;
import com.rp.packers.packersapp.actions.CustomerActionPerformer;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.service.CustomerService;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Component
public class CustomerController extends BaseController<Customer>{

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	@FXML
	private TextField custName;

	@FXML
	private TextArea address;

	@FXML
	private TextField vatOrTin;

	@FXML
	private TextField vendorCode;

	@FXML
	private TableColumn<Customer, Long> customerId;

	@FXML
	private TableColumn<Customer, String> customerName;

	@FXML
	private TableColumn<Customer, String> customerVat;

	@FXML
	private TableColumn<Customer, String> customerAddress;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerActionPerformer actionPerformer;
	
	private static final String CUSTOMER_SCREEN_PATH = "/fxml/customerView.fxml";
	
	@Override
	protected void dispatch(ActionEnum action, MouseEvent event, Stage stage) {
		try {
			switch (action) {
			case GO_TO_HOME:
				actionPerformer.loadScreen(HOME_SCREEN_PATH, getStage(event), springContext);
				break;
			case GO_TO_CUSTOMER_SCREEN:
				actionPerformer.loadScreen(CUSTOMER_SCREEN_PATH, stage, springContext);
				intializeData();
				selectedObj = new Customer();
				break;
			case SEARCH:
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			case ADD:
				selectedObj = new Customer();
				actionPerformer.add(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);
				break;
			case EDIT:
				actionPerformer.edit(custName, address, vatOrTin, vendorCode, selectedObj);
				/*actionPerformer.resetUI(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);*/
				break;
			case SAVE:
				actionPerformer.save(custName.getText(), address.getText(), vatOrTin.getText(), vendorCode.getText(),
						selectedObj, message, allObj);
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			case DELETE:
				actionPerformer.delete(selectedObj.getId(), allObj);
				actionPerformer.resetUI(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			default:
				LOGGER.warn("Action {} not found. Please check.", action.name());
			}
		} catch (IOException e) {
			LOGGER.error("Exception :", e);
		}
	}
	
	@Override
	protected void loadScreen(Stage stage) {
		LOGGER.info("Customer button pressed.");
		dispatch(ActionEnum.GO_TO_CUSTOMER_SCREEN, null, stage);
	}
	
	@Override
	protected void initializeTableData() {
		customerId.setCellValueFactory(new PropertyValueFactory<Customer, Long>("id"));
		customerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		customerVat.setCellValueFactory(new PropertyValueFactory<Customer, String>("tin"));
		customerAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));

		table.getItems().setAll(allObj.values().stream()
                .collect(Collectors.toList()));
	}
 
	@Override
	public CustomerService getService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}
