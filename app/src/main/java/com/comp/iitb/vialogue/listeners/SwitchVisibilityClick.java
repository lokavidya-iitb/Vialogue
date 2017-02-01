package com.comp.iitb.vialogue.listeners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shubh on 14-01-2017.
 */

public class SwitchVisibilityClick implements View.OnClickListener {
    View mSourceView;
    View mDestinationView;
    Context mContext;

    public SwitchVisibilityClick(@NonNull Context context, @NonNull View source, @NonNull View destination) {
        mContext = context;
        mSourceView = source;
        mDestinationView = destination;
    }

    @Override
    public void onClick(View v) {
        mSourceView.setVisibility(View.GONE);
        mDestinationView.setVisibility(View.VISIBLE);
        mDestinationView.requestFocus();
        if (mSourceView instanceof TextView) {
            if (mDestinationView instanceof EditText) {
                final EditText destination = (EditText) mDestinationView;
                TextView source = (TextView) mSourceView;
                destination.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager keyboard = (InputMethodManager)
                                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(destination, 0);
                    }
                }, 200);
                destination.setText(source.getText());
            }
        }
    }
}
