package com.example.thecompany.fragments;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.thecompany.MainActivity.client;
import static com.example.thecompany.MainActivity.socket;


public class StartFragment extends Fragment {
    private static final String url= MainActivity.url + "/login";

    Button btnSignIn;
    Button btnReg;

    ProgressBar PB_loading;

    RelativeLayout RL_back;

    EditText ET_nick;
    EditText ET_password;

    String NickName = "";
    String Email = "";
    String Session_id = "";

    Boolean AutoRun = false;

    public static final String APP_PREFERENCES = "user";
    public static final String APP_PREFERENCES_NICK = "nick";
    public static final String APP_PREFERENCES_PASSWORD = "password";

    private SharedPreferences mSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        btnSignIn = view.findViewById(R.id.fragmentStart_btn_enter);
        btnReg = view.findViewById(R.id.fragmentStart_btn_register);
        ET_nick = view.findViewById(R.id.fragmentRegister_ET_nick);
        ET_password = view.findViewById(R.id.fragmentStart_ET_password);
        PB_loading = view.findViewById(R.id.fragmentStart_PB);
        RL_back = view.findViewById(R.id.fragmentGamesList_RL_back);

        PB_loading.setVisibility(View.INVISIBLE);
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //if (mSettings.contains(APP_PREFERENCES_EMAIL) && mSettings.contains(APP_PREFERENCES_PASSWORD)) {
        if (!mSettings.getString(APP_PREFERENCES_NICK, "").equals("")) {
            // Получаем значение из настроек
            String mEmail = mSettings.getString(APP_PREFERENCES_NICK, "");
            String mPassword = mSettings.getString(APP_PREFERENCES_PASSWORD, "");
            // Выводим на экран данные из настроек
            Log.d("kkk", "SharedPref mEmail - " + mEmail);
            Log.d("kkk", "SharedPref mPassword - " + mPassword);

            AutoRun = true;

            MainActivity.nick = mEmail;
            MainActivity.password = mPassword;

            Login(container);
        }
        else
        {
            Log.d("kkk", "SharedPref mEmail, mPassword - нет данных");
        }

        RL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkOnline(getContext())) {
                    MainActivity.nick = ET_nick.getText().toString();
                    MainActivity.password = ET_password.getText().toString();
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_NICK, String.valueOf(ET_nick.getText()));
                    editor.putString(APP_PREFERENCES_PASSWORD, String.valueOf(ET_password.getText()));
                    editor.apply();
                    if (!ET_password.getText().toString().equals("") && !ET_nick.getText().toString().equals("")) {
                        AutoRun = false;
                        Login(container);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Не все поля заполнены!");
                        TV_error.setText("Вы должны заполнить все поля");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                    TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Нет подключения к интернету!");
                    TV_error.setText("Проверьте соединение сети");
                    AlertDialog alert = builder.create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.show();
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new RegisterFragment()).commit();
            }
        });
        return view;
    }

    public void Login(ViewGroup container) {
        try {
            final JSONObject json = new JSONObject();
            PB_loading.setVisibility(View.VISIBLE);
            final String[] resp = {""};
            SharedPreferences mSettings;
            mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            json.put("email", MainActivity.nick);
            json.put("password", MainActivity.password);
            json.put("current_game_version", MainActivity.CURRENT_GAME_VERSION);

            Log.d("kkk", "Отправил: " + json);

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));

            Request request = new Request.Builder()
                    .url(url).post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.d("kkk", "Failure: " + e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    resp[0] = response.body().string();
                    String Answer = resp[0];
                    Log.d("kkk", "Принял: " + Answer);
                    try {
                        switch (Answer) {
                            case "incorrect_email":
                                ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                    PB_loading.setVisibility(View.INVISIBLE);
                                    if (!AutoRun) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                        builder.setView(viewDang);
                                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                                        TV_title.setText("Такого аккаунта не существует!");
                                        TV_error.setText("Возможно, вы указали неверный домен почты (например: @mail.ru вместо @gmail.com) или ошиблись в написании почти");
                                        AlertDialog alert = builder.create();
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alert.show();
                                    }
                                });
                                break;
                            case "incorrect_password":
                                ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                    PB_loading.setVisibility(View.INVISIBLE);
                                    if (!AutoRun) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                        builder.setView(viewDang);
                                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                                        TV_title.setText("Неправильный пароль!");
                                        TV_error.setText("Если вы забыли пароль, то его всегда можно восстановить по вашей почте");
                                        AlertDialog alert = builder.create();
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alert.show();
                                    }
                                });

                                break;
                            case "incorrect_game_version":
                                ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_information, null);
                                    builder.setView(viewDang);
                                    TextView TV_title = viewDang.findViewById(R.id.dialogInformation_TV_title);
                                    TextView TV_text = viewDang.findViewById(R.id.dialogInformation_TV_text);
                                    TV_title.setText("Обновите игру!");
                                    TV_text.setText("Уже доступно новое обновление");
                                    AlertDialog alert = builder.create();
                                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alert.show();
                                });
                                break;
                            default:
                                JSONObject data = new JSONObject(resp[0]);
                                if (data.has("session_id"))
                                {
                                    NickName = data.get("nick").toString();
                                    Email = data.get("email").toString();
                                    Session_id = data.get("session_id").toString();
                                    MainActivity.User_id = data.get("user_id").toString();
                                    MainActivity.Sid = data.get("sid").toString();
                                    MainActivity.Role = data.get("role").toString();
                                    MainActivity.Rang = data.getInt("rang");
                                    MainActivity.MyInviteCode = data.getInt("my_invite_code");
                                    if (data.getString("avatar") == null || data.getString("avatar").equals("") || data.getString("avatar").equals("null")) {
                                        ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                            builder.setView(viewDang);
                                            TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                                            TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                                            TV_title.setText("Ух ты!");
                                            TV_error.setText("Вы не поставили аватарку. Её можно установить в настройках");
                                            AlertDialog alert = builder.create();
                                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            alert.show();
                                        });
                                    }

                                    MainActivity.NickName = NickName;
                                    MainActivity.Session_id = Session_id;
                                    MainActivity.onResume = true;
                                    final JSONObject json2 = new JSONObject();
                                    try {
                                        json2.put("nick", NickName);
                                        json2.put("session_id", Session_id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    socket.emit("connection", json2);
                                    Log.d("kkk", "CONNECTION after Login");
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new MenuFragment()).commit();
                                }
                                break;
                        }
                    } catch (Exception e) {
                        Log.d("kkk", String.valueOf(e.getMessage()));
                    }
                }
            });
        } catch (Exception e) {

        }
    }

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
}