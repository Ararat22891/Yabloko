package com.tatdep.yabloko;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.tatdep.yabloko.cods.requestParty;

import java.util.Objects;


public class EnterPartyFragment extends Fragment {

    private Button btn;
    private TextView main;
    RelativeLayout rlt;
    View free;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    getSplittedPathChild g = new getSplittedPathChild();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter_party, container, false);
        init(v);
        return v;
    }

    private void init(View view){
        btn = view.findViewById(R.id.btn);
        main = view.findViewById(R.id.main_text);
        rlt = view.findViewById(R.id.rlt);
        free = view.findViewById(R.id.free);

        getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
       database.getReference().child("request").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){

                   if(ds!= null){
                       String s = getSplittedPathChild.getSplittedPathChild(user.getEmail());
                       if(Objects.equals(ds.getKey(), s)){
                           main.setText("Вы успешно подали заявку на вступление в партию, в данный момент мы занимаемся её рассмотрением");
                           btn.setVisibility(View.GONE);
                       }
                   }
                }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        DatabaseReference myStatus = FirebaseDatabase.getInstance().getReference().child("user").child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").child("dolz");
        myStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dolz = snapshot.getValue(String.class);
                if (dolz != null){
                    if(dolz.equals("Член партии")){
                        rlt.setVisibility(View.GONE);
                        free.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).pushFragments("mainParty", ((MainActivity) getActivity()).mainParty);
            }
        });
    }
}