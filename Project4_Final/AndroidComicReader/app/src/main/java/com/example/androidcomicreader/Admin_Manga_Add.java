package com.example.androidcomicreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MyImageAdapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Admin_Manga_Add extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Spinner spinner_image;
    private ArrayList<Comic> mComics;
    private MyImageAdapter mAdapter;
    EditText edit_Name, edit_Image;
    Button btn_Adds;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manga_add);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        //View
        btn_Adds = findViewById(R.id.button_insert_manga);

        edit_Name = findViewById(R.id.edt_name_addmanga);
        edit_Image = findViewById(R.id.edt_image);
        spinner_image = findViewById(R.id.spinner_image);

        initList();

        Spinner spinner_image = findViewById(R.id.spinner_image);

        mAdapter = new MyImageAdapter(this, mComics);
        spinner_image.setAdapter(mAdapter);

        spinner_image.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Comic clickedItem = (Comic) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getName();
                String clickedLink = clickedItem.getImage();
                edit_Image.setText(clickedLink);
                Toast.makeText(Admin_Manga_Add.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_Adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addManga(edit_Name.getText().toString(), edit_Image.getText().toString());
            }
        });
    }

    private void initList() {
        mComics = new ArrayList<>();
        mComics.add(new Comic("Overlord", "https://i3.wp.com/nhattruyenz.com/images/overlord.jpg",  R.drawable.overlord));
        mComics.add(new Comic("Call of the night", "https://cdn.myanimelist.net/images/anime/1045/123711.jpg",  R.drawable.callofnigjht));
        mComics.add(new Comic("Assasin Classroom", "https://cdn.myanimelist.net/images/anime/5/75639.jpg",  R.drawable.assasin));
        mComics.add(new Comic("Classroom of the elite", "https://truyenconect.com/uploads/story/2021/06/13/lop-hoc-biet-tuot.jpg",  R.drawable.classroom));
        mComics.add(new Comic("Sword Art Online", "https://m.media-amazon.com/images/M/MV5BYjY4MDU2YjMtNzY1MC00ODg1LWIwMzYtMWE5YTA3YTI4ZjMxXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_FMjpg_UX1000_.jpg",  R.drawable.swordartonline));
    }

    private void addManga(String name, String image) {
        if(name.equals("") || image.equals("")){
            Toast.makeText(Admin_Manga_Add.this,"Please input all form",Toast.LENGTH_SHORT).show();
        }else{
        // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);
            compositeDisposable
                    .add(myAPI.addManga(name,image)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_Manga_Add.this,""+s,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_Manga_Add.this, MainActivity.class));
                                }

                            }));
        }
    }
}