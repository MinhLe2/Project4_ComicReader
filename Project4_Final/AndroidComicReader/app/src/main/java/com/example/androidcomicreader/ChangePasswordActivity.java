package com.example.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText txtOldPassword,txtNewPassword,txtEmail;
    Button btnSend;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        txtEmail = findViewById(R.id.txt_Email);
        txtOldPassword = findViewById(R.id.txt_oldPassword);
        txtNewPassword = findViewById(R.id.txt_newPassword);

        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Form is now Checking", Toast.LENGTH_LONG).show();
                checkPassword(txtEmail.getText().toString(),txtOldPassword.getText().toString(),txtNewPassword.getText().toString() );
            }
        });


    }

//    private void checkPassword(String email,String oldPassword,String newPassword){
//        compositeDisposable
//                .add(myAPI.checkPassword(email, oldPassword)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                //convert string to jsonobject => object
//                                //errorcode: 1001, 1002,
//                                if(s.contains("Password Correct")){
//                                    //update(id,generateString(6).toString(),password);
//                                    compositeDisposable
//                                            .add(myAPI.changePassword(email, newPassword)
//                                                    .subscribeOn(Schedulers.io())
//                                                    .observeOn(AndroidSchedulers.mainThread())
//                                                    .subscribe(new Consumer<String>() {
//                                                        @Override
//                                                        public void accept(String s) throws Exception {
//                                                            Toast.makeText(ChangePasswordActivity.this, "" + s, Toast.LENGTH_SHORT).show();
//
//                                                        }
//                                                    }));
//                                }else{
//                                    Toast.makeText(ChangePasswordActivity.this,""+s,Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }));
//    }

    private void checkPassword(String email, String password, String newPassword) {
        if (email.equals("") || password.equals("") || newPassword.equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "Please input all form", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable
                    .add(myAPI.changePassword(email, password, newPassword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(ChangePasswordActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                                }
                            }));
        }
    }
}