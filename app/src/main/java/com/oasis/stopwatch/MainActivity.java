package com.oasis.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView txtTimerDisplay;
    private Button btnStart, btnHold, btnStop;

    // Time-Tracking Engine Variables
    private Handler timerHandler = new Handler();
    private long startTimeMillis = 0L;
    private long timeBufferingMillis = 0L;
    private long updatedTimeMillis = 0L;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimerDisplay = findViewById(R.id.txtTimerDisplay);
        btnStart = findViewById(R.id.btnStart);
        btnHold = findViewById(R.id.btnHold);
        btnStop = findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnHold.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnStart) {
            if (!isTimerRunning) {
                // Begin or resume time sequence tracking
                startTimeMillis = SystemClock.uptimeMillis();
                timerHandler.postDelayed(updateTimerThread, 0);
                isTimerRunning = true;

                // Toggle interface interactive rules
                btnStart.setEnabled(false);
                btnHold.setEnabled(true);
                btnStop.setEnabled(true);
                btnHold.setText("HOLD");
            }
        }
        else if (id == R.id.btnHold) {
            if (isTimerRunning) {
                // Freeze counting loop temporarily (Hold state)
                timeBufferingMillis += SystemClock.uptimeMillis() - startTimeMillis;
                timerHandler.removeCallbacks(updateTimerThread);
                isTimerRunning = false;

                btnStart.setEnabled(true);
                btnHold.setText("RESET");
                btnStop.setEnabled(false);
            } else {
                // If clicked while already paused, behaves as a clear slate RESET
                startTimeMillis = 0L;
                timeBufferingMillis = 0L;
                updatedTimeMillis = 0L;
                txtTimerDisplay.setText("00:00:00");
                btnHold.setEnabled(false);
                btnHold.setText("HOLD");
            }
        }
        else if (id == R.id.btnStop) {
            // Hard terminal cut-off stop
            timerHandler.removeCallbacks(updateTimerThread);
            startTimeMillis = 0L;
            timeBufferingMillis = 0L;
            updatedTimeMillis = 0L;
            isTimerRunning = false;

            txtTimerDisplay.setText("00:00:00");
            btnStart.setEnabled(true);
            btnHold.setEnabled(false);
            btnHold.setText("HOLD");
            btnStop.setEnabled(false);
        }
    }

    // The Background Asynchronous Thread Process
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTimeMillis;
            updatedTimeMillis = timeBufferingMillis + timeInMilliseconds;

            // Mathematical conversions from total milliseconds
            int secs = (int) (updatedTimeMillis / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTimeMillis % 1000) / 10; // Scaled to two digits

            // Render dynamic hot reload updates directly onto visual interface
            txtTimerDisplay.setText(String.format(Locale.US, "%02d:%02d:%02d", mins, secs, milliseconds));

            // Loop code callback execution automatically every 10 milliseconds
            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Guardrail: Safety cleanup memory leaks check if app is closed while counting
        timerHandler.removeCallbacks(updateTimerThread);
    }
}