package com.rp.packers.packersapp.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.controller.CustomerController;
import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.Invoice;
import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.service.InvoiceService;
import com.rp.packers.packersapp.service.PurchaseOrderService;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Component
public class InvoiceActionPerformer implements ActionPerformer<Invoice> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private InvoiceService service;
	
	@Autowired
	private PurchaseOrderService orderService;

	@Override
	public void filteredTable(TableView<Invoice> table, Map<Long, Invoice> invoices, String searchByCriteria,
			TextField searchText) {
		List<Invoice> list = new ArrayList<>();
		if (searchByCriteria != null) {
			LOGGER.info("searching by: " + searchByCriteria);
			try {
				switch (searchByCriteria) {
				case "By Invoice ID":
					list = invoices.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(invoice -> invoice.getId().equals(Long.valueOf(searchText.getText())))
							.collect(Collectors.toList());
					break;
				case "By Customer Name":
					list = invoices.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(invoice -> invoice.getOrders().get(0).getCustomer().getName().toLowerCase().contains(searchText.getText().toLowerCase()))
							.collect(Collectors.toList());
					break;
				default:
					list = invoices.values().stream()
			                .collect(Collectors.toList());
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Exception while searching.", e);
			}
		} else {
			LOGGER.info("searching all ");
			list = invoices.values().stream()
	                .collect(Collectors.toList());
		}
		
		table.getItems().clear();
		table.getItems().setAll(list);
		
	}

	@Override
	public void delete(Long id, Map<Long, Invoice> allObj) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void save(Map<Long, PurchaseOrder> selectedOrders, String total, String cgst,
			String sgst, String finalAmount, Invoice selectedObj, Label message, Map<Long, Invoice> allObj) {
		Invoice newInvoice = buildInvoice(selectedOrders, total, cgst, sgst, finalAmount, selectedObj);
		//selectedOrders.values().stream().forEach(order -> order.setInvoice(newInvoice));
		//service.create(newInvoice);
		newInvoice.setOrders((selectedOrders.values().stream().collect(Collectors.toList())));
		service.update(newInvoice);
		
		//orderService.saveAll(selectedOrders.values().stream().collect(Collectors.toList()));
		message.setText("Invoice saved successfully.");
		//if(selectedObj == null) {
			//newInvoice = selectedOrders.get(0).getInvoice();
			allObj.put(newInvoice.getId(), newInvoice);
			//allObj.add(newOrder);
		//}
		
	}
	
	private Invoice buildInvoice(Map<Long, PurchaseOrder> selectedOrders, String total, String cgst,
			String sgst, String finalAmount, Invoice selectedObj) {
		/*if(selectedObj == null) {
			selectedObj = new Invoice();
		}*/
		List<PurchaseOrder> collect = selectedOrders.values().stream().collect(Collectors.toList());
		//collect.addAll(selectedObj.getOrders());
		selectedObj.setOrders(collect);
		selectedObj.setTotal(Double.valueOf(total));
		selectedObj.setCgst(Double.valueOf(cgst));
		selectedObj.setSgst(Double.valueOf(sgst));
		selectedObj.setFinalAmount(Double.valueOf(finalAmount));
		return selectedObj;
	}
	
	public void resetUI(Button editButton, Button deleteButton, ComboBox<Customer> customerName, TextField total, TextField cgst, TextField sgst,
			TextField finalAmount, Label message, Invoice selectedObj, Map<Long, PurchaseOrder> selectedOrders, TableView<PurchaseOrder> orderTable) {
		// TODO Auto-generated method stub
		editButton.setDisable(true);
		deleteButton.setDisable(true);
		//selectedObj = null;
		
		customerName.getSelectionModel().clearSelection();
		customerName.setPromptText("Please select Customer");
		total.clear();
		cgst.clear();
		sgst.clear();
		finalAmount.clear();
		
		message.setText("");
		
		selectedOrders.clear();
		orderTable.getItems().clear();
	}

	public void edit(ComboBox<Customer> customerName, TextField total, TextField cgst, TextField sgst,
			TextField finalAmount, Invoice selectedObj, Map<Long, PurchaseOrder> selectedOrders,
			TableView<PurchaseOrder> orderTable) {
		customerName.getSelectionModel().select(selectedObj.getOrders().get(0).getCustomer());
		total.setText(String.valueOf(selectedObj.getTotal()));
		cgst.setText(String.valueOf(selectedObj.getCgst()));
		sgst.setText(String.valueOf(selectedObj.getSgst()));
		finalAmount.setText(String.valueOf(selectedObj.getFinalAmount()));
		
		Map<Long, PurchaseOrder> savedOrdersMap = (Map<Long, PurchaseOrder>) selectedObj.getOrders().stream().collect(Collectors.toMap(PurchaseOrder::getId, c -> c));
		
		List<PurchaseOrder> ordersByCustomer = selectedObj.getOrders().get(0).getCustomer().getOrder();
		ordersByCustomer.stream()
		.filter(order -> savedOrdersMap.containsKey(order.getId()))
		.forEach(order -> order.setIsSelected(true));
		
		orderTable.getItems().setAll(ordersByCustomer);
	}

	public void add(Button editButton, Button deleteButton, ComboBox<Customer> customerName, TextField total,
			TextField cgst, TextField sgst, TextField finalAmount, Label message, Invoice selectedObj,
			Map<Long, PurchaseOrder> selectedOrders, TableView<PurchaseOrder> orderTable) {
		resetUI(editButton, deleteButton, customerName, total, cgst, sgst, finalAmount, message, selectedObj, selectedOrders, orderTable);
		
	}

}
