package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean createTransfer(Transfer transfer)
    {
        int transferType = transfer.getTransferType();
        int transferStatus = transfer.getTransferStatus();
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferType, transferStatus, accountFrom, accountTo, amount);
        if (newTransferId == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public List<Integer> listTransferIds(int accountId)
    {
        String sql = "SELECT transfer_id FROM transfer WHERE account_from = ? OR account_to = ?";
        List<Integer> transferIds = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next())
        {
            String str = results.getString("transfer_id");
            transferIds.add(Integer.parseInt(str));
        }

        return transferIds;
    }

    @Override
    public List<Transfer> listTransfers(int accountId)
    {
        String sql = "SELECT * FROM transfer WHERE (account_from = ? OR account_to = ?) AND transfer_status_id != 1";
        List<Transfer> transfers = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next())
        {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer getTransferById(int id)
    {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next())
        {
            Transfer transferById = mapRowToTransfer(results);
            return transferById;
        }
        return null;

    }

    @Override
    public List<Transfer> listRequests(int accountId)
    {
        String sql = "SELECT * FROM transfer WHERE account_to = ? AND transfer_type_id = 1 AND transfer_status_id = 1;";
        List<Transfer> transfers = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        while (results.next())
        {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public void updateStatus(int transferId, int transferStatus)
    {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferStatus, transferId);
    }


    private Transfer mapRowToTransfer(SqlRowSet rs)
    {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferType(rs.getInt("transfer_type_id"));
        transfer.setTransferStatus(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
