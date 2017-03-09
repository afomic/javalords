package com.afomic.javalords.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afomic.javalords.R;
import com.afomic.javalords.models.Developer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by afomic on 08-Mar-17.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Developer> developers;
    public ListAdapter(Context context, ArrayList<Developer> developers){
        this.context=context;
        this.developers=developers;
    }

    @Override
    public int getCount() {
        return developers.size();
    }

    @Override
    public Developer getItem(int position) {
        return developers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
            holder=new Holder();
            holder.developerThumbnail=(ImageView) convertView.findViewById(R.id.thumbnail);
            holder.developerUsername=(TextView) convertView.findViewById(R.id.developer_username);
            convertView.setTag(holder);
        }else {
            holder=(Holder) convertView.getTag();
        }
        Developer item=getItem(position);
        holder.developerUsername.setText(item.getUsername());
        Glide.with(context).load(item.getProfileImageURL())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.developerThumbnail);
        return convertView;
    }
    private class Holder{
        ImageView developerThumbnail;
        TextView developerUsername;
    }
}
