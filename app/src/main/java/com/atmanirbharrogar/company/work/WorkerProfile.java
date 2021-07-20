package com.atmanirbharrogar.company.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class WorkerProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase, msubref,workref;
    TextView name, LandMark, aadhaar, mobile, state, city, address, profession,newwork, category,comments, exper;
    ImageView profile;
    Toolbar toolbar;
    ProgressBar pd;
    ImageView header, image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

      //  LandMark = findViewById(R.id.mail_recruiter);
        name = findViewById(R.id.Seeker_name);
        aadhaar = findViewById(R.id.Seeker_adhar_no);
        mobile = findViewById(R.id.Seeker_mob_no);
        comments = findViewById(R.id.textviewcomments);
        exper = findViewById(R.id.exp);
        newwork = findViewById(R.id.new_work);
        category = findViewById(R.id.Seeker_type);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        pd = new ProgressBar(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        msubref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());
        image = findViewById(R.id.Img_profile_Seeker);
        workref = mDatabase.child("Extra detail of seeker").child("Experience of all").child(mAuth.getCurrentUser().getUid());



        /*setSupportActionBar(findViewById(R.id.toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new checkInternetConnection(this).checkConnection();
        name = (TextView) findViewById(R.id.name_seeker);
        mail = (TextView) findViewById(R.id.mail_seeker);
        address = (TextView) findViewById(R.id.seeker_address);
        aadhaar = (TextView) findViewById(R.id.seeker_aadhaar);
        city = (TextView) findViewById(R.id.seeker_city);
        mobile = (TextView) findViewById(R.id.seeker_mobile);
        state = (TextView) findViewById(R.id.seeker_state);
        profession = (TextView) findViewById(R.id.seeker_profile_profession);
        profile = findViewById(R.id.profile_image_seeker);*/

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        retrieve();

        //retrieve();

       /* final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Your Profile");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });*/
    }




   /* public void retrieve() {
        FirebaseUser user = mAuth.getCurrentUser();
        msubref = mDatabase.child("Users").child(user.getUid());
        msubref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue(String.class));
                mail.setText(dataSnapshot.child("Email").getValue(String.class));
                address.setText(dataSnapshot.child("Address").getValue(String.class));
                aadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
                city.setText(dataSnapshot.child("City").getValue(String.class));
                mobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
                state.setText(dataSnapshot.child("State").getValue(String.class));
                profession.setText(dataSnapshot.child("Profession").getValue(String.class));
                if (dataSnapshot.hasChild("urlToImage")) {
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(profile);
                    //Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }*/





    public void retrieve() {



        msubref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue(String.class));
               // LandMark.setText(dataSnapshot.child("LandMark").getValue(String.class));
                mobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
                aadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
                category.setText(dataSnapshot.child("Profession").getValue(String.class));
                String experience = dataSnapshot.child("Experience").getValue(String.class);
                String xp = String.valueOf(experience);
                exper.setText(xp);
                //headermail.setText(dataSnapshot.child("Email").getValue(String.class));
                //headername.setText(dataSnapshot.child("Name").getValue(String.class));
                //newwork.setText(dataSnapshot.child("").getValue(String.class));
                if (dataSnapshot.hasChild("urlToImage")) {
                  //  Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(headerimage);
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        workref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("New work")) {
                    newwork.setText(dataSnapshot.child("New work").getValue(Long.class).toString());
                } else {
                    newwork.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

















    @Override
    public void onRestart() {
        super.onRestart();
        //overridePendingTransition(0, 0);
    }

    public void edit_seeker(View v) {
        startActivity(new Intent(WorkerProfile.this, WorkerEdit.class));
    }
}