package com.gaelanbolger.woltile.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.gaelanbolger.woltile.input.MacAddressFilter;

public class MacAddressEditText extends TextInputEditText {

    public MacAddressEditText(Context context) {
        super(context);
        init();
    }

    public MacAddressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MacAddressEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new InputFilter.AllCaps(), new MacAddressFilter()});
    }
}
