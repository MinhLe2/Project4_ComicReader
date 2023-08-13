package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MyChapterAdapter;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Retrofit.IComicAPI;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChapterActivity extends AppCompatActivity {

    IComicAPI iComicAPI;
    RecyclerView recycler_chapter;
    SharedPreferences sharedPreferences;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_chapter;
    Button btn_Insert;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        //Init API
        iComicAPI = Common.getAPI();

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = sharedPreferences.getString("NAME", "");

        //View
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selected_comic.getName());
        //Set icon for toolbar
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp);
        //toolbar.setNavigationIcon(R.drawable.ic_remove_circle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChapterActivity.this,MainActivity.class)); // go back
            }
        });

        Spinner mySpinner = (Spinner) findViewById(R.id.fab);

        if (name.equals("Admin")) {
            //mySpinner.setEnabled(name.equals("Admin"));
        } else {
            mySpinner.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ChapterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.optionT));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String test = mySpinner.getSelectedItem().toString();
                if (test.equals("Add Chapter")){
                    Toast.makeText(ChapterActivity.this, "add manga is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChapterActivity.this,Admin_Chapter_Add.class));
                }else if (test.equals("Add Manga Category")){
                    Toast.makeText(ChapterActivity.this, "Add manga category is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChapterActivity.this,Admin_mCategory_Add.class));
                }else if (test.equals("Delete Manga Category")){
                    Toast.makeText(ChapterActivity.this, "delete manga category is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChapterActivity.this,Admin_mCategory_Delete.class));
                }else if(test.equals("Update Manga Category")){
                    Toast.makeText(ChapterActivity.this, "update manga category is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChapterActivity.this,Admin_mCategory_Update.class));
                }else{
                    Toast.makeText(ChapterActivity.this, "No item selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recycler_chapter = (RecyclerView) findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        txt_chapter = (TextView) findViewById(R.id.txt_chapter);
        //iComicAPI = Common.getAPI();
        fetchChapter(Common.selected_comic.getID());
        //getComicId(Common.selected_comic.getID());
    }

    private void fetchChapter(int comicId) { //comicId = Comon.selected_comic o trong Common.java
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Please Wait...").setCancelable(false).build();
        dialog.show();

        compositeDisposable.add(iComicAPI.getChapterList(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Chapter>>() {
                    @Override
                    public void accept(List<Chapter> chapters) throws Exception {
                        Common.chapterList = chapters;// save chapter to back,next
                        //getBaseContext()
                        //ChapterActivity.this
                        recycler_chapter.setAdapter(new MyChapterAdapter(ChapterActivity.this, chapters));
                        //recycler_chapter.setAdapter(new MyChapterUpdateAdapter(ChapterActivity.this, chapters));
                        txt_chapter.setText(new StringBuilder("CHAPTER (")
                                .append(chapters.size())
                                .append(")"));
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ChapterActivity.this, "Error While Loading Chapter", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }));
    }
}