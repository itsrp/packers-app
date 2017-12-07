package com.rp.packers.packersapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rp.packers.packersapp.actions.ActionEnum;
import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.service.CrudService;
import com.rp.packers.packersapp.service.PurchaseOrderService;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

@Component
public class PurchaseOrderController extends BaseController<PurchaseOrder>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderController.class);
	
	@Autowired
	private PurchaseOrderService service;
	
	private static final String ORDER_SCREEN_PATH = "/fxml/orderView.fxml";

	@Override
	protected void initializeTableData() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	protected void dispatch(ActionEnum action, MouseEvent event, Stage stage) {
		// TODO Auto-generated method stub
		
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

}
