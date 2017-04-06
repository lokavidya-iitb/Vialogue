package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.comp.iitb.vialogue.R;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;
import com.comp.iitb.vialogue.library.Storage;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseFieldsClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Question")
public class Question extends BaseResourceClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public Question(){}

    public Question getNewInstance() {
        return new Question();
    }

    public static final class Fields implements BaseFieldsClass {
        public static final String

        QUESTION_STRING_FIELD = "question_string",
        QUESTION_TYPE_FIELD = "type",
        OPTIONS_FIELD = "options",
        CORRECT_OPTIONS_FIELD = "correct_options",
        SOLUTION_FIELD = "solution",
        HINTS_FIELD = "hints",
        IS_COMPULSORY_FIELD = "is_compulsory",
        TIME_FIELD = "time";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    QUESTION_STRING_FIELD,
                    QUESTION_TYPE_FIELD,
                    OPTIONS_FIELD,
                    CORRECT_OPTIONS_FIELD,
                    SOLUTION_FIELD,
                    HINTS_FIELD,
                    IS_COMPULSORY_FIELD,
                    TIME_FIELD
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    public static final class Type {
        public static final String

        MCQ = "mcq";
    }

    public Question(
            String questionString,
            String questionType,
            ArrayList<String> options,
            ArrayList<Integer> correctOptions,
            String solution,
            ArrayList<String> hints,
            boolean isCompulsory
            ) {
        super();
        setQuestionString(questionString);
        setQuestionType(questionType);
        setOptions(options);
        setCorrectOptions(correctOptions);
        setSolution(solution);
        setHints(hints);
        setIsCompulsory(isCompulsory);
    }

    public static Uri getQuestionThumbnailUri(Context context) {
        return Storage.resourceToUri(context, R.drawable.ic_question);
    }

    public Question(
            String questionString,
            ArrayList<String> options,
            ArrayList<Integer> correctOptions
    ) {
        this(questionString, "mcq", options, correctOptions, "", new ArrayList<String>(), true);
    }

    public String getQuestionString() {
        return getString(Fields.QUESTION_STRING_FIELD);
    }

    public void setQuestionString(String questionString) {
        put(Fields.QUESTION_STRING_FIELD, questionString);
    }

    public QuestionType getQuestionType() {
        return (QuestionType) getParseObject(Fields.QUESTION_TYPE_FIELD);
    }

    public void setQuestionType(QuestionType questionType) {
        put(Fields.QUESTION_TYPE_FIELD, questionType);
    }

    public void setQuestionType(String questionTypeString) {
        setQuestionType(new QuestionType(questionTypeString));
    }

    public ArrayList<String> getOptions() {
        return (ArrayList) getList(Fields.OPTIONS_FIELD);
    }

    public void setOptions(ArrayList<String> options) {
        put(Fields.OPTIONS_FIELD, options);
    }

    public ArrayList<Integer> getCorrectOptions() {
        return (ArrayList) getList(Fields.CORRECT_OPTIONS_FIELD);
    }

    public void setCorrectOptions(ArrayList<Integer> correctOptions) {
        put(Fields.CORRECT_OPTIONS_FIELD, correctOptions);
    }

    public String getSolution() {
        return getString(Fields.SOLUTION_FIELD);
    }

    public void setSolution(String solution) {
        put(Fields.SOLUTION_FIELD, solution);
    }

    public ArrayList<String> getHints() {
        return (ArrayList) getList(Fields.HINTS_FIELD);
    }

    public void setHints(ArrayList<String> hints) {
        put(Fields.HINTS_FIELD, hints);
    }

    public boolean getIsCompulsory() {
        return getBoolean(Fields.IS_COMPULSORY_FIELD);
    }

    public void setIsCompulsory(boolean isCompulsory) {
        put(Fields.IS_COMPULSORY_FIELD, isCompulsory);
    }

    public void setTime(long time) {
        put(Fields.TIME_FIELD, time);
    }

    public long getTime() {
        return getLong(Fields.TIME_FIELD);
    }

    /**
     * Created by ironstein on 22/02/17.
     */

    @ParseClassName("QuestionType")
    public static class QuestionType extends BaseParseClass {

        private static final class Fields {
            public static final String

            NAME = "name";
        }

        public QuestionType() {}

        public QuestionType(String name) {
            setName(name);
        }

        public QuestionType getNewInstance() {
            return new QuestionType();
        }

        public String getName() {
            return getString(Fields.NAME);
        }

        public void setName(String name) {
            put(Fields.NAME, name);
        }

    }

//    @Override
//    public String toString() {
//        String returnString = "";
//        returnString +=
//                Fields.QUESTION_STRING_FIELD + " : " + getQuestionString() + "\n" +
//                Fields.QUESTION_TYPE_FIELD + " : " + getQuestionType() + "\n" +
//                Fields.OPTIONS_FIELD + " : " + getOptions() + "\n" +
//                Fields.CORRECT_OPTIONS_FIELD + " : " + getCorrectOptions() + "\n" +
//                Fields.SOLUTION_FIELD + " : " + getSolution() + "\n" +
//                Fields.HINTS_FIELD + " : " + getHints() + "\n" +
//                Fields.IS_COMPULSORY_FIELD + " : " + getIsCompulsory() + "\n";
//
//        return returnString;
//    }

    public boolean doesStoreFile() {
        return false;
    }
}
