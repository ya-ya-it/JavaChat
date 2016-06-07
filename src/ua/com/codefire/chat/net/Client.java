/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.chat.net;

/**
 *
 * @author human
 */
public interface Client {

    public static final String PING = "PING";
    public static final String PONG = "PONG";
    public static final String COMPLETE = "OK";

    /**
     * Connect to destination.
     *
     * @return true if connected successfully, otherwise false.
     */
    public boolean connect();

}
