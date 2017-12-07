package com.rp.packers.packersapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService implements CrudService<Customer>{
	
	@Autowired
	private CustomerRepository repository;

	@Override
	public Customer get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void create(Customer t) {
		repository.save(t);
	}

	@Override
	public void update(Customer t) {
		create(t);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public List<Customer> getAll() {
		return repository.findAll();
	}

}
