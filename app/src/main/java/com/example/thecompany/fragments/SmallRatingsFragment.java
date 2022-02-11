package com.example.thecompany.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;
import com.example.thecompany.adapters.RatingsAdapter;
import com.example.thecompany.models.RatingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

import static com.example.thecompany.MainActivity.socket;

public class SmallRatingsFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    RatingsAdapter ratingsAdapter;
    boolean pushed_1 = true;
    String main_name = "";

    ArrayList<RatingModel> list_ratings = new ArrayList<>();

    JSONObject json;

    ListView LV_rating;
    ProgressBar PB_loading;
    TextView TV_questionRatings;
    TextView TV_noRatings;

    public static SmallRatingsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SmallRatingsFragment fragment = new SmallRatingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_small_ratings, container, false);

        switch (mPage)
        {
            case 1:
                view = inflater.inflate(R.layout.fragment_small_ratings, container, false);
                LV_rating = view.findViewById(R.id.smallFragmentRatings_LV);
                PB_loading = view.findViewById(R.id.smallFragmentRatings_PB);
                TV_noRatings = view.findViewById(R.id.smallFragmentRatings_TV_noRatings);
                /*
                TV_questionRatings = view.findViewById(R.id.smallFragmentRatings_TV_questionRatings);

                TV_questionRatings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_information, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogInformation_TV_title);
                        TextView TV_text = viewDang.findViewById(R.id.dialogInformation_TV_text);
                        TV_title.setText("Награды за места в рейтинге");
                        TV_text.setText("В этой таблице представлены награды за места в рейтинге. '1 - 500 золота' означает, что игрок на первом месте получит эту награду. Награда за день выдаётся в 3 ночи по Москве, награда за неделю выдаётся в 3 ночи в понедельник, награда за месяц выдаётся в 3 ночи в 1 число месяца");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    }
                });
                 */

                ratingsAdapter = new RatingsAdapter(list_ratings, getContext());
                LV_rating.setAdapter(ratingsAdapter);

                PB_loading.setVisibility(View.VISIBLE);

                socket.on("get_rating", onGetRating);
                main_name = "game_counter";


                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_small_ratings, container, false);
                LV_rating = view.findViewById(R.id.smallFragmentRatings_LV);
                PB_loading = view.findViewById(R.id.smallFragmentRatings_PB);
                TV_noRatings = view.findViewById(R.id.smallFragmentRatings_TV_noRatings);
                /*
                TV_questionRatings = view.findViewById(R.id.smallFragmentRatings_TV_questionRatings);

                TV_questionRatings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_information, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogInformation_TV_title);
                        TextView TV_text = viewDang.findViewById(R.id.dialogInformation_TV_text);
                        TV_title.setText("Награды за места в рейтинге");
                        TV_text.setText("В этой таблице представлены награды за места в рейтинге. '1 - 500 золота' означает, что игрок на первом месте получит эту награду. Награда за день выдаётся в 3 ночи по Москве, награда за неделю выдаётся в 3 ночи в понедельник, награда за месяц выдаётся в 3 ночи в 1 число месяца");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    }
                });
                 */

                PB_loading.setVisibility(View.VISIBLE);

                ratingsAdapter = new RatingsAdapter(list_ratings, getContext());
                LV_rating.setAdapter(ratingsAdapter);

                socket.on("get_rating", onGetRating);
                main_name = "wins";
                break;
        }

        return view;
    }

    private final Emitter.Listener onGetRating = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray JA_users;
                    JSONObject JO_user;
                    String name = "";
                    String nick, status, color, user_id, avatar, score;
                    boolean online;
                    Log.d("kkk", "принял - get_rating1 - " + data);

                    try {
                        name = data.getString("name");
                        if (name.contains(main_name))
                        {
                            JA_users = data.getJSONArray("data");
                            for (int i = 0; i < JA_users.length(); i++) {
                                JO_user = JA_users.getJSONObject(i);
                                nick = JO_user.getString("nick");
                                avatar = JO_user.getString("avatar");
                                score = JO_user.getString("score");
                                JO_user.remove("avatar");
                                Log.e("kkk", String.valueOf(JO_user));
                                list_ratings.add(new RatingModel(nick, fromBase64(avatar), list_ratings.size() + 1, score));
                            }
                            PB_loading.setVisibility(View.INVISIBLE);
                            ratingsAdapter.notifyDataSetChanged();
                            if (list_ratings.size() == 0)
                            {
                                TV_noRatings.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public Bitmap fromBase64(String image) {
        // Декодируем строку Base64 в массив байтов
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);

        // Декодируем массив байтов в изображение
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Помещаем изображение в ImageView
        return decodedByte;
    }

    public void CallSocket(String name)
    {
        json = new JSONObject();
        try {
            json.put("nick", MainActivity.NickName);
            json.put("session_id", MainActivity.Session_id);
            json.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get_rating", json);
        Log.d("kkk", "Socket_отправка - get_rating - "+ json.toString());
    }
}