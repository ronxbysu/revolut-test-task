package com.drastic.transfer;

import com.drastic.Database;
import com.drastic.server.Request;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class TransferModule extends AbstractModule {
    protected void configure() {
        bind(Transferable.class).to(MoneyTransfer.class);
    }

    @Provides
    @Singleton
    Database getDatabase() {
        return new Database();
    }
}
