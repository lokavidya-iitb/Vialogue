package com.comp.iitb.vialogue.models.ParseObjects.models.Resources;

import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.interfaces.BaseResourceClass;
import com.parse.ParseClassName;

import java.util.ArrayList;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Question")
public class Question extends BaseResourceClass {

    private static final class Fields {
        public static final String

        QUESTION_STRING_FIELD = "question_string",
        QUESTION_TYPE_FIELD = "type",
        OPTIONS_FIELD = "options",
        CORRECT_OPTIONS_FIELD = "correct_options",
        SOLUTION_FIELD = "solution",
        HINTS_FIELD = "hints",
        IS_COMPULSORY_FIELD = "is_compulsory";
    }

    public Question() {}

    public Question(
            String questionString,
            String questionType,
            ArrayList<String> options,
            ArrayList<String> correctOptions,
            String solution,
            ArrayList<String> hints,
            boolean isCompulsory
            ) {
        super();
    }

    public Question(
            String questionString,
            ArrayList<String> options,
            ArrayList<String> correctOptions
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

    public ArrayList<String> getCorrectOptions() {
        return (ArrayList) getList(Fields.CORRECT_OPTIONS_FIELD);
    }

    public void setCorrectOptions(ArrayList<String> correctOptions) {
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

        public String getName() {
            return getString(Fields.NAME);
        }

        public void setName(String name) {
            put(Fields.NAME, name);
        }

    }
}
