package com.rp.packers.packersapp.service;

import java.util.List;

public interface CrudService<T> {

	T get(Long id);
	
	List<T> getAll();
	
	void create(T t);
	
	void update(T t);
	
	void delete(Long id);
	
}
