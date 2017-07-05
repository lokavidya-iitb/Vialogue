package com.comp.iitb.vialogue.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.comp.iitb.vialogue.coordinators.OnPhoneNumberValidityChanged;

/**
 * Created by ironstein on 08/03/17.
 */

public class PhoneNumberEditTextValidityListener {

    private static final String PHONE_NUMBER_REGEX = "(^)(?:\\+91)([\\d]){10}$";

    private EditText mPhoneNumberEditText;
    private OnPhoneNumberValidityChanged mOnPhoneNumberValidityChanged;
    private boolean isValid = false;

    public PhoneNumberEditTextValidityListener(EditText phoneNumberEditText, final OnPhoneNumberValidityChanged onPhoneNumberValidityChanged) {

        mPhoneNumberEditText = phoneNumberEditText;
        mOnPhoneNumberValidityChanged = onPhoneNumberValidityChanged;

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(validatePhoneNumber(editable.toString())) {
                    if(!isValid) {
                        isValid = true;
                        mOnPhoneNumberValidityChanged.onValidityChanged(true);
                    }
                } else {
                    if(isValid) {
                        isValid = false;
                        mOnPhoneNumberValidityChanged.onValidityChanged(false);
                    }
                }
            }
        });
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if(("+91" + phoneNumber).matches(PHONE_NUMBER_REGEX)) {
            return true;
        } return false;
    }

}
