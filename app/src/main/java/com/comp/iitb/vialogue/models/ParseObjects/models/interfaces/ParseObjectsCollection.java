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

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public ParseObjectsCollection() {}

    private static final class Fields {
        public static final String

        ELEMENTS_FIELD = "elements";
    }

    private void setList(ArrayList<T> list) {
        try {
            remove(Fields.ELEMENTS_FIELD);
        } catch (Exception e) {}
        for(T object: list) {
            add(Fields.ELEMENTS_FIELD, (ParseObject) object);
        }
    }

    private ArrayList<T> getList() {
        ArrayList<T> list = null;
        try {
            list = (ArrayList) getList(Fields.ELEMENTS_FIELD);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(list == null) {
            list = new ArrayList<T>();
            setList(list);
        }
        return list;
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
        return getList().size();
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

    // TODO implement
//    public void saveParseObject() {
//        try {
//            save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }
}
