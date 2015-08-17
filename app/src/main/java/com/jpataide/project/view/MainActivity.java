package com.jpataide.project.view;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.R;
import com.jpataide.project.adapter.ItemAdapter;
import com.jpataide.project.data.Item;
import com.jpataide.project.utils.AppController;
import com.jpataide.project.utils.LivroUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements Response.Listener<JSONObject>, Response.ErrorListener, IServerHandler, SearchView.OnQueryTextListener {
    private ListView listItens;
    private TextView txtExibindo;
    private ItemAdapter livroAdapter = null;
    private LivroUtils livroUtilsInstance;
    private final String TEXTO_EXIBINDO = "Exibindo %d de %d";
    public static final String INTENT_URL = "LIVRO_URL";
    private View progress;
    ProgressBar pbRodape;
    private String lastQuery = "";
    List<Item> livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listItens = (ListView) findViewById(R.id.lst_lista);
        txtExibindo = (TextView) findViewById(R.id.txt_exibindo);
        progress = findViewById(R.id.progressBar);

        listItens.addFooterView(getRodape());


        listItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LivroActivity.class);
                Item item = livroAdapter.getItem(position);
                intent.putExtra(INTENT_URL, item.getSelfLink());
                startActivity(intent);
            }
        });

        livroUtilsInstance = AppController.getInstance().getLivroUtils();

        txtExibindo.setText(String.format(TEXTO_EXIBINDO, 0, 0));
        livros = new ArrayList<>();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onResponse(JSONObject response) {
        progress.setVisibility(View.GONE);

        try {
            livros = livroUtilsInstance.getList(response);

            if (livroUtilsInstance.isEndOfList()) {
                pbRodape.setVisibility(View.GONE);
            } else {
                pbRodape.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.atencao)
                    .setMessage(R.string.exception_json)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            AppController.getInstance().cancelPendingRequests();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.atencao)
                    .setMessage(R.string.exception_response)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            e.printStackTrace();
        }

        if (livroAdapter == null) {
            livroAdapter = new ItemAdapter(this, livros);
            listItens.setAdapter(livroAdapter);
        } else {
            livroAdapter.notifyDataSetChanged();
        }
        txtExibindo.setText(String.format(TEXTO_EXIBINDO, livroUtilsInstance.getCurrentIndex(), livroUtilsInstance.getTotalItems()));
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

    private ProgressBar getRodape() {
        pbRodape = new ProgressBar(getBaseContext());
        pbRodape.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));
        return pbRodape;
    }

    public void connectToServer(String url) {

        JsonObjectRequest jsObjRequest =
                new JsonObjectRequest(
                        Request.Method.GET, // Requisição via HTTP_GET
                        url,   // url da requisição
                        null,  // JSONObject a ser enviado via POST
                        this,  // Response.Listener
                        this); // Response.ErrorListener

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setQuery("android",true);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals(lastQuery)) {
            livroUtilsInstance.refreshList(this, query);
            lastQuery = query;
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}