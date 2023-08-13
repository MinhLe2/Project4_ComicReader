package com.example.androidcomicreader.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INodeJS {
    @POST("updatePassword")
    @FormUrlEncoded
    Observable<String>UpdatePassword(@Field("email")String email,
                                     @Field("password")String password);

    @POST("changePassword")
    @FormUrlEncoded
    Observable<String>changePassword(@Field("email")String email,
                                     @Field("password")String password,
                                     @Field("newPassword")String newPassword);
    @POST("registers")
    @FormUrlEncoded
    Observable<String>registerUser(@Field("name")String name,
                                    @Field("email")String email,
                                   @Field("password")String password);
    @POST("login")
    @FormUrlEncoded
    Observable<String>loginUser(@Field("name")String name,
                                @Field("password")String password);
    @POST("addmanga")
    @FormUrlEncoded
    Observable<String>addManga(@Field("name")String name,
                                @Field("image")String image);
    @POST("updatemanga")
    @FormUrlEncoded
    Observable<String>updateManga(@Field("id")int id,
                                @Field("name")String name,
                                @Field("image")String image);
    @POST("deletemanga")
    @FormUrlEncoded
    Observable<String>deleteManga(@Field("id")int id);

    @POST("addchapter")
    @FormUrlEncoded
    Observable<String>addChapter(@Field("name")String name,
                                @Field("mangaid")int mangaid);
    @POST("updatechapter")
    @FormUrlEncoded
    Observable<String>updateChapter(@Field("id")int id,
                                @Field("name")String name,
                                @Field("mangaid")int mangaid);
    @POST("deletechapter")
    @FormUrlEncoded
    Observable<String>deleteChapter(@Field("id")int id);

    @POST("addlink")
    @FormUrlEncoded
    Observable<String>addLink(@Field("link")String name,
                                @Field("chapterid")int chapterid);

    @POST("updatelink")
    @FormUrlEncoded
    Observable<String>updateLink(@Field("id")int id,
                                 @Field("link")String link,
                                @Field("chapterid")int chapterid);
    @POST("deletelink")
    @FormUrlEncoded
    Observable<String>deleteLink(@Field("id")int id);

    @POST("addMangaCategory")
    @FormUrlEncoded
    Observable<String>addmangacategory(@Field("mangaid")int mangaid,
                              @Field("categoryid")int categoryid);

    @POST("updatemangacategory")
    @FormUrlEncoded
    Observable<String>updatemangacategory(@Field("mangaid")int mangaid,
                                         @Field("categoryid")int categoryid,
                                          @Field("id")int id);

    @POST("deletemangacategory")
    @FormUrlEncoded
    Observable<String>deletemangacategory(@Field("id")int id);

}
