package com.jpataide.project.utils;

import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.data.Livro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jpataide on 8/16/15.
 */
public class LivroUtils {
    private ArrayList<Livro> livrosList = new ArrayList<>();
    private static final int MAX_RESULTS = 10;
    private int currentIndex = 1;
    private String url = "https://www.googleapis.com/books/v1/volumes?q=%s&startIndex=%d&maxResults=%d";
    private String query;
    private int totalItems;
    private final String JSON_TOTAL_ITEMS = "totalItems";
    private final String JSON_ITEMS = "items";
    private final String JSON_VOLUME_INFO = "volumeInfo";
    private final String JSON_IMAGE_LINKS = "imageLinks";
    private final String JSON_TITLE = "title";
    private final String JSON_SMALL_THUMBNAIL = "smallThumbnail";
    private final String JSON_SELF_LINK = "selfLink";
    private final String JSON_SMALL_IMAGE = "small";
    private final String JSON_AUTHORS = "authors";
    private final String JSON_PUBLISHER = "publisher";
    private final String JSON_DESCRIPTION = "description";
    private final String JSON_PAGE_COUNT = "pageCount";


    public void refreshList(IServerHandler serverHandler, String mQuery) {
        if (!mQuery.equalsIgnoreCase(this.query)) {
            livrosList.clear();
            currentIndex = 1;
            query = mQuery.replace(" ", "+");
        }
        serverHandler.connectToServer(String.format(url, query, currentIndex - 1, MAX_RESULTS));
        incCurrentIndex();
    }

    public void refreshList(IServerHandler serverHandler) {
        serverHandler.connectToServer(String.format(url, query, currentIndex - 1, MAX_RESULTS));
        incCurrentIndex();
    }

    public void getLivroDetails(IServerHandler serverHandler, String url) {
        serverHandler.connectToServer(url);
    }

    public ArrayList<Livro> getList(JSONObject jsonList) throws JSONException {

        if (totalItems == 0) {
            totalItems = jsonList.getInt(JSON_TOTAL_ITEMS);
        }
        JSONArray livros = jsonList.getJSONArray(JSON_ITEMS);

        for (int i = 0; i < livros.length(); i++) {
            JSONObject jsonItem =
                    livros.getJSONObject(i);
            JSONObject jsonInfo = jsonItem.getJSONObject(JSON_VOLUME_INFO);

            JSONObject jsonImage = jsonInfo.getJSONObject(JSON_IMAGE_LINKS);

            String nome =
                    jsonInfo.getString(JSON_TITLE);
            String thumbnail =
                    jsonImage.getString(JSON_SMALL_THUMBNAIL);

            String selfLink = jsonItem.getString(JSON_SELF_LINK);

            Livro livro = new Livro(nome, thumbnail, selfLink);
            livrosList.add(livro);
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

    public Livro getDetails(JSONObject jsonObject) throws JSONException {

        JSONObject jsonInfo = jsonObject.getJSONObject(JSON_VOLUME_INFO);

        JSONObject jsonImage = jsonInfo.getJSONObject(JSON_IMAGE_LINKS);

        String nome =
                jsonInfo.getString(JSON_TITLE);
        String thumbnail =
                jsonImage.getString(JSON_SMALL_IMAGE);

        JSONArray jsonAutores = jsonInfo.getJSONArray(JSON_AUTHORS);

        String autores = "";
        for (int i = 0; i < jsonAutores.length(); i++) {
            autores = autores + (jsonAutores.getString(i) + System.getProperty("line.separator"));
        }

        autores = autores.substring(0, autores.lastIndexOf(System.getProperty("line.separator")));

        String editora = jsonInfo.getString(JSON_PUBLISHER);

        String descicao = "";

        if (jsonInfo.has(JSON_DESCRIPTION)) {
            descicao = jsonInfo.getString(JSON_DESCRIPTION);
        }

        String selfLink = jsonObject.getString(JSON_SELF_LINK);

        String paginas = jsonInfo.getString(JSON_PAGE_COUNT);

        return new Livro(nome, thumbnail, selfLink, autores, editora, descicao, paginas);
    }

    private void incCurrentIndex(){
        currentIndex += MAX_RESULTS;
        if (currentIndex > MAX_RESULTS){
            currentIndex = MAX_RESULTS;
        }
    }
}
