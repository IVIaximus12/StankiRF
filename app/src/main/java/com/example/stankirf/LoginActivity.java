package com.example.stankirf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    // private attributes

    private Button buttonSignIn;
    private TextView registerTextView;
    private EditText loginView, passwordView;

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private String password, login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            nextActivity();
        }
    }


    // private methods

    private void initViews(){

        buttonSignIn = findViewById(R.id.buttonSingIn);
        loginView = findViewById(R.id.editInputLogin);
        passwordView = findViewById(R.id.editInputPassword);
        registerTextView = findViewById(R.id.textViewRegister);
    }

    private void getTextLoginPassword(){
        login = loginView.getText().toString();
        password = passwordView.getText().toString();
    }

    private void setListeners(){

        final Activity activity = this;

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTextLoginPassword();
                signIn(login,password);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment(activity, mAuth);
                registerDialogFragment.show(getSupportFragmentManager(), "RegisterFragment");
            }
        });
    }

    private void nextActivity(){
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            nextActivity();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (login.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.validLogin,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, R.string.validPassword,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
