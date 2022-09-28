package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
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
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class RecruiterMain extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private com.atmanirbharrogar.company.work.MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    ImageView header;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase, mref, msubref;
    ProgressDialog pd;

    TextView name, mail;

    CardView  android, web_developer,surgeon,tutor,electrician,mason,plumber,labourer,carpenter,painter;


    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_main);

        new checkInternetConnection(this).checkConnection();

        name = findViewById(R.id.header_name);
        mail = findViewById(R.id.header_mail);
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



        pd = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());

        //checking user is already registered or not


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        pd.setTitle("Fetching your details...");
        pd.show();
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
                            Toast.makeText(RecruiterMain.this, "Please complete your profile", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            startActivity(new Intent(RecruiterMain.this, MainActivity.class));
                            pd.dismiss();
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        //String mobile = getIntent().getExtras().getString("mobile");
        Intent intent = getIntent();
        if (intent.getStringExtra("mobile")!=null){
            String mobile = intent.getStringExtra("mobile");
             mref.child("Mobile").setValue(mobile);
        }


        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        header = findViewById(R.id.image_header);


        // Initialize the views
        mViewHolder = new ViewHolder();


        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();



      //  mMenuAdapter.setViewSelected(0, true);
       // setTitle(mTitles.get(0));

       // getSupportActionBar().setLogo(R.drawable.worker);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name.setText(dataSnapshot.child("Name").getValue(String.class));
                    mail.setText(dataSnapshot.child("Email").getValue(String.class));
                    if (dataSnapshot.hasChild("urlToImage")) {
                        Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(header);
                        //Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).into(header);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);

    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this, mViewHolder.mDuoDrawerLayout, mViewHolder.mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        pd.setTitle("Please Wait");
        pd.setMessage("Logging Out");
        pd.show();
        mAuth.signOut();
        startActivity(new Intent(RecruiterMain.this, MainActivity.class));
        finish();
    }

    @Override
    public void onHeaderClicked() {
        Intent i = new Intent(this, RecruiterProfile.class);
        ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this, header, ViewCompat.getTransitionName(header));
        startActivity(i, actop.toBundle());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        switch (position) {
            case 3:
                startActivity(new Intent(RecruiterMain.this, SeekerMain.class));
                finish();
                break;
            case 2:
                startActivity(new Intent(RecruiterMain.this, AboutUs.class));
                break;
            case 1:
                Intent i = new Intent(this, RecruiterProfile.class);
                ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this, header, ViewCompat.getTransitionName(header));
                startActivity(i, actop.toBundle());
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            default:
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = findViewById(R.id.toolbar);
        }
    }

    public void dothis(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String categorie;
        String img_var;
        Intent intent;
        switch (v.getId()) {

            case R.id.android_developer:
                categorie = "Android_Developer";
                editor.putString("categorie", categorie);
                editor.commit();

                StyleableToast.makeText(this,"Android Developer",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();

                intent = new Intent(RecruiterMain.this, AndroiddeveloperView.class);
                startActivity(intent);
                break;


            case R.id.web_developer:
                categorie = "Web_Developer";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Web Developer",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(RecruiterMain.this, WebdeveloperView.class);
                startActivity(intent);
                break;

            case R.id.generalsurgeon:
                categorie = "General_Surgeon";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"General Surgeon",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(RecruiterMain.this, GeneralsurgeonView.class);
                startActivity(intent);
                break;

            case R.id.tutor:
                categorie = "Tutor";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Tutor",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(RecruiterMain.this, TutorView.class);
                startActivity(intent);
                break;


            case R.id.electrician:
                categorie = "Electrician";
                editor.putString("categorie", categorie);
                editor.commit();
                StyleableToast.makeText(this,"Electrician",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                intent = new Intent(RecruiterMain.this, ElectricianView.class);
                startActivity(intent);
                break;

            case R.id.bricklayer:

                StyleableToast.makeText(this,"Mason",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Mason";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(RecruiterMain.this, MasonView.class);
                startActivity(intent);
                break;
            case R.id.carpainter:
                StyleableToast.makeText(this,"Carpainter",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Carpenter";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(this, CarpenterView.class);
                startActivity(intent);
                break;
            case R.id.plumber:
                StyleableToast.makeText(this,"Plumber",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Plumber";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(RecruiterMain.this, PlumberView.class);
                startActivity(intent);
                break;
            case R.id.painter:
                StyleableToast.makeText(this,"Painter",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Painter";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(RecruiterMain.this, PainterView.class);
                startActivity(intent);
                break;
            case R.id.labour:
                StyleableToast.makeText(this,"Labour",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
                categorie = "Labour";
                editor.putString("categorie", categorie);
                editor.commit();
                intent = new Intent(RecruiterMain.this, LabourView.class);
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
}
