package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.reflect.Method;


public class IconPopupMenu extends PopupMenu {

    public IconPopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
    }

    public IconPopupMenu(@NonNull Context context, @NonNull View anchor, int gravity) {
        super(context, anchor, gravity);
    }

    public IconPopupMenu(@NonNull Context context, @NonNull View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
    }

    @Override
    public void show() {
        try {
            Class<?> popupHelperClass = Class.forName(mPopup.getClass().getName());
            Method setForceShowIcon = popupHelperClass.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(mPopup, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.show();
    }
}
