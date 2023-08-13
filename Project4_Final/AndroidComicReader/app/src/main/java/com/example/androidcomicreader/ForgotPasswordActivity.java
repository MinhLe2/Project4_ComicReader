package com.example.androidcomicreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidcomicreader.Retrofit.INodeJS;
import com.example.androidcomicreader.Retrofit.RetrofitClient;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText _txtEmail;
    Button _btnSend;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);
        _txtEmail = findViewById(R.id.txtEmail);
        _btnSend = findViewById(R.id.btnSend);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        _btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = generateString(6).toString();
                String email = _txtEmail.getText().toString().trim();
                if (email.equals("")) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please input all form", Toast.LENGTH_SHORT).show();
                }else if (email.matches(emailPattern)) {
                    //Toast.makeText(Register.this, "valid email address", Toast.LENGTH_SHORT).show();
                    final String username = "anh.nc.a1908g05@aptechlearning.edu.vn";
                    final String password = "Kanh2108";
                    //String messageToSend=_txtMessage.getText().toString();
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                    try {

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));

                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(_txtEmail.getText().toString()));
                        message.setSubject("Sending email without opening gmail apps");
                        //message.setText(messageToSend);
                        message.setText("THIS IS A MAIL TO CHANGE YOUR PASSWORD " + "\n"
                                + " YOUR NEW PASSWORD IS: " + text + "\n"
                                + " PLEASE CHANGE YOUR PASSWORD IMMEDIATELY");
                        Transport.send(message);
                        updatePassword(email, text);


                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    // or

                }

                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//
//      password

    }

    private void updatePassword(String email, String password) {
        compositeDisposable
                .add(myAPI.UpdatePassword(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(ForgotPasswordActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "email send Succesfully", Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(ForgotPasswordActivity.this, Login_Register.class));
                            }
                        }));

    }


    private String generateString(int lenght) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghiklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}