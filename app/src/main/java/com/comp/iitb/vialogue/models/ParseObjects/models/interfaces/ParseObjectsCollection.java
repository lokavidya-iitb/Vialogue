package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.app.Application;

import com.comp.iitb.vialogue.models.ParseObjects.models.Slide;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public static final class Fields {
        public static final String

        ELEMENTS_FIELD = "elements";
    }

    private void setList(ArrayList<T> list) {
        System.out.println("setList : called");
        try {
            remove(Fields.ELEMENTS_FIELD);
        } catch (Exception e) {}
        System.out.println("setList : list.size() : " + list.size());
        for(T object: list) {
            System.out.println("----");
            add(Fields.ELEMENTS_FIELD, object);
        }
    }

    private ArrayList<T> getList_() {
        System.out.println("getList_ : called");
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println("----------------------");
        for(StackTraceElement s : stackTraceElements) {

            System.out.println(s);
        }
        System.out.println("----------------------");
        System.out.println(stackTraceElements);
        ArrayList<T> list = null;
        try {
            list = (ArrayList) getList(Fields.ELEMENTS_FIELD);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(list == null) {
            System.out.println(has(Fields.ELEMENTS_FIELD));
            System.out.println("list is null");
            list = new ArrayList<T>();
            System.out.println("getLIst : inside if : list.size() : " + list.size());
            setList(list);
        }
        System.out.println("getList : outside if : list.size() : " + list.size());
        return list;
    }

    public void addObject(T object) {
        System.out.println("addObject : called");
        add(Fields.ELEMENTS_FIELD, object);
    }

    public void remove(int index) {
        System.out.println("remove : called");
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
        System.out.println("size : called");
        return getList_().size();
    }

    public void move(int initialPosition, int finalPosition) {
        System.out.println("move : called");
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
        System.out.println("getObjectPosition : called");
        return getList_().indexOf(object);
    }

    @Override
    public void pinParseObject() {
        for(T object : getList_()) {
            try {
                object.pinParseObject();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void fetchChildrenParseObjects() {
//        System.out.println("fetchChildrenParseObjects : called");
////        super.fetchChildrenParseObjects();
//        ArrayList<T> newList = new ArrayList<T>();
//        System.out.println("before calling getList");
//        ArrayList<T> list = getList_();
//        System.out.println("fetchChildrenParseObjects : list.size : " + list.size());
//        for(T object : list) {
////            System.out.println(new GenericClass<T>().getMyType());
//            System.out.println(object.getClassName());
//            System.out.println(object);
//            ParseQuery<T> objectQuery = ParseQuery.getQuery(object.getClassName());
//            objectQuery.fromLocalDatastore();
//            try {
//                T o = (T) objectQuery.get(object.getObjectId());
//                System.out.println(o);
//                o.fetchChildrenParseObjects();
//                newList.add(o);
//                System.out.println("fetchChildrenParseObjects : no eerror");
//            } catch (ParseException e) {
//                System.out.println("fetchChildrenParseObjects : error");
//                e.printStackTrace();
//            }
//        }
//        setList(newList);
//        System.out.println(getList_());

//        try {
//            put(Fields.ELEMENTS_FIELD, ParseObject.f((ArrayList) getList(Fields.ELEMENTS_FIELD)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

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
