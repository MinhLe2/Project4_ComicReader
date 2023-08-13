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

import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Model.MangaCategory;
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

public class Admin_mCategory_Update extends AppCompatActivity {

    IComicAPI myAPI;
    INodeJS nodeAPI;
    Spinner spinnerMangaId,spinnerCategoryId,spinnerMCategoryId;
    Retrofit retrofit = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edt_mangaId;
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
        setContentView(R.layout.activity_admin_mcategory_update);

        edt_mangaId = findViewById(R.id.edt_mangaid);
        //id from
        //spinnerChapterId =(Spinner) findViewById(R.id.link_id_chapter);
        spinnerMCategoryId =(Spinner) findViewById(R.id.mangaC_id);
        spinnerMangaId =(Spinner) findViewById(R.id.manga_idUpdate);
        spinnerCategoryId = (Spinner)findViewById(R.id.category_idUpdate);


        List<Comic> comics = new ArrayList<>();
        List<MangaCategory> mangaCategories = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        ArrayAdapter<Comic> adapterComic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,comics);
        ArrayAdapter<Category> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
        ArrayAdapter<MangaCategory> adapterMangaCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mangaCategories);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);

        //Observable<List<Comic>> observable = myAPI.getComicList();
        Call<List<Comic>> callComic = myAPI.getComicList2();
        Call<List<Category>> callCategory = myAPI.getCategoryList2();
        Call<List<MangaCategory>> callMangaCategory = myAPI.getMangaCategory2();
        //Call<List<Link>> callLink = myAPI.getImageList2(Common.selected_chapter.getID());
        //String chapterName =  Common.selected_chapter.getName();

        callMangaCategory.enqueue(new Callback<List<MangaCategory>>() {
            @Override
            public void onResponse(Call<List<MangaCategory>> callComic, Response<List<MangaCategory>> response) {
                if(response.isSuccessful()){

                    for(MangaCategory mangaCategory : response.body()){
                        mangaCategories.add(mangaCategory);
                    }
                    adapterMangaCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMCategoryId.setAdapter(adapterMangaCategory);
                }
            }
            @Override
            public void onFailure(Call<List<MangaCategory>> call, Throwable t) {
                Toast.makeText(Admin_mCategory_Update.this,""+call,Toast.LENGTH_SHORT).show();
            }
        });

        callComic.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> callComic, Response<List<Comic>> response) {
                if(response.isSuccessful()){

                    for(Comic comic : response.body()){
                        comics.add(comic);
                    }
                    adapterComic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMangaId.setAdapter(adapterComic);
                }
            }
            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                Toast.makeText(Admin_mCategory_Update.this,""+call,Toast.LENGTH_SHORT).show();
            }
        });

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
                Toast.makeText(Admin_mCategory_Update.this,""+call,Toast.LENGTH_SHORT).show();
            }
        });

//        deleteManga(1);
        btn_Update = (Button) findViewById(R.id.button_Updates);
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category selectedCategory = (Category)spinnerCategoryId.getSelectedItem();
                Comic selectedComic = (Comic) spinnerMangaId.getSelectedItem();
                MangaCategory selectedMangaCategory = (MangaCategory) spinnerMCategoryId.getSelectedItem();
                int categoryID = selectedCategory.getID();
                int comicID = selectedComic.getID();
                int ID = selectedMangaCategory.getID();
                updateMCategory(categoryID,comicID,ID);
            }
        });
    }
    private void updateMCategory(int categoryID, int comicID, int ID) {
        if(categoryID == 0 || comicID == 0 || ID == 0){
            Toast.makeText(Admin_mCategory_Update.this,"Please input all form",Toast.LENGTH_SHORT).show();;
        }else{
            // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);
            compositeDisposable
                    .add(nodeAPI.updatemangacategory(comicID,categoryID,ID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_mCategory_Update.this,""+s,Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_mCategory_Update.this, MainActivity.class));
                                }

                            }));
        }
    }

}