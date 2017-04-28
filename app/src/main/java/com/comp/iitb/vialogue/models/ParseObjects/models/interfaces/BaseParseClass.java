package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ironstein on 13/02/17.
 */

public abstract class BaseParseClass extends ParseObject {

    // default constructor required by Parse
    // DO NOT USE THIS CONSTRUCTOR (ONLY FOR USE BY PARSE)
    // USE THE OTHER CONSTRUCTOR THAT REQUIRES PARAMETERS DURING
    // INSTANTIATING THE OBJECT
    public BaseParseClass() {}

    public static class Fields implements BaseFieldsClass {
        public static final String

        CHILDREN_RESOURCES = "children_resources";

        public ArrayList<String> getAllFields() {
            return new ArrayList<String>(Arrays.asList(new String[] {
                    CHILDREN_RESOURCES
            }));
        }
    }

    public ArrayList<String> getAllFields() {
        return new Fields().getAllFields();
    }

//    public BaseParseClass() {
        // TODO think of how to implement observer observable
//        mIsEditedObservable = new Observable();
//        mIsEditedObserver = new Observer() {
//            @Override
//            public void update(Observable observable, Object o) {
//                setIsEdited(true);
//            }
//        };
//        mIsEdited = false;
//        setChildrenResources(new ParseObjectsCollection<BaseResourceClass>());
//    }

    // ID
    // everything related to ID's is already implemented by Parse
    // getObjectId and setObjectId methods can be used as getter and setter

    // IS_EDITED_OBSERVABLE
//    private Observable mIsEditedObservable;
//
//    public final void addIsEditedObserver(Observer observer) {
//        mIsEditedObservable.addObserver(observer);
//    }
//
//    public final void deleteIsEditedObserver(Observer observer) {
//        mIsEditedObservable.deleteObserver(observer);
//    }
//
//    // IS_EDITED_OBSERVER
//    private Observer mIsEditedObserver;
//    public final void observeChildrenResources() {
//        for(BaseResourceClass r : getChildrenResources().getAll()) {
//            r.addIsEditedObserver(mIsEditedObserver);
//        }
//    }
//
//    // IS_EDITED
//    private boolean mIsEdited;
//
//    public final boolean getIsEdited() {
//        return mIsEdited;
//    }
//
//    public final void setIsEdited(boolean isEdited) {
//        mIsEdited = isEdited;
//        if(mIsEdited) {
//            mIsEditedObservable.notifyObservers();
//        }
//    }

    // CHILDREN_RESOURCES
    public final ParseObjectsCollection<BaseResourceClass> getChildrenResources() {
        ParseObjectsCollection<BaseResourceClass> childrenResources = null;
        try {
            childrenResources =  (ParseObjectsCollection) getParseObject(Fields.CHILDREN_RESOURCES);
        } catch (Exception e) {}

        if(childrenResources == null) {
            childrenResources = (ParseObjectsCollection) new ParseObjectsCollection<BaseParseClass>();
            setChildrenResources(childrenResources);
        }

        return childrenResources;
    }

    public final void setChildrenResources(ParseObjectsCollection<BaseResourceClass> childrenResources) {
        put(Fields.CHILDREN_RESOURCES, childrenResources);
    }

    // constructor
//    public void init() {
//        observeChildrenResources();
//    }

    public void pinParseObject() throws ParseException {
        for(String key : keySet()) {
            if(get(key) instanceof BaseParseClass) {
                // is an instance of BaseParseClass
                ((BaseParseClass) this.getParseObject(key)).pinParseObject();
            }
        }

        // to pin this object
        pin();
    }

    public void pinParseObjectInBackground() throws ParseException {
        for(String key : keySet()) {
            if(get(key) instanceof BaseParseClass) {
                // is an instance of BaseParseClass
                ((BaseParseClass) this.getParseObject(key)).pinParseObjectInBackground();
            }
        }

        // to pin this object
        pinInBackground();
    }

    public BaseParseClass deepCopy() throws Exception {
        BaseParseClass copiedParseObject = getNewInstance();
        for(String key : this.keySet()) {
            if(get(key) instanceof BaseParseClass) {
                if(get(key) instanceof  ParseObjectsCollection) {
                    // Collection
                    copiedParseObject.put(key, ((ParseObjectsCollection) get(key)).deepCopy());
                } else if(get(key) instanceof BaseResourceClass) {
                    // Resource
                    copiedParseObject.put(key, ((BaseResourceClass) get(key)).deepCopy());
                } else {
                    // ParseObject
                    copiedParseObject.put(key, ((BaseParseClass) get(key)).deepCopy());
                }
            } else {
                // not a ParseObject
                copiedParseObject.put(key, get(key));
            }
        }
        return copiedParseObject;
    }

    public void saveParseObject(Context context) throws ParseException {
        // call the mySave method for all the children BaseParseClass instances
        for(String key : this.keySet()) {
            if(get(key) instanceof ParseObjectsCollection) {
                // is an instance of BaseParseClass
                ((ParseObjectsCollection) getParseObject(key)).saveParseObject(context);
            } else if(get(key) instanceof BaseResourceClass) {
                // is an instance of BaseResourceClass
                ((BaseResourceClass) get(key)).saveParseObject(context);
            }
        }

        // now save this
        save();
    }

    public void saveParseObjectEventually() {
        // call the mySave method for all the children BaseParseClass instances
        for(String key : this.keySet()) {
            if(get(key) instanceof ParseObjectsCollection) {
                // is an instance of BaseParseClass
                ((ParseObjectsCollection) getParseObject(key)).saveParseObjectEventually();
            } else if(get(key) instanceof BaseResourceClass) {
                // is an instance of BaseResourceClass
                ((BaseResourceClass) get(key)).saveParseObjectEventually();
            }
        }

        // now save this
        saveEventually();
    }

    public void fetchChildrenObjects() {
        for(String key : keySet()) {
            if(get(key) instanceof BaseParseClass) {
                try {
                    getParseObject(key).fetchFromLocalDatastore();
                    ((BaseParseClass) getParseObject(key)).fetchChildrenObjects();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fetchChildrenObjectsFromServer() {
        for(String key : keySet()) {
            if(get(key) instanceof BaseParseClass) {
                try {
                    getParseObject(key).fetch().pin();
                    ((BaseParseClass) getParseObject(key)).fetchChildrenObjectsFromServer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if(get(key) instanceof BaseResourceClass) {

            }
        }
    }

    public abstract BaseParseClass getNewInstance();

}
