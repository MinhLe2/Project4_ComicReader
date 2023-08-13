package com.example.androidcomicreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MultipleChooseAdapter;
import com.example.androidcomicreader.Adapter.MyChapterAdapter;
import com.example.androidcomicreader.Adapter.MyComicAdapter;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryFilter extends AppCompatActivity {
    Button btn_filter,btn_search;
    IComicAPI iComicAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_filter;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);
        iComicAPI = Common.getAPI();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fetchCategory(); // load tat ca Category from server
            }
        });

        //View
        btn_filter = (Button) findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog
                showOptionDialog();
            }
        });
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Dialog
                showSearchDialog();
            }
        });

        recycler_filter = (RecyclerView) findViewById(R.id.recycler_filter);
        recycler_filter.setHasFixedSize(true);
        recycler_filter.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryFilter.this);
        alertDialog.setTitle("Search Comic");

        LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search,null);

        EditText edt_search = (EditText)search_layout.findViewById(R.id.edt_search);

        alertDialog.setView(search_layout);

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchSearchComic(edt_search.getText().toString());
            }
        });
        alertDialog.show();
    }

    private void fetchSearchComic(String search) {
        compositeDisposable.add(iComicAPI.getSearchedComic(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new MyComicAdapter(CategoryFilter.this,comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this, "No Comic Found", Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    private void showOptionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryFilter.this);
        alertDialog.setTitle("Select Category");

        LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options,null);

        RecyclerView recycler_options = (RecyclerView) filter_layout.findViewById(R.id.recycler_options);
        recycler_options.setHasFixedSize(true);
        recycler_options.setLayoutManager(new LinearLayoutManager(this));
        final MultipleChooseAdapter adapter = new MultipleChooseAdapter(getBaseContext(),Common.categories);
        recycler_options.setAdapter(adapter);

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchFilterCategory(adapter.getFilterArray());
            }
        });

        alertDialog.show();

    }

    private void fetchFilterCategory(String data) {
        compositeDisposable.add(iComicAPI.getFilteredComic(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new MyComicAdapter(CategoryFilter.this,comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this,"No comic Found",Toast.LENGTH_SHORT).show();
                    }
                }));

//        compositeDisposable.add(iComicAPI.getSearchedComic(search)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Comic>>() {
//                    @Override
//                    public void accept(List<Comic> comics) throws Exception {
//                        recycler_filter.setVisibility(View.VISIBLE);
//                        recycler_filter.setAdapter(new MyComicAdapter(getBaseContext(),comics));
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        recycler_filter.setVisibility(View.INVISIBLE);
//                        Toast.makeText(CategoryFilter.this, "No Comic Found", Toast.LENGTH_SHORT).show();
//                    }
//                }));
    }

    private void fetchCategory(){
        compositeDisposable.add(iComicAPI.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        Common.categories = categories;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CategoryFilter.this, "Load Categories Error", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}