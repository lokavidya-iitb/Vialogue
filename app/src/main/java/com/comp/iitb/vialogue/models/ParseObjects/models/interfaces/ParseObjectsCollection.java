package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.app.Application;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ironstein on 20/02/17.
 */

@ParseClassName("Collection")
public class ParseObjectsCollection<T extends BaseParseClass> extends BaseParseClass {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public ParseObjectsCollection() {}

    public ParseObjectsCollection getNewInstance() {
        return new ParseObjectsCollection<T>();
    }

    public static final class Fields implements BaseFieldsClass {
        public static final String

        ELEMENTS_FIELD = "elements";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    ELEMENTS_FIELD
            }));
        }
    }

    @Override
    public ArrayList<String> getAllFields() {
        ArrayList<String> fields = new Fields().getAllFields();
        fields.addAll(super.getAllFields());
        return fields;
    }

    private void setList(ArrayList<T> list) {
        try {
            remove(Fields.ELEMENTS_FIELD);
        } catch (Exception e) {}
        for(T object: list) {
            add(Fields.ELEMENTS_FIELD, object);
        }
    }

    private ArrayList<T> getList_() {
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

    public void addObject(T object) {
        add(Fields.ELEMENTS_FIELD, object);
    }

    public void remove(int index) {
        ArrayList<T> list = getList_();
        list.remove(index);
        setList(list);
    }

    public T get(int index) {
        return getList_().get(index);
    }

    public ArrayList<T> getAll() {
        return getList_();
    }

    public void removeAll() {
        setList(new ArrayList<T>());
    }

    public int size() {
        return getList_().size();
    }

    public void move(int initialPosition, int finalPosition) {
        ArrayList<T> list = getList_();
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
        return getList_().indexOf(object);
    }

    @Override
    public void pinParseObject() throws ParseException {
        super.pinParseObject();
        for(T object : getList_()) {
            object.pinParseObject();
        }
    }

    @Override
    public void pinParseObjectInBackground() throws ParseException {
        super.pinParseObjectInBackground();
        for(T object : getList_()) {
            object.pinParseObjectInBackground();
        }
    }

    public void fetchChildrenObjects() {
        super.fetchChildrenObjects();
        System.out.println("fetching : " + getClassName());
        System.out.println((ArrayList) getList(Fields.ELEMENTS_FIELD));
        System.out.println(getList_().size());
        for(T object: getList_()) {
            try {
                object.fetchFromLocalDatastore();
                System.out.println(object);
                object.fetchChildrenObjects();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveParseObject() throws ParseException {
        for(T object: getList_()) {
            object.saveParseObject();
        }
        save();
    }

    public void saveParseObjectEventually() {
        for(T object: getList_()) {
            object.saveParseObjectEventually();
        }
        saveEventually();
    }

    @Override
    public ParseObjectsCollection deepCopy() throws Exception {
        ParseObjectsCollection<T> copiedCollection = getNewInstance();

        for(T obj: getList_()) {
            copiedCollection.add(Fields.ELEMENTS_FIELD, obj);
        }

        return copiedCollection;
    }
}
