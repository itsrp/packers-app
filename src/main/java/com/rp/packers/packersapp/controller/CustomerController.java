package com.rp.packers.packersapp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.service.CustomerService;
import com.sun.glass.ui.Screen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Component
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	private Parent rootNode;
	
	@FXML
	private TextField custName;
	
	@FXML
	private TextArea address;

	@FXML
	private TextField vatOrTin;

	@FXML
	private TextField vendorCode;
	
	@FXML
	private Label message;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ConfigurableApplicationContext springContext;

	@FXML
	private void goToHome(MouseEvent event) throws IOException {
		LOGGER.info("Home button pressed.");
		Button source = (Button) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/HomeView.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootNode = fxmlLoader.load();
		stage.setScene(new Scene(rootNode, Screen.getMainScreen().getWidth(), Screen.getMainScreen().getHeight()));
		stage.setMaximized(false);
		stage.show();
	}
	
	@FXML
	private void saveCustomer(MouseEvent event) throws IOException {
		LOGGER.info("Save Customer button pressed.");
		
		Customer customer = buildCustomer(custName.getText(), address.getText(), vatOrTin.getText(), vendorCode.getText());
		
		customerService.create(customer);
		
		message.setText("Customer saved successfully.");
		
		LOGGER.info("Customer saved successfully with ID: " + customer.getId());
	}

	private Customer buildCustomer(String name, String address, String vatOrTin, String vendorCode) {
		return new Customer(name, address, vatOrTin, vendorCode);
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	
}
