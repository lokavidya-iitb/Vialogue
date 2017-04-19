package com.comp.iitb.vialogue.models;

import com.comp.iitb.vialogue.models.ParseObjects.models.Resources.Question;

/**
 * Created by shubh on 10-02-2017.
 */

public class QuestionAnswer {
/**
 * {
 "id"{string | null}: ...,
 "type" {mcq | ...}: ...,
 "is_compulsary" {boolean}: ...,
 "question_string" {string}: ...,
 "options" {String Array}: ...,
 "answers" {Integer Array}: ...,
 "hints" {String Array}: ...,
 "solution" {String}: ...,
 "is_edited" {boolean}: ...
 }
 *
 * */

    private long mTime;
    private String mId;
    private QuestionType mType;
    private boolean mIsCompulsory;
    private String mQuestion;
    private String[] mOptions;
    private String[] mAnswers;
    private String[] mHints;
    private String[] mSolution;
    private boolean mIsEdited;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long mTime) {
        this.mTime = mTime;
    }

    public QuestionType getType() {
        return mType;
    }

    public void setType(QuestionType mType) {
        this.mType = mType;
    }

    public boolean isCompulsory() {
        return mIsCompulsory;
    }

    public void setIsCompulsory(boolean mIsCompulsory) {
        this.mIsCompulsory = mIsCompulsory;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String[] getOptions() {
        return mOptions;
    }

    public void setOptions(String[] mOptions) {
        this.mOptions = mOptions;
    }

    public String[] getAnswers() {
        return mAnswers;
    }

    public void setAnswers(String[] mAnswers) {
        this.mAnswers = mAnswers;
    }

    public String[] getHints() {
        return mHints;
    }

    public void setHints(String[] mHints) {
        this.mHints = mHints;
    }

    public String[] getSolution() {
        return mSolution;
    }

    public void setSolution(String[] mSolution) {
        this.mSolution = mSolution;
    }

    public boolean isEdited() {
        return mIsEdited;
    }

    public void setIsEdited(boolean mIsEdited) {
        this.mIsEdited = mIsEdited;
    }

}
