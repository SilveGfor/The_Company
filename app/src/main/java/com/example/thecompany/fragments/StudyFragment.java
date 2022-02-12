package com.example.thecompany.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecompany.R;
import com.example.thecompany.classes.OnBackPressedListener;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import org.json.JSONObject;

public class StudyFragment extends Fragment implements OnBackPressedListener {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);

        TV_money = view.findViewById(R.id.fragmentGame_TV_money);
        TV_question = view.findViewById(R.id.fragmentGame_TV_question);
        btn_1 = view.findViewById(R.id.fragmentGame_btn_1);
        btn_2 = view.findViewById(R.id.fragmentGame_btn_2);
        btn_3 = view.findViewById(R.id.fragmentGame_btn_3);
        btn_4 = view.findViewById(R.id.fragmentGame_btn_4);

        man_1 = view.findViewById(R.id.man_right_1);
        man_2 = view.findViewById(R.id.man_right_2);
        man_3 = view.findViewById(R.id.man_right_3);
        man_4 = view.findViewById(R.id.man_left_1);
        man_5 = view.findViewById(R.id.man_left_2);
        fon = view.findViewById(R.id.fragmentGame_IV_fon);

        new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(fon,"Привет, ты попал в TheCompany - игру, в которой ты выигрываешь в самом начале! Это экран самой игры, сейчас я проведу тебе небольшую экскурсию","")
                                .outerCircleColor(R.color.button)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(160),
                        TapTarget.forView(TV_question,"В игре вам предстоит управлять собственной компанией. В этом окне будут отображаться игровые ситуации, на которые вам надо будет реагировать","")
                                .outerCircleColor(R.color.button)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(120),
                        TapTarget.forView(btn_1,"У вас будет от 2 до 4 вариантов реакции на событие. Если вы не сможете принять решение за 15-20 секунд, то совет директоров вашей компании примет решение за вас","")
                                .outerCircleColor(R.color.button)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(fon,"В игре вы уже победили, у вас есть много денег и своя компания, ваша задача - не потерять всё это","")
                                .outerCircleColor(R.color.button)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(120),
                        TapTarget.forView(TV_money,"В начале игры у вас будет около 10M $. Если вы сможете не уйти в минус за 5 минут - вы победили. Если вы ушли в минус раньше - проиграли. В рейтинге можно посмотреть лучшие результаты остальных игроков","")
                                .outerCircleColor(R.color.colorAccent)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(10)
                                .descriptionTextColor(R.color.black)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60)
                ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new MenuFragment()).commit();
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        }).start();

        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new MenuFragment()).commit();
    }
}