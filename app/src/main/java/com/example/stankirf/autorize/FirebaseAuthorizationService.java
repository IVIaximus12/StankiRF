package com.example.stankirf.autorize;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FirebaseAuthorizationService implements AuthorizationService {

    // private

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private boolean signInIsCorrect;

    // public Constructors

    public FirebaseAuthorizationService(){

        initAuth();
    }

    // public methods

    public void signIn(String login, String password, Activity activity) {

        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            signInIsCorrect = true;
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            signInIsCorrect = false;
                        }
                    }
                });
    }

    public boolean signOut(){
        mAuth.signOut();
        return true;
    }

    public boolean checkCurrentUser(){
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public boolean signInIsCorrect() { return signInIsCorrect;}

    // private methods

    private void initAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

}
