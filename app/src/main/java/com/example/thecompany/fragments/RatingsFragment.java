package com.example.thecompany.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.thecompany.R;
import com.example.thecompany.adapters.RatingsPagerAdapter;
import com.example.thecompany.classes.MovieGifView;
import com.example.thecompany.classes.OnBackPressedListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import static com.example.thecompany.MainActivity.socket;


public class RatingsFragment extends Fragment implements OnBackPressedListener {

    public TabLayout tabLayout;

    public ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ratings, container, false);

        tabLayout = view.findViewById(R.id.fragmentRatings_tabLayout);
        viewPager = view.findViewById(R.id.fragmentRatings_viewPager);

        socket.off("get_rating");

        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager.setAdapter(
                new RatingsPagerAdapter(getActivity().getSupportFragmentManager()));

        // Передаём ViewPager в TabLayout
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new MenuFragment()).commit();
    }
}