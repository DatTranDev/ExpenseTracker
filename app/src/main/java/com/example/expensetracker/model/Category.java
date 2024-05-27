package com.example.expensetracker.model;

public class Category {
    private String id;
    private String name;
    private String iconId;
    private Icon icon;
    private String parentCategoryId;
    private String type;
    private boolean isPublic;
    private int _v;

    // Constructor
    public Category() {
    }
    public Category(String id, String name, boolean isPublic, String iconId, String image, String parentCategoryId, String type) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.iconId = iconId;
        this.parentCategoryId = parentCategoryId;
        this.type = type;
    }

    // Getters and setters
    public int get_v() {
        return _v;
    }
    public void set_v(int _v) {
        this._v = _v;
    }
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

    public Icon getIcon() {
        return icon;
    }
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

