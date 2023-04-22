package com.tatdep.yabloko;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.getSplittedPathChild;


public class EnterPartyFragment extends Fragment {

    private Button btn;
    private TextView main;
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
       database.getReference().child("request").child(g.getSplittedPathChild(user.getEmail())).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){

                    main.setText("Вы успешно подали заявку на вступление в партию, в данный момент мы занимаемся её рассмотрением");
                    btn.setVisibility(View.GONE);
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