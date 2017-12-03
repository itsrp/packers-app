package com.rp.packers.packersapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rp.packers.packersapp.model.PurchaseOrder;
import com.rp.packers.packersapp.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderService implements CrudService<PurchaseOrder>{
	
	@Autowired
	private PurchaseOrderRepository repository;

	@Override
	public PurchaseOrder get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void create(PurchaseOrder t) {
		repository.save(t);
	}

	@Override
	public void update(PurchaseOrder t) {
		create(t);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

}
