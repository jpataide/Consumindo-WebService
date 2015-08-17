package com.jpataide.project.objects;

/**
 * Created by jpataide on 8/17/15.
 */
public class VolumeInfo {
    private String title;
    private String authors[];
    private String publisher;
    private String description;
    private String pageCount;
    private ImageLinks imageLinks;

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getPageCount() {
        return pageCount;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public String getAutores(){
        String autores = "";
        for (int i = 0; i < authors.length; i++){
            autores += authors[i]+System.getProperty("line.separator");
        }
        return autores.substring(0,autores.lastIndexOf(System.getProperty("line.separator")));
    }
}
