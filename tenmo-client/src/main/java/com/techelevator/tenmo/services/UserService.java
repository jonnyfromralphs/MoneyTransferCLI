package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
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

public class UserService extends AuthenticatedApiService<User>
{
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();


    public UserService(String baseUrl)
    {
        this.baseUrl=baseUrl;
    }


    public BigDecimal getBalance()
    {
        HttpEntity<Void> entity = makeAuthEntity();
        String url = baseUrl + "account/balance";
        BigDecimal balance = null;
        try
        {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class);
            balance = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public List<User> getRecipients()
    {
        HttpEntity<Void> entity = makeAuthEntity();
        String url = baseUrl + "transfer";
        List<User> users = null;
        try
        {
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            User[] usersArray = response.getBody();
            users = Arrays.asList(usersArray);
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }


}
