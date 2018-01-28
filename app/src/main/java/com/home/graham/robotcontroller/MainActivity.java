package com.home.graham.robotcontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Graham Home on 1/27/2018.
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private EditText transmitData;
    private Button transmitButton;
    private Button pathLauncherButton;
    private TextView receivedDataDisplay;

    private static Thread serverThread;

    private final static String SERVER_IP_ADDRESS = "10.101.53.42";//"129.21.60.175";
    private final static int SERVER_PORT = 6789;

    public static ConcurrentLinkedQueue<Square> queue = new ConcurrentLinkedQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transmitData = findViewById(R.id.transmit_data);
        transmitButton = findViewById(R.id.transmit_button);
        pathLauncherButton = findViewById(R.id.path_launcher_button);
        receivedDataDisplay = findViewById(R.id.receivedData);

        if (serverThread == null || !serverThread.isAlive()) {
            (serverThread = new Thread(new ServerCommunicator(MainActivity.this, SERVER_IP_ADDRESS, SERVER_PORT))).start();
        }

        /*transmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                queue.add(transmitData.getText().toString());
            }
        });*/

        pathLauncherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TrailBlazer.class));
            }
        });
    }

    @Override
    public void update(Observable observable, final Object result) {
        receivedDataDisplay.post(new Runnable() {
            @Override
            public void run() {
                receivedDataDisplay.setText(result.toString());
            }
        });
    }
}
