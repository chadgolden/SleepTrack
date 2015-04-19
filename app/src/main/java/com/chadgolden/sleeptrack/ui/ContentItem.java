package com.chadgolden.sleeptrack.ui;

/**
 * Created by Chad on 4/19/2015.
 */
public class ContentItem {

    private String itemName;
    private String itemDescription;

    public ContentItem(String name, String description) {
        this.itemName = name;
        this.itemDescription = description;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

}
