package com.tatdep.yabloko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.AccountsDataAdapterRecyclerView;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.Appeal;
import com.tatdep.yabloko.cods.AppealAdapterRecyclerView;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.ArrayList;

public class AdminMainModeratorsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ArrayList<Accounts_Data> arrayList;
    TextView emptyTextView;
    Button btn;
    private AccountsDataAdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_moderators);
        init();

    }

    private void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSplittedPathChild pC = new getSplittedPathChild();

        db = FirebaseDatabase.getInstance().getReference("user").getRef();
        arrayList = new ArrayList<>();
        adapter = new AccountsDataAdapterRecyclerView(getApplicationContext() ,arrayList);
        recyclerView = findViewById(R.id.vlss);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AdminMainModeratorsActivity.this, AdminMainActivity.class);
                AdminMainModeratorsActivity.this.startActivity(mainIntent);

                AdminMainModeratorsActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });

        addDataOnRecyclerView();

    }

    private void addDataOnRecyclerView() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList.size() > 0) {
                    arrayList.clear();
                }
                for(DataSnapshot appealSnapshot: snapshot.getChildren() ){
                    getSplittedPathChild pC = new getSplittedPathChild();
                    User user = appealSnapshot.getValue(User.class);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("user").child(pC.getSplittedPathChild(user.email)).child("acc").getRef();
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot childsnapshot) {
                            Accounts_Data acc = childsnapshot.getValue(Accounts_Data.class);
                            assert acc != null;
                            if (!acc.dolz.equals("Модератор"))
                                arrayList.add(acc);
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        db.addValueEventListener(valueEventListener);
    }
}