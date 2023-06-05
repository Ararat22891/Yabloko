package com.tatdep.yabloko;


import static android.content.ContentValues.TAG;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.EmailSender;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    AccountFragment accountFragment = new AccountFragment();
    private ListView listView;
    private TableLayout table;
    private CalendarView calendarView;
    MeroprFragment meroprFragment = new MeroprFragment();



    NewsFragment newsFragment = new NewsFragment();
    EnterPartyFragment enterPartyFragment = new EnterPartyFragment();
    AddEventFragment addEventFragment = new AddEventFragment();
    MainParty mainParty = new MainParty();
    Calenarragment calendar = new Calenarragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    AppealReadFragment appealReadFragment = new AppealReadFragment();

    private MenuItem menuItem;
    private ImageButton enter,search,settings;
    private TextView textView;
    private Toolbar toolbar;
    private  boolean isPart;
    Button donateButton;
    String USER = "user";
    private TextView nameAge,rasp, txt1,txt2,txt3,txt4,txt5;
    private EditText ed1,ed2,ed3,ed4,ed5;
    private CircleImageView icon;


    private boolean edit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            meroprFragment.setContext(MainActivity.this);
            pushFragments("account", accountFragment);
            pushFragments("news", newsFragment);
            pushFragments("appealReadFragment", appealReadFragment);
            pushFragments("merop",meroprFragment);
            pushFragments("addEventFragment", addEventFragment);
            pushFragments("enter", enterPartyFragment);
            pushFragments("calendar", calendar);

        }

        setContentView(R.layout.activity_main);
        init();

        getSplittedPathChild pC = new getSplittedPathChild();
        DatabaseReference db;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String splittedPathChild = pC.getSplittedPathChild(user.getEmail());

        db = FirebaseDatabase.getInstance().getReference("user").child(splittedPathChild).child("acc").child("dolz").getRef();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chlenPart = snapshot.getValue(String.class);
                isPart = Objects.equals(chlenPart, "Член партии");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textView = findViewById(R.id.textView8);

        enter= findViewById(R.id.imageButton4);
        search = findViewById(R.id.imageButton5);
        settings = findViewById(R.id.settings_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);


        bottomNavigationView.setOnNavigationItemSelectedListener(getBottom());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, newsFragment);
        fragmentTransaction.commit();
    }

    @NonNull
    private BottomNavigationView.OnNavigationItemSelectedListener getBottom(){

        return (item ) ->{


            switch (item.getItemId()){
                case R.id.accs:
                    textView.setText("Аккаунт");
//                    change_Fragment(accountFragment, "acc");

                    pushFragments("account", accountFragment); //mainParty для проверки  account
                    settings.setVisibility(View.VISIBLE);
                    enter.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    search.setImageResource(R.drawable.ic_baseline_edit_24);
                    break;
                case R.id.merop:
                    textView.setText("Обращения");
                    if (isPart) {
                        pushFragments("appealReadFragment", appealReadFragment);
                    } else {
                        pushFragments("merop", meroprFragment);
                    }
                    settings.setVisibility(View.GONE);
                    enter.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    break;
                case R.id.news:
//                    change_Fragment(newsFragment, "news");
                    textView.setText("Новости");


                    pushFragments("news", newsFragment);
                    if (isPart) {
                        enter.setVisibility(View.VISIBLE);
                    } else {
                        enter.setVisibility(View.GONE);
                    }
                    search.setImageResource(R.drawable.search);
                    settings.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    //search.setVisibility(View.VISIBLE);

                    break;
                case R.id.enterto:
                    textView.setText("Вступить");
//                    change_Fragment(enterPartyFragment, "partyIn");
                    pushFragments("enter", enterPartyFragment);
                    settings.setVisibility(View.GONE);
                    enter.setVisibility(View.GONE);
                    search.setVisibility(View.GONE);
                    break;


            }
            return true;
        };


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.imageButton4:
//                change_Fragment(addEventFragment, "addEventFragment");
                pushFragments("addEventFragment", addEventFragment);
                break;
            case R.id.button:

                pushFragments("calendar", calendar);
                break;
            case R.id.imageButton5:
                 RelativeLayout rl1,rl2,rl3,rl4,rl5;
                 ImageView redact;
                 txt1 = findViewById(R.id.pol);
                 txt2 = findViewById(R.id.polx);
                 txt3 = findViewById(R.id.pola);
                 txt4 = findViewById(R.id.polq);
                 txt5 = findViewById(R.id.polw);


                init();
                rl1 = findViewById(R.id.relative1);
                rl2 = findViewById(R.id.relative2);
                rl3 = findViewById(R.id.relative3);
                rl4 = findViewById(R.id.relative4);
                rl5 = findViewById(R.id.relative5);
                redact = findViewById(R.id.imageButton5);
                if(edit==false){

                    edit = true;
                    redact.setImageResource(R.drawable.save);
                    rl1.setBackground(getResources().getDrawable(R.drawable.shape,null));
                    rl5.setBackground(getResources().getDrawable(R.drawable.shape,null));
                    rl4.setBackground(getResources().getDrawable(R.drawable.shape,null));
                    rl3.setBackground(getResources().getDrawable(R.drawable.shape,null));
                    rl2.setBackground(getResources().getDrawable(R.drawable.shape,null));

                    txt1.setTextColor(getResources().getColor(R.color.white));
                    txt2.setTextColor(getResources().getColor(R.color.white));
                    txt3.setTextColor(getResources().getColor(R.color.white));
                    txt4.setTextColor(getResources().getColor(R.color.white));
                    txt5.setTextColor(getResources().getColor(R.color.white));

                    ed1.setTextColor(getResources().getColor(R.color.white));
                    ed1.setEnabled(true);

                    ed2.setTextColor(getResources().getColor(R.color.white));
                    ed2.setEnabled(true);

                    ed3.setTextColor(getResources().getColor(R.color.white));
                    ed3.setEnabled(true);

                    ed4.setTextColor(getResources().getColor(R.color.white));
                    ed4.setEnabled(true);

                    ed5.setTextColor(getResources().getColor(R.color.white));
                    ed5.setEnabled(true);

                }
                else
                {
                    edit = false;
                    saveData();
                    txt1.setTextColor(getResources().getColor(R.color.gray_text));
                    txt2.setTextColor(getResources().getColor(R.color.gray_text));
                    txt3.setTextColor(getResources().getColor(R.color.gray_text));
                    txt4.setTextColor(getResources().getColor(R.color.gray_text));
                    txt5.setTextColor(getResources().getColor(R.color.gray_text));

                    redact.setImageResource(R.drawable.ic_baseline_edit_24);
                    rl1.setBackground(getResources().getDrawable(R.drawable.shape_non,null));
                    ed1.setHintTextColor(getResources().getColor(R.color.gray_text));
                    ed1.setTextColor(getResources().getColor(R.color.gray_text));
                    ed1.setEnabled(false);

                    rl5.setBackground(getResources().getDrawable(R.drawable.shape_non,null));
                    ed2.setTextColor(getResources().getColor(R.color.gray_text));
                    ed2.setEnabled(false);

                    rl4.setBackground(getResources().getDrawable(R.drawable.shape_non,null));
                    ed3.setTextColor(getResources().getColor(R.color.gray_text));
                    ed3.setEnabled(false);

                    rl3.setBackground(getResources().getDrawable(R.drawable.shape_non,null));
                    ed4.setTextColor(getResources().getColor(R.color.gray_text));
                    ed4.setEnabled(false);

                    rl2.setBackground(getResources().getDrawable(R.drawable.shape_non,null));
                    ed5.setTextColor(getResources().getColor(R.color.gray_text));
                    ed5.setEnabled(false);
                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }



private void saveData(){
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference ref = db.getReference(USER);
    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();

    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            User user1 = snapshot.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").getValue(User.class);
            Map<String, Object> map = user1.toMap();
                map.put("polit_pred",ed1.getText().toString());
                map.put("regilgia",ed2.getText().toString());
                map.put("mainInLife",ed3.getText().toString());
                map.put("mainInMan",ed4.getText().toString());
                map.put("vdox",ed5.getText().toString());
                ref.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").updateChildren(map);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    private void init(){
        ed1 = findViewById(R.id.ed1);

        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        ed5 = findViewById(R.id.ed5);
        icon = findViewById(R.id.icon);
        listView = findViewById(R.id.list_view);
        table = findViewById(R.id.main_menu);
    }

    public void onSettingsClick(View v){
        pushFragments("settings", settingsFragment);
        search.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);
    }

    public void onDonateClick(View v){
        PaymentFragment paymentFragment = new PaymentFragment();
        pushFragments("donate", paymentFragment);

    }

    private void change_Fragment(Fragment fr , String tag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.framelayout, fr);
        ft.addToBackStack(null);


        ft.commit();
    }

    public void hideTopMenu(boolean hide){
        if (hide) {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.news_top_menu);
            // запуск анимации
            table.startAnimation(animation);

            table.setVisibility(View.GONE);
        } else {
            table.setVisibility(View.VISIBLE);
        }

    }

    public void pushFragments(String tag, Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null){
            ft.add(R.id.framelayout, fragment, tag);
        }

        Fragment accountFragment = manager.findFragmentByTag("account");
        Fragment appealReadFragment = manager.findFragmentByTag("appealReadFragment");
        Fragment news = manager.findFragmentByTag("news");
        Fragment settings = manager.findFragmentByTag("settings");
        Fragment calendar = manager.findFragmentByTag("calendar");
        Fragment addEventFragment = manager.findFragmentByTag("addEventFragment");
        Fragment enter = manager.findFragmentByTag("enter");
        Fragment merop = manager.findFragmentByTag("merop");
        Fragment mainPartys = manager.findFragmentByTag("mainParty");
        Fragment donate = manager.findFragmentByTag("donate");

        if (accountFragment !=null)
            ft.hide(accountFragment);
        if(appealReadFragment != null)
            ft.hide(appealReadFragment);
        if (news !=null)
            ft.hide(news);
        if (settings!=null)
            ft.hide(settings);
        if (calendar!=null)
            ft.hide(calendar);
        if (addEventFragment !=null)
            ft.hide(addEventFragment);
        if (enter!=null)
            ft.hide(enter);
        if (merop!=null)
            ft.hide(merop);
        if (mainPartys!=null)
            ft.hide(mainPartys);
        if (donate!=null)
            ft.hide(donate);


        if (tag == "account"){
            if (accountFragment != null)
                ft.show(accountFragment);
        }

        if (tag == "appealReadFragment"){
            if (appealReadFragment != null)
                ft.show(appealReadFragment);
        }

        if (tag == "donate"){
            if (donate != null)
                ft.show(donate);
        }

        if (tag == "news"){
            if (news != null)
                ft.show(news);
        }

        if (tag == "settings"){
            if (settings != null)
                ft.show(settings);
        }

        if (tag == "calendar"){
            if (calendar != null)
                ft.show(calendar);
        }

        if (tag == "addEventFragment"){
            if (addEventFragment != null)
                ft.show(addEventFragment);
        }



        if (tag == "enter"){
            if (enter != null)
                ft.show(enter);
        }

        if (tag == "merop"){
            if (merop != null)
                ft.show(merop);
        }
        if (tag == "mainParty"){
            if (mainPartys != null)
                ft.show(mainPartys);
        }
        ft.commitAllowingStateLoss();
    }
    
    public void updateFragment(String tag,Fragment fr){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(enterPartyFragment);
        ft.replace(R.id.framelayout, enterPartyFragment);
        ft.commitAllowingStateLoss();
    }

}