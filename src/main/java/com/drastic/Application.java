package com.drastic;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFound;
import com.drastic.server.Request;
import com.drastic.transfer.Transfer;
import com.drastic.transfer.TransferMaker;
import com.drastic.transfer.TransferModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;

public class Application {

    public static void main(String[] args) throws NoCounterpartyFound, InsufficientFundsException, NegativeAmountException {

        final Injector injector = Guice.createInjector(new TransferModule());
        final TransferMaker transferMaker = injector.getInstance(TransferMaker.class);

        if (args.length < 1) return;

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("New client connected");

                new Thread(new Request(socket, transferMaker)).start();
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }

        Transfer transfer = new Transfer("Roman", "Tanya", new BigDecimal(50));


        transferMaker.makeTransfer(transfer);

    }

}
