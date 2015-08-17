package com.jpataide.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jpataide.project.Interfaces.IServerHandler;
import com.jpataide.project.R;
import com.jpataide.project.data.Livro;
import com.jpataide.project.utils.AppController;
import com.jpataide.project.utils.LivroUtils;

import java.util.List;

/**
 * Created by jpataide on 8/16/15.
 */
public class LivroAdapter extends ArrayAdapter<Livro> {

    static final int LAYOUT = R.layout.item_lista;

    public LivroAdapter(Context context,
                        List<Livro> objects) {

        super(context, LAYOUT, objects);
    }

    @Override
    public View getView(int position,
                        View convertView, ViewGroup parent) {

        Context ctx = parent.getContext();
        if (convertView == null){
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.item_lista, null);
        }
        NetworkImageView img = (NetworkImageView)
                convertView.findViewById(R.id.img);
        TextView txt = (TextView)
                convertView.findViewById(R.id.txt_Nome);

        Livro item = getItem(position);
        txt.setText(item.getNome());
        img.setImageUrl(
                item.getImageUrl(),
                AppController.getInstance().getImageLoader());

        if(position == getCount() - 1){
            AppController.getInstance().getLivroUtils().refreshList((IServerHandler) getContext());
        }

        return convertView;
    }


}