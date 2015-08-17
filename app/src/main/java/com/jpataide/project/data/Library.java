package com.jpataide.project.data;

import java.util.ArrayList;

/**
 * Created by jpataide on 8/17/15.
 */
public class Library {
    private ArrayList<Item> items;
    private int totalItems;

    public int getTotalItems(){
        return totalItems;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
