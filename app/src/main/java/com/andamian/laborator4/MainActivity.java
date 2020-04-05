package com.andamian.laborator4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Handler handler1;
    private Handler handler2;

    private Button button1;
    private Button button2;
    private TextView textView;
    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor pool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.textView2);


        handler1 = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messageText = (String) msg.obj;
                textView.setText(messageText);
            }
        };

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Downloading the file");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        Message message = handler1.obtainMessage();
                        message.obj = "the file was downloaded";
                        message.sendToTarget();
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        });


        handler2 = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String messageText = (String) msg.obj;
                textView.append(messageText);

            }
        };

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Downloading 20 files...\n");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {

                        }

                        Message message = handler2.obtainMessage();
                        message.obj = "donwloaded by : " + Thread.currentThread().getName() + ".\n";
                        message.sendToTarget();
                    }
                };

                queue = new LinkedBlockingQueue<>();
                pool = new ThreadPoolExecutor(
                        20,
                        20,
                        1,
                        TimeUnit.SECONDS,
                        queue);

                for (int i = 0; i < 20; i++) {
                    pool.execute(runnable);
                }
            }
        });
    }
}
