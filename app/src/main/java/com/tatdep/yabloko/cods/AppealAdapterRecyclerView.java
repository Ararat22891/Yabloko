package com.tatdep.yabloko.cods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AppealAdapterRecyclerView extends RecyclerView.Adapter<AppealAdapterRecyclerView.ViewHolder> {
    private ArrayList<Appeal> appeals;
    private Context context;
    Appeal currentAppeal;
    public String btn_name = "Отправить";
    public boolean isDeleting = false;
    private ItemClickListener itemClickListener;
    private DatabaseReference databaseReference;
    private StatusUpdateListener statusUpdateListener;




    public AppealAdapterRecyclerView(Context context, ArrayList<Appeal> appeals) {
        this.context = context;
        this.appeals = appeals;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appeals, parent, false);
        return new ViewHolder(view);
    }

    public interface StatusUpdateListener {
        void onStatusUpdate(int position, String newStatus);
    }

    public void setStatusUpdateListener(StatusUpdateListener listener) {
        this.statusUpdateListener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         currentAppeal = appeals.get(position);

        if (isDeleting) {
            holder.edtReplay.setVisibility(View.GONE);
        } else {
            holder.edtReplay.setVisibility(View.VISIBLE);
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.senderEmail.setText(currentAppeal.getSenderEmail());
        holder.appealSubject.setText(currentAppeal.getAppealType());
        holder.appealStatus.setText(currentAppeal.getStatus());
        holder.userMessage.setText(currentAppeal.getMessage());
        holder.btn.setText(btn_name);
        getSplittedPathChild pC = new getSplittedPathChild();

        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("user").child(splittedPathChild).child("appeals").getRef();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Изменение видимости expandableContent
                if (holder.expandableContent.getVisibility() == View.VISIBLE) {
                    holder.expandableContent.setVisibility(View.GONE);
                } else {
                    holder.expandableContent.setVisibility(View.VISIBLE);
                }

                if (!isDeleting){
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appealSnapshot : dataSnapshot.getChildren()) {
                                Appeal updatedAppeal = appealSnapshot.getValue(Appeal.class);
                                if (updatedAppeal != null && updatedAppeal.equals(currentAppeal)) {
                                    if (Objects.equals(updatedAppeal.getStatus(), "Прочитано")){
                                        return;
                                    }

                                    if(Objects.equals(updatedAppeal.getStatus(), "Отвечено")){
                                        return;
                                    }
                                    currentAppeal = updatedAppeal;

                                    // Изменение статуса обращения
                                    currentAppeal.setStatus("Прочитано");

                                    // Обновление только для текущей позиции
                                    notifyItemChanged(holder.getAdapterPosition());

                                    // Обновление статуса в Firebase
                                    DatabaseReference appealRef = appealSnapshot.getRef();
                                    appealRef.child("status").setValue(currentAppeal.getStatus());

                                    break; // Прерываем цикл, так как элемент уже найден
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Обработка ошибки
                        }
                    });
                }
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isDeleting) {
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot appealSnapshot : dataSnapshot.getChildren()) {
                                Appeal updatedAppeal = appealSnapshot.getValue(Appeal.class);
                                if (updatedAppeal != null && updatedAppeal.equals(currentAppeal)) {

                                    currentAppeal = updatedAppeal;

                                    // Изменение статуса обращения
                                    currentAppeal.setStatus("Отвечено"); // Замените "Новый статус" на нужное значение

                                    // Обновление только для текущей позиции
                                    notifyItemChanged(holder.getAdapterPosition());

                                    // Обновление статуса в Firebase
                                    DatabaseReference appealRef = appealSnapshot.getRef();
                                    appealRef.removeValue();

                                    String recipient = currentAppeal.getSenderEmail(); // Адрес получателя
                                    String subject = "Ответ по вашему обращению по теме \"" + currentAppeal.getAppealType() + "\""; // Тема письма
                                    String body = holder.edtReplay.getText().toString();

                                    String bd = holder.edtReplay.getText().toString().trim();
                                    if (body.isEmpty() || body.trim().isEmpty()) {
                                        // Выводите сообщение об ошибке или выполняйте другие действия, соответствующие пустому полю body
                                        Toast.makeText(view.getContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else {
                                        String htmlBody = "<html><body>" +
                                                "<table>" +
                                                "<tr>" +
                                                "<td style=\"vertical-align: top; padding-right: 10px;\">" +
                                                "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg/1024px-%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%AF%D0%B1%D0%BB%D0%BE%D0%BA%D0%BE_%D0%BC%D0%B0%D0%BB%D0%B5%D0%BD%D1%8C%D0%BA%D0%B8%D0%B9_%D0%BF%D1%80%D0%BE%D0%B7%D1%80%D0%B0%D1%87%D0%BD%D1%8B%D0%B9.svg.png\" style=\"width: 50px; height: 50px;\">" +
                                                "</td>" +
                                                "<td>" +
                                                "<h1>Привет!</h1>" +
                                                "<p>Ниже приведен ответ на ваше сообщение:</p>" +
                                                "<p style=\"font-size: 18px;\"><strong>" + body + "</strong></p>" +
                                                "<p>Партия Яблоко</p>" +
                                                "</td>" +
                                                "</tr>" +
                                                "</table>" +
                                                "</body></html>";

                                        EmailSender.sendEmail(recipient, subject, htmlBody);
                                    }




                                    return; // Прерываем цикл, так как элемент уже найден
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Обработка ошибки
                        }
                    });
                }
                else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("user").getRef();

                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot appealSnapshot: snapshot.getChildren() ){
                                getSplittedPathChild pC = new getSplittedPathChild();
                                User user = appealSnapshot.getValue(User.class);
                                if (user !=null) {
                                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("user").child(pC.getSplittedPathChild(user.email)).child("appeals").getRef();
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot childsnapshot) {
                                            for (DataSnapshot child: childsnapshot.getChildren()){
                                                Appeal acc = child.getValue(Appeal.class);
                                                if (acc != null) {
                                                    if (acc.equals(currentAppeal)) {
                                                        currentAppeal = acc;
                                                        DatabaseReference appealRef = child.getRef();
                                                        appealRef.removeValue();
                                                    }
                                                }
                                            }


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
            }
        });
    }



    @Override
    public int getItemCount() {
        return appeals.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setDatabaseReference(DatabaseReference reference) {
        this.databaseReference = reference;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderEmail, appealSubject, appealStatus, userMessage;
        Button btn;
        LinearLayout expandableContent;
        EditText edtReplay;

        public ViewHolder(View itemView) {
            super(itemView);
            senderEmail = itemView.findViewById(R.id.text_sender_email);
            appealSubject = itemView.findViewById(R.id.text_appeal_subject);
            appealStatus = itemView.findViewById(R.id.text_appeal_status);
            userMessage = itemView.findViewById(R.id.text_user_message);
            expandableContent = itemView.findViewById(R.id.expandable_content);
            edtReplay = itemView.findViewById(R.id.edit_reply);
            btn = itemView.findViewById(R.id.button_send_reply);
        }
    }
}
