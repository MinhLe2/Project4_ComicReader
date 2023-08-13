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

public class Admin_Chapter_Delete extends AppCompatActivity {

    IComicAPI myAPI;
    INodeJS nodeAPI;
    //Spinner spinnerDelete;
    Retrofit retrofit = RetrofitClient.getInstance();
    private Chapter selectedChapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Name;
    Button btn_Delete;

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
        setContentView(R.layout.activity_admin_chapter_delete);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        Intent intent = getIntent();
        selectedChapter = (Chapter) intent.getExtras().get("selectedChapter");

//        deleteManga(1);
        btn_Delete = (Button) findViewById(R.id.button_delete);
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Chapter selectedChapter = (Chapter)spinnerDelete.getSelectedItem();
//                int chapterID = selectedChapter.getID();
                deleteChapter(selectedChapter.getID());
            }
        });
    }
    private void deleteChapter(int id) {

        compositeDisposable
                .add(nodeAPI.deleteChapter(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(Admin_Chapter_Delete.this,""+s,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Admin_Chapter_Delete.this, ChapterActivity.class));
                            }

                        }));
    }
}