package com.jpataide.project.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.R;
import com.jpataide.project.data.Livro;
import com.jpataide.project.utils.AppController;
import com.jpataide.project.utils.LivroUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * Created by jpataide on 8/16/15.
 */
public class LivroActivity extends Activity implements IServerHandler, Response.ErrorListener, Response.Listener<JSONObject> {
    private LivroUtils livroUtilsInstance;
    private TextView txtTitulo, txtAutores, txtEditora, txtDescricao, txtPaginas;
    View progress, layout;
    NetworkImageView img;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_livro);
        layout = findViewById(R.id.layout_livro);
        layout.setVisibility(View.GONE);
        progress = findViewById(R.id.progressBar2);

        txtTitulo = (TextView) findViewById(R.id.txt_titulo);
        txtAutores = (TextView) findViewById(R.id.txt_autores);
        txtEditora = (TextView) findViewById(R.id.txt_editora);
        txtDescricao = (TextView) findViewById(R.id.txt_descricao);
        txtPaginas = (TextView) findViewById(R.id.txt_paginas);
        img = (NetworkImageView) findViewById(R.id.img);


        livroUtilsInstance = AppController.getInstance().getLivroUtils();
        if(getIntent().hasExtra(MainActivity.INTENT_URL)){
            Bundle extras = getIntent().getExtras();
            url = extras.getString(MainActivity.INTENT_URL);
        }
        livroUtilsInstance.getLivroDetails(this, url);


    }

    @Override
    public void connectToServer(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest =
                new JsonObjectRequest(
                        Request.Method.GET, // Requisição via HTTP_GET
                        url,   // url da requisição
                        null,  // JSONObject a ser enviado via POST
                        this,  // Response.Listener
                        this); // Response.ErrorListener

        queue.add(jsObjRequest);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(this, "Erro!",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            Livro livro = livroUtilsInstance.getDetails(response);

            if (livro != null){
                txtTitulo.setText(livro.getNome());
                txtAutores.setText(livro.getAutores());
                txtDescricao.setText(livro.getDescricao());
                txtEditora.setText(livro.getEditora());
                txtPaginas.setText(livro.getPaginas());
                img.setImageUrl(
                        livro.getImageUrl(),
                        AppController.getInstance().getImageLoader());
                progress.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                //this.notifyAll();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
