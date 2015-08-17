package com.jpataide.project.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.R;
import com.jpataide.project.adapter.LivroAdapter;
import com.jpataide.project.data.Livro;
import com.jpataide.project.utils.AppController;
import com.jpataide.project.utils.LivroUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener, IServerHandler {
    private ListView listItens;
    private EditText edtBusca;
    private TextView txtExibindo;
    private Button btnBuscar;
    private LivroAdapter livroAdapter = null;
    private String query;
    private LivroUtils livroUtilsInstance;
    private final String TEXTO_EXIBINDO = "Exibindo %d de %d";
    public static final String INTENT_URL = "LIVRO_URL";
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listItens = (ListView) findViewById(R.id.lst_lista);
        edtBusca = (EditText) findViewById(R.id.edt_busca);
        btnBuscar = (Button) findViewById(R.id.btn_busca);
        txtExibindo = (TextView) findViewById(R.id.txt_exibindo);
        btnBuscar.setOnClickListener(this);
        progress = findViewById(R.id.progressBar);

        listItens.addFooterView(getRodape());


        listItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LivroActivity.class);
                Livro item = livroAdapter.getItem(position);
                intent.putExtra(INTENT_URL, item.getSelfLink());
                startActivity(intent);
            }
        });

        query = edtBusca.getText().toString();
        livroUtilsInstance = AppController.getInstance().getLivroUtils();

        livroUtilsInstance.setQuery(edtBusca.getText().toString());

        txtExibindo.setText(String.format(TEXTO_EXIBINDO,0,0));

        livroUtilsInstance.refreshList(this, query);
    }

    @Override
    public void onResponse(JSONObject response) {
        progress.setVisibility(View.GONE);
        List<Livro> livros = new ArrayList<Livro>();

        try {
            livros = livroUtilsInstance.getList(response);

        } catch (Exception e){
            e.printStackTrace();
        }

        if(livroAdapter == null){
            livroAdapter = new LivroAdapter(this,livros);
            listItens.setAdapter(livroAdapter);
        } else {
            livroAdapter.notifyDataSetChanged();
        }
        txtExibindo.setText(String.format(TEXTO_EXIBINDO, livroUtilsInstance.getCurrentIndex(), livroUtilsInstance.getTotalItems()));
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        AppController.getInstance().cancelPendingRequests();
        Toast.makeText(this, "Erro!",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_busca){
            query = edtBusca.getText().toString();
            livroUtilsInstance.refreshList(this, query);
        }

    }

    private ProgressBar getRodape(){
        ProgressBar pb = new ProgressBar(getBaseContext());
        pb.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));
        return pb;
    }

    public void connectToServer(String url){

        JsonObjectRequest jsObjRequest =
                new JsonObjectRequest(
                        Request.Method.GET, // Requisição via HTTP_GET
                        url,   // url da requisição
                        null,  // JSONObject a ser enviado via POST
                        this,  // Response.Listener
                        this); // Response.ErrorListener

        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }
}