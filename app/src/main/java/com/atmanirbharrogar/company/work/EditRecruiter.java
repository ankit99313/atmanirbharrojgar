package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class EditRecruiter extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase, msubref, dupref;
    TextInputEditText name, mobile, state, city, address;
    TextView mail, aadhaar;
    ImageView profile;
    ProgressDialog pd;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recruiter);

        new checkInternetConnection(this).checkConnection();

        name = findViewById(R.id.name_recruiter_edit);
        mail = findViewById(R.id.mail_recruiter_edit);
        address = findViewById(R.id.recruiter_address_edit);
        aadhaar = findViewById(R.id.recruiter_aadhaar_edit);
        city = findViewById(R.id.recruiter_city_edit);
        mobile = findViewById(R.id.recruiter_mobile_edit);
        state = findViewById(R.id.recruiter_state_edit);
        profile = findViewById(R.id.profile_image_edit);
        pd = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        profile.setOnClickListener(v -> startActivity(new Intent(EditRecruiter.this, PhotoUpload.class)));

        setSupportActionBar(findViewById(R.id.toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
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
                    collapsingToolbarLayout.setTitle("Edit Profile");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        retrieve();
    }

    public void retrieve() {
        final FirebaseUser user = mAuth.getCurrentUser();
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
                if (dataSnapshot.hasChild("urlToImage")) {
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(profile);
                }
                dupref = mDatabase.child("Recruiter").child(user.getUid());
                dupref.child("urlToImage").setValue(dataSnapshot.child("urlToImage").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updatedata(View v) {
        pd.setTitle("Updating User Details Please Wait....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FirebaseUser user = mAuth.getCurrentUser();
        msubref = mDatabase.child("Users").child(user.getUid());


        msubref.child("Name").setValue(name.getText().toString());
        msubref.child("Address").setValue(address.getText().toString());
        msubref.child("City").setValue(city.getText().toString());
        msubref.child("State").setValue(state.getText().toString());
        msubref.child("Mobile").setValue(mobile.getText().toString());

        //Toast.makeText(this , "Data Updated" , Toast.LENGTH_SHORT).show();
        pd.dismiss();
        finish();

    }
}
