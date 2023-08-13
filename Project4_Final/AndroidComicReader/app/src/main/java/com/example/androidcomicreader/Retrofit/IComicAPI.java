package com.example.androidcomicreader.Retrofit;

import com.example.androidcomicreader.Model.Banner;
import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Model.Link;
import com.example.androidcomicreader.Model.MangaCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IComicAPI {
    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("comic")
    Observable<List<Comic>> getComicList();

    @GET("comic")
    Call<List<Comic>> getComicList2();

    @GET("mangacategory")
    Call<List<MangaCategory>> getMangaCategory2();

    @GET("categories")
    Call<List<Category>> getCategoryList2();

    @GET("links/{chapterid}")
    Call<List<Link>>getImageList2(@Path("chapterid")int chapterid);

    @GET("link")
    Call<List<Link>> getLinkList2();

    @GET("chapter/{comicid}")
    Observable<List<Chapter>>getChapterList(@Path("comicid")int comicid);

    @GET("links/{chapterid}")
    Observable<List<Link>>getImageList(@Path("chapterid")int chapterid);

    @GET("categories")
    Observable<List<Category>> getCategoryList();

    @POST("filter")
    @FormUrlEncoded
    Observable<List<Comic>>getFilteredComic(@Field("data")String data);

    @POST("search")
    @FormUrlEncoded
    Observable<List<Comic>>getSearchedComic(@Field("search")String search);
}
