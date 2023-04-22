package com.tatdep.yabloko;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.User;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    String USER = "user";
    private TextView nameAge,rasp;
    private EditText ed1,ed2,ed3,ed4,ed5;
    private CircleImageView icon;
    private SpinKitView progressBarr;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.fragment_account, container, false);
         setRetainInstance(true);

         init(view);
         icon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 photo_change();
             }
         });
        viewData();
        return view;
    }


    private void viewData(){
        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDb.getReference(USER);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setTextPhoto(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(View v){
        icon=v.findViewById(R.id.icon);
        nameAge = v.findViewById(R.id.name_surname);
        rasp = v.findViewById(R.id.rasp);
        ed1 =v.findViewById(R.id.ed1);
        ed2 =v.findViewById(R.id.ed2);
        ed3 =v.findViewById(R.id.ed3);
        ed4 =v.findViewById(R.id.ed4);
        ed5 =v.findViewById(R.id.ed5);

        progressBarr = v.findViewById(R.id.progress_bar);
    }

    private void setTextPhoto(DataSnapshot snapshot){
        String nameAges, dolzRegion, politPredp, religia, mainInLife, maininMan, vdpx;
        getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Accounts_Data acData = snapshot.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").getValue(Accounts_Data.class);
        if(!acData.age.isEmpty()) {
            nameAges= acData.surname+" "+ acData.name+", "+acData.age;
        }
        else
            nameAges= acData.surname+" "+ acData.name;

        if (!acData.dolz.isEmpty()){
            dolzRegion = acData.dolz+", "+acData.region;
        }
        else
            dolzRegion = acData.region;
        politPredp = acData.polit_pred;
        religia = acData.regilgia;
        mainInLife = acData.mainInLife;
        maininMan = acData.mainInMan;
        vdpx = acData.vdox;

        if (acData.profileIcon.isEmpty()){
            icon.setImageResource(R.drawable.kreugl);
        }
        else{
            Picasso.get().load(acData.profileIcon).placeholder(R.drawable.kreugl)
                    .into(icon);
        }

        ed1.setText(politPredp);
        ed2.setText(religia);
        ed3.setText(mainInLife);
        ed4.setText(maininMan);
        ed5.setText(vdpx);
        nameAge.setText(nameAges);
        rasp.setText(dolzRegion);

    }

    public  void photo_change(){
        CropImage.activity()
                .setAspectRatio(1,1)
                .setRequestedSize(600, 600)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start((MainActivity)getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            progressBarr.setVisibility(View.VISIBLE);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK && data !=null){
            Uri uri = CropImage.getActivityResult(data).getUri();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
            FirebaseDatabase database = FirebaseDatabase.getInstance();



            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference path = storageReference.child("profilePhotos").child(getSplittedPathChild.getSplittedPathChild(user.getEmail()));
            path.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task2) {
                                if (task2.isSuccessful()){
                                    String photoUrl = task2.getResult().toString();
                                    database.getReference().child("user").child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").child("profileIcon").setValue(photoUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task3) {
                                            if (task3.isSuccessful()){
                                                Picasso.get().load(photoUrl).placeholder(R.drawable.kreugl)
                                                        .into(icon);
                                                progressBarr.setVisibility(View.GONE);

                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            });
        }
    }

}