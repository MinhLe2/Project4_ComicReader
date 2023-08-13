package com.example.androidcomicreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidcomicreader.Model.Link;
import com.example.androidcomicreader.R;

import java.util.ArrayList;

public class MyLinkAdapter extends ArrayAdapter<Link> {
    public MyLinkAdapter(Context context, ArrayList<Link> linkArrayList) {
        super(context, 0, linkArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layoutlink, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        Link currentItem = getItem(position);

        if (currentItem != null) {
            //Picasso.get().load("https://i3.wp.com/nhattruyenz.com/images/mob-psycho-100.jpg").into(imageViewFlag);
            textViewName.setText(currentItem.getLink());
            imageViewFlag.setImageResource(currentItem.getmFlagImage());
            //imageViewFlag.
        }

        return convertView;
    }
}
