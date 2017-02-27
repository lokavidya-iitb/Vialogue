package com.comp.iitb.vialogue.models;

/**
 * Created by jeffrey on 13/2/17.
 */
/**
 * Created by jeffrey on 12/2/17.
 */
public class CategoryType {
    public CategoryType(String id, String name, String desc,String imageURL){
        this.id=id;
        this.name= name;
        this.desc= desc;
        this.imageURL=imageURL;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private String name;
    private String id;
    private String desc;
    private String imageURL;

}
