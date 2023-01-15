package com.techelevator.tenmo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer
{
    //make changes to reflect DTO
    private int transferId;
    private String transferType;
    private String transferStatus;
    private String senderName;
    private String recipientName;
    private BigDecimal amount;

    public int getTransferId()
    {
        return transferId;
    }

    public void setTransferId(int transferId)
    {
        this.transferId = transferId;
    }

    public String getTransferType()
    {
        return transferType;
    }

    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public String getTransferStatus()
    {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public String getRecipientName()
    {
        return recipientName;
    }

    public void setRecipientName(String recipientName)
    {
        this.recipientName = recipientName;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
