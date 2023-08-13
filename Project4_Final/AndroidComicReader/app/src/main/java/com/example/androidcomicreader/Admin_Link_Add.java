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

import com.example.androidcomicreader.Adapter.MyImageAdapter;
import com.example.androidcomicreader.Adapter.MyLinkAdapter;
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

public class Admin_Link_Add extends AppCompatActivity {

    IComicAPI myAPI;
    INodeJS nodeAPI;
    private ArrayList<Link> mLinks;
    private MyLinkAdapter mAdapter;
    Spinner spinnerAdd;
    //    Retrofit retrofit = RetrofitClient.getInstance();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText edit_Link;
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
        setContentView(R.layout.activity_admin_link_add);

        Retrofit retrofit = RetrofitClient.getInstance();
        edit_Link = findViewById(R.id.edt_link);

        myAPI = retrofit.create(IComicAPI.class);
        nodeAPI = retrofit.create(INodeJS.class);
        spinnerAdd = findViewById(R.id.spinner_imageLinkAdd);
        //spinnerAdd =(Spinner) findViewById(R.id.link_id_spinner);

//        deleteManga(1);
        btn_Add = (Button) findViewById(R.id.button_Add);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Chapter selectedItem = (Chapter) spinnerAdd.getSelectedItem();
//                int chapterId = selectedItem.getID();
                int chapterId =  Common.selected_chapter.getID();
                addLink(edit_Link.getText().toString(),chapterId);
            }
        });

        initList();

        mAdapter = new MyLinkAdapter(this, mLinks);
        spinnerAdd.setAdapter(mAdapter);

        spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Link clickedItem = (Link) parent.getItemAtPosition(position);
                String clickedLink = clickedItem.getLink();
                //String clickedLinkPicture = clickedItem.get();
                edit_Link.setText(clickedLink);
                //Toast.makeText(Admin_Link_Add.this, clickedLink + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initList() {
        mLinks = new ArrayList<>();
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX19nqV91PWGfc%2BW6cyDFL44mkCT9Bck2zqdh%2BUAbhCDD%2FYne%2BFaQ6YSXUxdXbzzZHxZyhNMTIMAq9qDS3aypoJ71KAs%2FU40XM6v4c2WPhA8uuAHfQt%2BQS4%2FH/khotruyen.net.jpg",  R.drawable.dmon1));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX1%2FDb3MutJxkq4stqotFCtXhztNnUzy8F5o2THCfU9sc64Nn7o6Jvz0X%2FfEXRIo9dV3TQRcXjO1pT3Jm2E5xC4tMygTVpBCXSMqxqk5kzwIB7ZHW%2FBHumQCZ/khotruyen.net.jpg",  R.drawable.dmon2));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX1%2BT4F6OyzGfklnmHhY9ewzhf9DCsbSUT0WPGfehBz9uhMYsE5AaX8ID5FHN3nTBQLVkSDGi8KMj3O3lm9dkjSTZ8%2BGs4Cg%2B%2FzU3SFTlcx9gYbtwM%2BwsOWsQ/khotruyen.net.jpg",  R.drawable.dmon3));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX19jzrgBSRbIk1OgDNPiTb%2FrhumY9gBLd6HWM0v6B79k2r5dOf4D5x8x7GryeqBV%2Bnrb6cdFE%2F45sLWhrbAcjhXCu6HRbGeZYq1FW3YwGXJjEFbcZTl4W7HD/khotruyen.net.jpg",  R.drawable.dmon4));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX18O1PfFdf8E4WRQDDRIv95PMNjbF%2BLahjfQvAgGEgzcSoOw7cUk5DFJFu6yn5dW%2B5UX227tKiT93PFUXPQgU1hZNew%2ByZMUZF4st%2BBpm%2F5aOPA0fuwmZxF5/khotruyen.net.jpg",  R.drawable.dmon5));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX1%2BDVrfeSnL%2FdGpgIFtdLTU%2BlK5jOVVBSGkHvaTXn%2B7pYPyOUVv7Xtb74cRGypeVKUEOOfIEyTdpvQjjox9B%2FgTb3duEMFwt9IyZlGKw5dSRtkKm9ee231n0/khotruyen.net.jpg",  R.drawable.dmon6));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX1%2FRwt6w7%2FMir5visD30XeFVHrYSi%2F7kNyKkAg4uP28dlNrTg8ZNgCq8F3wNmASAGU0oo2SeshN250U7SpG1MJnIQE8CKCmBURjXj52wJNaL%2BQ2w4qov5Lmi/khotruyen.net.jpg",  R.drawable.dmon7));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX19dx9U6HnmtTsGTLyN2up5GA7rQRdBW8goELCOGMlZ46HbnXXfBiu%2FtE3DjNf4ppDAZZQ0m40aGN7m9J%2BR89EcwvOpimQQmIJmzbfNMTtpjdhl2etnn9gUk/khotruyen.net.jpg",  R.drawable.dmon8));
        mLinks.add(new Link("", "https://i1.wp.com/i0.timvl.net/rd/U2FsdGVkX18NFmnesbjCaud1j8o3%2Bs41J%2FrgWC0HYhQnhGe5UXG2pTpSDFhqSCy0rcWE%2FAMMYXmbvyupUAiGIpzmOHOP9uwKX7mFZ3hcXXj7RV5gvw2mGAcgluc%2FF720/khotruyen.net.jpg",  R.drawable.dmon9));
    }
    private void addLink(String link, int chapterid) {
        if (link.equals("") || chapterid == 0) {
            Toast.makeText(Admin_Link_Add.this, "Please input all form", Toast.LENGTH_SHORT).show();
        } else {
            // View register_layout = LayoutInflater.from(this).inflate(R.layout.activity_admin_manga_add, null);
            compositeDisposable
                    .add(nodeAPI.addLink(link, chapterid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Admin_Link_Add.this, "" + s, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_Link_Add.this, ViewDetail.class));
                                }

                            }));
        }
    }
}