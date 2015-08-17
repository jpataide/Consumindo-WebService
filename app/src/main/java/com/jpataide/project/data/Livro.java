package com.jpataide.project.data;

/**
 * Created by jpataide on 8/16/15.
 */
public class Livro {
    String nome;
    String imageUrl;
    String selfLink;
    String autores;
    String editora;
    String descricao;
    String paginas;

    public Livro(String nome, String imageUrl, String selfLink) {
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.selfLink = selfLink;
    }

    public Livro(String nome, String imageUrl, String selfLink, String autores, String editora, String descricao, String paginas) {
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.selfLink = selfLink;
        this.autores = autores;
        this.editora = editora;
        this.descricao = descricao;
        this.paginas = paginas;
    }

    public String getNome() {
        return nome;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public String getAutores() {
        return autores;
    }

    public String getEditora() {
        return editora;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPaginas(){
        return paginas;
    }

}
