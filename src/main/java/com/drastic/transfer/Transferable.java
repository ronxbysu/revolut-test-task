package com.drastic.transfer;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFoundException;
import com.drastic.transfer.model.Transfer;

public interface Transferable {
    void transfer(Transfer moneyTransfer) throws NoCounterpartyFoundException, InsufficientFundsException, NegativeAmountException;
}
