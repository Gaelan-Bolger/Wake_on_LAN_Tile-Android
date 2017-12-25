package com.gaelanbolger.woltile.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.util.AttributeSet;

public class IpAddressEditText extends TextInputEditText {

    public IpAddressEditText(Context context) {
        super(context);
        init();
    }

    public IpAddressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IpAddressEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new IpAddressFilter()});
    }
}
