package com.example.thecompany.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.thecompany.R;
import com.example.thecompany.classes.MovieGifView;


public class RatingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ratings, container, false);

        ImageView imageView = view.findViewById(R.id.Glig);

        Glide
                .with(this)
                .load(R.drawable.loadingproject1)
                .into(imageView);

        return view;
    }
}