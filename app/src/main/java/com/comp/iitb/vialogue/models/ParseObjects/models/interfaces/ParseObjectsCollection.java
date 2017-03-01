package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;

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

    public void removeAll() {
        mObjectsArray = new ArrayList<T>();
    }

    public int size() {
        return mObjectsArray.size();
    }

    public void move(int initialPosition, int finalPosition) {
        if(initialPosition >= mObjectsArray.size() || finalPosition >= mObjectsArray.size()) {
            throw new IndexOutOfBoundsException();
        }

        if(initialPosition == finalPosition) {
            return;
        }

        ArrayList<T> newObjectsArray = new ArrayList<T>();
        if(initialPosition > finalPosition) {
            for(int i=0; i<finalPosition; i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            newObjectsArray.add(mObjectsArray.get(initialPosition));
            for(int i=finalPosition; i<initialPosition; i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            for(int i=initialPosition+1; i<mObjectsArray.size(); i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            mObjectsArray = newObjectsArray;
        } else {
            for(int i=0; i<initialPosition; i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            for(int i=initialPosition+1; i<finalPosition; i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            newObjectsArray.add(mObjectsArray.get(initialPosition));
            for(int i=finalPosition; i<mObjectsArray.size(); i++) {
                newObjectsArray.add(mObjectsArray.get(i));
            }
            mObjectsArray = newObjectsArray;
        }
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
