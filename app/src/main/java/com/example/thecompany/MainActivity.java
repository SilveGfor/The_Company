package com.example.thecompany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.thecompany.classes.OnBackPressedListener;
import com.example.thecompany.fragments.StartFragment;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

public class MainActivity extends AppCompatActivity {

    public static OkHttpClient client;
    public static String NickName = "";
    public static String NickName_2 = "";
    public static String Session_id = "";
    public static String RoomName = "";
    public static String User_id = "";
    public static String User_id_2 = "";
    public static String Sid = "";
    public static String Role = "";
    public static String Theme = "";
    public static String PlayersMinMaxInfo = "";
    public static String Password = "";
    public static boolean onResume = false;
    public static int Game_id;
    public static int Rang;
    public static int MyInviteCode;
    public static Bitmap bitmap_avatar_1;
    public static Bitmap bitmap_avatar_2;
    public static String CURRENT_GAME_VERSION = "0.0.1";

    public static String url = "https://mafiagoserver.online:5000";

    public static String password = "";
    public static String nick = "";

    public static final String APP_PREFERENCES = "user";
    public static final String APP_PREFERENCES_FULLSCREEN = "fullscreen";

    private SharedPreferences mSettings;

    private int currentApiVersion;

    public static Socket socket;
    {
        IO.Options options = IO.Options.builder()
                .setReconnection(true)
                .setReconnectionAttempts(Integer.MAX_VALUE)
                .setReconnectionDelay(1_000)
                .setReconnectionDelayMax(3_000)
                .setRandomizationFactor(0.5)
                .setTimeout(20_000)
                .build();
        socket = IO.socket(URI.create(url), options); //главный namespace
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Boolean fullscreen = mSettings.getBoolean(APP_PREFERENCES_FULLSCREEN, true);

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);

        socket.connect();

        if (fullscreen) {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            currentApiVersion = android.os.Build.VERSION.SDK_INT;

            if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(flags);
                final View decorView = getWindow().getDecorView();
                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
            }
        }


        client = new OkHttpClient.Builder().//connectionSpecs(Collections.singletonList(spec)).
                connectTimeout(30, TimeUnit.SECONDS).callTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).build();


        getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new StartFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}