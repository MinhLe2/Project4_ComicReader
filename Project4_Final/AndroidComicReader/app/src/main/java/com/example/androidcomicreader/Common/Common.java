package com.example.androidcomicreader.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import com.example.androidcomicreader.Model.Category;
import com.example.androidcomicreader.Model.Chapter;
import com.example.androidcomicreader.Model.Comic;
import com.example.androidcomicreader.Retrofit.IComicAPI;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static Comic selected_comic;
    public static Chapter selected_chapter;
    public static int chapter_index = -1;
    public static List<Chapter> chapterList = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    public static IComicAPI getAPI(){
        return RetrofitClient.getInstance().create(IComicAPI.class);

    }

    public static String formatString(String name) {
        //neu ki tu qua dai, hay substring
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0,15)+"...":name);
        return finalResult.toString();
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for(int i =0; i < info.length;i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }

        }
        return false;
    }
}
