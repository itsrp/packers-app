package com.rp.packers.packersapp.actions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.controller.PurchaseOrderController;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.service.PurchaseOrderService;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Component
public class OrderActionPerformer implements ActionPerformer<PurchaseOrder>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderActionPerformer.class);
	
	@Autowired
	private PurchaseOrderService service;

	@Override
	public void filteredTable(TableView<PurchaseOrder> table, Map<Long, PurchaseOrder> orders,
			String searchByCriteria, TextField searchText) {
		List<PurchaseOrder> list = new ArrayList<>();
		if (searchByCriteria != null) {
			LOGGER.info("searching by: " + searchByCriteria);
			try {
				switch (searchByCriteria) {
				case "By ID":
					list = orders.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(order -> order.getCustomer().getId().equals(Long.valueOf(searchText.getText())))
							.collect(Collectors.toList());
					break;
				case "By Name":
					list = orders.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(order -> order.getCustomer().getName().toLowerCase().contains(searchText.getText().toLowerCase()))
							.collect(Collectors.toList());
					break;
				default:
					list = orders.values().stream()
			                .collect(Collectors.toList());
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Exception while searching.", e);
			}
		} else {
			LOGGER.info("searching all ");
			list = orders.values().stream()
	                .collect(Collectors.toList());
		}
		
		table.getItems().clear();
		table.getItems().setAll(list);
		
	}
	
	private PurchaseOrder buildPurchaseOrder(Customer selectedItem, String description, String itemCode, String rate, String quantity, String total,
			PurchaseOrder selectedObj) {
		if(selectedObj == null) {
			selectedObj = new PurchaseOrder();
			selectedObj.setDateOfOrder(LocalDateTime.now());
			selectedObj.setIsCompleted(false);
		}
		selectedObj.setCustomer(selectedItem);
		selectedObj.setDescription(description);
		selectedObj.setItemCode(itemCode);
		selectedObj.setRate(Double.valueOf(rate));
		selectedObj.setQuantity(Integer.valueOf(quantity));
		return selectedObj;
	}

	public void save(Customer selectedItem, String description, String itemCode, String rate, String quantity, String total,
			PurchaseOrder selectedObj, Label message, Map<Long, PurchaseOrder> allObj) {
		PurchaseOrder newOrder = buildPurchaseOrder(selectedItem, description, itemCode, rate, quantity, total, selectedObj);
		service.create(newOrder);
		message.setText("Order saved successfully.");
		if(selectedObj == null) {
			allObj.put(newOrder.getId(), newOrder);
			//allObj.add(newOrder);
		}
	}
	
	public void add(Button editButton, Button deleteButton, ComboBox<Customer> customerName,
			TextArea description, TextField itemCode, TextField rate,
			TextField quantity, TextField total, PurchaseOrder selectedObj, Label message) {
		
		resetUI(editButton, deleteButton, customerName,
				description, itemCode, rate,
				quantity, total, selectedObj, message);
	}
	
	public void resetUI(Button editButton, Button deleteButton, ComboBox<Customer> customerName, TextArea description,
			TextField itemCode, TextField rate, TextField quantity, TextField total, PurchaseOrder selectedObj,
			Label message) {
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		selectedObj = null;
		
		customerName.getSelectionModel().clearSelection();
		description.clear();
		itemCode.clear();
		rate.clear();
		quantity.clear();
		total.clear();
		
		message.setText("");
		
	}

	public void edit(ComboBox<Customer> customerName,
			TextArea description, TextField itemCode, TextField rate,
			TextField quantity, TextField total, PurchaseOrder selectedObj) {
		
		customerName.getSelectionModel().select(selectedObj.getCustomer());
		description.setText(selectedObj.getDescription());
		itemCode.setText(selectedObj.getItemCode());
		rate.setText(String.valueOf(selectedObj.getRate()));
		quantity.setText(String.valueOf(selectedObj.getQuantity()));
		total.setText(String.valueOf(selectedObj.getRate() * selectedObj.getQuantity()));
		
	}
	
	@Override
	public void delete(Long id, Map<Long, PurchaseOrder> orders) {
		service.delete(id);
	}

}
