package com.gaelanbolger.woltile.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaelanbolger.woltile.R;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

public class TileView extends CardView {

    @BindView(android.R.id.icon)
    ImageView imageView;
    @BindView(android.R.id.title)
    TextView textView;

    public TileView(@NonNull Context context) {
        this(context, null);
    }

    public TileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.tile, this);
        bind(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, new int[]{
                android.R.attr.src, android.R.attr.text, R.attr.selectableItemBackgroundBorderless
        });
        setIcon(ta.getResourceId(0, R.drawable.ic_laptop_general_normal));
        setTitle(ta.getString(1));
        setForeground(ta.getDrawable(2));
        setUseCompatPadding(true);
        setRadius(0f);
        ta.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        imageView.setEnabled(enabled);
        textView.setEnabled(enabled);
    }

    public void setIcon(@DrawableRes int iconResId) {
        imageView.setImageResource(iconResId);
    }

    public void setIcon(Drawable icon) {
        imageView.setImageDrawable(icon);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setTitle(@StringRes int titleResId) {
        setTitle(getContext().getString(titleResId));
    }
}
