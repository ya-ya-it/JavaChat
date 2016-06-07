/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.chat.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author human
 */
public class ChatIncomeServer implements Runnable {

    private static final int SERVER_PORT = 7813;

    private ServerSocket serverSocket;

    private boolean work;

    public ChatIncomeServer() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        serverSocket.setSoTimeout(1000);
    }

    @Override
    public void run() {
        System.out.println("@CHAT SERVER STARTED ON " + SERVER_PORT);

        work = true;

        while (work) {
            try {
                Socket acceptedSocket = serverSocket.accept();
                System.out.println("CONNECTED: " + acceptedSocket.getInetAddress());

                DataInputStream dis = new DataInputStream(acceptedSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(acceptedSocket.getOutputStream());

                String command = dis.readUTF();
                System.out.println("COMMAND: " + command);

                switch (command) {
                    case "PING":
                        dos.writeUTF("PONG");
                        dos.flush();
                        break;
                    case "MSG": {
                        String recipient = dis.readUTF();
                        String message = dis.readUTF();

                        System.out.printf("Send message:\n  address: %s\n  message: %s\n\n", recipient, message);

                        dos.writeUTF("OK");
                        dos.flush();
                        break;
                    }
                }
            } catch (SocketTimeoutException ex) {
                // NOOP
            } catch (IOException ex) {
                Logger.getLogger(ChatIncomeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
