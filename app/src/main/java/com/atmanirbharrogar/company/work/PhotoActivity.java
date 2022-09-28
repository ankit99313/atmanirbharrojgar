package com.atmanirbharrogar.company.work;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    ImageView iv;
    StorageReference UserProfileImagesReference;
    FirebaseUser currentUserId;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference mDatabase, msubref;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setTitle("Your Image");

        new checkInternetConnection(this).checkConnection();
        iv = findViewById(R.id.iv);
        progressBar = findViewById(R.id.image_loading_progress);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileImagesReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();
        msubref = mDatabase.child("Users").child(user.getUid());
        msubref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("urlToImage")) {
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            iv.setImageResource(R.drawable.ic_baseline_error_24);
                        }

                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    iv.setColorFilter(ContextCompat.getColor(PhotoActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}