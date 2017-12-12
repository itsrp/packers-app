package com.rp.packers.packersapp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.rp.packers.packersapp.actions.ActionEnum;
import com.rp.packers.packersapp.actions.CustomerActionPerformer;
import com.rp.packers.packersapp.actions.OrderActionPerformer;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.service.CrudService;
import com.rp.packers.packersapp.service.CustomerService;
import com.rp.packers.packersapp.service.PurchaseOrderService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Component
public class PurchaseOrderController extends BaseController<PurchaseOrder>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderController.class);
	
	@Autowired
	private PurchaseOrderService service;
	
	@Autowired
	private OrderActionPerformer actionPerformer;
	
	@FXML
	private ComboBox<Customer> customerName;
	
	@FXML
	private TextArea description;
	
	@FXML
	private TextField itemCode;
	
	@FXML
	private TextField rate;
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField total;
	
	@FXML
	private TableColumn<PurchaseOrder, Long> customerIdColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, String> customerNameColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, LocalDateTime> orderDateColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, String> itemCodeColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, Integer> quantityColumn;
	
	@Autowired
	private CustomerService customerService;
	
	private static final String ORDER_SCREEN_PATH = "/fxml/orderView.fxml";

	@Override
	protected void initializeTableData() {
		customerIdColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, Long>("customerId"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, String>("customerName"));
		orderDateColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, LocalDateTime>("dateOfOrder"));
		//orderDateColumn.setCellFactory(getCustomCellFactory());
		itemCodeColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, String>("itemCode"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, Integer>("quantity"));
		
		table.getItems().setAll(allObj.values().stream()
                .collect(Collectors.toList()));
	}

	private Callback<TableColumn<PurchaseOrder, LocalDateTime>, TableCell<PurchaseOrder, LocalDateTime>> getCustomCellFactory() {
        return new Callback<TableColumn<PurchaseOrder, LocalDateTime>, TableCell<PurchaseOrder, LocalDateTime>>() {
            @Override
            public TableCell<PurchaseOrder, LocalDateTime> call(TableColumn<PurchaseOrder, LocalDateTime> param
            ) {
                return new TableCell<PurchaseOrder, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty); 
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String formatDateTime = item.format(formatter);
                            setText(formatDateTime);
                        }
                    }

                };
            }
        };
    }
	
	@Override
	protected void dispatch(ActionEnum action, MouseEvent event, Stage stage) {
		try {
			switch (action) {
			case GO_TO_HOME:
				actionPerformer.loadScreen(HOME_SCREEN_PATH, getStage(event), springContext);
				break;
			case GO_TO_ORDER_SCREEN:
				actionPerformer.loadScreen(ORDER_SCREEN_PATH, stage, springContext);
				intializeData();
				break;
			case SEARCH:
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			case ADD:
				selectedObj = new PurchaseOrder();
				actionPerformer.add(editButton, deleteButton, customerName,
						description, itemCode, rate, quantity,
						total, selectedObj, message);
				break;
			case EDIT:
				actionPerformer.edit(customerName, description, itemCode, rate, quantity, total, selectedObj);
				/*actionPerformer.resetUI(editButton, deleteButton, customerName,
						description, itemCode, rate,
						quantity, total, selectedObj, message);*/
				break;
			case SAVE:
				actionPerformer.save(customerName.getSelectionModel().getSelectedItem(),
						description.getText(), itemCode.getText(), rate.getText(), quantity.getText(),
						total.getText(), selectedObj, message, allObj);
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			case DELETE:
				actionPerformer.delete(selectedObj.getId(), allObj);
				actionPerformer.resetUI(editButton, deleteButton, customerName,
						description, itemCode, rate,
						quantity, total, selectedObj, message);
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
	public CrudService getService() {
		return service;
	}

	@Override
	protected void loadScreen(Stage stage) {
		LOGGER.info("Order button pressed.");
		dispatch(ActionEnum.GO_TO_ORDER_SCREEN, null, stage);
	}
	
	private void onFocusOutSetValue(Boolean newPropertyValue) {
		try {
			if (!newPropertyValue && !StringUtils.isEmpty(rate.getText()) && !StringUtils.isEmpty(quantity.getText())) {
				total.setText(String.valueOf(Double.valueOf(rate.getText()) * Integer.valueOf(quantity.getText())));
			}
		} catch (NumberFormatException e) {
			LOGGER.error("Exception while calculating total from Rate:{} and Quantity:{}.", rate.getText(), quantity.getText());
		}
	}

	@Override
	protected void intializeData() {
		super.intializeData();
		selectedObj = new PurchaseOrder();
		rate.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				onFocusOutSetValue(newPropertyValue);
			}

		});
		
		quantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				onFocusOutSetValue(newPropertyValue);
			}

		});
		
		customerName.setConverter(new StringConverter<Customer>() {

			@Override
			public String toString(Customer object) {
				return object.getId() + " - " + object.getName();
			}

			@Override
			public Customer fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		List<Customer> customers = customerService.getAll();
		//customers.sort(Comparator.comparing(Customer::getName));
		
		customerName.getItems().addAll(customers);
		customerName.setPromptText("Please select Customer");
	}
	
	

}
