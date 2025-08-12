package com.example.spring_jdbc_customers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbc;

    public CustomerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Customer> findAll() {
        String sql = """
            SELECT id, full_name, email, social_security_number
            FROM customer
            ORDER BY id
            """;
        return jdbc.query(sql, (rs, i) -> {
            Customer c = new Customer();
            c.setId(rs.getLong("id")); // Long!
            c.setFullName(rs.getString("full_name"));
            c.setEmail(rs.getString("email"));
            c.setSocialSecurityNumber(rs.getString("social_security_number"));
            return c;
        });
    }

    public int create(String fullName, String email, String ssn) {
        String sql = """
            INSERT INTO customer(full_name, email, social_security_number)
            VALUES (?, ?, ?)
            """;
        return jdbc.update(sql, fullName, email, ssn);
    }
}