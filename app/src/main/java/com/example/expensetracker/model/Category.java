package com.example.expensetracker.model;

public class Category {
    private String id;
    private String name;
    private String iconId;
    private String parentCategoryId;
    private String type;
    private boolean isPublic;

    // Constructor
    public Category(String id, String name, boolean isPublic, String iconId, String image, String parentCategoryId, String type) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.iconId = iconId;
        this.parentCategoryId = parentCategoryId;
        this.type = type;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

