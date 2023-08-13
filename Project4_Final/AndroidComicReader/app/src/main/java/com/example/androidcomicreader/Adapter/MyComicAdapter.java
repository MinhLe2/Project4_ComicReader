package com.example.androidcomicreader.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcomicreader.Admin_Chapter_Delete;
import com.example.androidcomicreader.Admin_Chapter_Update;
import com.example.androidcomicreader.Admin_Manga_Delete;
import com.example.androidcomicreader.Admin_Manga_Update;
import com.example.androidcomicreader.ChapterActivity;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {
    Context context;
    public static List<Comic> comicList;
    static ViewGroup viewGroup;
    //Intent myIntent = new Intent();
    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.comic_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(comicList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(comicList.get(position).getName());
        //phai nho them no vi khong them khi an vao comic se bi crash
        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {
                //bat dau tao moi activity
                context.startActivity(new Intent(context, ChapterActivity.class));
                Common.selected_comic = comicList.get(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        Button delete_button,update_button;

        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            textView = (TextView) itemView.findViewById(R.id.manga_name);

            delete_button = (Button)itemView.findViewById(R.id.btn_deleteComic);
            update_button = (Button)itemView.findViewById(R.id.btn_updateComic);
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
            String name = sharedPreferences.getString("NAME", "");

            if (name.equals("Admin")) {
                //mySpinner.setEnabled(name.equals("Admin"));
            } else {
                delete_button.setVisibility(View.INVISIBLE);
                update_button.setVisibility(View.INVISIBLE);
            }

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Common.selected_chapter = chapterList.get(position);
//                    Common.chapter_index = position;
                    Intent intent = new Intent(context, Admin_Manga_Delete.class);
                    intent.putExtra("selectedComic", (Comic)comicList.get(getAdapterPosition()));
                    MyComicAdapter.this.context.startActivity(intent);


                }
            });
            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Admin_Manga_Update.class);
                    intent.putExtra("selectedComic",
                            (Comic) comicList.get(getAdapterPosition()));
                    MyComicAdapter.this.context.startActivity(intent);
                }
            });

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerOnClick.onClick(v,getAdapterPosition());
//            Intent myIntent = new Intent();
//            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
}
