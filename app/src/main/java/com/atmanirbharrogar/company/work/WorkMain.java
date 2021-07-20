package com.atmanirbharrogar.company.work;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class WorkMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ViewHolder mViewHolder;
    Toolbar toolbar;
    private DrawerLayout drawer;
    private RecyclerView WorkShowPost;
    private FirebaseDatabase mdatabase;

    FirebaseAuth mAuth;

    DatabaseReference mDatabase, msubref,mref,mref2;


    private ArrayList<String> mTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_main);

        WorkShowPost=findViewById(R.id.workpost_list);
        LinearLayoutManager ll=new LinearLayoutManager(WorkMain.this);
        // ShowPost.setLayoutManager(new LinearLayoutManager(ShowCreatedPost.this));
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);
        WorkShowPost.setLayoutManager(ll);

        setTitle("Home");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        msubref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());


        toolbar=(Toolbar)findViewById(R.id.toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);



        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
       toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        retrieve();



    }


    public void retrieve() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView headername = (TextView) header.findViewById(R.id.header_name);
        TextView headermail = (TextView) header.findViewById(R.id.header_mail);
        ImageView headerimage = (ImageView) header.findViewById(R.id.image_header);


        msubref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                headermail.setText(dataSnapshot.child("Email").getValue(String.class));
                headername.setText(dataSnapshot.child("Name").getValue(String.class));
                //newwork.setText(dataSnapshot.child("").getValue(String.class));
                if (dataSnapshot.hasChild("urlToImage")) {
                    Picasso.get().load(dataSnapshot.child("urlToImage").getValue(String.class)).transform(new CircularTransformation()).into(headerimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences pre=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor edito=pre.edit();
        switch (item.getItemId())
        {

            case R.id.nav_home:
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragement()).commit();
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;




            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                Intent intent=new Intent(WorkMain.this,WorkerProfile.class);
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

            case R.id.nav_switch:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                Intent intent1=new Intent(WorkMain.this,HireMain.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragement()).commit();
                mAuth.signOut();
                startActivity(new Intent(WorkMain.this, MainActivity.class));
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
               startActivity(new Intent(WorkMain.this,AboutUs.class));
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


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pre=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor edito=pre.edit();

        String gg= pre.getString("Profession","nhi mm");

        //auth= FirebaseAuth.getInstance();
        mdatabase=FirebaseDatabase.getInstance();
        mref=mdatabase.getReference("Order");

        Log.d(gg,"jiii");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("NewOrder");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if (snapshot.getValue() != null) {

                        FirebaseRecyclerOptions<ShowPost> option = new FirebaseRecyclerOptions.Builder<ShowPost>().setQuery(reference.orderByChild("category").equalTo(gg), ShowPost.class)
                                .build();


                        FirebaseRecyclerAdapter<ShowPost, ShowPostViewHolder> adapter = new FirebaseRecyclerAdapter<ShowPost, ShowPostViewHolder>(option) {
                            @Override
                            protected void onBindViewHolder(@NonNull ShowPostViewHolder showPostViewHolder, int position, @NonNull ShowPost showPost) {

                                showPostViewHolder.Profession.setText(showPost.getCategory());
                                showPostViewHolder.NeedWorkerIn.setText(showPost.getWorkerTime());
                                showPostViewHolder.JudgeWork.setText(showPost.getJudgeWork());
                                showPostViewHolder.Title.setText(showPost.getTitle());
                                showPostViewHolder.LongDescription.setText(showPost.getLongDescription());
                                showPostViewHolder.Address.setText(showPost.getAddress());
                                showPostViewHolder.Mobile.setText(showPost.getMobile());
                                showPostViewHolder.pin.setText(showPost.getPin());


                            }

                            @NonNull
                            @Override
                            public ShowPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmain_post_layout, parent, false);
                                ShowPostViewHolder holde = new ShowPostViewHolder(view);
                                return holde;
                            }
                        };
                        WorkShowPost.setAdapter(adapter);
                        adapter.startListening();

                    } else {

                    }


                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}