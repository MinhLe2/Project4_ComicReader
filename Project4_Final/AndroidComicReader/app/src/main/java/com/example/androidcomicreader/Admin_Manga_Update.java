package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MyImageAdapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Admin_Manga_Update extends AppCompatActivity {

    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerManga;
    private ArrayList<Comic> mComics;
    private MyImageAdapter mAdapter;
    Retrofit retrofit = RetrofitClient.getInstance();
    private Comic selectedComics;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Name, edit_Image;
    Button button_Update;

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
        setContentView(R.layout.activity_admin_manga_update);

        edit_Name = findViewById(R.id.edt_name);
        edit_Image = findViewById(R.id.edt_image);

        spinnerManga = findViewById(R.id.spinner_image);

        initList();

        mAdapter = new MyImageAdapter(this, mComics);
        spinnerManga.setAdapter(mAdapter);

        spinnerManga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Comic clickedItem = (Comic) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getName();
                String clickedLink = clickedItem.getImage();
                edit_Image.setText(clickedLink);
                Toast.makeText(Admin_Manga_Update.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        spinnerManga =(Spinner) findViewById(R.id.manga_id_updateManga_spinner);

//        List<Comic> comics = new ArrayList<>();
//        ArrayAdapter<Comic> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,comics);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        selectedComics = (Comic) intent.getExtras().get("selectedComic");

        //Observable<List<Comic>> observable = myAPI.getComicList();
//        Call<List<Comic>> call = myAPI.getComicList2();
//
//        call.enqueue(new Callback<List<Comic>>() {
//            @Override
//            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
//                if(response.isSuccessful()){
//
//                    for(Comic comic : response.body()){
//                        comics.add(comic);
//                    }
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinnerManga.setAdapter(adapter);
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Comic>> call, Throwable t) {
//                Toast.makeText(Admin_Manga_Update.this,""+call,Toast.LENGTH_SHORT).show();
//            }
//        });

        button_Update = findViewById(R.id.button_update);
        button_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Comic selectedComic = (Comic)spinnerManga.getSelectedItem();
                //int comicID = selectedComic.getID();

//                String comicName = selectedComic.getName();
//                String comicImage = selectedComic.getImage();
//                edit_Name.setText(comicName);
//                edit_Name.setText(comicImage);
                updateManga(selectedComics.getID(),edit_Name.getText().toString(),edit_Image.getText().toString());
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

    private void updateManga(int id, String name, String image) {
        if( id == 0|| name.equals("") || image.equals("")){
            Toast.makeText(Admin_Manga_Update.this,"Please input all form",Toast.LENGTH_SHORT).show();
        }else{
        compositeDisposable
                .add(nodeAPI.updateManga(id,name,image)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(Admin_Manga_Update.this,""+s,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Admin_Manga_Update.this, MainActivity.class));
                            }

                        }));
        }
    }
}