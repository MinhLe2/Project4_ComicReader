package com.example.androidcomicreader.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcomicreader.Admin_Chapter_Delete;
import com.example.androidcomicreader.Admin_Chapter_Update;
import com.example.androidcomicreader.Admin_Link_Delete;
import com.example.androidcomicreader.Admin_Link_Update;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Interface.IRecyclerOnClick;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.R;
import com.example.androidcomicreader.ViewDetail;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder> {
    Context context;

    public static List<Chapter> chapterList;
    //Intent myIntent = new Intent();

    public MyChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context= context;
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.chapter_item,viewGroup,false);
        return new MyViewHolder(itemView);
    }

//    protected void DeleteBtn(){
//        Button delete_btn;
//        delete_btn =  findViewById(R.id.btn_delete);
//        delete_btn.setOnClickListener();
//    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Picasso.get().load(chapterLists.get(position).getMangaID()).into(holder.imageView);


        holder.txt_chapter_number.setText(new StringBuilder(chapterList.get(position).Name));
        Common.selected_chapter = chapterList.get(position);
        //Common.chapter_index = position;
        Common.chapter_index = holder.getAdapterPosition();



        //start new activity
        //context.startActivity(new Intent(context,ViewDetail.class));
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {
                Common.selected_chapter = chapterList.get(position);
                Common.chapter_index = position;

                MyChapterAdapter.this.context.startActivity(new Intent(context,ViewDetail.class));


            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_chapter_number;
        Button btn_delete, btn_update;
        IRecyclerOnClick iRecyclerOnClick;


        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_chapter_number = (TextView)itemView.findViewById(R.id.txt_chapter_number);
            btn_delete = (Button)itemView.findViewById(R.id.btn_delete);
            btn_update = (Button)itemView.findViewById(R.id.btn_update);    

            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
            String name = sharedPreferences.getString("NAME", "");

            if (name.equals("Admin")) {
                //mySpinner.setEnabled(name.equals("Admin"));
            } else {
                btn_delete.setVisibility(View.INVISIBLE);
                btn_update.setVisibility(View.INVISIBLE);
            }

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Common.selected_chapter = chapterList.get(position);
//                    Common.chapter_index = position;
                    Intent intent = new Intent(context, Admin_Chapter_Delete.class);
                    intent.putExtra("selectedChapter",
                            (Chapter)chapterList.get(getAdapterPosition()));
                    MyChapterAdapter.this.context.startActivity(intent);


                }
            });
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Admin_Chapter_Update.class);
                    intent.putExtra("selectedChapter",
                            (Chapter)chapterList.get(getAdapterPosition()));
                    MyChapterAdapter.this.context.startActivity(intent);
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerOnClick.onClick(v,getAdapterPosition());
        }
    }
}
