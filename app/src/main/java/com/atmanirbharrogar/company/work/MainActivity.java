package com.atmanirbharrogar.company.work;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;

public class MainActivity extends AppCompatActivity  {
    ProgressDialog pd;
    Button button_send;
    Toolbar toolbar;
    ListView listView;
    String[] st = {"English", "हिन्दी"};

    TextView hindi,english;
    private static final String TAG = "PhoneAuthActivity";

    //Adding a member variable for the key verification in progress
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    //Creating FirebaseAuth member variable
    private FirebaseAuth mAuth;

    TextInputEditText editTextNumber;
    TextInputEditText editTextOtp;
    CardView englishh, Hindi;
    //MaterialButton button_send,button_verify,button_resend;


    //Setting Boolean to say whether or not we are in progress.
    private boolean mVerificationInProgress = false;

    //Adding verification id as a member variable.
    private String mVerificationId;

    //Adding a member variable for PhoneAuthProvider.ForceResendingToken callback.
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //Adding a member variable for a callback which is our PhoneAuthProvider.OnVerificationStateChangeCallbacks.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocale();
        setContentView(R.layout.activity_main);

        englishh=(CardView)findViewById(R.id.english);
        Hindi=(CardView)findViewById(R.id.hindi);

        Hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("hi");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Hindi.setBackgroundColor(getResources().getColor(R.color.blue2));
                startActivity(intent);
                finish();
            }
        });

        englishh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                englishh.setBackgroundColor(getResources().getColor(R.color.blue2));
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
     //   listView=(ListView)findViewById(R.id.listview);


     //   button_send=(Button)findViewById(R.id.button_send);

     //   hindi=(TextView)findViewById(R.id.hindi);
       // english=(TextView)findViewById(R.id.english);
       /* toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        new checkInternetConnection(this).checkConnection();



       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, st);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = listView.getItemAtPosition(position).toString();


                 if (listItem == "English") {
                     setLocale("en");
                     parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.blue));
                     Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                     finish();
                }
                else if (listItem == "हिन्दी") {
                     setLocale("hi");
                     parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.blue));
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

        });
*/


        // Restoring the instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }



    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config= new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared prefernce
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", lang);
        editor.apply();
    }
    //load language saved in shared prefernce
    public void loadLocale(){
        SharedPreferences prefs= getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My Lang", "");
        setLocale(language);
    }

    // Creating onStart method.
    @Override
    public void onStart() {
        super.onStart();

        // Checking if the user is signed in or not. If signed in, then update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            SharedPreferences pref = getSharedPreferences("ankit", Context.MODE_PRIVATE);


            String gg = pref.getString("option", "nhi mm");
            String gg2 = pref.getString("name", "nhi mm2");
            String gg3 = pref.getString("signup", "nhi mm3");

            Log.d(gg, "hiii");


            if (gg.equals("work")) {
                Intent intent = new Intent(MainActivity.this, WorkMain.class);
                startActivity(intent);
                finish();
            } else if (gg.equals("hire")) {
                Intent intent = new Intent(MainActivity.this, HireMain.class);
                startActivity(intent);
                finish();
            } else if (gg.equals("student")) {
                Intent intent = new Intent(MainActivity.this, HireMain.class);
                startActivity(intent);
                finish();
            } else if (gg2.equals("notnull")) {
                Intent intent = new Intent(MainActivity.this, AccountType.class);
                startActivity(intent);
                finish();
            }
            else if (gg3.equals("yes")){
                Intent intent= new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }


        }





    }}