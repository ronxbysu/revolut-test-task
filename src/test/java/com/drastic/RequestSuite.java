package com.drastic;

import com.drastic.server.Request;
import com.drastic.server.Status;
import com.drastic.transfer.TransferMaker;
import com.drastic.transfer.TransferModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;

import static com.drastic.server.StatusConstants.FAILED;
import static com.drastic.server.StatusConstants.TRANSFERRED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class RequestSuite {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private TransferMaker transferMaker;
    private Request request;
    private Socket socket;

    private ByteArrayInputStream byteArrayInputStream;
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private String status;

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {new ByteArrayInputStream(bytesTransferred), TRANSFERRED},
                {new ByteArrayInputStream(bytesFailed), FAILED}
        });
    }

    public RequestSuite(ByteArrayInputStream byteArrayInputStream, String status) {
        this.byteArrayInputStream = byteArrayInputStream;
        this.status = status;
    }

    private final static byte[] bytesTransferred = (
            "POST /transfer HTTP/1.1\n" +
                    "Host: localhost:8081\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0\n" +
                    "Accept: */*\n" +
                    "Accept-Language: pl-PL\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Content-Length: 50\n" +
                    "Content-Type: text/plain;charset=UTF-8\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "\n" +
                    "{\"from\":\"CashMachine\",\"to\":\"Roman\",\"amount\":\"500\"}"
    ).getBytes();

    private final static byte[] bytesFailed = (
            "POST /transfer HTTP/1.1\n" +
                    "Host: localhost:8081\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0\n" +
                    "Accept: */*\n" +
                    "Accept-Language: pl-PL\n" +
                    "Accept-Encoding: gzip, deflate\n" +
                    "Content-Length: 50\n" +
                    "Content-Type: text/plain;charset=UTF-8\n" +
                    "DNT: 1\n" +
                    "Connection: keep-alive\n" +
                    "\n" +
                    "{\"from\":\"CashMachine\",\"to\":\"Roman\",\"amount\":\"-500\"}"
    ).getBytes();

    @Before
    public void before() {
        final Injector injector = Guice.createInjector(new TransferModule());
        transferMaker = injector.getInstance(TransferMaker.class);
        socket = mock(Socket.class);
    }

    @Test
    public void requestTransferredTest() throws IOException {
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);

        request = new Request(socket, transferMaker);
        request.run();
        Status status = gson.fromJson(new String(byteArrayOutputStream.toByteArray()), Status.class);
        assertEquals(this.status, status.getStatus());
    }
}
