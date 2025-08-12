package com.example.spring_jdbc_customers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        String sql = """
            SELECT id, full_name, email, social_security_number
            FROM customer
            ORDER BY id
        """;
        return jdbcTemplate.query(sql, new CustomerRowMapper());
    }

    @Override
    public Optional<Customer> findById(Long id) {
        String sql = """
            SELECT id, full_name, email, social_security_number
            FROM customer
            WHERE id = ?
        """;
        List<Customer> list = jdbcTemplate.query(sql, new CustomerRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public Long create(Customer c) {
        String sql = """
            INSERT INTO customer (full_name, email, social_security_number)
            VALUES (?, ?, ?)
        """;

        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getSocialSecurityNumber());
            return ps;
        }, kh);

        return kh.getKey() == null ? null : kh.getKey().longValue();
    }

    @Override
    public int update(Customer c) {
        String sql = """
            UPDATE customer
            SET full_name = ?, email = ?, social_security_number = ?
            WHERE id = ?
        """;
        return jdbcTemplate.update(sql,
                c.getFullName(),
                c.getEmail(),
                c.getSocialSecurityNumber(),
                c.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}