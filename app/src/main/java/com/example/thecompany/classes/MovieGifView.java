package com.example.thecompany.classes;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.view.View;

public class MovieGifView extends View {

    private Movie mMovie;
    private InputStream mStream;
    private long mMoviestart;
    private int mMovieWidth, mMovieHeight;
    private long mMovieDuration;

    public MovieGifView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MovieGifView(Context context, int resId) {
        super(context);

        InputStream stream = context.getResources().openRawResource(resId);

        mMovie = Movie.decodeStream(stream);
        mMovieWidth = mMovie.width();
        mMovieHeight = mMovie.height();
        mMovieDuration = mMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mMovieWidth, mMovieHeight);
    }

    public int getMovieWidth() {
        return mMovieWidth;
    }

    public int getMovieHeight() {
        return mMovieHeight;
    }

    public long getMovieDuration() {
        return mMovieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();
        if (mMoviestart == 0) {
            mMoviestart = now;
        }

        final int relTime = (int) ((now - mMoviestart) % mMovie.duration());
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);
        this.invalidate();
    }
}