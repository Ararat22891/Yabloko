package com.tatdep.yabloko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.Objects;

import kotlinx.coroutines.Delay;

public class splashScreen extends AppCompatActivity {
private  final int splash_screen_delay = 2000;
private FirebaseUser firebaseUser;
private FirebaseAuth firebaseAuth;
private String dolz = "Пользователь";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                    }
                });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        TableLayout tableLayout = findViewById(R.id.table_splash);
        Animation tablego = AnimationUtils.loadAnimation(this,R.anim.splash_exiting);
        tableLayout.startAnimation(tablego);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseUser!=null) {
                    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();

                    DatabaseReference  ref = FirebaseDatabase.getInstance().getReference("user").child(getSplittedPathChild.getSplittedPathChild(firebaseUser.getEmail())).child("acc").getRef();
                    dolz = "Пользователь";
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Accounts_Data acc = snapshot.getValue(Accounts_Data.class);

                            dolz = acc != null?acc.dolz:"";
                            if (acc != null && (Objects.equals(dolz, "Модератор") || Objects.equals(dolz, "Администратор"))){
                                Intent mainIntent = new Intent(splashScreen.this, MainActivity.class);
                                switch (dolz){
                                    case "Администратор":
                                        mainIntent = new Intent(splashScreen.this, AdminMainActivity.class);
                                        break;
                                    case "Модератор":
                                        mainIntent = new Intent(splashScreen.this, ModeratorMainActivity.class);
                                        break;
                                }
                                splashScreen.this.startActivity(mainIntent);

                                splashScreen.this.finish();

                                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                            }

                            if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified() ){
                                assert firebaseUser!=null;

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String ref = "user";
                                DatabaseReference myRef = database.getReference(ref);
                                String childRef = getSplittedPathChild.getSplittedPathChild(firebaseUser.getEmail());

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.child(childRef).getValue(User.class);
                                        String isNew = user.isNew.toString();
                                        if (isNew.equals("new")) {
                                            Intent intent = new Intent(splashScreen.this, RegisterFullActivity.class);
                                            splashScreen.this.startActivity(intent);
                                            splashScreen.this.finish();
                                            overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                                        } else if (user.isNew != "old") {
                                            Intent x = new Intent(splashScreen.this, MainActivity.class);
                                            splashScreen.this.startActivity(x);
                                            splashScreen.this.finish();
                                            overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });




                            }
                            else {
                                Intent intent = new Intent(splashScreen.this, EmailConfirm.class);
                                splashScreen.this.startActivity(intent);

                                splashScreen.this.finish();
                                overridePendingTransition(R.anim.splash_exiting,R.anim.login_entering);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                else {
                    Intent mainIntent = new Intent(splashScreen.this, RegisterActivity.class);
                    splashScreen.this.startActivity(mainIntent);

                    splashScreen.this.finish();

                    overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
                }
            }
        }, splash_screen_delay);
    }
        @Override
        public void  onBackPressed()
        {
            super.onBackPressed();
        }

    }