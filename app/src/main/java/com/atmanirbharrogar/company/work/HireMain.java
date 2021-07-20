package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HireMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    Toolbar toolbar;
    ImageView header;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase, mref, msubref;
    ProgressDialog pd;


    CardView android, web_developer,surgeon,tutor,electrician,mason,plumber,labourer,carpenter,painter,createpost;


    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_main);




        new checkInternetConnection(this).checkConnection();


        android=findViewById(R.id.android_developer);
        web_developer=findViewById(R.id.web_developer);
        surgeon=findViewById(R.id.generalsurgeon);
        tutor=findViewById(R.id.tutor);
        electrician=findViewById(R.id.electrician);
        mason=findViewById(R.id.bricklayer);
        plumber=findViewById(R.id.plumber);
        labourer=findViewById(R.id.labour);
        carpenter=findViewById(R.id.carpainter);
        painter=findViewById(R.id.painter);
        createpost=findViewById(R.id.createpost);
        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawer=findViewById(R.id.drawer_layout);

        createpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        web_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        surgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        mason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        labourer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        carpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });
        painter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dothis(v);
            }
        });



       /* DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        pd.setTitle("Fetching your details...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        userRef
                .orderByChild("Name")
                //.equalTo("")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //it means user already registered
                            //Toast.makeText(RecruiterMain.this, "name found", Toast.LENGTH_SHORT).show();
                            pd.dismiss();


                        } else {
                            Toast.makeText(HireMain.this, "Please complete your profile", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            startActivity(new Intent(HireMain.this, MainActivity.class));
                            pd.dismiss();
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/



        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mref.addValueEventListener(new ValueEventListener() {
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
    }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences pref=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor edito=pref.edit();
        String gg2= pref.getString("workmode","nhi mm2");
        switch (item.getItemId())
        {

            case R.id.nav_home:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragement()).commit();
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;




            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                Intent intent=new Intent(HireMain.this,HireProfile.class);
                startActivity(intent);
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_switch:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                if (gg2.equals("nhi mm2")||gg2.equals(null)){
                    Intent intent2=new Intent(HireMain.this,AccountType.class);
                    intent2.putExtra("switch1",1);
                    startActivity(intent2);
                }
                else {
                    Intent intent1=new Intent(HireMain.this,WorkMain.class);
                    startActivity(intent1);
                }

                finish();
                break;

            case R.id.nav_logout:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                mAuth.signOut();
                startActivity(new Intent(HireMain.this, MainActivity.class));
                edito.remove("option");
                edito.remove("workmode");
                edito.remove("Profession");
                edito.remove("signup");
                edito.remove("name");
                edito.apply();
                finish();
                break;

            case R.id.about_us:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                startActivity(new Intent(HireMain.this,AboutUs.class));
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


    public void dothis(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String categorie;
        String img_var;
        Intent intent;
        switch (v.getId()) {
            case R.id.createpost:
               intent=new Intent(HireMain.this,CreatePost.class);
               startActivity(intent);
               break;

            case R.id.android_developer:
                categorie = "Android_Developer";
                editor.putString("categorie", categorie);
                editor.commit();

                StyleableToast.makeText(this,"Android Developer",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();

                intent = new Intent(HireMain.this, ViewAndroidDeveloper.class);
                startActivity(intent);
                break;


            case R.id.web_developer:
                categorie = "Web_Developer";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Web Developer",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(HireMain.this, ViewWebDeveloper.class);
                startActivity(intent);
                break;

            case R.id.generalsurgeon:
                categorie = "General_Surgeon";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"General Surgeon",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(HireMain.this, ViewGeneralSurgeon.class);
                startActivity(intent);
                break;

            case R.id.tutor:
                categorie = "Tutor";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Tutor",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(HireMain.this, ViewTutor.class);
                startActivity(intent);
                break;


            case R.id.electrician:
                categorie = "Electrician";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Electrician",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(HireMain.this, ViewElectrician.class);
                startActivity(intent);
                break;

            case R.id.bricklayer:

                StyleableToast.makeText(this,"Mason",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Mason";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(HireMain.this, ViewMason.class);
                startActivity(intent);
                break;
            case R.id.carpainter:
                StyleableToast.makeText(this,"Carpainter",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Carpenter";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(HireMain.this, ViewCarpainter.class);
                startActivity(intent);
                break;
            case R.id.plumber:
                StyleableToast.makeText(this,"Plumber",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Plumber";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(HireMain.this, ViewPlumber.class);
                startActivity(intent);
                break;
            case R.id.painter:
                StyleableToast.makeText(this,"Painter",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Painter";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(HireMain.this, ViewPainter.class);
                startActivity(intent);
                break;
            case R.id.labour:
                StyleableToast.makeText(this,"Labour",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Labour";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(HireMain.this, ViewLabour.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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