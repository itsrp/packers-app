package com.rp.packers.packersapp.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.rp.packers.packersapp.actions.ActionEnum;
import com.rp.packers.packersapp.actions.CustomerActionPerformer;
import com.rp.packers.packersapp.actions.InvoiceActionPerformer;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.Invoice;
import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.service.CrudService;
import com.rp.packers.packersapp.service.CustomerService;
import com.rp.packers.packersapp.service.InvoiceService;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Controller
public class InvoiceController extends BaseController<Invoice>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
	
	@FXML
	private TableColumn<Invoice, Long> invoiceIdColumn;
	
	@FXML
	private TableColumn<Invoice, String> customerNameColumn;
	
	@FXML
	private ComboBox<Customer> customerName;
	
	@FXML
	private TextField total;
	
	@FXML
	private TextField cgst;
	
	@FXML
	private TextField sgst;
	
	@FXML
	private TextField finalAmount;
	
	@FXML
	private TableColumn<PurchaseOrder, CheckBox> selectColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, Long> orderNoColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, String> descriptionColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, String> itemCodeColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, Integer> quantityColumn;
	
	@FXML
	private TableColumn<PurchaseOrder, Double> rateColumn;
	
	@FXML
	private TableView<PurchaseOrder> orderTable;
	
	@Autowired
	private InvoiceActionPerformer actionPerformer;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	protected Map<Long, PurchaseOrder> selectedOrders;
	
	private static final String INVOICE_SCREEN_PATH = "/fxml/invoiceView.fxml";

	@Override
	protected void initializeTableData() {
		invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<Invoice, Long>("id"));
		customerNameColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("customerName"));

		table.getItems().setAll(allObj.values().stream()
                .collect(Collectors.toList()));
		
	}

	@Override
	protected CrudService getService() {
		return invoiceService;
	}

	@Override
	protected void dispatch(ActionEnum action, MouseEvent event, Stage stage) {
		try {
			switch (action) {
			case GO_TO_HOME:
				actionPerformer.loadScreen(HOME_SCREEN_PATH, getStage(event), springContext);
				break;
			case GO_TO_INVOICE_SCREEN:
				actionPerformer.loadScreen(INVOICE_SCREEN_PATH, stage, springContext);
				intializeData();
				break;
			case SAVE:
				actionPerformer.save(selectedOrders,
						total.getText(), cgst.getText(), sgst.getText(), finalAmount.getText(),
						selectedObj, message, allObj);
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			case SEARCH:
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			/*case ADD:
				actionPerformer.add(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);
				break;
			case EDIT:
				actionPerformer.edit(custName, address, vatOrTin, vendorCode, selectedObj);
				actionPerformer.resetUI(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);
				break;
			
			case DELETE:
				actionPerformer.delete(selectedObj.getId(), allObj);
				actionPerformer.resetUI(editButton, deleteButton, custName, address, vatOrTin, vendorCode, message,
						selectedObj);
				actionPerformer.filteredTable(table, allObj, searchByCriteria, searchText);
				break;
			default:
				LOGGER.warn("Action {} not found. Please check.", action.name());*/
			}
		} catch (IOException e) {
			LOGGER.error("Exception :", e);
		}
		
	}

	@Override
	protected void loadScreen(Stage stage) {
		//LOGGER.info("Customer button pressed.");
		dispatch(ActionEnum.GO_TO_INVOICE_SCREEN, null, stage);
	}
	
	private void loadOrdersByCustomer(List<PurchaseOrder> orders) {
		selectedOrders = new HashMap<>();
		orderTable.getItems().setAll(orders);
	} 
	
	private void initializeOrdersTableData() {
		orderNoColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, Long>("id"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, String>("description"));
		itemCodeColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, String>("itemCode"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, Integer>("quantity"));
		rateColumn.setCellValueFactory(new PropertyValueFactory<PurchaseOrder, Double>("rate"));
		selectColumn.setMinWidth(20);
		selectColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PurchaseOrder, CheckBox>, ObservableValue<CheckBox>>() {
			@Override
			public ObservableValue<CheckBox> call(CellDataFeatures<PurchaseOrder, CheckBox> param) {
				PurchaseOrder po = param.getValue();
				CheckBox checkBox = new CheckBox();
				checkBox.selectedProperty().setValue(po.getIsSelected());
				checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                            Boolean oldVal, Boolean newVal) {
                        po.setIsSelected(newVal);
                        if(newVal) {
                        	selectedOrders.put(po.getId(), po);
                        } else {
                        	selectedOrders.remove(po.getId());
                        }
                        
                        doActivityOnCheckboxChange();
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
			}});
	}
	
	private void doActivityOnCheckboxChange() {
		if(!CollectionUtils.isEmpty(selectedOrders)) {
			Double sum = selectedOrders.values().stream()
			.filter(order -> order.getQuantity() != null)
			.mapToDouble(order -> order.getQuantity() * order.getRate())
			.sum();
			
			total.setText(String.valueOf(sum));
			
			finalAmount.setText(String
					.valueOf(sum + ((Double.valueOf(cgst.getText()) + Double.valueOf(sgst.getText())) * sum) / 100));
			saveButton.setDisable(false);
		} else {
			total.setText("");
			finalAmount.setText("");
			saveButton.setDisable(true);
		}
		
	}
	
	@Override
	protected void intializeData() {
		super.intializeData();
		initializeOrdersTableData();
		customerName.valueProperty().addListener(new ChangeListener<Customer>() {
			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
				loadOrdersByCustomer(newValue.getOrder());
			}
        });
		
		/*rate.focusedProperty().addListener(new ChangeListener<Boolean>() {
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

		});*/
		
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
