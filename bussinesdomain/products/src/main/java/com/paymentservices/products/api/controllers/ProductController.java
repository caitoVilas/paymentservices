package com.paymentservices.products.api.controllers;

import com.paymentservices.products.domain.entities.Product;
import com.paymentservices.products.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @PostMapping
    public Product create(@RequestBody Product product){
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return productRepository.findById(id).orElse(null);
    }
}

