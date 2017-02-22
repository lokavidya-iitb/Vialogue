
package riyanshkarani011235.com.github.io.models_test_app.models.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AuthorJson extends BaseJsonClass {

    /**
     *
     * (Required)
     *
     */
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_edited")
    @Expose
    private Boolean isEdited;
    @SerializedName("children_resources")
    @Expose
    private List<ResourceJson> childrenResources = null;
    /**
     *
     * (Required)
     *
     */
    @SerializedName("first_name")
    @Expose
    private String firstName;
    /**
     *
     * (Required)
     *
     */
    @SerializedName("last_name")
    @Expose
    private String lastName;
    /**
     *
     * (Required)
     *
     */
    @SerializedName("email")
    @Expose
    private String email;

    /**
     *
     * (Required)
     *
     */
    public String getId() {
        return id;
    }

    /**
     *
     * (Required)
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public List<ResourceJson> getChildrenResources() {
        return childrenResources;
    }

    public void setChildrenResources(List<ResourceJson> childrenResources) {
        this.childrenResources = childrenResources;
    }

    /**
     *
     * (Required)
     *
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * (Required)
     *
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * (Required)
     *
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * (Required)
     *
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * (Required)
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * (Required)
     *
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
