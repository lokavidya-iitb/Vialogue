package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public static class Fields {
        public static final String

        CHILDREN_RESOURCES = "children_resources";
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

    public void fetchChildrenParseObjects() {
        for(String key : keySet()) {
            if(get(key) instanceof BaseParseClass) {
                BaseParseClass object = (BaseParseClass) get(key);
                ParseQuery<ParseObject> objectQuery = ParseQuery.getQuery(object.getClassName());
                objectQuery.fromLocalDatastore();
                try {
                    BaseParseClass newObject = (BaseParseClass) objectQuery.get(object.getObjectId());
                    newObject.fetchChildrenParseObjects();
                    put(key, newObject);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO implement
//    public void saveParseObject() {
//        // call the mySave method for all the children BaseParseClass instances
//        for(String key : this.keySet()) {
//            if(this.get(key) instanceof BaseParseClass) {
//                // is an instance of BaseParseClass
//                ((BaseParseClass) this.getParseObject(key)).saveParseObject();
//            }
//        }
//
//        // now save this
//        try {
//            save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
