package com.tatdep.yabloko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.tatdep.yabloko.cods.Appeal;
import com.tatdep.yabloko.cods.AppealAdapterRecyclerView;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.ArrayList;

public class AppealsClearActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ArrayList<Appeal> arrayList;
    private AppealAdapterRecyclerView adapter;
    TextView emptyTextView;
    Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeals_clear);
        init();
    }


    private void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSplittedPathChild pC = new getSplittedPathChild();
        emptyTextView =findViewById(R.id.emptyTextView);
        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());
        db = FirebaseDatabase.getInstance().getReference("user").getRef();
        arrayList = new ArrayList<>();
        adapter = new AppealAdapterRecyclerView(getApplicationContext(), arrayList);
        adapter.btn_name = "Удалить обращение";
        adapter.isDeleting = true;
        btn = findViewById(R.id.btn);
        recyclerView = findViewById(R.id.vlss);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setAdapter(adapter);
        addDataOnRecyclerView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(AppealsClearActivity.this, ModeratorMainActivity.class);

                AppealsClearActivity.this.startActivity(mainIntent);

                AppealsClearActivity.this.finish();

                overridePendingTransition(R.anim.splash_exiting, R.anim.login_entering);
            }
        });
    }


    private void addDataOnRecyclerView() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList.size() > 0) {
                    arrayList.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User ps = ds.getValue(User.class);
                    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
                    if (ps != null){
                        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("user").child(getSplittedPathChild.getSplittedPathChild(ps.email)).child("appeals").getRef();
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot child) {
                                for (DataSnapshot childser: child.getChildren()){
                                    Appeal ap = childser.getValue(Appeal.class);
                                    if (ap != null){
                                        arrayList.add(ap);
                                    }
                                }
                                adapter.notifyDataSetChanged();


                                if(adapter.getItemCount()==0){
                                    emptyTextView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                else {
                                    emptyTextView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

//                    if (!ps.getStatus().equals("Отвечено")) {
//                        assert ps != null;
//                        arrayList.add(ps);
//                    }
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        db.addValueEventListener(valueEventListener);
    }

}