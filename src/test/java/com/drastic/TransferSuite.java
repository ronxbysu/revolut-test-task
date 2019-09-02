package com.drastic;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFoundException;
import com.drastic.transfer.TransferMaker;
import com.drastic.transfer.TransferModule;
import com.drastic.transfer.model.Transfer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TransferSuite {

    private Database database;
    private TransferMaker transferMaker;

    @Before
    public void before()    {
        final Injector injector = Guice.createInjector(new TransferModule());
        database = injector.getInstance(Database.class);
        transferMaker = injector.getInstance(TransferMaker.class);
    }

    @Test
    public void makeTransferTest() throws InsufficientFundsException, NegativeAmountException, NoCounterpartyFoundException {
        Transfer transfer = new Transfer("CashMachine", "Roman", new BigDecimal(500));
        transferMaker.makeTransfer(transfer);
        assertEquals(new BigDecimal(500), database.getStore().get(transfer.getFrom()));
    }

    @Test(expected = InsufficientFundsException.class)
    public void makeTransferInsufficientFundsTest() throws InsufficientFundsException, NegativeAmountException, NoCounterpartyFoundException {
        Transfer transfer = new Transfer("CashMachine", "Roman", new BigDecimal(1500));
        transferMaker.makeTransfer(transfer);
    }

    @Test(expected = NegativeAmountException.class)
    public void makeTransferNegativeAmountTest() throws InsufficientFundsException, NegativeAmountException, NoCounterpartyFoundException {
        Transfer transfer = new Transfer("CashMachine", "Roman", new BigDecimal(-500));
        transferMaker.makeTransfer(transfer);
    }

    @Test(expected = NoCounterpartyFoundException.class)
    public void makeTransferNoCounterpartyFoundTest() throws InsufficientFundsException, NegativeAmountException, NoCounterpartyFoundException {
        Transfer transfer = new Transfer("Tanya", "Roman", new BigDecimal(500));
        transferMaker.makeTransfer(transfer);
    }
/*
    @Test
    public void smoke() {
        Application.main(new String[]{"8082"});
    }
*/
}
