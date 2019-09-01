package com.drastic.transfer;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFound;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TransferMaker {

    private Transferable transferable;

    @Inject
    public TransferMaker(Transferable transferable) {
        this.transferable = transferable;
    }

    public void makeTransfer(Transfer moneyTranfer) throws NoCounterpartyFound, InsufficientFundsException, NegativeAmountException {
        transferable.transfer(moneyTranfer);
    }
}
