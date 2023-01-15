package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/account")
@PreAuthorize("isAuthenticated()")
public class AccountController
{
    private final UserDao userDao;
    private final AccountDao accountDao;

    public AccountController(UserDao userDao, AccountDao accountDao)
    {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal)
    {
        int id = userDao.findIdByUsername(principal.getName());
        BigDecimal balance = accountDao.getBalanceById(id);
        return balance;
    }
}
