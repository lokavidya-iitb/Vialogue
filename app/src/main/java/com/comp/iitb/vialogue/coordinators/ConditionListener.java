package com.comp.iitb.vialogue.coordinators;

/**
 * Created by shubh on 03-02-2017.
 */

public interface ConditionListener {
    void conditionSatisfied(Object sender);
    void conditionFailed(Object sender);
}
