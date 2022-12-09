package com.atanu.hgscmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText emailBox,passwordBox,nameBox;
    Button loginBtn,signupBtn;
    FirebaseFirestore database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        nameBox = findViewById(R.id.nameBox);

        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signupButton);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass,name;
                email=emailBox.getText().toString();
                pass=passwordBox.getText().toString();
                name=nameBox.getText().toString();


                if(name.isEmpty()){
                    nameBox.setError("Enter your name!");
                    nameBox.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    emailBox.setError("Email is required!");
                    emailBox.requestFocus();
                    return;
                }

                if(pass.isEmpty()){
                    passwordBox.setError("Password is required!");
                    passwordBox.requestFocus();
                    return;
                }

                if(pass.length() < 6){
                    passwordBox.setError("Password is too short!");
                    passwordBox.requestFocus();
                    return;
                }
                User user = new User();
                user.setEmail(email);
                user.setPass(pass);
                user.setName(name);
                progressDialog.show();




                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            database.collection("Users")
                                    .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                            Toast.makeText(SignupActivity.this, "Account is Created Succesfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignupActivity.this, "Registration Failed, Try again!", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }
}