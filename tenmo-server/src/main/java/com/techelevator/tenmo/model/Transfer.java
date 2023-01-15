package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer
{
    private int transferId;
    private int transferType;
    private int transferStatus;
    private int accountFrom;
    private int accountTo;
    BigDecimal amount;

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public int getTransferType()
    {
        return transferType;
    }

    public void setTransferType(int transferType)
    {
        this.transferType = transferType;
    }

    public int getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    public int getAccountFrom()
    {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom)
    {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo()
    {
        return accountTo;
    }

    public void setAccountTo(int accountTo)
    {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String convertTransferType()
    {
        switch (transferType)
        {
            case 1:
                return "Request";
            case 2:
                return "Send";
        }
        return null;
    }

    public String convertTransferStatus()
    {
        switch (transferStatus)
        {
            case 1:
                return "Pending";
            case 2:
                return "Approved";
            case 3:
                return "Rejected";
        }
        return null;
    }
}
