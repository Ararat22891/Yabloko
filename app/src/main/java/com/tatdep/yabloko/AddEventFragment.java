package com.tatdep.yabloko;

import static android.app.Activity.RESULT_OK;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static okhttp3.internal.http.HttpDate.format;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tatdep.yabloko.cods.Accounts_Data;
import com.tatdep.yabloko.cods.autosave;
import com.tatdep.yabloko.cods.generatorWord;
import com.tatdep.yabloko.cods.getSplittedPathChild;
import com.tatdep.yabloko.cods.post_Data;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEventFragment extends Fragment {

    private  Button button, buttonAddMedia;
    private TextView textViewNews;
    private EditText editText;
    private  String USER = "user";
    private  String POST = "post";
    private RelativeLayout main;
    static  String  photoUrl = "";
    String userFI ="";
    private  String pathDb;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference pathMain = storageReference.child("postPhotos");
    private String textOfPost = "";
    private SpinKitView progress;
    private Date date = new Date();
    getSplittedPathChild getSplittedPathChild = new getSplittedPathChild();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbuser = database.getReference().child("user");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_add_event, container, false);
       init(view);
       buttonAddMedia.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               photo_add();
           }
       });
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                hideKeyboard(view);

                if (pathMain.getActiveUploadTasks().size() ==0){
                    if (editText.getText().toString().isEmpty()){
                        Toast.makeText(((MainActivity)getActivity()), "Вы не ввели текст", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Format format = new SimpleDateFormat("dd-MM-yyyy HH-mm");
                    String d = format(date);
                    pathDb = getSplittedPathChild.getSplittedPathChild(generatorWord.generateRandomWord(15).toString());

                    dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            photoUrl = autosave.urls;
                            userFI = getName(snapshot);

                            post_Data post = new post_Data(photoUrl,text_db_add(), d,userFI, "0", Timestamp.now().getSeconds());
                            database.getReference().child("post").child(pathDb).setValue(post);
                            editText.setText("");
                            ((MainActivity)getActivity()).pushFragments("news", ((MainActivity) getActivity()).newsFragment);
                            autosave.setPathOfPhoto("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



           }
       });
        return view;
    }
    private void init(View view){
        button = view.findViewById(R.id.button_addNews);
        editText = view.findViewById(R.id.textOfNews);
        buttonAddMedia = view.findViewById(R.id.buttonAddMedia);
        textViewNews = view.findViewById(R.id.textViewAddedMedia);
        progress = view.findViewById(R.id.progrss_bar);
        main = view.findViewById(R.id.main_layout);
    }


    private  String text_db_add(){
        textOfPost = editText.getText().toString();
        return textOfPost;
    }


    public  void photo_add(){
        CropImage.activity()
                .setAspectRatio(1,1)
                .setRequestedSize(848, 480)
                .start((MainActivity)getActivity(), this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK && data !=null){
            Uri uri = CropImage.getActivityResult(data).getUri();

            pathDb = getSplittedPathChild.getSplittedPathChild(generatorWord.generateRandomWord(15).toString());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference path = storageReference.child("postPhotos").child(pathDb);

            path.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task2) {
                                List<UploadTask> tasks = path.getActiveUploadTasks();
                                if (task2.isSuccessful() && tasks.size() ==0){
                                    photoUrl = task2.getResult().toString();
                                    autosave.urls = photoUrl;
                                }
                            }
                        });

                    }
                }
            });
        }
    }


    private String getName(DataSnapshot snapshot){
        Accounts_Data acData = snapshot.child(getSplittedPathChild.getSplittedPathChild(user.getEmail())).child("acc").getValue(Accounts_Data.class);
        assert acData != null;
        return acData.name + " " + acData.surname;
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}