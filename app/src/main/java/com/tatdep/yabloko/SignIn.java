package com.tatdep.yabloko;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.common.internal.Objects.equal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button textView;
    private TextView email,password;
    private String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


        public void onClick (View v){
            if (email.getText().toString().isEmpty()|| password.getText().toString().isEmpty()){
                Toast.makeText(SignIn.this,"Вы ввели не все данные", Toast.LENGTH_SHORT).show();
                return;
            }

            else
                checkingNewUser();
    }


    public void onClickCreate(View v){
        hideKeyboard();
        Intent mainIntent = new Intent(SignIn.this, RegisterActivity.class);

        SignIn.this.startActivity(mainIntent);

        SignIn.this.finish();

        overridePendingTransition(R.anim.splash_exiting,R.anim.login_entering);
    }

    private void init(){
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }


    private void checkingNewUser(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            assert user != null;
                            if (user.isEmailVerified()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String ref = USER;
                                DatabaseReference myRef = database.getReference(ref);
                                getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
                                String childRef = getSplittedPathChild.getSplittedPathChild(user.getEmail());

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        hideKeyboard();
                                        User user = snapshot.child(childRef).getValue(User.class);
                                            String isNew = user.isNew.toString();
                                        if (isNew.equals("new")) {
                                            Intent intent = new Intent(SignIn.this, RegisterFullActivity.class);
                                            SignIn.this.startActivity(intent);
                                            SignIn.this.finish();
                                            overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                                        } else if (user.isNew != "old") {
                                            Intent x = new Intent(SignIn.this, MainActivity.class);
                                            SignIn.this.startActivity(x);
                                            SignIn.this.finish();
                                            overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                            if (!user.isEmailVerified()) {
                                Toast.makeText(SignIn.this, "Пожалуйста, подтвердите почту", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn.this, EmailConfirm.class);
                                SignIn.this.startActivity(intent);
                                SignIn.this.finish();
                                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                            }
                        }
                    }
                });
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}