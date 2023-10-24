package com.paymentchain.customer.api.controller;

import com.paymentchain.customer.domain.entities.Customer;
import com.paymentchain.customer.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController{
    private final CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getAll(){
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id){
        return customerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }
}
