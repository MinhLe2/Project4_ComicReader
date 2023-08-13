package com.example.androidcomicreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Admin_Manga_Delete extends AppCompatActivity {
    //http://localhost:3000/comic

    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerManga;
    private Comic selectedComic;
    Retrofit retrofit = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Name, edit_Image;
    Button btn_delete;

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
        setContentView(R.layout.activity_admin_manga_delete);
        myAPI = retrofit.create(IComicAPI.class);
       nodeAPI = retrofit.create(INodeJS.class);

//        spinnerManga =(Spinner) findViewById(R.id.manga_id_spinner);

//        List<Comic> comics = new ArrayList<>();
//
//        ArrayAdapter<Comic> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,comics);
//        myAPI = retrofit.create(IComicAPI.class);
//        nodeAPI = retrofit.create(INodeJS.class);
//        IComicAPI iComicAPI =myAPI;
//        //Observable<List<Comic>> observable = myAPI.getComicList();
//        Call<List<Comic>> call = myAPI.getComicList2();

        Intent intent = getIntent();
        selectedComic = (Comic) intent.getExtras().get("selectedComic");

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
//                Toast.makeText(Admin_Manga_Delete.this,""+call,Toast.LENGTH_SHORT).show();
//            }
//        });

//        deleteManga(1);
        btn_delete = (Button) findViewById(R.id.button_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Comic selectedComic = (Comic)spinnerManga.getSelectedItem();
//                int comicID = selectedComic.getID();
                deleteManga(selectedComic.getID());
            }
        });
    }

    private void deleteManga(int id) {
            compositeDisposable
                    .add(nodeAPI.deleteManga(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_Manga_Delete.this,""+s,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_Manga_Delete.this, MainActivity.class));
                                }

                            }));
        }

    }