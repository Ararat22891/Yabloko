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

    EditText mail, passwordы;
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
        passwordы = findViewById(R.id.pass);
        add = findViewById(R.id.add_btn);
        back = findViewById(R.id.back_btn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = mail.getText().toString().trim();
                String password = passwordы.getText().toString().trim();

                if (!email.matches(emailPattern)) {
                    // Выводите сообщение об ошибке или выполняйте другие действия, соответствующие неправильному формату почты
                    Toast.makeText(CreateModeratorsActivity.this, "Введите корректный адрес электронной почты", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() <= 8) {
                    // Выводите сообщение об ошибке или выполняйте другие действия, соответствующие неправильной длине пароля
                    Toast.makeText(CreateModeratorsActivity.this, "Пароль должен содержать более 8 символов", Toast.LENGTH_SHORT).show();
                    return;
                }


                String tableName = UUID.randomUUID().toString();
                getSplittedPathChild  getSplittedPathChild = new getSplittedPathChild();
                ref = FirebaseDatabase.getInstance().getReference("user").child(getSplittedPathChild.getSplittedPathChild(mail.getText().toString()));
                User user = new User(mail.getText().toString(), passwordы.getText().toString(), "old");
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail.getText().toString(), passwordы.getText().toString());
                ref.setValue(user);
                ref = FirebaseDatabase.getInstance().getReference("user").child(getSplittedPathChild.getSplittedPathChild(mail.getText().toString())).child("acc").getRef();
                Accounts_Data acc = new Accounts_Data("", "", "", "", "", "Модератор",tableName, "", "", "", "", "Модератор", "");
                ref.setValue(acc);
                Toast.makeText(CreateModeratorsActivity.this, "Модератор успешно добавлен!", Toast.LENGTH_SHORT).show();
                mail.getText().clear();
                passwordы.getText().clear();
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