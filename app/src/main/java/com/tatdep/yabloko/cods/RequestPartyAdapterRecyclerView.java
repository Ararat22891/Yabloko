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

public class RequestPartyAdapterRecyclerView extends RecyclerView.Adapter<RequestPartyAdapterRecyclerView.ViewHolder>{
    private ArrayList<requestParty> requestParties;
    private Context context;
    requestParty currentRequest;
    public String btn_name = "Отправить";
    public boolean isDeleting = false;
    private String parentName;
    private String mail;
    private RequestPartyAdapterRecyclerView.ItemClickListener itemClickListener;
    private DatabaseReference databaseReference;
    private RequestPartyAdapterRecyclerView.StatusUpdateListener statusUpdateListener;

    public RequestPartyAdapterRecyclerView(Context context, ArrayList<requestParty> requestParties){
        this.context = context;
        this.requestParties = requestParties;
    }

    public void setItemClickListener(RequestPartyAdapterRecyclerView.ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public RequestPartyAdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_list, parent, false);
        return new RequestPartyAdapterRecyclerView.ViewHolder(view);
    }

    public interface StatusUpdateListener {
        void onStatusUpdate(int position, String newStatus);
    }

    public void setStatusUpdateListener(RequestPartyAdapterRecyclerView.StatusUpdateListener listener) {
        this.statusUpdateListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestPartyAdapterRecyclerView.ViewHolder holder, int position) {
        currentRequest = requestParties.get(position);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.regotd.setText(currentRequest.regOtd);
        holder.mestOtd.setText(currentRequest.mestnOtd);
        holder.fio.setText(currentRequest.fio);
        holder.telnumb.setText(currentRequest.telNumber);
        holder.sex.setText(currentRequest.sex);
        holder.birth.setText(currentRequest.dateOfBirth);
        holder.citizen.setText(currentRequest.grazd);
        getSplittedPathChild pC = new getSplittedPathChild();

        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("request").getRef();

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dt: snapshot.getChildren()){
                            requestParty req = dt.getValue(requestParty.class);

                            if (currentRequest.equals(req)){
                                parentName = dt.getKey();
                                currentRequest = req;
                                dt.getRef().removeValue();
                                DatabaseReference updateValue = FirebaseDatabase.getInstance().getReference().child("user").child(parentName).child("acc").child("dolz");
                                updateValue.setValue("Член партии");
                                updateValue =FirebaseDatabase.getInstance().getReference().child("user").child(parentName).child("email");

                                updateValue.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mail = snapshot.getValue(String.class);
                                        String subject = "Принято решение по вопросу вступления в партию"; // Тема письма


                                        String htmlBody = "<html><body>" +
                                                "<table>" +
                                                "<tr>" +
                                                "<td style=\"vertical-align: top; padding-right: 10px;\">" +
                                                "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg/1024px-%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg.png\" style=\"width: 50px; height: 50px;\">" +
                                                "</td>" +
                                                "<td>" +
                                                "<h1>Привет!</h1>" +
                                                "<p>Согласно решению ответственного мы приняли решение принять Вас в партию!</p>" +
                                                "<p>Партия Яблоко</p>" +
                                                "</td>" +
                                                "</tr>" +
                                                "</table>" +
                                                "</body></html>";

                                        EmailSender.sendEmail(mail, subject, htmlBody);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dt: snapshot.getChildren()){
                            requestParty req = dt.getValue(requestParty.class);

                            if (currentRequest.equals(req)){
                                parentName = dt.getKey();
                                currentRequest = req;
                                dt.getRef().removeValue();
                                DatabaseReference updateValue =FirebaseDatabase.getInstance().getReference().child("user").child(parentName).child("email");

                                updateValue.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mail = snapshot.getValue(String.class);
                                        String subject = "Принято решение по вопросу вступления в партию"; // Тема письма


                                        String htmlBody = "<html><body>" +
                                                "<table>" +
                                                "<tr>" +
                                                "<td style=\"vertical-align: top; padding-right: 10px;\">" +
                                                "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg/1024px-%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg.png\" style=\"width: 50px; height: 50px;\">" +
                                                "</td>" +
                                                "<td>" +
                                                "<h1>Привет!</h1>" +
                                                "<p>Согласно решению ответственного мы приняли решение не принимать Вас в партию из-за ошибок в данных. Пожалуйста, исправьте ошиюки и попробуйте ещё раз!</p>" +
                                                "<p>Партия Яблоко</p>" +
                                                "</td>" +
                                                "</tr>" +
                                                "</table>" +
                                                "</body></html>";

                                        EmailSender.sendEmail(mail, subject, htmlBody);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
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
        return requestParties.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setDatabaseReference(DatabaseReference reference) {
        this.databaseReference = reference;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView regotd, fio, telnumb, sex, birth, citizen, mestOtd;
        Button btnAdd, btnNo;
        LinearLayout expandableContent;
        EditText edtReplay;

        public ViewHolder(View itemView) {
            super(itemView);
            regotd = itemView.findViewById(R.id.regotd);
            fio = itemView.findViewById(R.id.fio);
            telnumb = itemView.findViewById(R.id.telnumb);
            sex = itemView.findViewById(R.id.sex);
            birth = itemView.findViewById(R.id.birth);
            citizen = itemView.findViewById(R.id.citizen);
            btnAdd = itemView.findViewById(R.id.add);
            btnNo = itemView.findViewById(R.id.otkl);
            mestOtd = itemView.findViewById(R.id.mestOtd);
        }
    }
}
