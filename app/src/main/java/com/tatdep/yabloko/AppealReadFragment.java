package com.tatdep.yabloko;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.tatdep.yabloko.cods.Appeal;
import com.tatdep.yabloko.cods.AppealAdapterRecyclerView;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.ArrayList;


public class AppealReadFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference db;
    ArrayList<Appeal> arrayList;
    TextView emptyTextView;
    private AppealAdapterRecyclerView adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_appeal_read, container, false);
        init(v);
        return v;
    }

    private  void init (View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSplittedPathChild pC = new getSplittedPathChild();
        emptyTextView = v.findViewById(R.id.emptyTextView);

        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());
        db = FirebaseDatabase.getInstance().getReference("user").child(splittedPathChild).child("appeals").getRef();
        arrayList = new ArrayList<>();
        adapter = new AppealAdapterRecyclerView(getContext(), arrayList);
        recyclerView = v.findViewById(R.id.res);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        addDataOnRecyclerView();

    }


    private void addDataOnRecyclerView() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList.size() > 0) {
                    arrayList.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Appeal ps = ds.getValue(Appeal.class);
                    if (!ps.getStatus().equals("Отвечено")) {
                        assert ps != null;
                        arrayList.add(ps);
                    }
                }


                adapter.notifyDataSetChanged();
                if(adapter.getItemCount()==0){
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    sendNotification("Новое сообщение", "Вы получили новое сообщение");
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        db.addValueEventListener(valueEventListener);
    }


    private void sendNotification(String title, String body) {
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("588579874213" + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(getMessageId()))
                .addData("title", title)
                .addData("body", body)
                .build());
    }

    private int getMessageId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}