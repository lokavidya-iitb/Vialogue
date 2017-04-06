package com.comp.iitb.vialogue.utils;

/**
 * Created by jeffrey on 3/4/17.
 */
import android.os.Environment;

import com.comp.iitb.vialogue.models.QuestionAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ReadJSONFile {
    public ReadJSONFile (){

    }
    public static List<QuestionAnswer> getQuestionsFromJSON(String path) {
        ArrayList<QuestionAnswer> list = new ArrayList<>();
        try {
            File yourFile = new File(Environment.getExternalStorageDirectory(), path);
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            JSONArray data = new JSONArray(jsonStr);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String question_string = c.getString("question_string");
                String answer = c.getString("answer");
                Long time = c.getLong("time");
                JSONArray options = c.getJSONArray("options");
                QuestionAnswer questionAnswer = new QuestionAnswer();
                ArrayList<String> arrayList = new ArrayList<String>();
                if (options != null) {
                    int len = options.length();
                    for (int iterator = 0; iterator < len; iterator++) {
                        arrayList.add(options.get(iterator).toString());
                    }
                }
                String[] optionsArray = new String[arrayList.size()];
                optionsArray = arrayList.toArray(optionsArray);
                questionAnswer.setOptions(optionsArray);
                questionAnswer.setTime(time);
                questionAnswer.setQuestion(question_string);
                list.add(questionAnswer);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}