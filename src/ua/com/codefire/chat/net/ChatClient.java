/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.chat.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author human
 */
public class ChatClient implements Client, AutoCloseable {

    private String address;
    private int port;

    private Socket socket;

    private DataOutputStream dos;
    private DataInputStream dis;

    public ChatClient(String addres, int port) {
        this.address = addres;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean connect() {
        if (socket == null) {
            try {
                socket = new Socket();
                socket.setSoTimeout(5000);

                InetSocketAddress serverDestination = new InetSocketAddress(address, port);
                socket.connect(serverDestination);

                connected(socket);

                return true;
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    /**
     * Invokes when socket connected to destination. Also fill IO streams for connection.
     *
     * @param socket sources socket object.
     * @throws IOException
     */
    public void connected(Socket socket) throws IOException {
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

//        boolean pingpong = sendPingPong(dos, dis);
//
//        if (pingpong) {
//            System.out.println("SUCCESSFULLY PING-PONG");
//        }
    }

    /**
     * Send ping and receive pong messages.
     *
     * @param dos output stream for sending ping.
     * @param dis input stream for receiving pong.
     * @return true if succeeded, otherwise false.
     * @throws IOException
     */
    private boolean sendPingPong(DataOutputStream dos, DataInputStream dis) throws IOException {
        dos.writeUTF(Client.PING);
        dos.flush();

        return Client.PONG.equals(dis.readUTF());
    }

    /**
     * Send message to server with recipient address.
     *
     * @param recipient recipient address.
     * @param message message for send.
     * @return true if sent successfully, otherwise false.
     * @throws IOException
     */
    public boolean sendMessage(String recipient, String message) throws IOException {
        dos.writeUTF("MSG");
        dos.flush();

        dos.writeUTF(recipient);
        dos.flush();

        dos.writeUTF(message);
        dos.flush();

        return COMPLETE.equals(dis.readUTF());
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

}
