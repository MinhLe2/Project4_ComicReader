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

import com.example.androidcomicreader.Adapter.MyLinkAdapter;
import com.example.androidcomicreader.Common.Common;
import com.example.androidcomicreader.Model.Category;
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

public class Admin_mCategory_Add extends AppCompatActivity {
    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerMangaId,spinnerCategoryId;
    Retrofit retrofit = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_mangaId;
    Button btn_Add;

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
        setContentView(R.layout.activity_admin_mcategory_add);

        //edt_mangaId = findViewById(R.id.edt_mangaid);

        //id from
        //spinnerChapterId =(Spinner) findViewById(R.id.link_id_chapter);

        //spinnerMangaId =(Spinner) findViewById(R.id.manga_id);
        spinnerCategoryId = (Spinner)findViewById(R.id.category_id);

        //List<Comic> comics = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        //ArrayAdapter<Comic> adapterComic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,comics);
        ArrayAdapter<Category> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        //Observable<List<Comic>> observable = myAPI.getComicList();

        //Call<List<Comic>> callComic = myAPI.getComicList2();

        Call<List<Category>> callCategory = myAPI.getCategoryList2();
        //Call<List<Link>> callLink = myAPI.getImageList2(Common.selected_chapter.getID());
        //String chapterName =  Common.selected_chapter.getName();

//        callComic.enqueue(new Callback<List<Comic>>() {
//            @Override
//            public void onResponse(Call<List<Comic>> callComic, Response<List<Comic>> response) {
//                if(response.isSuccessful()){
//
//                    for(Comic comic : response.body()){
//                        comics.add(comic);
//                    }
//                    adapterComic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinnerMangaId.setAdapter(adapterComic);
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Comic>> call, Throwable t) {
//                Toast.makeText(Admin_mCategory_Add.this,""+call,Toast.LENGTH_SHORT).show();
//            }
//        });

        callCategory.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> callCategory, Response<List<Category>> response) {
                if(response.isSuccessful()){

                    for(Category category : response.body()){
                        categories.add(category);
                    }
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoryId.setAdapter(adapterCategory);
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(Admin_mCategory_Add.this,""+call,Toast.LENGTH_SHORT).show();
            }
        });


//        deleteManga(1);
        btn_Add = (Button) findViewById(R.id.button_Adds);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category selectedCategory = (Category)spinnerCategoryId.getSelectedItem();
                //Comic selectedComic = (Comic) spinnerMangaId.getSelectedItem();
                int categoryID = selectedCategory.getID();
                //int comicID = selectedComic.getID();
                int mangaID = Common.selected_comic.getID();
                addMCategory(categoryID,mangaID);
            }
        });


    }
    private void addMCategory(int categoryID, int comicID) {
        if(categoryID == 0 || comicID == 0){
            Toast.makeText(Admin_mCategory_Add.this,"Please input all form",Toast.LENGTH_SHORT).show();;
        }else{
            // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);
            compositeDisposable
                    .add(nodeAPI.addmangacategory(comicID,categoryID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_mCategory_Add.this,""+s,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_mCategory_Add.this, MainActivity.class));
                                }

                            }));
        }
    }
}