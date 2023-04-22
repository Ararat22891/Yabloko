package com.tatdep.yabloko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.tatdep.yabloko.cods.autosave;

public class EmailConfirm extends AppCompatActivity {
    private FirebaseUser user;
    private TextView textView;
    private   FirebaseAuth.AuthStateListener mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        init();
        findViewById(R.id.but_sign).setVisibility(View.INVISIBLE);
        textView.setText("Нажмите кнопку, чтобы подтвердить свой аккаунт");
    }

    public void onClick(View v){
                findViewById(R.id.but_sign).setVisibility(View.VISIBLE);
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    textView.setText("Подтверждение выслано на почту" + ' '+ user.getEmail());

                                }
                                else {
                                    textView.setText("Возникли ошибки, пожалуйста, повторите позже");
                                }

                            }
                        });
    }

    public void onClickRegister(View v){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent mainIntent = new Intent(EmailConfirm.this, SignIn.class);
            EmailConfirm.this.startActivity(mainIntent);

            EmailConfirm.this.finish();

            overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
    }


    public void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        textView = findViewById(R.id.textView2);
    }

}