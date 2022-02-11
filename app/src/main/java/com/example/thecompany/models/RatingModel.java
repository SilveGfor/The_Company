package com.example.thecompany.models;
import android.graphics.Bitmap;

public class RatingModel {
    public String nick;
    public Bitmap avatar;
    public int place;
    public String score;

    public RatingModel(String nick, Bitmap avatar, int place, String score) {
        this.nick = nick;
        this.avatar = avatar;
        this.place = place;
        this.score = score;
    }
}
