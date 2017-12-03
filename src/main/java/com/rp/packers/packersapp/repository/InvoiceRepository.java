package com.rp.packers.packersapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rp.packers.packersapp.model.Customer;
import com.rp.packers.packersapp.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{

}
