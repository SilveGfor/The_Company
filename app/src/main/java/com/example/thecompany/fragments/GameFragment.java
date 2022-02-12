package com.example.thecompany.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;
import com.example.thecompany.classes.OnBackPressedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

import static com.example.thecompany.MainActivity.socket;

public class GameFragment extends Fragment implements OnBackPressedListener {

    TextView TV_money;
    TextView TV_question;
    TextView btn_1;
    TextView btn_2;
    TextView btn_3;
    TextView btn_4;

    JSONObject json;

    int num;
    int balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        TV_money = view.findViewById(R.id.fragmentGame_TV_money);
        TV_question = view.findViewById(R.id.fragmentGame_TV_question);
        btn_1 = view.findViewById(R.id.fragmentGame_btn_1);
        btn_2 = view.findViewById(R.id.fragmentGame_btn_2);
        btn_3 = view.findViewById(R.id.fragmentGame_btn_3);
        btn_4 = view.findViewById(R.id.fragmentGame_btn_4);

        btn_1.setVisibility(View.INVISIBLE);
        btn_2.setVisibility(View.INVISIBLE);
        btn_3.setVisibility(View.INVISIBLE);
        btn_4.setVisibility(View.INVISIBLE);

        socket.off("create_game");
        socket.off("get_my_game_info");
        socket.off("new_event");
        socket.off("event_consequence");
        socket.off("get_current_balance");


        socket.on("create_game", onCreateGame);
        socket.on("get_my_game_info", onGetMyGameInfo);
        socket.on("new_event", onNewEvent);
        socket.on("event_consequence", onEventConsequence);
        socket.on("get_current_balance", onGetCurrentBalance);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDang = getLayoutInflater().inflate(R.layout.dialog_game, null);
        builder.setView(viewDang);
        TextView TV_title = viewDang.findViewById(R.id.dialogGame_TV_title);
        TextView TV_text = viewDang.findViewById(R.id.dialogGame_TV_text);
        ImageView IV = viewDang.findViewById(R.id.dialogGame_IV_fon);
        Button btnStart = viewDang.findViewById(R.id.dialogGame_btn_start);

        AlertDialog alert = builder.create();

        btnStart.setOnClickListener(view1 -> {
            json = new JSONObject();
            try {
                json.put("nick", MainActivity.nick);
                json.put("session_id", MainActivity.Session_id);
                json.put("mode", "offline");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("create_game", json);
            Log.d("kkk", "Socket_отправка - create_game"+ json.toString());
            alert.cancel();
        });

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);
        alert.show();




        btn_1.setOnClickListener(v -> {
            json = new JSONObject();
            try {
                json.put("nick", MainActivity.nick);
                json.put("session_id", MainActivity.Session_id);
                json.put("num", num);
                json.put("choice_index", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user_choice", json);
            Log.d("kkk", "Socket_отправка - user_choice"+ json.toString());
        });

        btn_2.setOnClickListener(v -> {
            json = new JSONObject();
            try {
                json.put("nick", MainActivity.nick);
                json.put("session_id", MainActivity.Session_id);
                json.put("num", num);
                json.put("choice_index", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user_choice", json);
            Log.d("kkk", "Socket_отправка - user_choice"+ json.toString());
        });

        btn_1.setOnClickListener(v -> {
            json = new JSONObject();
            try {
                json.put("nick", MainActivity.nick);
                json.put("session_id", MainActivity.Session_id);
                json.put("num", num);
                json.put("choice_index", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user_choice", json);
            Log.d("kkk", "Socket_отправка - user_choice"+ json.toString());
        });

        btn_1.setOnClickListener(v -> {
            json = new JSONObject();
            try {
                json.put("nick", MainActivity.nick);
                json.put("session_id", MainActivity.Session_id);
                json.put("num", num);
                json.put("choice_index", 3);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user_choice", json);
            Log.d("kkk", "Socket_отправка - user_choice"+ json.toString());
        });

        return view;
    }

    //создаём комнату и сразу в неё входим
    private final Emitter.Listener onCreateGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - create_game - " + args[0]);
                try {
                    num = data.getInt("num");

                    json = new JSONObject();
                    try {
                        json.put("nick", MainActivity.nick);
                        json.put("session_id", MainActivity.Session_id);
                        json.put("num", num);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("connect_to_game", json);
                    Log.d("kkk", "Socket_отправка - connect_to_game "+ json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    //Получаем начальный баланс в игре
    private final Emitter.Listener onGetMyGameInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - get_my_game_info - " + args[0]);
                try {
                    balance = data.getInt("balance");

                    TV_money.setText(balance + " $");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    //получаем новый квест
    private final Emitter.Listener onNewEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - new_event - " + args[0]);
                JSONObject JO_event;
                String task;
                JSONArray JA_variants_list = new JSONArray();
                ArrayList<String> variants_list = new ArrayList<>();
                try {
                    JO_event = data.getJSONObject("event");
                    task = JO_event.getString("task");
                    JA_variants_list = JO_event.getJSONArray("variants");

                    TV_question.setText(task);

                    for (int i = 0; i < JA_variants_list.length(); i++)
                    {
                        switch (i)
                        {
                            case 0:
                                btn_1.setText(JA_variants_list.get(i).toString());
                                btn_1.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                btn_2.setText(JA_variants_list.get(i).toString());
                                btn_2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                btn_3.setText(JA_variants_list.get(i).toString());
                                btn_3.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                btn_4.setText(JA_variants_list.get(i).toString());
                                btn_4.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    //последствия ивента
    private final Emitter.Listener onEventConsequence = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - event_consequence - " + args[0]);
                String consequence;
                try {
                    consequence = data.getString("consequence");
                    balance = data.getInt("balance");

                    TV_money.setText(balance + " $");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_information, null);
                    builder.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogInformation_TV_title);
                    TextView TV_text = viewDang.findViewById(R.id.dialogInformation_TV_text);
                    //TV_title.setText("Заголовок!");
                    TV_text.setText(consequence);
                    AlertDialog alert = builder.create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    //получить текущий баланс
    private final Emitter.Listener onGetCurrentBalance = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - get_current_balance - " + args[0]);
                try {
                    balance = data.getInt("balance");

                    TV_money.setText(balance + " $");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        askToLeave();
    }

    public void askToLeave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewQuestion = getLayoutInflater().inflate(R.layout.dialog_ok_no, null);
        builder.setView(viewQuestion);
        AlertDialog alert = builder.create();
        TextView TV_text = viewQuestion.findViewById(R.id.dialogOkNo_text);
        Button btn_yes = viewQuestion.findViewById(R.id.dialogOkNo_btn_yes);
        Button btn_no = viewQuestion.findViewById(R.id.dialogOkNo_btn_no);
        TV_text.setText("Вы уверены, что хотите выйти из комнаты?");
        btn_yes.setOnClickListener(v1 -> {
            final JSONObject json2 = new JSONObject();
            try {
                json2.put("nick", MainActivity.NickName);
                json2.put("session_id", MainActivity.Session_id);
                json2.put("room", num);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("kkk", "Socket_отправка leave_game - " + json2.toString());
            socket.emit("leave_game", json2);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new MenuFragment()).commit();
            alert.cancel();
        });
        btn_no.setOnClickListener(v12 -> {
            alert.cancel();
        });
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }
}