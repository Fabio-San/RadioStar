package com.star.radio.radiostar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    boolean isPlaying=false;
    private final static String RADIO_STATION_URL = "http://91.194.90.147:8010/;stream.mp3";

    private ProgressBar playSeekBar;

    //private ImageButton buttonPlay;

    //private ImageButton buttonStopPlay;

    private MediaPlayer player;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();

        initializeMediaPlayer();
    }
    private void initializeUIElements() {

        //playSeekBar = (ProgressBar) findViewById(R.id.progressBar1);
        //playSeekBar.setMax(100);
        //playSeekBar.setVisibility(View.INVISIBLE);

        //buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        //buttonPlay.setOnClickListener(this);

        //buttonStopPlay = (ImageButton) findViewById(R.id.buttonStopPlay);
        //buttonStopPlay.setEnabled(false);
        //buttonStopPlay.setOnClickListener(this);

        mFab = (FloatingActionButton) findViewById(R.id.status_fab);

    }

    public void onClick(View v) {
        //sul click del FAB
        if(isPlaying){
            if (player.isPlaying()) {
                player.stop();
                player.release();
                initializeMediaPlayer();
                mFab.setBackgroundResource(R.drawable.ic_play);
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
                Intent svc=new Intent(this, BackgroundSoundService.class);
                startService(svc);
            }
            isPlaying=false;
        } else {
            mFab.setBackgroundResource(R.drawable.ic_pause);
            player.prepareAsync();
            player.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }

            });
            isPlaying=true;
            Context context = getApplicationContext();
            CharSequence text = "In Connessione...";
            int duration = android.widget.Toast.LENGTH_LONG;
            android.widget.Toast toast = android.widget.Toast.makeText(context, text, duration);
            toast.show();
            Object nm;
            nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder =

                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Radio Star")
                            .setContentText("In Diretta");
            PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify( 0, mBuilder.build());
        }
            //startPlaying();
        //Context context = getApplicationContext();
        //CharSequence text = "In Connessione...";
        //int duration = android.widget.Toast.LENGTH_LONG;
        //android.widget.Toast toast = android.widget.Toast.makeText(context, text, duration);
        //toast.show();
        //Object nm;
        //nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //NotificationCompat.Builder mBuilder =

        //(NotificationCompat.Builder) new NotificationCompat.Builder(this)
        //.setSmallIcon(R.drawable.ic_launcher)
        //.setContentTitle("Radio Star")
        //.setContentText("In Diretta");
        //PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.notify( 0, mBuilder.build());
        //} else if (v == buttonStopPlay) {
        //stopPlaying();
        //Intent svc=new Intent(this, BackgroundSoundService.class);
        //startService(svc);
        }
    //}

    //private void startPlaying() {
        //if (player.isPlaying()) {
//buttonPlay.setVisibility(View.INVISIBLE);
//}
//buttonStopPlay.setEnabled(true);
                //buttonPlay.setEnabled(false);
//buttonPlay.setVisibility(View.INVISIBLE);
//playSeekBar.setVisibility(View.VISIBLE);


//player.prepareAsync();

    //player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }

        //});

    //}

//private void stopPlaying() {
//if (player.isPlaying()) {
    //player.stop();
//player.release();
//initializeMediaPlayer();
//NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//notificationManager.cancel(0);
//}

    //buttonPlay.setEnabled(true);
    //buttonStopPlay.setEnabled(false);
    //playSeekBar.setVisibility(View.INVISIBLE);
    //buttonPlay.setVisibility(View.VISIBLE);

    //}

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(RADIO_STATION_URL);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(false);


    }
}