package com.rp.packers.packersapp;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.sun.glass.ui.Screen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
@ComponentScan(basePackages="com.rp.packers")
public class PackersAppApplication extends Application {
	
	private ConfigurableApplicationContext springContext;
    private Parent rootNode;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
    public void init() throws Exception {
        springContext = SpringApplication.run(PackersAppApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/HomeView.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(rootNode, Screen.getMainScreen().getWidth(),
        		Screen.getMainScreen().getHeight()));
        stage.setMaximized(false);

        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
