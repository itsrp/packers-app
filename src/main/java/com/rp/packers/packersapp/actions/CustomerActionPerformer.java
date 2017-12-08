package com.rp.packers.packersapp.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.controller.CustomerController;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.service.CustomerService;
import com.sun.glass.ui.Screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class CustomerActionPerformer implements ActionPerformer<Customer>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private ConfigurableApplicationContext springContext;
	
	@Autowired
	private CustomerService customerService;
	
	/*@Override
	public void loadScreen(String fxmlPath, Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
		fxmlLoader.setControllerFactory(springContext::getBean);
		Parent rootNode = fxmlLoader.load();
		stage.setScene(new Scene(rootNode, Screen.getMainScreen().getWidth(), Screen.getMainScreen().getHeight()));
		stage.setMaximized(false);
		stage.show();
		
	}*/

	@Override
	public void filteredTable(TableView<Customer> customerTable, Map<Long, Customer> customers, String searchByCriteria, TextField searchText) {
		List<Customer> list = new ArrayList<>();
		if (searchByCriteria != null) {
			LOGGER.info("searching by: " + searchByCriteria);
			try {
				switch (searchByCriteria) {
				case "By ID":
					list = customers.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(customer -> customer.getId().equals(Long.valueOf(searchText.getText())))
							.collect(Collectors.toList());
					break;
				case "By Name":
					list = customers.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(customer -> customer.getName().toLowerCase().contains(searchText.getText().toLowerCase()))
							.collect(Collectors.toList());
					break;
				default:
					list = customers.values().stream()
			                .collect(Collectors.toList());
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Exception while searching.", e);
			}
		} else {
			LOGGER.info("searching all ");
			list = customers.values().stream()
	                .collect(Collectors.toList());
		}
		
		customerTable.getItems().clear();
		customerTable.getItems().setAll(list);
		
	}

	public void add(Button editButton, Button deleteButton, TextField custName, TextArea address, TextField vatOrTin, TextField vendorCode, Label message, Customer selectedCustomer) {
		resetUI(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message, selectedCustomer);
	}

	public void resetUI(Button editButton, Button deleteButton, TextField custName, TextArea address,
			TextField vatOrTin, TextField vendorCode, Label message, Customer selectedCustomer) {
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		selectedCustomer = null;
		custName.clear();
		address.clear();
		vatOrTin.clear();
		vendorCode.clear();
		message.setText("");
		
	}

	public void edit(TextField custName, TextArea address, TextField vatOrTin, TextField vendorCode, Customer selectedCustomer) {
		custName.setText(selectedCustomer.getName());
		address.setText(selectedCustomer.getAddress());
		vatOrTin.setText(selectedCustomer.getTin());
		vendorCode.setText(selectedCustomer.getVendorCode());
		
	}

	public void save(String name, String address, String vatOrTin, String vendorCode, Customer selectedCustomer, Label message, Map<Long, Customer> customers) {
		Customer newCustomer = buildCustomer(name, address, vatOrTin, vendorCode, selectedCustomer);
		customerService.create(newCustomer);
		message.setText("Customer saved successfully.");
		if(selectedCustomer == null) {
			customers.put(newCustomer.getId(), newCustomer);
		}
	}
	
	private Customer buildCustomer(String name, String address, String vatOrTin, String vendorCode, Customer selectedCustomer) {
		if(selectedCustomer == null) {
			selectedCustomer = new Customer();
		}
		selectedCustomer.setName(name);
		selectedCustomer.setAddress(address);
		selectedCustomer.setTin(vatOrTin);
		selectedCustomer.setVendorCode(vendorCode);
		return selectedCustomer;
	}

	@Override
	public void delete(Long id, Map<Long, Customer> allObj) {
		customerService.delete(id);
		allObj.remove(id);
	}
}
