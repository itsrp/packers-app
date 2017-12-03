package com.rp.packers.packersapp.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Component
public class HomeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
    private Parent rootNode;
    
    @Autowired
	private ConfigurableApplicationContext springContext;
	
	@FXML
    private void onMouseClicked(MouseEvent event) throws IOException {
		Button source = (Button)event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		
		String buttonText = source.getText();
		
		if(buttonText.equals("Customer")) {
			LOGGER.info("Customer button pressed.");
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customerView.fxml"));
			fxmlLoader.setControllerFactory(springContext::getBean);
	        rootNode = fxmlLoader.load();
	        stage.setScene(new Scene(rootNode, Screen.getMainScreen().getWidth(),
	        		Screen.getMainScreen().getHeight()));
	        stage.setMaximized(false);
	        stage.show();
		}
    }

	
}
