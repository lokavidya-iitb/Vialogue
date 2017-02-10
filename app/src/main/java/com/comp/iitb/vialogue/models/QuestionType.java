package com.comp.iitb.vialogue.models;

/**
 * Created by shubh on 10-02-2017.
 */

public enum QuestionType {
    MCQ("Multiple Choice Question"),
    FIB("Fill in the blanks"),
    SCQ("Single choice questions");
    private String mType;

    QuestionType(String type) {
        mType = type;
    }

    public String toString() {
        return mType;
    }
}

