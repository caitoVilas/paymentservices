package com.paymentchain.customer.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.domain.entities.Customer;
import com.paymentchain.customer.domain.repositories.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/customer")
//@RequiredArgsConstructor
public class CustomerController{
    private final CustomerRepository customerRepository;
    private final WebClient.Builder webclient;

    public CustomerController(CustomerRepository customerRepository, WebClient.Builder webclient) {
        this.customerRepository = customerRepository;
        this.webclient = webclient;
    }
    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

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
        customer.getProducts().forEach(x -> {
            x.setCustomer(customer);
        });
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }

    @GetMapping("/full")
    public Customer getFull(@RequestParam String code){
        var customer = customerRepository.findByCode(code);
        customer.getProducts().forEach(x -> {
            String productName = this.getProductName(x.getId());
            x.setProductName(productName);
        });
        return customer;
    }

    private String getProductName(Long id){
        WebClient build =  webclient.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8082/products")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8082/products"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/"+ id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("name").asText();
        return name;
    }
}

