package com.home.graham.robotcontroller;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Graham Home on 1/27/2018.
 */

public class ServerCommunicator extends Observable implements Runnable {

    private MainActivity invoker;
    private String ipAddress;
    private int port;
    //private Socket socket;
    //private InputStreamReader serverStreamReader;
    //private BufferedReader serverReader;
    //private DataOutputStream serverWriter;
    public AtomicBoolean running = new AtomicBoolean(true);

    public ServerCommunicator(MainActivity invoker, String ipAddress, int port) {
        this.invoker = invoker;
        this.ipAddress = ipAddress;
        this.port = port;
        addObserver(invoker);
    }

    @Override
    public void run() {
        while(running.get()) {
            Socket socket = null;
            DataOutputStream serverWriter = null;
            Square data;
            while ((data = MainActivity.queue.poll()) != null) {
                try {
                    socket = (socket == null ? new Socket(ipAddress, port) : socket);
                    //serverReader = new BufferedReader(serverStreamReader = new InputStreamReader(socket.getInputStream()));
                    serverWriter = (serverWriter == null ? new DataOutputStream(socket.getOutputStream()) : serverWriter);
                    serverWriter.writeBytes(data.toString() + "\n");
                    serverWriter.flush();
                } catch (IOException e) {
                    setChanged();
                    notifyObservers(invoker.getApplicationContext().getString(R.string.create_client_failure));
                    return;
                }
            }
            if (socket != null) {
                try {
                    serverWriter.close();
                    socket.close();
                } catch (IOException e) {
                    notifyObservers(invoker.getApplicationContext().getString(R.string.shutdown_failure));
                }
            }
        }
    }

    private void closeEverything() {
        //try {

            //serverReader.close();
            //serverStreamReader.close();

        /*} catch (IOException e) {
            Toast.makeText(invoker.getApplicationContext(), invoker.getString(R.string.shutdown_failure), Toast.LENGTH_SHORT).show();
        }*/
    }
}
