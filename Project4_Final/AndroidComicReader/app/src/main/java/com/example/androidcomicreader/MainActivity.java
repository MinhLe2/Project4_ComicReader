package com.example.androidcomicreader;

import static com.example.androidcomicreader.Adapter.MyComicAdapter.comicList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcomicreader.Adapter.MyChapterAdapter;
import com.example.androidcomicreader.Adapter.MyComicAdapter;
import com.example.androidcomicreader.Adapter.MySliderAdapter;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Banner;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;
import com.example.androidcomicreader.Service.PicassoImageLoadingService;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {
    Slider slider;
    IComicAPI iComicAPI;
    SharedPreferences sharedPreferences;

//    public static Button btn_deleteManga,btn_updateManaga;

    static ViewGroup viewGroup;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_comic;
    TextView txt_comic,txt_User;

    SwipeRefreshLayout swipeRefreshLayout;
    MyComicAdapter myComicAdapter = new MyComicAdapter(MainActivity.this, comicList);

    ImageView btn_search;

    @Override
    protected void onStop(){
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
        setContentView(R.layout.activity_main);
        txt_User = findViewById(R.id.text_user);

        sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);

        String name = sharedPreferences.getString("NAME", "");
        txt_User.setText(name);


//        Spinner mySpinner = (Spinner)findViewById(R.id.spinner_menu);
        Button btnInsert = (Button) findViewById(R.id.button_Add);
        if(name.equals("Admin")){
            //mySpinner.setEnabled(name.equals("Admin"));
        }else{
            btnInsert.setVisibility(View.INVISIBLE);
        }

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Admin_Manga_Add.class));
            }
        });

                //init API
                iComicAPI = Common.getAPI();
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

//    txt_comic.setEnabled(false);
//    txt_comic.setAlpha(0);


    //View
    btn_search = (ImageView) findViewById(R.id.btn_filter);
    btn_search.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,CategoryFilter.class));
        }
    });

        txt_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(MainActivity.this, Login_Register.class);
                startActivity(intent);
            }
        });

    swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
    swipeRefreshLayout.setColorSchemeResources(R.color.purple_200,
            android.R.color.holo_green_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                fetchBanner();

                fetchComic();
            }else {
                Toast.makeText(MainActivity.this, "Cannot Connect to internet", Toast.LENGTH_SHORT).show();
            }

        }
    });

    //Default, load first time
    swipeRefreshLayout.post(new Runnable() {
        @Override
        public void run() {
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                fetchBanner();

                fetchComic();
            }else {
                Toast.makeText(MainActivity.this, "Cannot Connect to internet", Toast.LENGTH_SHORT).show();
            }
        }
    });



    slider = (Slider) findViewById(R.id.banner_slider);
    Slider.init(new PicassoImageLoadingService());

    recycler_comic = (RecyclerView)findViewById(R.id.recycler_comic);
    recycler_comic.setHasFixedSize(true);
    recycler_comic.setLayoutManager(new GridLayoutManager(this,2));

    txt_comic = (TextView) findViewById(R.id.txt_comic);


    }



    private void fetchComic() {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Please Wait...").setCancelable(false).build();
        if (!swipeRefreshLayout.isRefreshing())
            dialog.show();

        compositeDisposable.add(iComicAPI.getComicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {

                        recycler_comic.setAdapter(new MyComicAdapter(MainActivity.this, comics));
                        txt_comic.setText(new StringBuilder("NEW COMIC (")
                                .append(comics.size())
                                .append(")"));
                        if (!swipeRefreshLayout.isRefreshing())
                            dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!swipeRefreshLayout.isRefreshing())
                            dialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this,"Error while load Comics",Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void fetchBanner() {

        compositeDisposable.add(iComicAPI.getBannerList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                slider.setAdapter(new MySliderAdapter(banners));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(MainActivity.this, "Error while loading banner", Toast.LENGTH_SHORT).show();
            }
        }));
    }


}