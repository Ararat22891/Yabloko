package com.tatdep.yabloko;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button button;
    private TextView email,password, enterPass;
    private DatabaseReference mDataBase;
    private String USER_KEY = "user";
    private String splittedPathChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }


    public void onClick (View v){
                Intent mainIntent = new Intent(RegisterActivity.this, SignIn.class);
                RegisterActivity.this.startActivity(mainIntent);
                RegisterActivity.this.finish();
    }

    public void onRegister(View v){
        if (email.getText().toString().isEmpty()|| password.getText().toString().isEmpty()|| enterPass.getText().toString().isEmpty()){
            Toast.makeText(RegisterActivity.this,"Вы ввели не все данные", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
                onSigning();
        }
    }

    private void init(){
        button = findViewById(R.id.button);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        enterPass = findViewById(R.id.editTextTextPassword2);
        mAuth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }


    private void onSigning(){
        hideKeyboard();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            getSplittedPathChild pC = new getSplittedPathChild();
                            splittedPathChild = pC.getSplittedPathChild(email.getText().toString());
                            String emails = email.getText().toString();
                            String passww = password.getText().toString();
                            User user = new User(emails, passww, "new");
                            mDataBase.child(splittedPathChild).setValue(user);

                            autosave.setSomeProperty(email.getText().toString());
                            Intent intent = new Intent(RegisterActivity.this, EmailConfirm.class);
                            RegisterActivity.this.startActivity(intent);

                            RegisterActivity.this.finish();
                            overridePendingTransition(R.anim.splash_exiting,R.anim.login_entering);
                        }
                        else
                            Toast.makeText(RegisterActivity.this,"Уппс, что-то не так....", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}