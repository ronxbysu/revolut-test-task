package com.drastic.transfer;

import com.drastic.server.Request;
import com.google.inject.AbstractModule;

public class TransferModule extends AbstractModule {
    protected void configure() {
        bind(Transferable.class).to(MoneyTransfer.class);
    }
}
