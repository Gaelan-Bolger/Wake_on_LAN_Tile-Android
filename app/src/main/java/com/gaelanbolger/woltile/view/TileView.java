package com.gaelanbolger.woltile.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaelanbolger.woltile.R;


public class TileView extends FrameLayout {

    private ImageView imageView;
    private TextView textView;

    public TileView(@NonNull Context context) {
        this(context, null);
    }

    public TileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.tile, this);
        imageView = findViewById(android.R.id.icon);
        textView = findViewById(android.R.id.text1);
    }

    public void setIconResource(@DrawableRes int resId) {
        imageView.setImageResource(resId);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }
}
