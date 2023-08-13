package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MyViewPagerAdapter;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Link;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewDetail extends AppCompatActivity {
    IComicAPI iComicAPI;
    ViewPager myViewPager;
    SharedPreferences sharedPreferences;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_chapter_name;
    View back,next;


    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);
        String name = sharedPreferences.getString("NAME", "");

        Spinner mySpinner = (Spinner) findViewById(R.id.fabLink);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ViewDetail.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.optionLink));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        if (name.equals("Admin")) {
            //mySpinner.setEnabled(name.equals("Admin"));
        } else {
            mySpinner.setVisibility(View.INVISIBLE);
        }

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String test = mySpinner.getSelectedItem().toString();
                if (test.equals("AddLink")) {
                    Toast.makeText(ViewDetail.this, "add link is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewDetail.this, Admin_Link_Add.class));
                } else if (test.equals("DeleteLink")) {
                    Toast.makeText(ViewDetail.this, "delete link is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewDetail.this, Admin_Link_Delete.class));
                } else if (test.equals("UpdateLink")) {
                    Toast.makeText(ViewDetail.this, "update link is selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ViewDetail.this, Admin_Link_Update.class));
                } else {
                    Toast.makeText(ViewDetail.this, "No item selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iComicAPI = Common.getAPI();
        myViewPager = (ViewPager) findViewById(R.id.view_pager);
        txt_chapter_name = (TextView) findViewById(R.id.txt_chapter_name);
        back = findViewById(R.id.chapter_back);
        next = findViewById(R.id.chapter_next);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapter_index == 0) // neu user trong first chapter nhung lai press back
                {
                    Toast.makeText(ViewDetail.this, "You are reading the first chapter", Toast.LENGTH_SHORT).show();
                }else {
                    Common.chapter_index--;
                    //txt_chapter_name.setText(Common.formatString(Common.chapterList.get(Common.chapter_index).getName()));
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapter_index == Common.chapterList.size() - 1) // neu user trong last chapter nhung lai press next
                {
                    Toast.makeText(ViewDetail.this, "You are reading the last chapter", Toast.LENGTH_SHORT).show();

                }else {
                    Common.chapter_index++;
                    //txt_chapter_name.setText(Common.formatString(Common.chapterList.get(Common.chapter_index).getName()));
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });
        fetchLinks(Common.selected_chapter.getID());
    }

    private void fetchLinks(int id) {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Please Wait...").setCancelable(false).build();
        dialog.show();

        compositeDisposable.add(iComicAPI.getImageList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
                        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getBaseContext(), links);
                        myViewPager.setAdapter(adapter);
                        //txt_chapter_name.setText(Common.formatString(Common.selected_chapter.getName()));
                        txt_chapter_name.setText(Common.formatString(Common.chapterList.get(Common.chapter_index).getName()));
                        //Tao hieu ung Book Flip Page
                        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                        bookFlipPageTransformer.setScaleAmountPercent(20f);
                        myViewPager.setPageTransformer(true, bookFlipPageTransformer);

                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ViewDetail.this,"This chapter is being Translating",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }));
    }
}