package com.comp.iitb.vialogue.helpers;

import android.os.AsyncTask;
import android.widget.Switch;

import java.io.IOException;
import java.security.SignedObject;

/**
 * Created by DELL on 5/10/2017.
 */

    public class LVCreateAsyncTask extends AsyncTask<String, Void, String> {
    private String whatToDo = new String();
    private String JSON= new String();
    public LVCreateAsyncTask (String whatToDo){ this.whatToDo = whatToDo; }
    public LVCreateAsyncTask (String whatToDo, String JSON) {
        this.whatToDo = whatToDo;
        this.JSON =JSON;
    }
        @Override

        protected String doInBackground(String... params) {
            switch(whatToDo){
                case "createAnUser":
                    NetworkCalls callToCreateAUser = new NetworkCalls();
                    try {
                        callToCreateAUser.doPostRequest("/api/signup","<add_JSON_here. Gonna leave in 2 hours! Couldn't complete it>");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

