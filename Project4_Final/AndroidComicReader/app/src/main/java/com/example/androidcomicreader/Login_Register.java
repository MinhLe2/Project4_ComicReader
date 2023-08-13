package com.example.androidcomicreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Login_Register extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isRemembered = false;
    EditText edit_Name, edit_Password;
    Button btn_Register, btn_Login;
    CheckBox checkBox;
    TextView txv_changePassword, txv_forgetPassword;
    SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_login_register);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        checkBox = findViewById(R.id.checkBox);
        //View
        btn_Login = (MaterialButton)findViewById(R.id.login_button);
        btn_Register = (MaterialButton)findViewById(R.id.register_button);

        edit_Name = (EditText)findViewById(R.id.edt_namee);
        edit_Password = (EditText)findViewById(R.id.edt_passwordd);

        sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false);

        txv_changePassword = (TextView)findViewById(R.id.textViewChangePassword);
        txv_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Register.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        txv_forgetPassword = (TextView)findViewById(R.id.textViewForgetPassword);
        txv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Register.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //saved info
        if(isRemembered){
            Intent intent = new Intent(Login_Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //event
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_Name.getText().toString();
                boolean checked = checkBox.isChecked();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("NAME",name);
                editor.putBoolean("CHECKBOX",checked);
                editor.apply();
                if(checked){
                    Toast.makeText(Login_Register.this, "Information Saved", Toast.LENGTH_SHORT).show();
                }
                // check box
                loginUser(edit_Name.getText().toString(), edit_Password.getText().toString());

            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Register.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String name, String password ) {
        if(name.equals("") || password.equals("")){
            Toast.makeText(Login_Register.this,"Please input all form",Toast.LENGTH_SHORT).show();
        }else{

            compositeDisposable
                    .add(myAPI.loginUser(name, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread() )
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    //convert string to jsonobject => object
                                    //errorcode: 1001, 1002,
                                    if(s.contains("Login Successful")){
                                        //update(id,generateString(6).toString(),password);
                                        Toast.makeText(Login_Register.this,""+s,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login_Register.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(Login_Register.this,""+s,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    );
        }


    }
}