package com.example.spring_jdbc_customers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerDao customerDao;

    public CustomerController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    // GET /api/customers
    @GetMapping
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    // GET /api/customers/{id}
    @GetMapping("/{id}")
    public Customer getOne(@PathVariable Long id) {
        return customerDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer " + id + " not found"));
    }

    // POST /api/customers
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer payload) {
        Long id = customerDao.create(payload);   // якщо у тебе int — зроби: Long id = (long) customerDao.create(payload);
        payload.setId(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(payload);
    }

    // PUT /api/customers/{id}
    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer payload) {
        payload.setId(id);
        int rows = customerDao.update(payload);  // або boolean updated = customerDao.update(payload);
        if (rows == 0) {
            throw new NotFoundException("Customer " + id + " not found");
        }
        return payload;
    }

    // DELETE /api/customers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int rows = customerDao.delete(id);       // або boolean deleted = customerDao.delete(id);
        if (rows == 0) {
            throw new NotFoundException("Customer " + id + " not found");
        }
        return ResponseEntity.noContent().build();
    }
}