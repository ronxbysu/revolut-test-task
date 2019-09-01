package com.drastic.transfer;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFound;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MoneyTransfer implements Transferable {

    private ReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;
    private Map<String, BigDecimal> store;

    public MoneyTransfer() {
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        store = new HashMap<String, BigDecimal>() {{
            put("CashMachine", new BigDecimal(1000));
        }};
    }

    public void transfer(Transfer moneyTransfer) throws NoCounterpartyFound, InsufficientFundsException, NegativeAmountException {
        writeLock.lock();
        try {
            if (moneyTransfer.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new NegativeAmountException();
            if (store.get(moneyTransfer.getFrom()) == null) throw new NoCounterpartyFound();
            if (store.get(moneyTransfer.getFrom()).compareTo(moneyTransfer.getAmount()) < 0) throw new InsufficientFundsException();
            if (store.get(moneyTransfer.getTo()) == null) {
                System.out.println("No recipient found. Creating...");
                store.putIfAbsent(moneyTransfer.getTo(), new BigDecimal(0));
            }
            store.computeIfPresent(moneyTransfer.getFrom(), (k, v) -> store.get(k).subtract(moneyTransfer.getAmount()));
            store.computeIfPresent(moneyTransfer.getTo(), (k, v) -> store.get(k).add(moneyTransfer.getAmount()));
        } finally {
            writeLock.unlock();
        }

        list();
    }

    public void list()  {
        readLock.lock();
        store.forEach((key, value) -> System.out.println(key + " " + value));
        System.out.println();
        readLock.unlock();
    }
}
