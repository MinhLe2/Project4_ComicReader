package com.example.androidcomicreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Button btn_Register;

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
        setContentView(R.layout.activity_register);

        EditText edt_Email = (EditText) findViewById(R.id.edt_email);
        EditText edt_UserName = (EditText) findViewById(R.id.edt_register_name);
        EditText edt_Password = (EditText) findViewById(R.id.edt_register_password);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);
        //View
        btn_Register = (MaterialButton) findViewById(R.id.register_button);
        //event
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = edt_Email.getText().toString();
                String name = edt_UserName.getText().toString();
                String password = edt_Password.getText().toString();
                if (name.equals("") ||
                        email.equals("") ||
                        password.equals("")) {
                    Toast.makeText(Register.this, "Please input all data", Toast.LENGTH_SHORT).show();
                }
                else if (email.matches(emailPattern)) {
                    //Toast.makeText(Register.this, "valid email address", Toast.LENGTH_SHORT).show();
                    registerUser(edt_Email.getText().toString(), edt_UserName.getText().toString(), edt_Password.getText().toString());
                    // or

                } else {
                    Toast.makeText(Register.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    //or
                }

            }
        });
    }


    private void registerUser(String email, String name, String password) {
        if (email.equals("") || name.equals("") || password.equals("")) {
            Toast.makeText(Register.this, "Please input all form", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable
                    .add(myAPI.registerUser(name, email, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(Register.this, "" + s, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, Login_Register.class));
                                }

                            }));
        }
    }
}
