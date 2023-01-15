package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDto
{
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

    public int convertTransferType()
    {
        switch (transferType)
        {
            case "Request":
                return 1;
            case "Send":
                return 2;
        }
        return 0;
    }

    public int convertTransferStatus()
    {
        switch (transferStatus)
        {
            case "Pending":
                return 1;
            case "Approved":
                return 2;
            case "Rejected":
                return 3;
        }
        return 0;
    }

}
