package com.atanu.hgscmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailBox,passwordBox;
    Button loginBtn,signupBtn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);


        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signupButton);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Wait a moment...");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected(this)){
                    showCustomDialog();
                }

                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();

                if(email.isEmpty()){
                    emailBox.setError("Email is required");
                    emailBox.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    passwordBox.setError("Password is required");
                    passwordBox.requestFocus();
                    return;
                }

                if(password.length() < 6){
                    passwordBox.setError("Password is too short");
                    passwordBox.requestFocus();
                    return;
                }
                progressDialog.show();


                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logged in ! ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed !", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
            }

            private boolean isConnected(View.OnClickListener onClickListener) {
                ConnectivityManager connectivityManager = onClickListener.getSystemService(createAttributionContext().CONNECTIVITY_SERVICE);
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}