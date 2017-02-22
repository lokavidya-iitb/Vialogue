package com.comp.iitb.vialogue.models.ParseObjects.models;

import com.parse.Parse;
import com.parse.ParseClassName;
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
public class ParseObjectsCollection<T extends BaseParseClass> extends BaseParseClass {

    private static final class Fields {
        public static final String

        ELEMENTS_FIELD = "elements";
    }

    private ArrayList<T> mObjectsArray;

    public void ParseObjectsCollection() {
        mObjectsArray = new ArrayList<T>();
    }

    public Array[] toArray() {
        Array[] arrayList = new Array[mObjectsArray.size()];
        return mObjectsArray.toArray(arrayList);
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
        put(Fields.ELEMENTS_FIELD, toArray());

//        // call the mySave method for all the children BaseParseClass instances
//        for(T object : mObjectsArray) {
//            object.saveParseObject();
//        }

        super.saveParseObject();
    }
}
