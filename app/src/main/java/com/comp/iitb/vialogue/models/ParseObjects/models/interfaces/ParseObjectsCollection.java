package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.lang.reflect.Array;
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

    public ParseObjectsCollection() {}

    private void setList(ArrayList<T> list) {
        remove(Fields.ELEMENTS_FIELD);
        for(T object: list) {
            add(Fields.ELEMENTS_FIELD, (ParseObject) object);
        }
    }

    private ArrayList<T> getList() {
        return (ArrayList) getList(Fields.ELEMENTS_FIELD);
    }

    public void add(T object) {
        add(Fields.ELEMENTS_FIELD, object);
    }

    public void remove(int index) {
        ArrayList<T> list = getList();
        list.remove(index);
        setList(list);
    }

    public T get(int index) {
        return getList().get(index);
    }

    public ArrayList<T> getAll() {
        return getList();
    }

    public void removeAll() {
        setList(new ArrayList<T>());
    }

    public int size() {
        try {
            return getList().size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void move(int initialPosition, int finalPosition) {
        ArrayList<T> list = getList();
        if(initialPosition >= list.size() || finalPosition >= list.size()) {
            throw new IndexOutOfBoundsException();
        }

        if(initialPosition == finalPosition) {
            return;
        }

        ArrayList<T> newObjectsArray = new ArrayList<T>();
        if(initialPosition > finalPosition) {
            for(int i=0; i<finalPosition; i++) {
                newObjectsArray.add(list.get(i));
            }
            newObjectsArray.add(list.get(initialPosition));
            for(int i=finalPosition; i<initialPosition; i++) {
                newObjectsArray.add(list.get(i));
            }
            for(int i=initialPosition+1; i<list.size(); i++) {
                newObjectsArray.add(list.get(i));
            }
            list = newObjectsArray;
        } else {
            for(int i=0; i<initialPosition; i++) {
                newObjectsArray.add(list.get(i));
            }
            for(int i=initialPosition+1; i<finalPosition; i++) {
                newObjectsArray.add(list.get(i));
            }
            newObjectsArray.add(list.get(initialPosition));
            for(int i=finalPosition; i<list.size(); i++) {
                newObjectsArray.add(list.get(i));
            }
            list = newObjectsArray;
        }
        setList(list);
    }

    public int getObjectPosition(T object) {
        return getList().indexOf(object);
    }

    public void saveParseObject() {
        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
