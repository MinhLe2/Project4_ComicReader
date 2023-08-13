package com.example.androidcomicreader.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MultipleChooseAdapter extends RecyclerView.Adapter<MultipleChooseAdapter.MyViewHolder> {

    Context context;
    List<Category> categoryList;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    List<Category> selected_category = new ArrayList<>();

    public MultipleChooseAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.check_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ckb_options.setText(categoryList.get(position).getName());
        holder.ckb_options.setChecked(itemStateArray.get(position));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public String getFilterArray(){
        List<Integer> id_selected = new ArrayList<>();
        for (Category category:selected_category)
            id_selected.add(category.getID());
        return new Gson().toJson(id_selected);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox ckb_options;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ckb_options = (CheckBox)itemView.findViewById(R.id.check_options);
            ckb_options.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapterPosition = getAdapterPosition();
                    itemStateArray.put(adapterPosition,isChecked);
                    if (isChecked)
                        selected_category.add(categoryList.get(adapterPosition));
                    else
                        selected_category.remove(categoryList.get(adapterPosition));
                }
            });
        }
    }
}
