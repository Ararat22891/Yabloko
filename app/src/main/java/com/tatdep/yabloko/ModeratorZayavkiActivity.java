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
import com.tatdep.yabloko.cods.RequestPartyAdapterRecyclerView;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.tatdep.yabloko.cods.requestParty;

import java.util.ArrayList;

public class ModeratorZayavkiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference db;
    ArrayList<requestParty> arrayList;
    private RequestPartyAdapterRecyclerView adapter;
    TextView emptyTextView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_zayavki);
        init();
    }

    private void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSplittedPathChild pC = new getSplittedPathChild();
        emptyTextView =findViewById(R.id.emptyTextView);
        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());
        db = FirebaseDatabase.getInstance().getReference("request").getRef();
        arrayList = new ArrayList<>();
        adapter = new RequestPartyAdapterRecyclerView(getApplicationContext(), arrayList);
        recyclerView = findViewById(R.id.vlss);
        btn = findViewById(R.id.btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false) );


        recyclerView.setAdapter(adapter);
        addDataOnRecyclerView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ModeratorZayavkiActivity.this, ModeratorMainActivity.class);
                ModeratorZayavkiActivity.this.startActivity(mainIntent);

                ModeratorZayavkiActivity.this.finish();

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
                    requestParty ps = ds.getValue(requestParty.class);
                    if (ps != null){
                        arrayList.add(ps);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        db.addValueEventListener(valueEventListener);
    }
}