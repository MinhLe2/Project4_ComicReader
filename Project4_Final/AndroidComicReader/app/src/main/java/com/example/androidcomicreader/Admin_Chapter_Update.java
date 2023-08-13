package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Chapter;
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

public class Admin_Chapter_Update extends AppCompatActivity {

    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerMangaId;
    Retrofit retrofit = RetrofitClient.getInstance();
    private Chapter selectedChapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Name;
    Button btn_Update;

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
        setContentView(R.layout.activity_admin_chapter_update);

        edit_Name = findViewById(R.id.edt_name);
        //id from
//        spinnerMangaId =(Spinner) findViewById(R.id.chapter_id_manga);
        //spinnerChapterId =(Spinner) findViewById(R.id.chapter_id);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        selectedChapter = (Chapter) intent.getExtras().get("selectedChapter");

//        deleteManga(1);
        btn_Update = (Button) findViewById(R.id.button_Update);
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comic selectedComic = (Comic)spinnerMangaId.getSelectedItem();
                int mangaID = Common.selected_comic.getID();
                //int mangaID = selectedComic.getID();
                //chapter primary id
                int mangaChapterID = selectedChapter.getID();
                updateChapter(mangaChapterID,edit_Name.getText().toString(),mangaID);
            }
        });
    }

    private void updateChapter(int id,String name, int mangaid) {
        if(id == 0 ||name.equals("") || mangaid == 0){
            Toast.makeText(Admin_Chapter_Update.this,"Please input all form",Toast.LENGTH_SHORT).show();
        }else{
        // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);
        compositeDisposable
                .add(nodeAPI.updateChapter(id,name,mangaid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(Admin_Chapter_Update.this,""+s,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Admin_Chapter_Update.this, ChapterActivity.class));
                            }

                        }));
        }
    }

}