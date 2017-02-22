package riyanshkarani011235.com.github.io.models_test_app.models.json;

import java.util.List;

/**
 * Created by ironstein on 16/02/17.
 */

public abstract class BaseJsonClass {

    public abstract String getId();
    public abstract void setId(String id);
    public abstract Boolean getIsEdited();
    public abstract void setIsEdited(Boolean isEdited);
    public abstract List<ResourceJson> getChildrenResources();
    public abstract void setChildrenResources(List<ResourceJson> childrenResources);

}
