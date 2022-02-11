package com.example.thecompany.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thecompany.MainActivity;
import com.example.thecompany.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.socket.emitter.Emitter;

import static android.app.Activity.RESULT_OK;
import static com.example.thecompany.MainActivity.socket;

public class SettingsFragment extends Fragment {

    TextView TV_nick;
    ImageView IV_avatar;
    Button btn_changeNick;
    Button btn_changeAvatar;
    Button btn_changePassword;
    Button btn_study;
    Button btn_fullscreen;
    Button btn_exit;

    static final int GALLERY_REQUEST = 1;

    public static final String APP_PREFERENCES = "user";
    public static final String APP_PREFERENCES_NICK = "nick";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    public static final String APP_PREFERENCES_FULLSCREEN = "fullscreen";

    private SharedPreferences mSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TV_nick = view.findViewById(R.id.fragmentSettings_TV_nick);
        IV_avatar = view.findViewById(R.id.fragmentSettings_IV_avatar);
        btn_changeNick = view.findViewById(R.id.fragmentSettings_btn_changeNick);
        btn_changeAvatar = view.findViewById(R.id.fragmentSettings_btn_changeAvatar);
        btn_changePassword = view.findViewById(R.id.fragmentSettings_btn_changePassword);
        btn_study = view.findViewById(R.id.fragmentSettings_btn_study);
        btn_fullscreen = view.findViewById(R.id.fragmentSettings_btn_fullscreen);
        btn_exit = view.findViewById(R.id.fragmentSettings_btn_exit);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        TV_nick.setText(MainActivity.nick);
        IV_avatar.setImageBitmap(MainActivity.bitmap_avatar_1);

        socket.off("edit_profile");

        socket.on("edit_profile", onEditProfile);

        btn_changeAvatar.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        btn_changeNick.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View viewChangeNick = inflater.inflate(R.layout.dialog_change_nick, container, false);
            builder.setView(viewChangeNick);

            EditText ET_nick = viewChangeNick.findViewById(R.id.dialogChangeNick_ET_newNick);
            Button btn_changeNick = viewChangeNick.findViewById(R.id.dialogChangeNick_btn_changeNick);

            btn_changeNick.setOnClickListener(v13 -> {
                String nick = ET_nick.getText().toString();
                Log.e("kkk", nick);
                Log.e("kkk", String.valueOf(nick.length()));
                if (nick.length() >= 3)
                {
                    if (nick.length() <= 15) {
                        if (!nick.contains(".") && !nick.contains("{") && !nick.contains("}")) {
                            final JSONObject json2 = new JSONObject();
                            try {
                                json2.put("nick", MainActivity.NickName);
                                json2.put("session_id", MainActivity.Session_id);
                                json2.put("new_nick", nick);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("kkk", "Socket_отправка - edit_profile - " + json2.toString());
                            socket.emit("edit_profile", json2);
                        } else {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                            View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                            builder2.setView(viewDang);
                            TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                            TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                            TV_title.setText("Некорректный символ!");
                            TV_error.setText("Нельзя точки и скобки");
                            AlertDialog alert2 = builder2.create();
                            alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alert2.show();
                        }
                    }
                    else
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder2.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Длинный ник!");
                        TV_error.setText("Ваш ник должен быть меньше 16 символов");
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert2.show();
                    }
                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(viewDang);
                    TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                    TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Короткий ник!");
                    TV_error.setText("Ваш ник должен быть больше 2 символов");
                    AlertDialog alert2 = builder2.create();
                    alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert2.show();
                }
            });

