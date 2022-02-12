package com.example.thecompany.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;
import com.example.thecompany.models.RatingModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.thecompany.MainActivity.socket;

public class RatingsAdapter extends BaseAdapter {
    public ArrayList<RatingModel> list_ratings;
    public Context context;
    public LayoutInflater layout;

    public RatingsAdapter(ArrayList<RatingModel> list_ratings, Context context)
    {
        this.list_ratings = list_ratings;
        this.context = context;
        layout=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layout.inflate(R.layout.item_rating_user, null);

        TextView TV_nick = view.findViewById(R.id.itemRatingUser_TV_nick);
        TextView TV_score = view.findViewById(R.id.itemRatingUser_TV_score);
        TextView TV_place = view.findViewById(R.id.itemRatingUser_TV_place);
        ImageView IV_avatar = view.findViewById(R.id.itemRatingUser_IV_avatar);

        TV_score.setText(list_ratings.get(position).score);
        TV_place.setText(String.valueOf(list_ratings.get(position).place));

        if (list_ratings.get(position).avatar != null) {
            IV_avatar.setImageBitmap(list_ratings.get(position).avatar);
        }
        return view;
    }

    @Override
    public int getCount() {
        return list_ratings.size();
    }

    @Override
    public Object getItem(int position) {
        return list_ratings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
