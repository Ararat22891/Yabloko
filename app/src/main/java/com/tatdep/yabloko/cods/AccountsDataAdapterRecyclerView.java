package com.tatdep.yabloko.cods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.R;

import java.util.ArrayList;
import java.util.Objects;

public class AccountsDataAdapterRecyclerView extends RecyclerView.Adapter<AccountsDataAdapterRecyclerView.ViewHolder>{

    private ArrayList<Accounts_Data> accs;
    private Context context;
    Accounts_Data current;
    private AccountsDataAdapterRecyclerView.ItemClickListener itemClickListener;
    private DatabaseReference databaseReference;
    private AccountsDataAdapterRecyclerView.StatusUpdateListener statusUpdateListener;

    public AccountsDataAdapterRecyclerView(Context context, ArrayList<Accounts_Data> accs) {
        this.context = context;
        this.accs = accs;
    }

    public void setItemClickListener(AccountsDataAdapterRecyclerView.ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public AccountsDataAdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_delete, parent, false);
        return new AccountsDataAdapterRecyclerView.ViewHolder(view);
    }

    public interface StatusUpdateListener {
        void onStatusUpdate(int position, String newStatus);
    }

    public void setStatusUpdateListener(AccountsDataAdapterRecyclerView.StatusUpdateListener listener) {
        this.statusUpdateListener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull AccountsDataAdapterRecyclerView.ViewHolder holder, int position) {
        current = accs.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.fio.setText(current.surname + " "+ current.name+" "+ current.patronomyc);
        holder.textIdeology.setText(current.polit_pred);
        holder.textWorldview.setText(current.regilgia);
        holder.textLifePurpose.setText(current.mainInLife);

        holder.textPeopleOpinion.setText(current.mainInMan);
        holder.textInspiration.setText(current.vdox);
        holder.textPosition.setText(current.dolz);
        holder.textEmail.setText(current.telNumber);
        getSplittedPathChild pC = new getSplittedPathChild();

        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("user").getRef();

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot appealSnapshot: snapshot.getChildren() ){
                            getSplittedPathChild pC = new getSplittedPathChild();
                            User user = appealSnapshot.getValue(User.class);
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("user").child(pC.getSplittedPathChild(user.email)).child("acc").getRef();
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot childsnapshot) {
                                    Accounts_Data acc = childsnapshot.getValue(Accounts_Data.class);
                                    assert acc != null;
                                    if (acc.equals(current)){
                                        current = acc;
                                        DatabaseReference appealRef = snapshot.getRef();
                                        appealRef.removeValue();
                                    }
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
                });
            }
        });




    }



    @Override
    public int getItemCount() {
        return accs.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setDatabaseReference(DatabaseReference reference) {
        this.databaseReference = reference;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIdeology, textWorldview, textLifePurpose, textPeopleOpinion , textInspiration, textPosition,
                textEmail, fio;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            textIdeology = itemView.findViewById(R.id.textIdeology);
            textWorldview = itemView.findViewById(R.id.textWorldview);
            textLifePurpose = itemView.findViewById(R.id.textLifePurpose);
            textPeopleOpinion = itemView.findViewById(R.id.textPeopleOpinion);

            textInspiration = itemView.findViewById(R.id.textInspiration);
            textPosition = itemView.findViewById(R.id.textPosition);
            textEmail = itemView.findViewById(R.id.textEmail);
            fio = itemView.findViewById(R.id.labelName);
            btn = itemView.findViewById(R.id.btn);
        }
    }
}