package com.comp.iitb.vialogue.coordinators;

import android.widget.EditText;

/**
 * Created by shubh on 03-02-2017.
 */

public interface ConditionListener {
    void conditionSatisfied(EditText sender);
    void conditionFailed(EditText sender);
}
