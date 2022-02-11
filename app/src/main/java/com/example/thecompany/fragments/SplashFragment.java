package com.example.thecompany.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thecompany.MainActivity;
import com.example.thecompany.R;

public class SplashFragment extends Fragment {
    ImageView IV_fon, gif;
    TextView TV_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TV_name = view.findViewById(R.id.fragmentSplash_TV_name);
        IV_fon = view.findViewById(R.id.fragmentSplash_IV_fon);
        gif = view.findViewById(R.id.fragmentSplash_IV_gif);

        TV_name.animate().translationY(-2500).setDuration(1000).setStartDelay(6000);
        IV_fon.animate().translationY(2000).setDuration(1000).setStartDelay(6000);
        gif.animate().translationY(1500).setDuration(1000).setStartDelay(6000);

        Glide
                .with(this)
                .load(R.drawable.loadingproject1)
                .into(gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new StartFragment()).commit();

            }
        },8000);

        return view;
    }
}