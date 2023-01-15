package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService extends AuthenticatedApiService<Transfer>
{

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public boolean createTransfer(String transferType, String transferStatus, String senderName, String recipientName, BigDecimal amount)
    {
        Transfer transfer = new Transfer();
        transfer.setTransferId(0);
        transfer.setTransferType(transferType);
        transfer.setTransferStatus(transferStatus);
        transfer.setSenderName(senderName);
        transfer.setRecipientName(recipientName);
        transfer.setAmount(amount);

        String url = baseUrl + "transfer";

        HttpEntity<Transfer> entity = makeAuthEntity(transfer);
        boolean success = false;
        try
        {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
            success = true;
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public List<Transfer> getTransfers()
    {
        HttpEntity<Void> entity = makeAuthEntity();
        String url = baseUrl + "transfer/all";
        List<Transfer> transfers = null;
        try
        {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            Transfer[] transfersArray = response.getBody();
            transfers = Arrays.asList(transfersArray);
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer getTransferById(int id)
    {
        HttpEntity<Void> entity = makeAuthEntity();
        String url = baseUrl + "transfer/" + id;
        Transfer transfer = null;
        try
        {
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            transfer = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public boolean updateStatus(Transfer transfer)
    {
        String url = baseUrl + "transfer/update";

        HttpEntity<Transfer> entity = makeAuthEntity(transfer);
        boolean success = false;

        try
        {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
            success = true;
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }


    public List<Transfer> getPending()
    {
        HttpEntity<Void> entity = makeAuthEntity();
        String url = baseUrl + "transfer/pending";
        List<Transfer> transfers = null;
        try
        {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            Transfer[] transfersArray = response.getBody();
            transfers = Arrays.asList(transfersArray);
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
}
