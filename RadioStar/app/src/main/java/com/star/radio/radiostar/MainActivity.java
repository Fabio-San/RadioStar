package com.star.radio.radiostar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    boolean isPlaying=false;

    private final static String RADIO_STATION_URL = "http://91.194.90.147:8010/;stream.mp3";

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean initialDialogDisplayed = preferences.getBoolean("InitialDialog", false);
        if (!initialDialogDisplayed) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("InitialDialog", true);
            editor.commit();

            // Display the dialog here
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attenzione");
            builder.setMessage("Benvenuti nell'App Radio Star! Questa app necessita di una connessione ad internet per riprodurre la diretta basata sul sito web di www.radiostarnews.it."
                    + "\nAbilitate quindi una rete WiFi o una rete dati prima di premere il tasto Play."
                    + "\nGrazie per l'attenzione!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();
        }

        mFab = (FloatingActionButton) findViewById(R.id.status_fab);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void onClick(View v) {
        //sul click del FAB
        if(!isOnline()){
            Snackbar.make(findViewById(R.id.coordinatorLayout), "Connessione assente", Snackbar.LENGTH_SHORT).show();
        }
            if(isPlaying){
            if(player.isPlaying()) {
                player.stop();
                player.release();
                initializeMediaPlayer();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
                mFab.setImageResource(R.drawable.ic_play);
                isPlaying=false;
            }

        } else {
            if(isOnline()){
            player.prepareAsync();
            player.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    mFab.setImageResource(R.drawable.ic_pause);
                }

            });
            isPlaying=true;
            player.setLooping(true);
            player.isLooping();
                Snackbar.make(findViewById(R.id.coordinatorLayout), "Connesso", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Radio Star")
                        .setContentText("In Diretta")
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, mBuilder.build());
            }

                }
    }

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
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(false);


    }
}