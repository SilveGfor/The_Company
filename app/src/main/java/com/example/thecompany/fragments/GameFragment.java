package com.example.thecompany.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
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

    ImageView man_1;
    ImageView man_2;
    ImageView man_3;
    ImageView man_4;
    ImageView man_5;
    ImageView fon;

    boolean finish = false;

    private MediaPlayer casinoPlayer;
    private MediaPlayer coinsPlayer;
    private MediaPlayer kassaPlayer;
    private MediaPlayer endPlayer;

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

        casinoPlayer = MediaPlayer.create(getActivity(), R.raw.casino_sound);
        coinsPlayer = MediaPlayer.create(getActivity(), R.raw.falling_coins);
        kassaPlayer = MediaPlayer.create(getActivity(), R.raw.kassa_sound);
        endPlayer = MediaPlayer.create(getActivity(), R.raw.the_end);

        man_1 = view.findViewById(R.id.man_right_1);
        man_2 = view.findViewById(R.id.man_right_2);
        man_3 = view.findViewById(R.id.man_right_3);
        man_4 = view.findViewById(R.id.man_left_1);
        man_5 = view.findViewById(R.id.man_left_2);
        fon = view.findViewById(R.id.fragmentGame_IV_fon);

        btn_1.setVisibility(View.INVISIBLE);
        btn_2.setVisibility(View.INVISIBLE);
        btn_3.setVisibility(View.INVISIBLE);
        btn_4.setVisibility(View.INVISIBLE);

        socket.off("create_game");
        socket.off("get_my_game_info");
        socket.off("new_event");
        socket.off("event_consequence");
        socket.off("get_current_balance");
        socket.off("stop_game");

        socket.on("create_game", onCreateGame);
        socket.on("get_my_game_info", onGetMyGameInfo);
        socket.on("new_event", onNewEvent);
        socket.on("event_consequence", onEventConsequence);
        socket.on("get_current_balance", onGetCurrentBalance);
        socket.on("stop_game", onStopGame);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDang = getLayoutInflater().inflate(R.layout.dialog_game, null);
        builder.setView(viewDang);
        TextView TV_title = viewDang.findViewById(R.id.dialogGame_TV_title);
        TextView TV_text = viewDang.findViewById(R.id.dialogGame_TV_text);
        ImageView IV = viewDang.findViewById(R.id.dialogGame_IV_fon);
        Button btnStart = viewDang.findViewById(R.id.dialogGame_btn_start);
        btnStart.setVisibility(View.VISIBLE);

        AlertDialog alert = builder.create();

        casinoPlayer.start();

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
            btn_1.setVisibility(View.INVISIBLE);
            btn_2.setVisibility(View.INVISIBLE);
            btn_3.setVisibility(View.INVISIBLE);
            btn_4.setVisibility(View.INVISIBLE);
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
            btn_1.setVisibility(View.INVISIBLE);
            btn_2.setVisibility(View.INVISIBLE);
            btn_3.setVisibility(View.INVISIBLE);
            btn_4.setVisibility(View.INVISIBLE);
        });

        btn_3.setOnClickListener(v -> {
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
            btn_1.setVisibility(View.INVISIBLE);
            btn_2.setVisibility(View.INVISIBLE);
            btn_3.setVisibility(View.INVISIBLE);
            btn_4.setVisibility(View.INVISIBLE);
        });

        btn_4.setOnClickListener(v -> {
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
            btn_1.setVisibility(View.INVISIBLE);
            btn_2.setVisibility(View.INVISIBLE);
            btn_3.setVisibility(View.INVISIBLE);
            btn_4.setVisibility(View.INVISIBLE);
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
                String task, image;
                JSONArray JA_variants_list;
                if (!finish) {
                    try {
                        JO_event = data.getJSONObject("event");
                        task = JO_event.getString("task");
                        image = JO_event.getString("image");
                        JA_variants_list = JO_event.getJSONArray("variants");

                        btn_1.setVisibility(View.INVISIBLE);
                        btn_2.setVisibility(View.INVISIBLE);
                        btn_3.setVisibility(View.INVISIBLE);
                        btn_4.setVisibility(View.INVISIBLE);

                        man_1.setVisibility(View.INVISIBLE);
                        man_2.setVisibility(View.INVISIBLE);
                        man_3.setVisibility(View.INVISIBLE);
                        man_4.setVisibility(View.INVISIBLE);
                        man_5.setVisibility(View.INVISIBLE);

                        TV_question.setText(task);

                        int a = (int) (Math.random() * (4 + 1)) + 1;
                        switch (a) {
                            case 1:
                                man_1.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                man_2.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                man_3.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                man_4.setVisibility(View.VISIBLE);
                                break;
                            default:
                                man_5.setVisibility(View.VISIBLE);
                                break;
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                man_1.setVisibility(View.INVISIBLE);
                                man_2.setVisibility(View.INVISIBLE);
                                man_3.setVisibility(View.INVISIBLE);
                                man_4.setVisibility(View.INVISIBLE);
                                man_5.setVisibility(View.INVISIBLE);
                            }
                        }, 4000);

                        switch (image) {
                            case "stock_market":
                                fon.setImageResource(R.drawable.stock_market);
                                break;
                            case "office":
                                fon.setImageResource(R.drawable.office);
                                break;
                            case "tire_service":
                                fon.setImageResource(R.drawable.tire_service);
                                break;
                            case "casino":
                                fon.setImageResource(R.drawable.casino);
                                break;
                        }

                        for (int i = 0; i < JA_variants_list.length(); i++) {
                            switch (i) {
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
                    if (balance < data.getInt("balance"))
                    {
                        kassaPlayer.start();
                    }
                    else
                    {
                        coinsPlayer.start();
                    }
                    balance = data.getInt("balance");

                    TV_money.setText(balance + " $");

                    TV_question.setText(consequence);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_game, null);
                    builder.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogGame_TV_title);
                    TextView TV_text = viewDang.findViewById(R.id.dialogGame_TV_text);
                    ImageView IV = viewDang.findViewById(R.id.dialogGame_IV_fon);

                    TV_title.setText("");
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

    //конец игры
    private final Emitter.Listener onStopGame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                Log.d("kkk", "Socket_принять - stop_game - " + args[0]);
                int game_time;
                try {
                    finish = true;
                    game_time = data.getInt("game_time");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_game, null);
                    builder.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogGame_TV_title);
                    TextView TV_text = viewDang.findViewById(R.id.dialogGame_TV_text);
                    ImageView IV = viewDang.findViewById(R.id.dialogGame_IV_fon);

                    endPlayer.start();

                    if (game_time <= 300) {
                        TV_question.setText("Вы смогли продержать компанию " + game_time + " секунд");
                        IV.setImageResource(R.drawable.lose);
                        TV_title.setText("Вы проиграли!");
                        TV_text.setText("Что происходит? Ваш пропуск в компанию аннулирован? Как такое возможно? Неужели это конкуренты решили захватить вашу компанию?  Или это системная ошибка? Или в этом замешан ваш ассистент?.. В любом случае, вы слишком быстро потеряли все, чтобы что-то утверждать наверняка. А теперь, сидя около мусорного бака, осмыслите произошедшее и попробуйте вновь показать всем, что победитель на самом деле вы. А то, что вы все потеряли - на самом деле не тупиковая проблема, а лишь препятствие на пути к всемирной славе, ведь даже современные компьютерные нейросети не могут достичь великолепных результатов, не совершив до этого ошибок.");
                    }
                    else {
                        TV_question.setText("Вы смогли продержать компанию " + game_time + " секунд");
                        IV.setImageResource(R.drawable.govern);
                        TV_title.setText("Вы победили!");
                        TV_text.setText("Ужас... ваш ассистент оказался мошенником, его задержала полиция, но все деньги компании растрачены. Когда вы сорвали куш в казино, вы уже были победителем, вы готовы были свернуть горы, основывая вашу компанию. С вами она пережила взлеты и падения, но в итоге все деньги закончились. Думаете, вы проиграли? Напротив, вы еще больший победитель, ведь деньги через некоторое время возместит страховая, а пока у вас есть возможность осмыслить весь полученный опыт и подготовиться вновь войти в большой бизнес! Не совершайте нецелевые траты, думайте стратегически, заботьтесь о своих сотрудниках, и тогда весь мир будет у ваших ног! \n");
                    }

                    btn_1.setVisibility(View.INVISIBLE);
                    btn_2.setVisibility(View.INVISIBLE);
                    btn_3.setVisibility(View.INVISIBLE);
                    btn_4.setVisibility(View.INVISIBLE);

                    AlertDialog alert = builder.create();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.show();
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