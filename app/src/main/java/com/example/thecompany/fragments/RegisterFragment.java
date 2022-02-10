package com.example.thecompany.fragments;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;
import com.example.thecompany.classes.OnBackPressedListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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

public class RegisterFragment extends Fragment implements OnBackPressedListener {
    private static final String url2 = MainActivity.url + "/registration";

    String resp = "";

    Button btnReg;

    RelativeLayout RL_back;

    ShimmerTextView STV_text;

    EditText ETnick;
    EditText ETemail;
    EditText ETpassword1;
    EditText ETpassword2;
    EditText ETcode;
    EditText ET_inviteCode;
    TextView text_reg;

    ProgressBar loading;

    TextView TV_sendCodeOneMoreTime;
    TextView TV_repeatRegistration;
    TextView TV_question;

    public static final String APP_PREFERENCES = "user";
    public static final String APP_PREFERENCES_NICK = "nick";
    public static final String APP_PREFERENCES_PASSWORD = "password";

    private SharedPreferences mSettings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        view = inflater.inflate(R.layout.fragment_register, container, false);
        btnReg = view.findViewById(R.id.fragmentRegister_btn_reg);

        ETnick = view.findViewById(R.id.fragmentRegister_ET_nick);
        ETpassword1 = view.findViewById(R.id.fragmentRegister_ET_password1);
        ETpassword2 = view.findViewById(R.id.fragmentRegister_ET_password2);
        loading = view.findViewById(R.id.fragmentChangePassword_PB);
        RL_back = view.findViewById(R.id.fragmentGamesList_RL_back);
        STV_text = view.findViewById(R.id.fragmentRegister_TV_text);

        text_reg.setVisibility(View.GONE);
        ETcode.setVisibility(View.GONE);
        TV_repeatRegistration.setVisibility(View.GONE);
        TV_sendCodeOneMoreTime.setVisibility(View.GONE);

        loading.setVisibility(View.GONE);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        RL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new StartFragment()).commit();
            }
        });

        btnReg.setOnClickListener(v -> {
            if (isNetworkOnline(getContext())) {
                if (ETpassword1.getText().toString().equals(ETpassword2.getText().toString()) &&
                        !ETpassword1.getText().toString().trim().equals("") &&
                        !ETnick.getText().toString().trim().equals("") &&
                        (!ETnick.getText().toString().contains(".") &&
                        !ETnick.getText().toString().contains("{") &&
                        !ETnick.getText().toString().contains("}")) &&
                        ETpassword1.length() >= 7 &&
                        ETpassword1.length() <= 20) {

                    loading.setVisibility(View.VISIBLE);
                    final JSONObject json = new JSONObject();
                    try {
                        json.put("email", mSettings.getString(APP_PREFERENCES_NICK, ""));
                        json.put("password", mSettings.getString(APP_PREFERENCES_PASSWORD, ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("kkk", "Отправил: " + json);

                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
                    Request request = new Request.Builder().url(url2).post(body).build();
                    Call call = client.newCall(request);

                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("kkk", "Всё плохо");
                            ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                loading.setVisibility(View.GONE);
                            });
                            Log.d("kkk", e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            resp = response.body().string().toString();
                            Log.d("kkk", "Принял - " + resp);
                            ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                loading.setVisibility(View.GONE);
                            });
                            switch (resp) {
                                case "incorrect_nick":
                                    ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                        builder.setView(viewDang);
                                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                                        TV_title.setText("Этот ник уже занят!");
                                        TV_error.setText("Придумайте себе другой ник");
                                        AlertDialog alert = builder.create();
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alert.show();
                                    });
                                    break;
                                case "reg_in":
                                    ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        View viewError = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                        builder.setView(viewError);
                                        AlertDialog alert;
                                        alert = builder.create();

                                        TextView TV = viewError.findViewById(R.id.dialogError_TV_errorText);
                                        TextView TV_title = viewError.findViewById(R.id.dialogError_TV_errorTitle);

                                        TV.setText("Регистрация успешна!");
                                        TV_title.setText("Вы успешно зарегистрировались в Mafia Go!");
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alert.show();
                                    });
                                    break;
                                default:
                                    ContextCompat.getMainExecutor(getContext()).execute(() -> {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                                        builder.setView(viewDang);
                                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                                        TV_title.setText("Что-то пошло не так!");
                                        TV_error.setText("Напишите разработчику и подробно опишите проблему");
                                        AlertDialog alert = builder.create();
                                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        alert.show();
                                    });
                                    break;
                            }
                        }
                    });
                }
                else
                {
                    if (!ETpassword1.getText().toString().equals(ETpassword2.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Ваши пароли не совпадают!");
                        TV_error.setText("Напишите их ещё раз");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } else if (ETnick.getText().toString().contains(".") || ETnick.getText().toString().contains("{") || ETnick.getText().toString().contains("}")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Недопустимые символы в нике!");
                        TV_error.setText("В нике нельзя использовать точки и скобки");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } else if (ETpassword1.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Пустой пароль!");
                        TV_error.setText("Ваш пароль не может быть пустым");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } else if (ETnick.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Пустой ник!");
                        TV_error.setText("Ваш ник не может быть пустым");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } else if (ETpassword1.length() < 7) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Слишком короткий пароль!");
                        TV_error.setText("Пароль должен быть не менее, чем 7 символов");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } else if (ETpassword1.length() > 20) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Слишком длинный пароль!");
                        TV_error.setText("Ваш пароль должен быть меньше, чем 21 символ");
                        AlertDialog alert = builder.create();
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    }
                }
            }
            else {
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
        });
        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new StartFragment()).commit();
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