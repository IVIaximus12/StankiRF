package com.example.stankirf;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterDialogFragment extends DialogFragment {


    // private attributes

    private Button buttonRegister;
    private EditText login, password, passwordRepeat;
    private View view;
    private Activity activity;
    private String strLogin, strPassword, strPasswordRepeat;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.dialog_fragment_register, container, false);

        initViews();
        setListeners();

        return view;
    }

    public RegisterDialogFragment(Activity activity, FirebaseAuth mAuth){
        super();
        this.activity = activity;
        this.mAuth = mAuth;
    }


    // private methods

    private void initViews(){
        buttonRegister = view.findViewById(R.id.buttonRegister);
        login = view.findViewById(R.id.editInputLoginRegister);
        password = view.findViewById(R.id.editInputPasswordRegister);
        passwordRepeat = view.findViewById(R.id.editInputPasswordRegisterRepeat);
    }

    private void setListeners(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTextLoginPassword();
                if (validateForm()){
                    createAccount(strLogin, strPassword);
                }
            }
        });

    }

    private void getTextLoginPassword(){
        strLogin = login.getText().toString();
        strPassword = password.getText().toString();
        strPasswordRepeat = passwordRepeat.getText().toString();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            dismiss();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {

        if (strLogin.isEmpty()) {
            Toast.makeText(getActivity(), R.string.validLogin,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (strPassword.length() < 6) {
            Toast.makeText(getActivity(), R.string.validPassword,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!strPassword.equals(strPasswordRepeat)) {
            Toast.makeText(getActivity(), R.string.validSamePasswords,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
