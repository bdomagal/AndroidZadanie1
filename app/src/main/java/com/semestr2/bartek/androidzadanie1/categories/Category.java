package com.semestr2.bartek.androidzadanie1.categories;

import java.sql.Blob;

public class Category {
    private String name;
    private String categoryGroup;
    private byte[] image;
    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Category() {
    }

    public Category(String name, String categoryGroup, byte[] image) {

        this.name = name;
        this.categoryGroup = categoryGroup;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void toggle() {
        checked = !checked;
    }
}
