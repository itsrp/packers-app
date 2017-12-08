package com.rp.packers.packersapp.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import com.rp.packers.packersapp.model.Customer;
import com.sun.glass.ui.Screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public interface ActionPerformer<T> {
	
	default void loadScreen(String fxmlPath, Stage stage, ConfigurableApplicationContext springContext) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
		fxmlLoader.setControllerFactory(springContext::getBean);
		Parent rootNode = fxmlLoader.load();
		stage.setScene(new Scene(rootNode, Screen.getMainScreen().getWidth(), Screen.getMainScreen().getHeight()));
		stage.setMaximized(false);
		stage.show();
	}

	void filteredTable(TableView<T> customerTable, Map<Long, T> customers, String searchByCriteria,
			TextField searchText);

	void delete(Long id, Map<Long, T> allObj);
	

}
