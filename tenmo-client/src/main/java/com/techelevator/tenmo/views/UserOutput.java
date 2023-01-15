package com.techelevator.tenmo.views;


import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class UserOutput
{

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt)
    {
        int menuSelection;
        System.out.print(prompt);
        try
        {
            menuSelection = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e)
        {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting()
    {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu()
    {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu()
    {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials()
    {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt)
    {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a number.");
            }
        }
    }


    public BigDecimal promptForBigDecimal(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return new BigDecimal(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause()
    {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage()
    {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance)
    {
        System.out.println();
        System.out.println("Your current account balance is: $" + balance);
    }

    public void printRecipients(List<String> usernames)
    {
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println("Users");
        System.out.println("-----------------------------------");
        for (String user: usernames)
        {
            System.out.println(user);
        }
        System.out.println("-----------------------------------");
    }

    public void printWrongUser()
    {
        System.out.println("You are unable to send/transfer money to yourself.");
    }

    public void printApprovedTransfer()
    {
        System.out.println("Your transfer was successful.");
    }

    public void printRejectedTransfer()
    {
        System.out.println("Your transfer was rejected.");
    }

    public void printTransfer(Transfer transfer)
    {
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println("Transfer " + transfer.getTransferId());
        System.out.println("-----------------------------------");
        System.out.println("Transfer Type: " + transfer.getTransferType());
        System.out.println("Transfer Status: " + transfer.getTransferStatus());
        System.out.println("Account From: " + transfer.getSenderName());
        System.out.println("Account To: " + transfer.getRecipientName());
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public void printRequest()
    {
        System.out.println("Your request has been sent");
    }

    public String approveOrReject()
    {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println();
        int choice = 0;
        while (true)
        {
            choice = promptForInt("Press 1 to approve the request, 2 to deny it. ");
            if (choice == 1)
            {
                return "Approved";
            }
            if (choice == 2)
            {
                return "Rejected";
            }
        }
    }

    public void printInsufficientFunds()
    {
        System.out.println();
        System.out.println("Unable to send payment. Insufficient funds.");
    }

    public void printTransferUpdated()
    {
        System.out.println();
        System.out.println("Transfer was updated successfully.");
    }

}
