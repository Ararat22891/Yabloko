package com.tatdep.yabloko;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.tatdep.yabloko.cods.requestParty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class MainParty extends Fragment {

    private Spinner regOtd, mestnOtd;
    private EditText telNumber, fio, date_birth, grazd;
    private DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("user");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Button btn;
    private RadioGroup rd;
    private RadioButton rd1, rd2;
    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_party, container, false);
        init(view);
        String[] s = getResources().getStringArray(R.array.regions);
        String[] empty = new String[]{"не выбрано"};
        String[] vladimirsk = getResources().getStringArray(R.array.mestOtdVladimir);
        String[] Volgo = getResources().getStringArray(R.array.mestOtdVolg);
        String[] Vologodsk = getResources().getStringArray(R.array.mestOtdVologod);
        String[] ivanov = getResources().getStringArray(R.array.mestOtdIvanov);
        String[] irkutsk = getResources().getStringArray(R.array.mestOtdIrku);
        String[] moscow = getResources().getStringArray(R.array.mestOtdMoscow);
        String[] kabard =  getResources().getStringArray(R.array.mestOtdKabard);
        String[] kaliningrad =  getResources().getStringArray(R.array.mestOtdKaliningr);

        setDataAdapter(regOtd,s);
        regOtd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int gettedItem = regOtd.getSelectedItemPosition();

                switch (gettedItem) {
                    case 0:
                        setDataAdapter(mestnOtd, empty);
                        break;

                    case 1:
                            setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdAltkray));
                        break;

                    case 2:
                            setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdAmur));
                        break;

                    case 3:
                            setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdArhangel));
                            break;

                    case 4:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdAstrah));
                        break;

                    case 5:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdBelgor));
                        break;

                    case 6:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdBryansk));
                        break;

//
                    case 7:
                        setDataAdapter(mestnOtd, vladimirsk);
                        break;

                    case 8:
                        setDataAdapter(mestnOtd, Volgo);
                        break;

                    case 9:
                        setDataAdapter(mestnOtd, Vologodsk);
                        break;

                    case 10:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdVorone));
                        break;

                    case 11:
                        setDataAdapter(mestnOtd, moscow);
                        break;

                    case 12:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSpb));
                        break;
                        //

                    case 13:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdZabaik));
                        break;

                    case 14:
                        setDataAdapter(mestnOtd, ivanov);
                        break;

                    case 15:
                        setDataAdapter(mestnOtd, irkutsk);
                        break;

                    case 16:
                        setDataAdapter(mestnOtd, kabard);
                        break;

                    case 17:
                        setDataAdapter(mestnOtd, kaliningrad);
                        break;

                    case 18:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKaluga));
                        break;

//
                    case 19:
                        setDataAdapter(mestnOtd, vladimirsk);
                        break;

                    case 20:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKarac));
                        break;

                    case 21:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKemero));
                        break;

                    case 22:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKirovo));
                        break;

                    case 23:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKostrom));
                        break;

                    case 24:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKrasnodar));
                        break;
                        //

                    case 25:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKrasnoyarsk));
                        break;

                    case 26:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKurgans));
                        break;

                    case 27:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKursk));
                        break;

                    case 28:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdLeningr));
                        break;

                    case 29:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdLipetsk));
                        break;

                    case 30:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdMoscObl));
                        break;

//
                    case 31:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdMurmansk));
                        break;

                    case 32:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdNizhegorod));
                        break;

                    case 33:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdNovgorod));
                        break;

                    case 34:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdNovosib));
                        break;

                    case 35:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdOmsk));
                        break;

                    case 36:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdOrenburg));
                        break;
                    //

                    case 37:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdOrlov));
                        break;

                    case 38:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdPenzen));
                        break;

                    case 39:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdPerm));
                        break;

                    case 40:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdPrimor));
                        break;

                    case 41:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdPskov));
                        break;

                    case 42:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdAdyg));
                        break;

//
                    case 43:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdAlt));
                        break;

                    case 44:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdBashk));
                        break;

                    case 45:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdBuryat));

                    case 46:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdDages));
                        break;

                    case 47:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdIng));
                        break;

                    case 48:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdKalm));
                        break;
                    //
                    case 49:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdCarel));
                        break;

                    case 50:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdComi));
                        break;
                    //

                    case 51:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdMary));
                        break;

                    case 52:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdMord));
                        break;

                    case 53:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSaha));
                        break;

                    case 54:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdOse));
                        break;

                    case 55:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTatarstan));
                        break;

                    case 56:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTyva));
                        break;

