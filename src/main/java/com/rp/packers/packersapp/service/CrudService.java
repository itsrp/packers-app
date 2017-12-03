package com.rp.packers.packersapp.service;

public interface CrudService<T> {

	T get(Long id);
	
	void create(T t);
	
	void update(T t);
	
	void delete(Long id);
	
}
