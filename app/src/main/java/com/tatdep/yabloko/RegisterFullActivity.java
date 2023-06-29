package com.tatdep.yabloko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.context.AttributeContext;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class RegisterFullActivity extends AppCompatActivity {

    private EditText fio, telNumber,  region, inv;
    private CheckBox checkBox;
    String USER = "user";
    com.tatdep.yabloko.cods.getSplittedPathChild getSplittedPathChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_full);
        init();
    }


    public void onClick(View v){



        if (fio.getText().toString().trim().isEmpty() || fio.getText().toString().trim().split(" ").length != 3) {
            fio.setHint("Введите корректные значения!");
            Toast.makeText(RegisterFullActivity.this, "Пожалуйста, введите корректные ФИО", Toast.LENGTH_SHORT).show();
            fio.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }

        if (region.getText().toString().trim().isEmpty()) {
            region.setHint("Введите корректные значения!");
            Toast.makeText(RegisterFullActivity.this, "Пожалуйста, введите корректный регион из списка", Toast.LENGTH_SHORT).show();
            region.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }

        if (telNumber.getText().toString().trim().isEmpty() || telNumber.getText().toString().trim().length() != 18) {
            telNumber.setHint("Введите корректные значения!");
            telNumber.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }



        switch (v.getId()){
            case R.id.button:
                hideKeyboard();
                changeUsersStatus();
                addData();

                Intent intent = new Intent(RegisterFullActivity.this, MainActivity.class);

                RegisterFullActivity.this.startActivity(intent);
                RegisterFullActivity.this.finish();
                overridePendingTransition(R.anim.splash_exiting,R.anim.login_entering);
                break;
            case R.id.checkbox:
                hideKeyboard();
                checkBox = findViewById(R.id.checkbox);

                if (checkBox.isChecked()){
                    inv.setVisibility(View.VISIBLE);
                }
                else
                    inv.setVisibility(View.GONE);
                break;
        }
    }

    private void init(){
        telNumber = findViewById(R.id.editTextTextPassword);
        fio = findViewById(R.id.editTextTextEmailAddress);
        inv = findViewById(R.id.inv);
        region = findViewById(R.id.autocomplete);
        telNumber = findViewById(R.id.editTextTextPassword);
        checkBox = findViewById(R.id.checkbox);
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER));
        formatWatcher.installOn(telNumber);

        getSplittedPathChild = new getSplittedPathChild();
        String[] regions = getResources().getStringArray(R.array.regions);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autocomplete);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, regions);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }
    public void addData(){
        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        DatabaseReference ref= mDb.getReference(USER);
        String[] arr = fio.getText().toString().split(" ");
        autosave autosave = new autosave();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String surn = arr[0];
        String name = arr[1];
        String patronomyc = arr[2];
        String telNumbers = telNumber.getText().toString();
        String reg = region.getText().toString();
        String dolz = "";
        String pol_pred ="";
        String relig = "";
        String mainInLife = "";
        String mainInMan = "";
        String vdox = "";
        String age = "";

        if (checkBox.isChecked()){

            if(!inv.getText().toString().isEmpty()){
                dolz = inv.getText().toString();
            }
            else if (inv.getText().toString().isEmpty()){
                dolz = "Член партии";
            }
        }
        else
            dolz = "Пользователь";

        Accounts_Data accounts_data = new Accounts_Data(pol_pred, relig, mainInLife, mainInMan, vdox,surn, name, patronomyc, telNumbers,
                reg,age,dolz, "");
        ref.child( getSplittedPathChild.getSplittedPathChild(user.getEmail().toString())).child("acc").setValue(accounts_data);
    }


    private void changeUsersStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDb.getReference(USER);
        getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).getValue(User.class);
                Map<String, Object> map = user1.toMap();

                map.put("isNew","old");
                ref.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}