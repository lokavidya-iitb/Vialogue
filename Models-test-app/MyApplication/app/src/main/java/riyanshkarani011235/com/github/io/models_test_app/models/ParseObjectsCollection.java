package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Collection")
public class ParseObjectsCollection<T extends BaseParseClass> extends ParseObject {

    private static final class Fields {
        public static final String

        ELEMENTS_FIELD = "elements";
    }

    private ArrayList<T> mObjectsArray;

    public ParseObjectsCollection() {
        mObjectsArray = new ArrayList<T>();
    }

    public void add(T object) {
        mObjectsArray.add(object);
    }

    public void remove(int index) {
        mObjectsArray.remove(index);
    }

    public T get(int index) {
        return mObjectsArray.get(index);
    }

    public ArrayList<T> getAll() {
        return mObjectsArray;
    }

    public void saveParseObject() {

        remove(Fields.ELEMENTS_FIELD);
        for(T object: mObjectsArray) {
            add(Fields.ELEMENTS_FIELD, (ParseObject) object);
        }

        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
