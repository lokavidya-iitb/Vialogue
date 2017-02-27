package com.comp.iitb.vialogue.helpers;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jeffrey on 26/2/17.
 */
public class SharedPreferenceHelper {


        Context mContext;

        public SharedPreferenceHelper(Context mContext) {
            this.mContext = mContext;
        }

        public void saveToSharedPref(String key, Object value) throws Exception {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(key, Context.MODE_PRIVATE).edit();
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else {
                throw new Exception("Unacceptable object type");
            }

            editor.commit();
        }


        public String loadStringFromSharedPref(String key) throws Exception {
            SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            String restoredText = prefs.getString(key, null);

            return restoredText;
        }


        public Integer loadIntegerFromSharedPref(String key) throws Exception {
            SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            Integer restoredText = prefs.getInt(key, -1);

            return restoredText;
        }


        public Float loadFloatFromSharedPref(String key) throws Exception {
            SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            Float restoredText = prefs.getFloat(key, -1);

            return restoredText;
        }


        public Long loadLongFromSharedPref(String key) throws Exception {
            SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            Long restoredText = prefs.getLong(key, -1);

            return restoredText;
        }


        public Boolean loadBooleanFromSharedPref(String key) throws Exception {
            SharedPreferences prefs = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            Boolean restoredText = prefs.getBoolean(key, false);

            return restoredText;
        }
}

