package com.example.anonymus.synapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoadActivity extends AppCompatActivity {

    /*
    *   Variables needed for progress bar
    */
    private int mProgressStatus = 0;
    private ProgressBar mProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load);

        final TextView tvPercent = (TextView) findViewById(R.id.tvProgress);
        tvPercent.setText(getString(R.string.progressString, mProgressStatus));

        mProgress = (ProgressBar) findViewById(R.id.progressBar1);

        // Start lengthy operation in a background thread
        new Thread() {
            public void run() {
                while (mProgressStatus < 100) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mProgressStatus += 25;
                                tvPercent.setText(getString(R.string.progressString, mProgressStatus));
                                mProgress.setProgress(mProgressStatus);
                            }
                        });
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent nextActivity = new Intent(LoadActivity.this, MainActivity.class);
                startActivity(nextActivity);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
