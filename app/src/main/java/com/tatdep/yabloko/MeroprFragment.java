package com.tatdep.yabloko;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.Appeal;
import com.tatdep.yabloko.cods.getSplittedPathChild;

import java.util.ArrayList;
import java.util.List;

public class MeroprFragment extends Fragment {

    Spinner spinner, spinnerAppealsType;
    private Context context;

    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meropr, container, false);
        init(v);
        return v;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    private void init(View v) {
        spinner = v.findViewById(R.id.spinnerReg);
        spinnerAppealsType = v.findViewById(R.id.spinnerAppealsType);
        List<String> appeals = new ArrayList<>();
        appeals.add("Экономические вопросы");
        appeals.add("Социальные вопросы");
        appeals.add("Вопросы безопасности");
        appeals.add("Экологические вопросы");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spin, appeals);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);
        spinner.setAdapter(adapter);

        btn = v.findViewById(R.id.gr_button);
        // Сохраняем контекст фрагмента в переменную
        Context context = getContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> memberNames = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot accSnapshot = userSnapshot.child("acc");
                    Accounts_Data account = accSnapshot.getValue(Accounts_Data.class);

                    if (account != null && "Член партии".equals(account.dolz)) {
                        memberNames.add(account.surname+" " + account.name +" "+ account.patronomyc );
                    }
                }

                // Используем сохраненный контекст для создания адаптера
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spin, memberNames);
                adapter.setDropDownViewResource(R.layout.spin_dropdown);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок, если таковые возникнут
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSplittedPathChild getSplittedPathChild= new getSplittedPathChild();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String selectedMember = spinner.getSelectedItem().toString();
                String[] nameParts = selectedMember.split(" ");


                String senderName = getSplittedPathChild.getSplittedPathChild(currentUser.getEmail()); // Имя отправителя (первая часть ФИО)
                String senderEmail = currentUser.getEmail(); // Почта отправителя
                String message = "Текст сообщения"; // Текст сообщения

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("user");

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int index = 0; // Переменная для хранения индекса объекта
                        for (DataSnapshot accSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference userRef = accSnapshot.getRef().getParent(); // Получаем ссылку на родительский узел
                            Accounts_Data account = accSnapshot.child("acc").getValue(Accounts_Data.class);

                            if (account != null && "Член партии".equals(account.dolz) && index == spinner.getSelectedItemPosition()) {

                                String userId = accSnapshot.getRef().getKey();
                                DatabaseReference appealsRef = userRef.child(userId).child("appeals").push();
                                Appeal newAppeal = new Appeal(senderName, spinner.getSelectedItem().toString(), senderEmail, message, "Новое");

                                appealsRef.setValue(newAppeal);
                            }
                            index++;
                        }
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Обработка ошибок, если таковые возникнут
                    }
                });
            }
        });

    }


}