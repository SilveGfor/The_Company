package com.example.thecompany.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;
import com.example.thecompany.classes.OnBackPressedListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

import static com.example.thecompany.MainActivity.socket;

public class MenuFragment extends Fragment implements OnBackPressedListener {
    Button btnRating;
    Button btnGame;
    ImageButton btnSettings;

    TextView TV_nick;

    public static final String APP_PREFERENCES = "user";
    public static final String APP_PREFERENCES_NICK = "nick";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES_FULLSCREEN = "fullscreen";

    private SharedPreferences mSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        btnRating = view.findViewById(R.id.fragmentMenu_btn_rating);
        btnGame = view.findViewById(R.id.fragmentMenu_btn_game);
        btnSettings = view.findViewById(R.id.fragmentMenu_btn_settings);
        TV_nick = view.findViewById(R.id.fragmentMenu_TV_nick);

        //IV_avatar = view.findViewById(R.id.fragmentMenu_IV_avatar);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //TODO: Сделать что-то про последнюю роль
        //SetBackgroundRole(mSettings.getString(APP_PREFERENCES_LAST_ROLE, "mafia"));

        socket.off("get_profile");

        socket.on("get_profile", OnGetProfile);

        //настройки от Шлыкова
        //Nastroiki nastroiki = new Nastroiki();

        final JSONObject json = new JSONObject();
        try {
            json.put("nick", MainActivity.NickName);
            json.put("session_id", MainActivity.Session_id);
            json.put("info_nick", MainActivity.NickName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get_profile", json);
        Log.d("kkk", "Socket_отправка - get_profile - "+ json.toString());

        //final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_center);

        // amplitude 0.2 and frequency 20
        //BounceInterpolator interpolator = new BounceInterpolator();
        //animation.setInterpolator(interpolator);
        //CV_info.startAnimation(animation);

        btnSettings.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new SettingsFragment()).commit();
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new RatingsFragment()).commit();
            }
        });

        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkOnline(getContext()))
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new GameFragment()).commit();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogGame_TV_title);
                    TextView TV_error = viewDang.findViewById(R.id.dialogGame_TV_text);
                    TV_title.setText("Нет подключения к интернету!");
                    TV_error.setText("Проверьте соединение сети");
                    AlertDialog alert = builder.create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.show();
                }
            }
        });

        return view;
    }

    private void reset() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public Bitmap fromBase64(String image) {
        // Декодируем строку Base64 в массив байтов
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);

        // Декодируем массив байтов в изображение
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Помещаем изображение в ImageView
        return decodedByte;
    }

    private final Emitter.Listener OnGetProfile = args -> {
        if(getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                String nick = "", avatar = "";
                boolean online = false;
                int money = 0, exp = 0, gold = 0, rang = 0;
                JSONObject statistic = new JSONObject();
                JSONObject JO_statuses = new JSONObject();
                JSONObject JO_colors = new JSONObject();
                JSONObject JO_chance_of_role = new JSONObject();
                int game_counter = 0, max_money_score = 0, max_exp_score = 0;
                String general_pers_of_wins = "", mafia_pers_of_wins = "", peaceful_pers_of_wins = "", user_id_2 = "", main_status = "", main_personal_color = "", premium_time = "";
                int was_citizen = 0, was_sheriff = 0, was_doctor = 0, was_lover = 0, was_journalist = 0, was_bodyguard = 0, was_doctor_of_easy_virtue = 0, was_maniac = 0, was_mafia = 0, was_mafia_don = 0, was_terrorist = 0, was_poisoner = 0;
                //data.remove("avatar");
                //data.remove("statistics");
                //data.remove("chance_of_role");
                Log.d("kkk", "принял - get_profile - " + data);
                try {
                    avatar = data.getString("avatar");
                    nick = data.getString("nick");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (avatar != null && !avatar.equals("") && !avatar.equals("null")) {
                    MainActivity.bitmap_avatar_1 = fromBase64(avatar);
                    //IV_avatar.setImageBitmap(MainActivity.bitmap_avatar_1);
                }

                TV_nick.setText(nick);
            }
        });
    };

    public boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    @Override
    public void onBackPressed() {
        reset();
    }
}