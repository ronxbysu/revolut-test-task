package com.drastic.server;

import com.drastic.exception.InsufficientFundsException;
import com.drastic.exception.NegativeAmountException;
import com.drastic.exception.NoCounterpartyFound;
import com.drastic.transfer.model.Transfer;
import com.drastic.transfer.TransferMaker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

public final class Request implements Runnable {

    private TransferMaker transferMaker;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Socket socket;

    public Request(Socket socket, TransferMaker transferMaker) {
        this.socket = socket;
        this.transferMaker = transferMaker;
    }

    public void run() {
        Status status = new Status();
        status.setStatus("UNDEFINED");
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String headerLine;
            while ((headerLine = reader.readLine()).length() != 0) {
                System.out.println(headerLine);
            }
            StringBuilder payload = new StringBuilder();
            while (reader.ready()) {
                payload.append((char) reader.read());
            }
            Transfer transfer = gson.fromJson(payload.toString(), Transfer.class);
            transferMaker.makeTransfer(transfer);
            status.setStatus("TRANSFERRED");
        } catch (IOException | NoCounterpartyFound | InsufficientFundsException | NegativeAmountException ex) {
            status.setStatus("FAILED");
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try (OutputStream output = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(output, true)) {
                writer.println(gson.toJson(status));
                socket.close();
            } catch (IOException e) {
                System.out.println("Server exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
