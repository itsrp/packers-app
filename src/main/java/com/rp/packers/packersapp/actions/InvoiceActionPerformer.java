package com.rp.packers.packersapp.actions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
				case "By ID":
					list = invoices.values().stream()
			                .collect(Collectors.toList()).stream()
							.filter(invoice -> invoice.getId().equals(Long.valueOf(searchText.getText())))
							.collect(Collectors.toList());
					break;
				case "By Name":
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

	public void save(Map<Long, PurchaseOrder> selectedOrders, String total, String cgst,
			String sgst, String finalAmount, Invoice selectedObj, Label message, Map<Long, Invoice> allObj) {
		Invoice newInvoice = buildInvoice(selectedOrders, total, cgst, sgst, finalAmount, selectedObj);
		selectedOrders.values().stream().forEach(order -> order.setInvoice(newInvoice));
		
		//service.create(newInvoice);
		orderService.saveAll(selectedOrders.values().stream().collect(Collectors.toList()));
		message.setText("Invoice saved successfully.");
		if(selectedObj == null) {
			//newInvoice = selectedOrders.get(0).getInvoice();
			allObj.put(selectedOrders.get(0).getInvoice().getId(), selectedOrders.get(0).getInvoice());
			//allObj.add(newOrder);
		}
		
	}
	
	private Invoice buildInvoice(Map<Long, PurchaseOrder> selectedOrders, String total, String cgst,
			String sgst, String finalAmount, Invoice selectedObj) {
		if(selectedObj == null) {
			selectedObj = new Invoice();
		}
		selectedObj.setOrders(selectedOrders.values().stream().collect(Collectors.toList()));
		selectedObj.setTotal(Double.valueOf(total));
		selectedObj.setCgst(Double.valueOf(cgst));
		selectedObj.setSgst(Double.valueOf(sgst));
		selectedObj.setFinalAmount(Double.valueOf(finalAmount));
		return selectedObj;
	}

}
