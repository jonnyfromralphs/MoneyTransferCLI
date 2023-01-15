package com.techelevator.tenmo.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao
{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalanceById(int userId)
    {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public String getNameByAccount(int accountId)
    {
        String sql = "SELECT tu.username FROM tenmo_user AS tu " +
                "JOIN account AS a ON tu.user_id = a.user_id " +
                "WHERE a.account_id = ?;";

        String username;
        try
        {
            username = jdbcTemplate.queryForObject(sql, String.class, accountId);
        }
        catch (NullPointerException | EmptyResultDataAccessException e)
        {
            throw new UsernameNotFoundException("Account was not found.");
        }
        return username;
    }

    @Override
    public void updateBalance(int account_id, BigDecimal amount)
    {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql,amount,account_id);
    }

    @Override
    public int getAccountId(int id)
    {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        int accountId = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return accountId;
    }

    @Override
    public void processTransfer(int accountFrom, int accountTo, BigDecimal amount)
    {
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?; " +
                "COMMIT TRANSACTION;";
        jdbcTemplate.update(sql,amount, accountFrom, amount, accountTo);
    }


}
