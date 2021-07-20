package com.atmanirbharrogar.company.work;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountType extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    Toolbar toolbar;
    Button create_account;
    CardView work,hire;
    LinearLayout student;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mref0,mref;
    String type="";
    String backgroundcolor="";
    AutoCompleteTextView editTextFilledExposedProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawer=findViewById(R.id.drawer_layout);
        mref= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();

        editTextFilledExposedProfession=findViewById(R.id.spinner_profession);
        editTextFilledExposedProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountType.this,ShowDetails.class);
                startActivity(intent);
            }
        });
        //create a list of items for the spinner.
        String[] items1 = new String[]{"Select","Android_Developer","Web_Developer","Tutor","General_Surgeon","Electrician", "Mason", "Carpenter", "Painter", "Plumber", "Labour"};

      /*  ArrayAdapter<String> profession_adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, items1) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        //set the spinners adapter to the previously created one.
        editTextFilledExposedProfession.setEnabled(true);
        editTextFilledExposedProfession.setClickable(true);
        editTextFilledExposedProfession.setAdapter(profession_adapter);*/




        NavigationView navigationView=findViewById(R.id.nav_view);

        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.nav_switch);
        nav_item2.setEnabled(false);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        hire=(CardView)findViewById(R.id.hire);
        if (getIntent().getIntExtra("switch1",0)==1){
            hire.setVisibility(View.GONE);
        }



        mref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            NavigationView navigationView=findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
            TextView name=(TextView)header.findViewById(R.id.header_name);
            TextView mail=(TextView)header.findViewById(R.id.header_mail);
            ImageView headerimage=(ImageView)header.findViewById(R.id.image_header);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name.setText(dataSnapshot.child("Name").getValue(String.class));
                    mail.setText(dataSnapshot.child("Email").getValue(String.class));
                    if (dataSnapshot.hasChild("urlToImage")) {
                        Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(headerimage);
                        //Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(header);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        SharedPreferences pref=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        work=(CardView) findViewById(R.id.work);
        hire=(CardView) findViewById(R.id.hire);
        student=(LinearLayout) findViewById(R.id.student);
        create_account=(Button)findViewById(R.id.create_account);





        currentUser=mAuth.getCurrentUser();



        student.setBackgroundColor(R.drawable.account);

       // work.setBackgroundColor(getResources().getColor(R.color.gradStart));
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work.setCardBackgroundColor(getResources().getColor(R.color.gradStart));
                hire.setCardBackgroundColor(getResources().getColor(R.color.white));
                student.setBackgroundColor(R.drawable.account);


                //startActivity(new Intent(AccountType.this,WorkMain.class));
                //finish();
                type="work";
                create_account.setEnabled(true);
            }
        });



        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hire.setCardBackgroundColor(getResources().getColor(R.color.gradStart));
                work.setCardBackgroundColor(getResources().getColor(R.color.white));
                student.setBackgroundColor(R.drawable.account);
                //startActivity(new Intent(AccountType.this,HireMain.class));
                //finish();
                type="hire";
                create_account.setEnabled(true);

            }
        });



        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.setBackgroundColor(getResources().getColor(R.color.gradStart));
                hire.setBackgroundColor(R.drawable.account);
                work.setBackgroundColor(R.drawable.account);

                //startActivity(new Intent(AccountType.this,HireMain.class));
                //finish();
                type="student";
                create_account.setEnabled(true);

            }
        });





        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mref.child(currentUser.getUid()).child("type").setValue(type);

                if (type=="work")
                {
                   // mref.child(currentUser.getUid()).child("type").setValue("work");
                  //  Intent iin= getIntent();
                   // int bundle = iin.getIntExtra("switch1",0);
                    Intent intent=new Intent(AccountType.this,ServiceManForm.class);
                    intent.putExtra("switch12",getIntent().getIntExtra("switch1",0));
                    startActivity(intent);
                    finish();


                }
                 if (type=="hire"){
                     mref.child(currentUser.getUid()).child("type").setValue("hire");
                     editor.putString("option","hire");
                     editor.apply();

                    startActivity(new Intent(AccountType.this,HireMain.class));
                    finish();
                }
                 if(type=="student")
                {
                    mref.child(currentUser.getUid()).child("type").setValue("student");
                    editor.putString("option","student");
                    editor.apply();
                    startActivity(new Intent(AccountType.this,HireMain.class));
                    finish();
                }
            }
        });



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragement()).commit();
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;


            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                Intent intent = new Intent(AccountType.this, HireProfile.class);
                startActivity(intent);
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;

           /* case R.id.nav_switch:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                Intent intent1 = new Intent(AccountType.this, WorkMain.class);
                startActivity(intent1);
                fileList();
                break;*/

            case R.id.nav_logout:
                mAuth.signOut();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                mAuth.signOut();
                startActivity(new Intent(AccountType.this, MainActivity.class));
                finish();
                break;

            case R.id.about_us:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                startActivity(new Intent(AccountType.this, AboutUs.class));
                break;

            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.atmanirbharrogar.company.work";
                String shareSubject = "your subject here";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "share using"));

                break;


        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    @Override
    public void onRestart() {
        super.onRestart();
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }




}