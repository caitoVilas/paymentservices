package com.paymentchain.customer.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String code;
    private String iban;
    private String address;
    private String phone;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer",
              cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct>  products;
    @Transient
    private List<?> transactions;
}
