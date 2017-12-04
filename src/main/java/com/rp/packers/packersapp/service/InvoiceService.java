package com.rp.packers.packersapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rp.packers.packersapp.model.Invoice;
import com.rp.packers.packersapp.repository.InvoiceRepository;

@Service
public class InvoiceService implements CrudService<Invoice>{
	
	@Autowired
	private InvoiceRepository repository;

	@Override
	public Invoice get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void create(Invoice t) {
		repository.save(t);
	}

	@Override
	public void update(Invoice t) {
		create(t);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public List<Invoice> getAll() {
		return repository.findAll();
	}

}
