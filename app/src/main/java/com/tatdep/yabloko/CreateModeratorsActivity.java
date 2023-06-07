package com.tatdep.yabloko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.collect.Tables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.UUID;

public class CreateModeratorsActivity extends AppCompatActivity {

    EditText mail, password;
    Button add, back;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_moderators);
        init();
    }

    private void init(){

        mail = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        add = findViewById(R.id.add_btn);
        back = findViewById(R.id.back_btn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mail.getText().toString().isEmpty()|| password.getText().toString().isEmpty()){
                    Toast.makeText(CreateModeratorsActivity.this, "Вы ввели не все данные", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tableName = UUID.randomUUID().toString();
                getSplittedPathChild  getSplittedPathChild = new getSplittedPathChild();
                ref = FirebaseDatabase.getInstance().getReference("user").child(getSplittedPathChild.getSplittedPathChild(mail.getText().toString()));
                User user = new User(mail.getText().toString(), password.getText().toString(), "old");
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString());
                ref.setValue(user);
                ref = FirebaseDatabase.getInstance().getReference("user").child(getSplittedPathChild.getSplittedPathChild(mail.getText().toString())).child("acc").getRef();
                Accounts_Data acc = new Accounts_Data("", "", "", "", "", "Модератор",tableName, "", "", "", "", "Модератор", "");
                ref.setValue(acc);
                Toast.makeText(CreateModeratorsActivity.this, "Модератор успешно добавлен!", Toast.LENGTH_SHORT).show();
                mail.getText().clear();
                password.getText().clear();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(CreateModeratorsActivity.this, AdminMainActivity.class);
                CreateModeratorsActivity.this.startActivity(mainIntent);

                CreateModeratorsActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });
    }
}