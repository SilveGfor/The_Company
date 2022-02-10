package com.example.thecompany.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.thecompany.MainActivity.socket;

public class GameFragment extends Fragment {

    TextView TV_money;
    TextView TV_question;
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;

    JSONObject json;

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

        json = new JSONObject();
        try {
            json.put("nick", MainActivity.nick);
            json.put("session_id", MainActivity.Session_id);
            //json.put("room", player.getRoom_num());
            json.put("password", MainActivity.Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get_in_room", json);
        Log.d("kkk", "Socket_отправка - get_in_room from main "+ json.toString());

        return view;
    }
}