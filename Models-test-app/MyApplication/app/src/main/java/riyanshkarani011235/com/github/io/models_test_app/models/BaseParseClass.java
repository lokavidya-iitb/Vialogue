package riyanshkarani011235.com.github.io.models_test_app.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.parse.ParseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import riyanshkarani011235.com.github.io.models_test_app.models.json.BaseJsonClass;
import riyanshkarani011235.com.github.io.models_test_app.models.json.ProjectJson;
import riyanshkarani011235.com.github.io.models_test_app.models.json.ResourceJson;

/**
 * Created by ironstein on 13/02/17.
 */

public abstract class BaseParseClass extends ParseObject {

    private static class Fields {
        public static final String

        CHILDREN_RESOURCE_IDS = "children_resource_ids";
    }

    // IS_EDITED_OBSERVABLE
    private Observable mIsEditedObservable = new Observable();

    public final void addIsEditedObserver(Observer observer) {
        mIsEditedObservable.addObserver(observer);
    }

    public final void deleteIsEditedObserver(Observer observer) {
        mIsEditedObservable.deleteObserver(observer);
    }

    // IS_EDITED_OBSERVER
    private Observer mIsEditedObserver = new Observer() {
        @Override
        public void update(Observable observable, Object o) {
            setIsEdited(true);
        }
    };

    public final void observeChildrenResources() throws org.json.JSONException {
        List<Resource> childrenResources = getChildrenResources();
        for(Resource r : childrenResources) {
            r.addIsEditedObserver(mIsEditedObserver);
        }
    }

    // ID
    // everything related to ID's is already implemented by Parse
    // getObjectId and setObjectId methods can be used as getter and setter

    // IS_EDITED
    private boolean mIsEdited = false;
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
    private List<Resource> mChildrenResources = new ArrayList<Resource>();

    public final List<Resource> getChildrenResources() {
        return mChildrenResources;
    }

    public final void setChildrenResources(List<Resource> childrenResources) {
        mChildrenResources = childrenResources;
    }

    public final List<String> getChildrenResourceIds() {
        return getList(Fields.CHILDREN_RESOURCE_IDS);
    }

    public final void setChildrenResourceIds(List<String> childrenResourceIds) {
        put(Fields.CHILDREN_RESOURCE_IDS, childrenResourceIds);
    }

//    public void saveParseObject() throws com.parse.ParseException {
//        // save children resources independently
//        List<String> childrenResourceIds = new ArrayList<String>();
//        for(Resource r: getChildrenResources()) {
//            // TODO handle exception, and check the reason for display to the user
//            r.save();
//            childrenResourceIds.add(r.getObjectId());
//        }
//        setChildrenResourceIds(childrenResourceIds);
//        save();
//    }

//    public abstract void saveParseObject();

    // constructor
    public void init() throws org.json.JSONException {
        observeChildrenResources();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    // abstract methods
//    public abstract BaseJsonClass getJsonObject();
}
