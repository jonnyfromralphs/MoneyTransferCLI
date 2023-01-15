package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticatedApiService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.views.UserOutput;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class TenmoApp
{

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final UserOutput userOutput = new UserOutput();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    public void run()
    {
        userOutput.printGreeting();
        loginMenu();
        if (currentUser != null)
        {
            mainMenu();
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null)
        {
            userOutput.printLoginMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                handleRegister();
            }
            else if (menuSelection == 2)
            {
                handleLogin();
            }
            else if (menuSelection != 0)
            {
                System.out.println("Invalid Selection");
                userOutput.pause();
            }
        }
    }

    private void handleRegister()
    {
        System.out.println("Please register a new user account");
        UserCredentials credentials = userOutput.promptForCredentials();
        if (authenticationService.register(credentials))
        {
            System.out.println("Registration successful. You can now login.");
        }
        else
        {
            userOutput.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        UserCredentials credentials = userOutput.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        AuthenticatedApiService.setAuthToken(currentUser.getToken());
        if (currentUser == null)
        {
            userOutput.printErrorMessage();
        }
    }

    private void mainMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0)
        {
            userOutput.printMainMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewCurrentBalance();
            }
            else if (menuSelection == 2)
            {
                viewTransferHistory();
            }
            else if (menuSelection == 3)
            {
                viewPendingRequests();
            }
            else if (menuSelection == 4)
            {
                sendBucks();
            }
            else if (menuSelection == 5)
            {
                requestBucks();
            }
            else if (menuSelection == 0)
            {
                continue;
            }
            else
            {
                System.out.println("Invalid Selection");
            }
            userOutput.pause();
        }
    }

    private void viewCurrentBalance()
    {

        if (currentUser != null)
        {
            BigDecimal balance = userService.getBalance();
            userOutput.printBalance(balance);
        }
    }

    private String pickRecipient()
    {
        List<User> users = userService.getRecipients();
        int userId = currentUser.getUser().getId();

        List<String> usernames = users.stream().map(User::getUsername)
                                .filter(n -> !n.equals(currentUser.getUser().getUsername())).collect(Collectors.toList());

        userOutput.printRecipients(usernames);

        String recipientName = "";

        while(!usernames.contains(recipientName))
        {
            recipientName = userOutput.promptForString("Enter the name of the user (0 to cancel): ");
            if(recipientName.equals("0"))
            {
                break;
            }
            else if (recipientName.equals(currentUser.getUser().getUsername()))
            {
                userOutput.printWrongUser();
            }
        }
        return recipientName;
    }

    private int pickTransfer(List<Transfer> transfers)
    {
        List<Integer> transferIds = transfers.stream().map(Transfer::getTransferId).collect(Collectors.toList());

        int transferId = 0;

        while(!transferIds.contains(transferId))
        {
            transferId = userOutput.promptForInt("\nEnter the transfer ID (0 to cancel): ");
            if(transferId == 0)
            {
                break;
            }
        }
        return transferId;
    }


    private void viewTransferHistory()
    {
        // TODO Auto-generated method stub
        List<Transfer> transfers = transferService.getTransfers();
        for (Transfer transfer : transfers)
        {
            userOutput.printTransfer(transfer);
        }
    }

    private void viewPendingRequests()
    {
        // TODO Auto-generated method stub
        List<Transfer> pending = transferService.getPending();
        for (Transfer transfer : pending)
        {
            userOutput.printTransfer(transfer);
        }

        int transferId = pickTransfer(pending);

        if (transferId == 0)
        {
            return;
        }

        Transfer transfer = transferService.getTransferById(transferId);
        String choice = userOutput.approveOrReject();

        if (choice.equals(""))
        {
            return;
        }

        BigDecimal balance = userService.getBalance();
        BigDecimal requestedAmount = transfer.getAmount();

        if (balance.compareTo(requestedAmount) < 0 && choice.equals("Approved"))
        {
            transfer.setTransferStatus("Rejected");
            userOutput.printInsufficientFunds();
        }
        else
        {
            transfer.setTransferStatus(choice);
        }

        transferService.updateStatus(transfer);
        userOutput.printTransferUpdated();

    }

    private void sendBucks()
    {
        // TODO Auto-generated method stub\
        String recipientName = pickRecipient();
        if (recipientName.equals("0"))
        {
            return;
        }
        BigDecimal amount = pickValidAmount();
        String senderName = currentUser.getUser().getUsername();
        BigDecimal balance = userService.getBalance();

        if (amount.compareTo(balance) == -1)
        {
            transferService.createTransfer("Send", "Approved", senderName, recipientName, amount);
            userOutput.printApprovedTransfer();
        }
        else
        {
            transferService.createTransfer("Send", "Rejected", senderName, recipientName, amount);
            userOutput.printRejectedTransfer();
        }

    }

    private BigDecimal pickValidAmount()
    {
        BigDecimal amount;
        while (true)
        {
            amount = userOutput.promptForBigDecimal("Enter valid amount: ");
            if (amount.compareTo(BigDecimal.ZERO) == 1)
            {
                return amount;
            }
        }
    }

    private void requestBucks()
    {
        // TODO Auto-generated method stub
        String recipientName = pickRecipient();
        if (recipientName.equals("0"))
        {
            return;
        }
        BigDecimal amount = pickValidAmount();
        String senderName = currentUser.getUser().getUsername();
        transferService.createTransfer("Request", "Pending", senderName, recipientName, amount);
        userOutput.printRequest();
    }

}
