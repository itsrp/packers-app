package com.rp.packers.packersapp.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String address;
	
	private String tin;
	
	private String vendorCode;
	
	@OneToMany(mappedBy="customer")
	private List<PurchaseOrder> order;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	public Customer(String name, String address, String tin, String vendorCode) {
		super();
		this.name = name;
		this.address = address;
		this.tin = tin;
		this.vendorCode = vendorCode;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public List<PurchaseOrder> getOrder() {
		return order;
	}

	public void setOrder(List<PurchaseOrder> order) {
		this.order = order;
	}

	
	
}
