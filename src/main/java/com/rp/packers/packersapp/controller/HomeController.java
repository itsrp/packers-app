package com.rp.packers.packersapp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Component
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
    private Parent rootNode;
    
    @Autowired
	private ConfigurableApplicationContext springContext;
    
    @Autowired
    private CustomerController customerController;
    
    @Autowired
    private PurchaseOrderController purchaseOrderController;
    
    @Autowired
    private InvoiceController invoiceController;
	
	@FXML
    private void onMouseClicked(MouseEvent event) throws IOException {
		Button source = (Button)event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		
		String buttonText = source.getText();
		
		if(buttonText.equals("Customer")) {
			LOGGER.info("Customer button pressed.");
			customerController.loadScreen(stage);
		} else if(buttonText.equals("Purchase Order")) {
			LOGGER.info("Purchase Order button pressed.");
			purchaseOrderController.loadScreen(stage);
		} else if(buttonText.equals("Invoice")) {
			LOGGER.info("Invoice button pressed.");
			invoiceController.loadScreen(stage);
		}
    }

	
}
