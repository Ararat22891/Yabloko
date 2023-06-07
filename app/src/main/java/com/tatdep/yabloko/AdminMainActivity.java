package com.tatdep.yabloko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity {

    Button create_moder, delete_moder, delete_users, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        init();

    }

    private  void init(){
        create_moder = findViewById(R.id.moder_create_btn);
        delete_moder = findViewById(R.id.moder_delete_btn);
        delete_users = findViewById(R.id.user_delete_btn);
        exit = findViewById(R.id.exit_btn);

        create_moder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AdminMainActivity.this, CreateModeratorsActivity.class);
                AdminMainActivity.this.startActivity(mainIntent);

                AdminMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        delete_moder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AdminMainActivity.this, AdminMainModeratorsActivity.class);
                mainIntent.putExtra("isModers", true);
                AdminMainActivity.this.startActivity(mainIntent);

                AdminMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        delete_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AdminMainActivity.this, AdminMainModeratorsActivity.class);
                mainIntent.putExtra("isModers", false);

                AdminMainActivity.this.startActivity(mainIntent);

                AdminMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent mainIntent = new Intent(AdminMainActivity.this, SignIn.class);

                AdminMainActivity.this.startActivity(mainIntent);

                AdminMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });
    }
}