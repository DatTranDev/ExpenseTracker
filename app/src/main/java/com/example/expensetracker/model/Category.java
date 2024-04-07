package com.example.expensetracker.model;

public class Category {
    private int id;
    private String name;
    private boolean isPublic;
    private int iconId;
    private String image;
    private int parentCategoryId;
    private int typeId;

    // Constructor
    public Category(int id, String name, boolean isPublic, int iconId, String image, int parentCategoryId, int typeId) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.iconId = iconId;
        this.image = image;
        this.parentCategoryId = parentCategoryId;
        this.typeId = typeId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}

