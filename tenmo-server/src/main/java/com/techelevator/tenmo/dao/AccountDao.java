package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao
{
    BigDecimal getBalanceById(int userId);
    //change both accounts
    void updateBalance(int accountId,BigDecimal amount);
    int getAccountId(int id);
    void processTransfer(int accountFrom, int accountTo, BigDecimal amount);
    String getNameByAccount(int accountId);
    

}
