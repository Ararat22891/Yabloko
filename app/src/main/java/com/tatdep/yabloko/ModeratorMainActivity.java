package com.tatdep.yabloko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ModeratorMainActivity extends AppCompatActivity {

    Button add_to_party_btn, appeals_clear_btn, exit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_main);
        init();
    }

    private void init(){
        add_to_party_btn = findViewById(R.id.add_party_btn);
        appeals_clear_btn = findViewById(R.id.appeals_clear_btn);
        exit_btn = findViewById(R.id.exit_btn);


        add_to_party_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ModeratorMainActivity.this, ModeratorZayavkiActivity.class);

                ModeratorMainActivity.this.startActivity(mainIntent);

                ModeratorMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        appeals_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ModeratorMainActivity.this, AppealsClearActivity.class);

                ModeratorMainActivity.this.startActivity(mainIntent);

                ModeratorMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent mainIntent = new Intent(ModeratorMainActivity.this, SignIn.class);

                ModeratorMainActivity.this.startActivity(mainIntent);

                ModeratorMainActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });
    }
}