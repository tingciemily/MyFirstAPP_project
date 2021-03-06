package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registeruser;
    private EditText user_name, user_age, user_email, user_pwd;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registeruser = (Button) findViewById(R.id.registeruser);
        registeruser.setOnClickListener(this);

        user_name = (EditText) findViewById(R.id.Name);
        user_age = (EditText) findViewById(R.id.age);
        user_email = (EditText) findViewById(R.id.email);
        user_pwd = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registeruser:
                registeruser();
                break;
        }
    }

    private void registeruser(){
        String email = user_email.getText().toString().trim();
        String password = user_pwd.getText().toString().trim();
        String name = user_name.getText().toString().trim();
        String age = user_age.getText().toString().trim();

        if(name.isEmpty()){
            user_name.setError("????????????");
            user_name.requestFocus();
            return;
        }

        if(age.isEmpty()){
            user_age.setError("????????????");
            user_age.requestFocus();
            return;
        }

        if (email.isEmpty()){
            user_email.setError("????????????");
            user_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            user_email.setError("????????????");
            user_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            user_pwd.setError("????????????");
            user_pwd.requestFocus();
            return;
        }

        if(password.length() < 5){
            user_pwd.setError("????????????6??????");
            user_pwd.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            User user = new User(name, age, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task){


                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "????????????", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }else{
                                                Toast.makeText(Register.this, "????????????", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                            });
                        }else{
                            Toast.makeText(Register.this, "??????????????????", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}