package com.comp.iitb.vialogue.models.ParseObjects.models.interfaces;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ironstein on 13/02/17.
 */

public abstract class BaseParseClass extends ParseObject {

    private static class Fields {
        public static final String

        CHILDREN_RESOURCES = "children_resources";
    }

    public BaseParseClass() {
        mIsEditedObservable = new Observable();
        mIsEditedObserver = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                setIsEdited(true);
            }
        };
        mIsEdited = false;
        mChildrenResources = new ParseObjectsCollection<>();
    }

    // ID
    // everything related to ID's is already implemented by Parse
    // getObjectId and setObjectId methods can be used as getter and setter

    // IS_EDITED_OBSERVABLE
    private Observable mIsEditedObservable;

    public final void addIsEditedObserver(Observer observer) {
        mIsEditedObservable.addObserver(observer);
    }

    public final void deleteIsEditedObserver(Observer observer) {
        mIsEditedObservable.deleteObserver(observer);
    }

    // IS_EDITED_OBSERVER
    private Observer mIsEditedObserver;
    public final void observeChildrenResources() {
        for(BaseResourceClass r : getChildrenResources().getAll()) {
            r.addIsEditedObserver(mIsEditedObserver);
        }
    }

    // IS_EDITED
    private boolean mIsEdited;

    public final boolean getIsEdited() {
        return mIsEdited;
    }

    public final void setIsEdited(boolean isEdited) {
        mIsEdited = isEdited;
        if(mIsEdited) {
            mIsEditedObservable.notifyObservers();
        }
    }

    // CHILDREN_RESOURCES
    private ParseObjectsCollection<BaseResourceClass> mChildrenResources;

    public final ParseObjectsCollection<BaseResourceClass> getChildrenResources() {
        return (ParseObjectsCollection) getParseObject(Fields.CHILDREN_RESOURCES);
    }

    public final void setChildrenResources(ParseObjectsCollection<BaseResourceClass> childrenResources) {
        put(Fields.CHILDREN_RESOURCES, childrenResources);
    }

    // constructor
    public void init() {
        observeChildrenResources();
    }

    public void saveParseObject() {
        // call the mySave method for all the children BaseParseClass instances
        for(String key : this.keySet()) {
            if(this.get(key) instanceof BaseParseClass) {
                // is an instance of BaseParseClass
                ((BaseParseClass) this.getParseObject(key)).saveParseObject();
            } else if(this.get(key) instanceof ParseObjectsCollection) {
                // is an instance of ParseObjectCollection
                ((ParseObjectsCollection) this.getParseObject(key)).saveParseObject();
            }
        }

        // now save this
        try {
            save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
