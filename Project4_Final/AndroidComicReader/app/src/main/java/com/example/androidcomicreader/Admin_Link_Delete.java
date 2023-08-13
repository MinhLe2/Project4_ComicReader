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
import com.example.androidcomicreader.Model.Link;
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

public class Admin_Link_Delete extends AppCompatActivity {
    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerDelete;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        setContentView(R.layout.activity_admin_link_delete);

        Retrofit retrofit = RetrofitClient.getInstance();

        spinnerDelete =(Spinner) findViewById(R.id.link_id_spinnerDelete);

        List<Link> links = new ArrayList<>();
        ArrayAdapter<Link> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,links);
    //    Intent intent = getIntent();
//        selectedChapter = (Chapter) intent.getExtras().get("selectedChapter");
        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        //Observable<List<Comic>> observable = myAPI.getComicList();
        //int test = Common.selected_chapter.getID();
        //lay chapterId de truyen sang nhằm select các dữ liệu chỉ ở trong truyện
        Call<List<Link>> call = myAPI.getImageList2(Common.selected_chapter.getID());

        call.enqueue(new Callback<List<Link>>() {
            @Override
            public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {
                if(response.isSuccessful()){
                    for(Link link : response.body()){
                        links.add(link);
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDelete.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Link>> call, Throwable t) {
                Toast.makeText(Admin_Link_Delete.this,""+call,Toast.LENGTH_SHORT).show();
            }
        });

//        deleteManga(1);
        btn_Delete = (Button) findViewById(R.id.button_deleteLink);
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Link selectedComic = (Link)spinnerDelete.getSelectedItem();
                int linkID = selectedComic.getID();
                deleteLink(linkID);
            }
        });
    }
    
    private void deleteLink(int id) {
        compositeDisposable
                .add(nodeAPI.deleteLink(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(Admin_Link_Delete.this,""+s,Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Admin_Link_Delete.this, ViewDetail.class));
                            }

                        }));
    }
}