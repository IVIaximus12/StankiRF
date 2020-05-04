package com.example.stankirf.autorize;

import android.app.Activity;

public interface AuthorizationService {

    void signIn(String login, String password, Activity activity);
    boolean signOut();
    boolean checkCurrentUser();
    boolean signInIsCorrect();
}

