package com.jpataide.project.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.data.Item;
import com.jpataide.project.data.Library;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jpataide on 8/16/15.
 */
public class LivroUtils {
    private ArrayList<Item> livrosList = new ArrayList<>();
    private static final int MAX_RESULTS = 20;
    private int currentIndex = 1;
    private String url = "https://www.googleapis.com/books/v1/volumes?q=%s&startIndex=%d&maxResults=%d&key=AIzaSyB3IJe_nrKT7DAzWJ5LlggjMNx_TQ6dP0Y";
    private String query;
    private int totalItems;

    public void refreshList(IServerHandler serverHandler, String mQuery) {
        if (!mQuery.equalsIgnoreCase(this.query)) {
            livrosList.clear();
            currentIndex = 1;
            query = mQuery.replace(" ", "+");
            totalItems = 0;
        }
        if (totalItems > 0 && currentIndex > totalItems)
            return;

        serverHandler.connectToServer(String.format(url, query, currentIndex - 1, MAX_RESULTS));
        incCurrentIndex();
    }

    public void refreshList(IServerHandler serverHandler) {
        if (totalItems > 0 && currentIndex > totalItems)
            return;

        serverHandler.connectToServer(String.format(url, query, currentIndex - 1, MAX_RESULTS));
        incCurrentIndex();
    }

    public void getLivroDetails(IServerHandler serverHandler, String url) {
        serverHandler.connectToServer(url);
    }

    public ArrayList<Item> getList(JSONObject jsonList) throws JSONException {

        Gson gson = new GsonBuilder().create();
        Library library = gson.fromJson(jsonList.toString(), Library.class);

        if (totalItems == 0) {
            totalItems = library.getTotalItems();
            if (currentIndex >= totalItems){
                currentIndex = totalItems + 1;
            }
        }

        if (library != null){
            if (library.getItems() != null){
                livrosList.addAll(library.getItems());
            }
        }

        return livrosList;
    }

    public void setQuery(String mQuery) {
        this.query = mQuery.replace(" ", "+");
    }

    public int getCurrentIndex() {
        return currentIndex - 1;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Item getDetails(JSONObject jsonObject) throws JSONException {
        Gson gson = new GsonBuilder().create();
        Item item = gson.fromJson(jsonObject.toString(), Item.class);
        return item;
    }

    private void incCurrentIndex(){
        currentIndex += MAX_RESULTS;
        if (currentIndex > totalItems && totalItems > 0){
            currentIndex = totalItems + 1;
        }
    }

    public boolean isEndOfList(){
        return currentIndex > totalItems;
    }
}
