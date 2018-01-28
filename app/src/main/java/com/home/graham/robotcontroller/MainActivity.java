package com.home.graham.robotcontroller;

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
    private TextView receivedDataDisplay;

    private final static String SERVER_IP_ADDRESS = "129.21.60.175";
    private final static int SERVER_PORT = 6789;

    private Thread serverCommunicationThread;

    public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();


    public static AtomicBoolean error = new AtomicBoolean(false);
    public static String whatWentWrong;

    public static AtomicBoolean feedback = new AtomicBoolean((false));
    public static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transmitData = findViewById(R.id.transmit_data);
        transmitButton = findViewById(R.id.transmit_button);
        receivedDataDisplay = findViewById(R.id.receivedData);

        new Thread(new ServerCommunicator(MainActivity.this, SERVER_IP_ADDRESS, SERVER_PORT)).start();

        transmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                queue.add(transmitData.getText().toString());
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
