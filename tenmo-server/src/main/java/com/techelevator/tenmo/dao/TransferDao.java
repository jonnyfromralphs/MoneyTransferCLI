package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface TransferDao
{
    //create transfer
    boolean createTransfer(Transfer transfer);

    //view list of transferIds
    List<Integer> listTransferIds(int accountId);

    //view transfers
    List<Transfer> listTransfers(int accountId);

    //get transfer by Id
    Transfer getTransferById(int id);

    //list pending requests
    List<Transfer> listRequests(int accountId);

    //update transfer
    void updateStatus(int transferId, int transferStatus);
}
