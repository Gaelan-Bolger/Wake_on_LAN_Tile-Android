package com.gaelanbolger.woltile.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.gaelanbolger.woltile.util.Bundler;

public class ConfirmActionDialog extends DialogFragment {

    public interface OnActionConfirmedListener {
        void onActionConfirmed();
    }

    public static final String TAG = ConfirmActionDialog.class.getSimpleName();

    private OnActionConfirmedListener mListener;

    public static ConfirmActionDialog newInstance(String message, OnActionConfirmedListener listener) {
        ConfirmActionDialog dialog = new ConfirmActionDialog();
        dialog.setArguments(new Bundler().with("message", message).bundle());
        dialog.setListener(listener);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setMessage(getArguments().getString("message"))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (null != mListener)
                            mListener.onActionConfirmed();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public void setListener(OnActionConfirmedListener listener) {
        mListener = listener;
    }
}