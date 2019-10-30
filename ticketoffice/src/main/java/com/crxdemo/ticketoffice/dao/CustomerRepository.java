package com.crxdemo.ticketoffice.dao;

import com.crxdemo.ticketoffice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer, Long> {

    @Transactional(readOnly = true)
    List<Customer> findAll();

    @Transactional(readOnly = true)
    Customer findById(long id);

}
