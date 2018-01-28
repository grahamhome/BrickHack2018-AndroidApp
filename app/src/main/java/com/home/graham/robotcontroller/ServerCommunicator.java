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
    private Socket socket;
    private InputStreamReader serverStreamReader;
    private BufferedReader serverReader;
    private DataOutputStream serverWriter;
    public AtomicBoolean running = new AtomicBoolean(true);

    public ServerCommunicator(MainActivity invoker, String ipAddress, int port) {
        this.invoker = invoker;
        this.ipAddress = ipAddress;
        this.port = port;
        addObserver(invoker);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ipAddress, port);
            serverReader = new BufferedReader(serverStreamReader = new InputStreamReader(socket.getInputStream()));
            serverWriter = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            setChanged();
            notifyObservers(e.getMessage());
            return;
        }
        while(running.get()) {
            try {
                if (serverReader.ready()) {
                    String input = serverReader.readLine();
                    if (input != null) {
                        setChanged();
                        notifyObservers(input);
                    }
                } else {
                    String data;
                    while ((data = MainActivity.queue.poll()) != null) {
                        serverWriter.writeBytes(data + "\n");
                    }
                    serverWriter.flush();
                }
            } catch (IOException e) {
                setChanged();
                notifyObservers(invoker.getApplicationContext().getString(R.string.read_failure));
            }
        }
        closeEverything();
    }

    private void closeEverything() {
        try {
            serverWriter.close();
            serverReader.close();
            serverStreamReader.close();
            socket.close();
        } catch (IOException e) {
            Toast.makeText(invoker.getApplicationContext(), invoker.getString(R.string.shutdown_failure), Toast.LENGTH_SHORT).show();
        }
    }
}
