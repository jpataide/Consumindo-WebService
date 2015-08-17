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
import com.jpataide.project.objects.Item;
import com.jpataide.project.utils.AppController;

import java.util.List;

/**
 * Created by jpataide on 8/16/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    static final int LAYOUT = R.layout.item_lista;

    public ItemAdapter(Context context,
                       List<Item> objects) {

        super(context, LAYOUT, objects);
    }

    @Override
    public View getView(int position,
                        View convertView, ViewGroup parent) {

        Context ctx = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.item_lista, null);
        }
        NetworkImageView img = (NetworkImageView)
                convertView.findViewById(R.id.img);
        TextView txt = (TextView)
                convertView.findViewById(R.id.txt_Nome);

        Item item = getItem(position);
        txt.setText(item.getVolumeInfo().getTitle());

        if (item.getVolumeInfo().getImageLinks() != null) {
            img.setImageUrl(
                    item.getVolumeInfo().getImageLinks().getSmallThumbnail(),
                    AppController.getInstance().getImageLoader());
        } else {
            img.setImageUrl(
                    "",
                    AppController.getInstance().getImageLoader());
        }

        if (position == getCount() - 1) {
            AppController.getInstance().getLivroUtils().refreshList((IServerHandler) getContext());
        }

        return convertView;
    }


}