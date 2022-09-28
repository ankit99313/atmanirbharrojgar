package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PhotoUpload extends AppCompatActivity {

    int SELECT_PICTURE = 100;
    ImageView image;
    Uri filepath;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private DatabaseReference rdatabase;
    private String ruserid;
    private StorageReference rstorage;
    private StorageReference rstorageReference;
    private StorageReference sstorageReference;
    private DatabaseReference msubref;
    private String category;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        setTitle("Select Image");
        new checkInternetConnection(this).checkConnection();
        image = findViewById(R.id.uploadimage);
        pd = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        rstorage = FirebaseStorage.getInstance().getReference();
        ruserid = mAuth.getCurrentUser().getUid();
        rdatabase = FirebaseDatabase.getInstance().getReference("Users").child(ruserid);
        msubref = FirebaseDatabase.getInstance().getReference("Seeker");
        rstorageReference = rstorage.child("Images").child("RecruiterImages");
        sstorageReference = rstorage.child("Images").child("SeekerImages");
        typeOfUser();

    }

    public void typeOfUser() {
        FirebaseDatabase.getInstance().getReference("Users").child(ruserid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category = dataSnapshot.child("Profession").getValue(String.class);
                type = dataSnapshot.child("Type").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void chooseimage(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void uploadimage(View v) {
        StorageReference ref;

        ref = sstorageReference.child(mAuth.getCurrentUser().getUid());

        if (filepath != null) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading....");
            pd.show();

            if (ref != null) ref = rstorageReference.child(mAuth.getCurrentUser().getUid());

            ref.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(pd::dismiss, 500);
                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                result.addOnSuccessListener(uri -> {
                    String urlToImage = uri.toString();
                    rdatabase.child("urlToImage").setValue(urlToImage);
                    if (type.equals("Seeker"))
                        FirebaseDatabase.getInstance().getReference("Seeker").child(category).child("urlToImage").setValue(urlToImage);
                    else if (type.equals("Recruiter"))
                        FirebaseDatabase.getInstance().getReference("Recruiter").child(category).child("urlToImage").setValue(urlToImage);

                    Picasso.get().load(urlToImage).transform(new CircularTransformation()).into(image);
                });


                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(getApplication(), "Uploading failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Uploaded " + (int) progress + "%");
            });


        } else {
            Toast.makeText(getApplication(), "No file selected", Toast.LENGTH_SHORT).show();
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                filepath = data.getData();

                if (filepath != null) {
                    Picasso.get().load(filepath).transform(new CropCircleTransformation()).into(image);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