            AlertDialog alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();
        });

        btn_changePassword.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View viewChangePas = inflater.inflate(R.layout.dialog_change_password, container, false);
            builder.setView(viewChangePas);

            Button btn_changePasswordInAlert = viewChangePas.findViewById(R.id.dialogChangePassword_btn_changePassword);
            EditText ET_oldPassword = viewChangePas.findViewById(R.id.dialogChangePassword_ET_oldPassword);
            EditText ET_newPassword1 = viewChangePas.findViewById(R.id.dialogChangePassword_ET_newPassword1);
            EditText ET_newPassword2 = viewChangePas.findViewById(R.id.dialogChangePassword_ET_newPassword2);

            btn_changePasswordInAlert.setOnClickListener(v14 -> {
                if (ET_newPassword1.getText().toString().equals(ET_newPassword2.getText().toString()) &&
                        !ET_newPassword1.getText().toString().trim().equals("") &&
                        ET_newPassword1.length() >= 7 &&
                        ET_newPassword1.length() <= 20) {
                    final JSONObject json2 = new JSONObject();
                    try {
                        json2.put("nick", MainActivity.NickName);
                        json2.put("session_id", MainActivity.Session_id);
                        json2.put("new_password", ET_newPassword1.getText());
                        json2.put("password", ET_oldPassword.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("kkk", "Socket_отправка - edit_profile - " + json2.toString());
                    socket.emit("edit_profile", json2);
                }
                else {
                    if (!ET_newPassword1.getText().toString().equals(ET_newPassword2.getText().toString())) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder2.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Ошибка!");
                        TV_error.setText("Ваши новые пароли не совпадают!");
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert2.show();
                    } else if (ET_newPassword1.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder2.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Ошибка!");
                        TV_error.setText("Ваш пароль не может быть пустым!");
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert2.show();
                    } else if (ET_newPassword1.length() < 7) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder2.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Ошибка!");
                        TV_error.setText("Ваш пароль слишком короткий!");
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert2.show();
                    } else if (ET_newPassword1.length() > 20) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                        builder2.setView(viewDang);
                        TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                        TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                        TV_title.setText("Ошибка!");
                        TV_error.setText("Ваш пароль слишком длинный!");
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert2.show();
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();


        });

        Boolean fullscreen = mSettings.getBoolean(APP_PREFERENCES_FULLSCREEN, false);
        if (fullscreen)
        {
            btn_fullscreen.setText("Не полный экран");
        }
        else
        {
            btn_fullscreen.setText("Полный экран");
        }

        btn_fullscreen.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mSettings.edit();
            if (fullscreen) {
                editor.putBoolean(APP_PREFERENCES_FULLSCREEN, false);
            }
            else {
                editor.putBoolean(APP_PREFERENCES_FULLSCREEN, true);
            }
            editor.apply();
            reset();
        });

        btn_exit.setOnClickListener(v -> {
            socket.emit("leave_app", "");
            Log.d("kkk", "Socket_отправка - leave_app");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActivity, new StartFragment()).commit();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_NICK, null);
            editor.putString(APP_PREFERENCES_PASSWORD, null);
            editor.apply();
        });

        return view;
    }

    private void reset() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent == null
                || imageReturnedIntent.getData() == null) {
            return;
        }
        // сжимает до ~500КБ максимум. Тогда как обычная картинка весит ~2МБ

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri uri = imageReturnedIntent.getData();
                    try {
                        /*
                        Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        Log.d("kkk", String.valueOf(bitmap2.getByteCount() / 1048576));
                        byte[] bytes = stream.toByteArray();
                        String base642 = Base64.encodeToString(bytes, Base64.DEFAULT);
                        //Log.d("kkk", String.valueOf(base642.length()));
                         */

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        int scaleDivider = 5;
                        int max = 0;
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            max = bitmap.getWidth();
                        } else {
                            max = bitmap.getHeight();
                        }
                        if (max <= 300) {
                            scaleDivider = 1;
                        } else if (max <= 600) {
                            scaleDivider = 2;
                        } else if (max <= 900) {
                            scaleDivider = 3;
                        } else if (max <= 1200) {
                            scaleDivider = 4;
                        } else if (max <= 1500) {
                            scaleDivider = 5;
                        } else if (max <= 1800) {
                            scaleDivider = 6;
                        } else if (max <= 2100) {
                            scaleDivider = 7;
                        } else if (max <= 2400) {
                            scaleDivider = 8;
                        } else if (max <= 2700) {
                            scaleDivider = 9;
                        } else if (max <= 3000) {
                            scaleDivider = 10;
                        } else if (max <= 3300) {
                            scaleDivider = 11;
                        } else if (max <= 3600) {
                            scaleDivider = 12;
                        } else if (max <= 3900) {
                            scaleDivider = 13;
                        } else if (max <= 4200) {
                            scaleDivider = 14;
                        }

                        int scaleWidth = bitmap.getWidth() / scaleDivider;
                        int scaleHeight = bitmap.getHeight() / scaleDivider;
                        Log.d("kkk", String.valueOf(scaleWidth));
                        Log.d("kkk", String.valueOf(scaleHeight));
                        byte[] downsizedImageBytes =
                                getDownsizedImageBytes(bitmap, scaleWidth, scaleHeight);
                        String base64 = Base64.encodeToString(downsizedImageBytes, Base64.DEFAULT);
                        Log.d("kkk", String.valueOf("Длина = " + base64.length()));
                        if (base64.length() <= 524288) {
                            IV_avatar.setImageBitmap(fromBase64(base64));
                            final JSONObject json2 = new JSONObject();
                            try {
                                json2.put("nick", MainActivity.NickName);
                                json2.put("session_id", MainActivity.Session_id);
                                json2.put("avatar", base64);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("kkk", "Socket_отправка - edit_profile - " + json2.toString());
                            socket.emit("edit_profile", json2);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View viewDang = getLayoutInflater().inflate(R.layout.dialog_error, null);
                            builder.setView(viewDang);
                            TextView TV_title = viewDang.findViewById(R.id.dialogError_TV_errorTitle);
                            TextView TV_error = viewDang.findViewById(R.id.dialogError_TV_errorText);
                            TV_title.setText("Слишком большое изображение!");
                            TV_error.setText("Выберите изображение поменьше или обрежьте/сожмите свою картинку");
                            AlertDialog alert = builder.create();
                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alert.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public byte[] getDownsizedImageBytes(Bitmap fullBitmap, int scaleWidth, int scaleHeight) throws IOException {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, scaleWidth, scaleHeight, true);

        // 2. Instantiate the downsized image content as a byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] downsizedImageBytes = baos.toByteArray();

        return downsizedImageBytes;
    }

    public Bitmap fromBase64(String image) {
        // Декодируем строку Base64 в массив байтов
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);

        // Декодируем массив байтов в изображение
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Помещаем изображение в ImageView
        return decodedByte;
    }

    private final Emitter.Listener onEditProfile = args -> {
        if(getActivity() == null)
            return;
        getActivity().runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            String status = "";
            Log.d("kkk", "принял - edit_profile - " + data);
            try {
                status = data.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
            View view2;
            switch (status)
            {
                case "OK":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_information, null);
                    builder2.setView(view2);
                    TextView TV_title = view2.findViewById(R.id.dialogInformation_TV_title);
                    TextView TV_text = view2.findViewById(R.id.dialogInformation_TV_text);
                    TV_title.setText("Профиль успешно изменён!");
                    TV_text.setText("Перезапустите игру, если вы сменили ник");
                    reset();
                    break;
                case "incorrect_password":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Вы неправильно ввели свой старый пароль!");
                    TV_text.setText("Попробуйте ещё раз");
                    break;
                case "new_nick_is_the_same_with_old_nick":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Ваш новый ник полностью повторяет старый!");
                    TV_text.setText("Придумайте новый ник");
                    break;
                case "incorrect_new_nick ":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Такой ник уже занят!");
                    TV_text.setText("Придумайте другой ник");
                    break;
                case "last_nick_update_was_less_than_a_month_ago":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Вы уже меняли ник в этом месяце!");
                    TV_text.setText("Не надо так часто менять ники");
                    break;
                case "mat_in_new_nick":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Ваш ник не проходит цензуру!");
                    TV_text.setText("Придумайте более приличный ник");
                    break;
                case "cant_change_nick_because_you_are_playing_in_room":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Нельзя изменить ник сейчас!");
                    TV_text.setText("В Mafia Go нельзя менять ник во время игры в комнате");
                    break;
                case "cant_change_nick_because_you_are_observer":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Нельзя изменить ник сейчас!");
                    TV_text.setText("В Mafia Go нельзя менять ник во время наблюдения за игрой");
                    break;
                case "invalid_personal_color":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Время действия этого цвета истекло!");
                    TV_text.setText("Вы можете купить его в магазине");
                    break;
                case "invalid_status":
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Время действия этого статуса истекло!");
                    TV_text.setText("Вы можете купить его в магазине");
                    break;
                default:
                    view2 = getLayoutInflater().inflate(R.layout.dialog_error, null);
                    builder2.setView(view2);
                    TV_title = view2.findViewById(R.id.dialogError_TV_errorTitle);
                    TV_text = view2.findViewById(R.id.dialogError_TV_errorText);
                    TV_title.setText("Что-то пошло не так!");
                    TV_text.setText("Напишите разработчику и подробно опишите проблему");
                    break;
            }
            AlertDialog alert2 = builder2.create();
            alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert2.show();
        });
    };
}