//
                    case 57:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdHa));
                        break;

                    case 58:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdRostov));
                        break;

                    case 59:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdRyazn));
                        break;

                    case 60:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSamara));
                        break;

                    case 61:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSaratov));
                        break;

                    case 62:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSahalin));
                        break;
                    //

                    case 63:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSverd));
                        break;

                    case 64:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdSmol));
                        break;

                    case 65:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdStavrop));
                        break;

                    case 66:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTambov));
                        break;

                    case 67:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTver));
                        break;

                    case 68:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTomsk));
                        break;

//
                    case 69:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTulsk));
                        break;

                    case 70:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdTumen));
                        break;

                    case 71:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdUdm));
                        break;

                    case 72:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdUlyan));
                        break;

                    case 73:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdHabarov));
                        break;

                    case 74:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdHantyMans));
                        break;
                    //

                    case 75:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdChelyab));
                        break;

                    case 76:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdChecnya));
                        break;

                    case 77:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdChuvash));
                        break;

                    case 78:
                        setDataAdapter(mestnOtd, getResources().getStringArray(R.array.mestOtdYarosl));
                        break;
                    //
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Accounts_Data acc = snapshot.child(getSplittedPathChild.getSplittedPathChild(user.getEmail().toString())).child("acc").getValue(Accounts_Data.class);
                fio.setText(acc.surname+" "+ acc.name+" "+ acc.patronomyc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    private void init(View view){

        fio = view.findViewById(R.id.fio);
        regOtd = view.findViewById(R.id.spinnerReg);
        mestnOtd = view.findViewById(R.id.spinnerOtd);
        telNumber = view.findViewById(R.id.tel);
        btn = view.findViewById(R.id.gr_button);
        rd = view.findViewById(R.id.rediogroup);
        rd1 = view.findViewById(R.id.rd1);
        rd2 = view.findViewById(R.id.rd2);
        date_birth = view.findViewById(R.id.date_birth);
        grazd = view.findViewById(R.id.grazd);

        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER));
        formatWatcher.installOn(telNumber);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fioString = fio.getText().toString().trim();
                if (fioString.isEmpty() || fioString.split(" ").length != 3) {
                    fio.setHint("Введите корректные значения!");
                    Toast.makeText(getActivity(), "Пожалуйста, введите корректные ФИО", Toast.LENGTH_SHORT).show();
                    fio.setHintTextColor(getResources().getColor(R.color.red));
                    return;
                }
                if (regOtd.getSelectedItem().toString().equals("не выбрано")){
                    Toast.makeText(getActivity(), "Пожалуйста, введите рег отделение", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mestnOtd.getSelectedItem().toString().equals("Не выбрано")){
                    Toast.makeText(getActivity(), "Пожалуйста, введите местное отделение", Toast.LENGTH_SHORT).show();
                    return;
                }

                String telNumberString = telNumber.getText().toString().trim();
                if (telNumberString.isEmpty() || telNumberString.length() != 18) {
                    telNumber.setHint("Введите корректные значения!");
                    telNumber.setHintTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(getActivity(), "Пожалуйста, введите корректный телефон", Toast.LENGTH_SHORT).show();
                    return;
                }

                String dateOfBirthString = date_birth.getText().toString().trim();
                if (dateOfBirthString.isEmpty() || !isValidDate(dateOfBirthString)) {
                    date_birth.setHint("Введите корректные значения!");
                    Toast.makeText(getActivity(), "Пожалуйста, введите корректную даты", Toast.LENGTH_SHORT).show();
                    date_birth.setHintTextColor(getResources().getColor(R.color.red));
                    return;
                }
                String grazdString = grazd.getText().toString().trim();
                if (grazdString.isEmpty()) {
                    grazd.setHint("Введите корректные значения!");
                    Toast.makeText(getActivity(), "Пожалуйста, введите корректное гражданство", Toast.LENGTH_SHORT).show();
                    grazd.setHintTextColor(getResources().getColor(R.color.red));
                    return;
                }

                String sex = "";
                int checkedRadioButtonId = rd.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText((MainActivity)getActivity(), "Вы не выбрали свой пол", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if (checkedRadioButtonId == R.id.rd1) {
                        sex = "мужской";
                    }

                    if (checkedRadioButtonId == R.id.rd2) {
                        sex = "женский";
                    }
                }

                requestParty requestParty = new requestParty(regOtd.getSelectedItem().toString(), mestnOtd.getSelectedItem().toString(),
                        fio.getText().toString(), telNumber.getText().toString(), sex, date_birth.getText().toString(), grazd.getText().toString());

                database.getReference().child("request").child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).setValue(requestParty).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ((MainActivity)getActivity()).pushFragments("enter", ((MainActivity) getActivity()).enterPartyFragment);
                        }

                    }
                });
            }
        });
    }

    private void setDataAdapter(Spinner spinner,String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spin, array);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

    }
    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    }
