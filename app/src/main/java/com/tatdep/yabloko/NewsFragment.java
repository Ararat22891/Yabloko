package com.tatdep.yabloko;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.tatdep.yabloko.adapters.newsAdapterListView;

import com.tatdep.yabloko.cods.NewsDataAddList;
import com.tatdep.yabloko.cods.OnDetectScrollListener;

import com.tatdep.yabloko.cods.getNormData;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.tatdep.yabloko.cods.post_Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import okhttp3.internal.cache.DiskLruCache;

public class NewsFragment extends Fragment {

    private com.tatdep.yabloko.cods.ListView listView;
    private RelativeLayout rel1;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("post").getRef();
    private TableLayout tb;
    ArrayList<NewsDataAddList> arrayList;
    private newsAdapterListView list;
    private boolean isPart = false;
    MainActivity mainActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        init(view);

        TypedValue tv = new TypedValue();

//        listView.setOnDetectScrollListener(new OnDetectScrollListener() {
//
//
//            @Override
//            public void onUpScrolling() {
//               if (tb.getVisibility()==View.GONE){
//                    tb.setVisibility(View.VISIBLE);
//
//               }
//
//            }
//
//            @Override
//            public void onDownScrolling() {
//                if (tb.getVisibility()==View.VISIBLE){
//                    tb.setVisibility(View.GONE);
//                }
//
//            }
//        });
        addDataOnListView();
        return view;
    }



    private void init(View view){

        rel1 = view.findViewById(R.id.rel1);
        tb = (TableLayout) getActivity().findViewById(R.id.main_menu);
        arrayList = new ArrayList<NewsDataAddList>();
        list = new newsAdapterListView(getActivity(), arrayList);
        listView = view.findViewById(R.id.list_view);
        mainActivity = (MainActivity)getActivity();

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
                ImageButton  enter= mainActivity.findViewById(R.id.imageButton4);
                enter.setVisibility(isPart?View.VISIBLE: View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataOnListView(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (arrayList.size()>0) arrayList.clear();



                for (DataSnapshot ds: snapshot.getChildren()){

                    post_Data ps = ds.getValue(post_Data.class);
                    assert  ps!=null;
                    arrayList.add(new NewsDataAddList(ps.userId, ps.text, ps.photoUrl , ps.quantityLike, getNormData.getDate(ps.dateOfPubl),  ps.tm));
                    arrayList.sort(new Comparator<NewsDataAddList>() {
                        @Override
                        public int compare(NewsDataAddList newsDataAddList, NewsDataAddList t1) {
                            return newsDataAddList.getTm().compareTo(t1.getTm());
                        }
                    });
                    Collections.reverse(arrayList);


                }

                list.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }


        };
        db.addValueEventListener(vListener);
        listView.setAdapter(list);
    }



}