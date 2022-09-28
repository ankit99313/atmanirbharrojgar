package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class EditSeeker extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase, msubref, dupref;
    TextInputEditText name, mobile, state, city, address;
    TextView mail, aadhaar;
    ImageView profile;
    ProgressDialog pd;
    String type;
    String Profession = "";
    AutoCompleteTextView editTextFilledExposedProfession;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seeker);

        setTitle("Edit Profile");
        new checkInternetConnection(this).checkConnection();

        name = findViewById(R.id.name_seeker_edit);
        mail = (TextView) findViewById(R.id.mail_seeker_edit);
        address = findViewById(R.id.seeker_address_edit);
        aadhaar = (TextView) findViewById(R.id.seeker_aadhaar_edit);
        city = findViewById(R.id.seeker_city_edit);
        mobile = findViewById(R.id.seeker_mobile_edit);
        state = findViewById(R.id.seeker_state_edit);
        editTextFilledExposedProfession = findViewById(R.id.spinner_profession);
        //profession = findViewById(R.id.profesion_seeker_edit);
        profile = (ImageView) findViewById(R.id.profile_image_seeker_edit);
        pd = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //create a list of items for the spinner.
        String[] items1 = new String[]{"Select","Android_Developer","Web_Developer","Tutor","General_Surgeon","Electrician", "Mason", "Carpenter", "Painter", "Plumber", "Labour"};

        ArrayAdapter<String> profession_adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, items1) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        //set the spinners adapter to the previously created one.
        editTextFilledExposedProfession.setEnabled(false);
        editTextFilledExposedProfession.setClickable(false);
        editTextFilledExposedProfession.setAdapter(profession_adapter);
        editTextFilledExposedProfession.setOnItemClickListener((adapterView, view, i, l) -> {
            Profession = adapterView.getItemAtPosition(i).toString();
            profession_adapter.notifyDataSetChanged();
        });
        editTextFilledExposedProfession.setOnDismissListener(() -> {
            editTextFilledExposedProfession.clearFocus();
            editTextFilledExposedProfession.dismissDropDown();
        });

        profile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PhotoUpload.class)));

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
                editTextFilledExposedProfession.setText(dataSnapshot.child("Profession").getValue(String.class));
                if (dataSnapshot.hasChild("urlToImage")) {
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(profile);
                }
                //type = dataSnapshot.child("Profession").getValue(String.class);
                //dupref = mDatabase.child("Seeker").child(type).child(user.getUid());
               // dupref.child("urlToImage").setValue(dataSnapshot.child("urlToImage").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updatedata(View v) {
        pd.setTitle("Updating User Details Please Wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        FirebaseUser user = mAuth.getCurrentUser();
        msubref = mDatabase.child("Users").child(user.getUid());

        msubref.child("Name").setValue(name.getText().toString());
        msubref.child("Address").setValue(address.getText().toString());
        msubref.child("City").setValue(city.getText().toString());
        msubref.child("State").setValue(state.getText().toString());
        msubref.child("Mobile").setValue(mobile.getText().toString());
        msubref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
        msubref.child("Aadhaar").setValue(aadhaar.getText().toString());
        msubref.child("Email").setValue(mail.getText().toString());

        //Toast.makeText(this , "Data Updated" , Toast.LENGTH_SHORT).show();
        pd.dismiss();
        finish();

    }
}

