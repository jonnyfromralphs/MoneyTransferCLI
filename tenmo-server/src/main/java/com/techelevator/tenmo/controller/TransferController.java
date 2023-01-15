package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController
{
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferController(UserDao userDao, AccountDao accountDao, TransferDao transferDao)
    {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> listRecipients()
    {
        List<User> users = userDao.findAll();
        return users;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<TransferDto> listTransfers(Principal principal)
    {
        int id = userDao.findIdByUsername(principal.getName());
        int accountId = accountDao.getAccountId(id);
        List<Transfer> transfers = transferDao.listTransfers(accountId);
        List<TransferDto> transferDtos = new ArrayList<>();
        for (Transfer transfer : transfers)
        {
            TransferDto transferDto = convertTransferToDto(transfer);
            transferDtos.add(transferDto);
        }
        return transferDtos;
    }

    @RequestMapping(value = "/pending", method = RequestMethod.GET)
    public List<TransferDto> listRequests(Principal principal)
    {
        int id = userDao.findIdByUsername(principal.getName());
        int accountId = accountDao.getAccountId(id);
        List<Transfer> transfers = transferDao.listRequests(accountId);
        List<TransferDto> transferDtos = new ArrayList<>();
        for (Transfer transfer : transfers)
        {
            TransferDto transferDto = convertTransferToDto(transfer);
            transferDtos.add(transferDto);
        }
        return transferDtos;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public TransferDto getTransferById(@PathVariable int id)
    {
        Transfer transfer = transferDao.getTransferById(id);
        TransferDto transferDto = convertTransferToDto(transfer);
        return transferDto;
    }

    @RequestMapping(value="/ids", method = RequestMethod.GET)
    public List<Integer> listTransferIds(Principal principal)
    {
        int id = userDao.findIdByUsername(principal.getName());
        List<Integer> ids = transferDao.listTransferIds(id);
        return ids;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createTransfer(@RequestBody TransferDto transferDto)
    {
        Transfer convertedDto = convertDtoToTransfer(transferDto);

        boolean created = transferDao.createTransfer(convertedDto);

        if (!created)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
        }
        else
        {
            int transferType = convertedDto.getTransferType();
            int transferStatus = convertedDto.getTransferStatus();
            int accountFrom = convertedDto.getAccountFrom();
            int accountTo = convertedDto.getAccountTo();
            BigDecimal amount = convertedDto.getAmount();

            if (transferType == 2 && transferStatus == 2)
            {
                accountDao.processTransfer(accountFrom, accountTo, amount);
            }
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateStatus(@RequestBody TransferDto transferDto)
    {

        Transfer convertedDto = convertDtoToTransfer(transferDto);

        int transferId = transferDto.getTransferId();
        int transferType = convertedDto.getTransferType();
        int transferStatus = convertedDto.getTransferStatus();
        int accountFrom = convertedDto.getAccountFrom();
        int accountTo = convertedDto.getAccountTo();
        BigDecimal amount = convertedDto.getAmount();

        transferDao.updateStatus(transferId, transferStatus);

        if (transferStatus == 2)
        {
            accountDao.processTransfer(accountTo, accountFrom, amount);
        }

    }


    public Transfer convertDtoToTransfer(TransferDto transferDto)
    {
        int transferType = transferDto.convertTransferType();
        int transferStatus = transferDto.convertTransferStatus();
        int userFrom = userDao.findIdByUsername(transferDto.getSenderName());
        int accountFrom = accountDao.getAccountId(userFrom);
        int userTo = userDao.findIdByUsername(transferDto.getRecipientName());
        int accountTo = accountDao.getAccountId(userTo);


        Transfer transfer = new Transfer();
        transfer.setTransferType(transferType);
        transfer.setTransferStatus(transferStatus);
        transfer.setAccountTo(accountTo);
        transfer.setAccountFrom(accountFrom);
        transfer.setAmount(transferDto.getAmount());

        return transfer;
    }

    public TransferDto convertTransferToDto(Transfer transfer)
    {
        int transferId = transfer.getTransferId();
        String transferType = transfer.convertTransferType();
        String transferStatus = transfer.convertTransferStatus();
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        String userFrom = accountDao.getNameByAccount(accountFrom);
        String userTo = accountDao.getNameByAccount(accountTo);
        BigDecimal amount = transfer.getAmount();


        TransferDto transferDto = new TransferDto();
        transferDto.setTransferId(transferId);
        transferDto.setTransferType(transferType);
        transferDto.setTransferStatus(transferStatus);
        transferDto.setSenderName(userFrom);
        transferDto.setRecipientName(userTo);
        transferDto.setAmount(amount);

        return transferDto;
    }

}
