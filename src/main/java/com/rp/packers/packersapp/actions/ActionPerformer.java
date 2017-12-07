package com.rp.packers.packersapp.actions;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public interface ActionPerformer<T> {

	void loadScreen(String fxmlPath, Stage stage) throws IOException;

	void filteredTable(TableView<T> customerTable, List<T> customers, String searchByCriteria,
			TextField searchText);

}
