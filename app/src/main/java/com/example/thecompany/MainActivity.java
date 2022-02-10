package com.example.thecompany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.thecompany.classes.OnBackPressedListener;

import java.net.URI;
import io.socket.client.IO;
import io.socket.client.Socket;

import okhttp3.OkHttpClient;

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
    public static Bitmap bitmap_avatar_2;
    public static String CURRENT_GAME_VERSION = "0.0.1";

    public static String url = "https://mafiagoserver.online:5000";

    public static String password = "";
    public static String nick = "";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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