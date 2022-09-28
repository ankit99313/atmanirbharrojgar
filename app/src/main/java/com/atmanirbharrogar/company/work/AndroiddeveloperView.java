package com.atmanirbharrogar.company.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class AndroiddeveloperView extends AppCompatActivity {
    private ImageView SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;
    private String locinp;
    private String loccur;


    Button btnLocation;
    TextView textView1,textView2;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_view);

        btnLocation = findViewById(R.id.bt_location);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);

        inputText = findViewById(R.id.search_person_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(AndroiddeveloperView.this));

        SearchInput = inputText.getText().toString().toLowerCase().trim();

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInput = inputText.getText().toString().toLowerCase().trim();

                onStart();


            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AndroiddeveloperView.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(AndroiddeveloperView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


    }


    private void getLocation() {

        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        btnLocation.setVisibility(View.GONE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(AndroiddeveloperView.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                        loccur= addresses.get(0).getLocality();
                        locinp=loccur.toLowerCase().trim();

                        textView1.setText(Html.fromHtml(

                                "<font color='#6200EE'><b>Your City:</b><br></font>"
                                        +addresses.get(0).getLocality()

                        ));
                        textView2.setText(Html.fromHtml(

                                "<font color='#6200EE'><b>Your Address:</b><br></font>"
                                        +addresses.get(0).getAddressLine(0)
                        ));



                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Seeker/Android_Developer");

                        FirebaseRecyclerOptions<Person> options =
                                new FirebaseRecyclerOptions.Builder<Person>()
                                        .setQuery(reference.orderByChild("City").startAt(locinp).endAt(locinp + "\uf8ff"), Person.class)
                                        .build();

                        FirebaseRecyclerAdapter<Person, PersonViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Person, PersonViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull PersonViewHolder holder, int position, @NonNull final Person model)
                                    {

                                        holder.txtpersonName.setText(model.getName());
                                        holder.txtpersonphone.setText(model.getMobile());
                                        holder.txtgender.setText(model.getGender());
                                        holder.txtaltpersonphone.setText(model.getAlternateMobile());
                                        holder.txtpersoncity.setText(model.getCity());
                                        holder.txtstate.setText(model.getState());
                                        holder.txtpersonProfession.setText(model.getProfession());
                                        holder.txtAddress.setText(model.getAddress());

                                        Picasso.get().load(model.geturlToImage()).into(holder.perimageView);
                                        // Picasso.get().load(model.geturlToImage()).placeholder(R.drawable.profile).into(holder.perimageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view)
                                            {

                                            }
                                        });
                                    }

                                    @NonNull
                                    @Override
                                    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                                    {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_items_layout, parent, false);
                                        PersonViewHolder holder = new PersonViewHolder(view);
                                        return holder;
                                    }
                                };

                        searchList.setAdapter(adapter);
                        adapter.startListening();







                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

















    @Override
    protected void onStart()
    {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Seeker/Android_Developer");

        FirebaseRecyclerOptions<Person> options =
                new FirebaseRecyclerOptions.Builder<Person>()
                        .setQuery(reference.orderByChild("City").startAt(SearchInput).endAt(SearchInput + "\uf8ff"), Person.class)
                        .build();

        FirebaseRecyclerAdapter<Person, PersonViewHolder> adapter =
                new FirebaseRecyclerAdapter<Person, PersonViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PersonViewHolder holder, int position, @NonNull final Person model)
                    {

                        holder.txtpersonName.setText(model.getName());
                        holder.txtpersonphone.setText(model.getMobile());
                        holder.txtgender.setText(model.getGender());
                        holder.txtaltpersonphone.setText(model.getAlternateMobile());
                        holder.txtpersoncity.setText(model.getCity());
                        holder.txtstate.setText(model.getState());
                        holder.txtpersonProfession.setText(model.getProfession());
                        holder.txtAddress.setText(model.getAddress());

                        Picasso.get().load(model.geturlToImage()).into(holder.perimageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_items_layout, parent, false);
                        PersonViewHolder holder = new PersonViewHolder(view);
                        return holder;
                    }
                };

        searchList.setAdapter(adapter);
        adapter.startListening();


    }


}