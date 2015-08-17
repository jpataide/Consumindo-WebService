package com.jpataide.project.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.R;
import com.jpataide.project.data.Item;
import com.jpataide.project.utils.AppController;
import com.jpataide.project.utils.LivroUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jpataide on 8/16/15.
 */
public class LivroActivity extends BaseActivity implements IServerHandler, Response.ErrorListener, Response.Listener<JSONObject> {
    private LivroUtils livroUtilsInstance;
    private TextView txtTitulo, txtAutores, txtEditora, txtDescricao, txtPaginas;
    View progress, layout;
    NetworkImageView img;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected int getLayoutResId() {
        return R.layout.activity_livro;
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
        new AlertDialog.Builder(this)
                .setTitle(R.string.atencao)
                .setMessage(R.string.exception_connection)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        AppController.getInstance().cancelPendingRequests();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            Item livro = livroUtilsInstance.getDetails(response);

            if (livro != null){
                txtTitulo.setText(livro.getVolumeInfo().getTitle());
                txtAutores.setText(livro.getVolumeInfo().getAutores());
                txtDescricao.setText(livro.getVolumeInfo().getDescription());
                txtEditora.setText(livro.getVolumeInfo().getPublisher());
                txtPaginas.setText(livro.getVolumeInfo().getPageCount());
                img.setImageUrl(
                        livro.getVolumeInfo().getImageLinks().getThumbnail(),
                        AppController.getInstance().getImageLoader());
                progress.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.atencao)
                    .setMessage(R.string.exception_json)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            AppController.getInstance().cancelPendingRequests();

        }
        catch (Exception e){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.atencao)
                    .setMessage(R.string.exception_response)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests();
    }

}
