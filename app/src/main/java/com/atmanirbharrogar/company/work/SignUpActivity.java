package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.Serializable;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    String[] states;
    String Email = "";
    String id;
    String Contact_Number;
    String Aadhar_Number;
    String Street_No;
    String Pincode;
    String State;
    String City;
    String Gender = "";
    String Password = "";
    String Confirm_password = "";
    String Profession = "";
    String Type = "";
    String Name = "";
    String Alternate_Contact_Number;
    TextInputEditText editTextEmail;
    TextInputEditText editTextContact_No;
    TextInputEditText editTextAadhar_No;
    TextInputEditText editTextStreet;
    TextInputEditText editTextPassword;
    TextInputEditText editTextConfirmPassword;
    TextInputEditText editTextPincode;
    TextInputEditText editTextName;
    TextInputEditText editTextAlternate_contact_No;
    ProgressDialog pd;
    RadioButton radioButtonseeker, radioButtonrecruiter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mref,mDatabase,database, rec_ref, subref, seeker_ref, dup_ref;
    ArrayList<User> Userlist;
    AutoCompleteTextView editTextFilledExposedGender, editTextFilledExposedProfession, editTextFilledExposedState, editTextFilledExposedCity;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       // mDatabase = FirebaseDatabase.getInstance().getReference();
        //mref = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid());




        ConnectivityManager managercheck =(ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkcheck=managercheck.getActiveNetworkInfo();
        if(null!=activeNetworkcheck) {
            if (activeNetworkcheck.getType() == ConnectivityManager.TYPE_WIFI) {
                // Toast.makeText(MainActivity.this, "YOUR WIFI ON", Toast.LENGTH_SHORT).show();

              //  StyleableToast.makeText(this,"Wifi Connected",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();

            } else if (activeNetworkcheck.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Toast.makeText(MainActivity.this, "YOUR MOBILE DATA ON", Toast.LENGTH_SHORT).show();
                //StyleableToast.makeText(this,"Mobile Data Connected",Toast.LENGTH_LONG,R.style.Maintoastgreen).show();
            }


        }
        else
        {
            // Toast.makeText(MainActivity.this, "NETWORK NOT AVAILABLE", Toast.LENGTH_SHORT).show();

            StyleableToast.makeText(this,"NETWORK NOT AVAILABLE",Toast.LENGTH_LONG,R.style.Maintoastred).show();

        }



        new checkInternetConnection(this).checkConnection();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference();
        dup_ref = database.child("Users");
        rec_ref = database.child("Recruiter");
        seeker_ref = database.child("Seeker");


        init();

        // to counter the issue where the backgroung image is not static and is compressing along with the scroll view wheneverthe keboard pops up
        getWindow().setBackgroundDrawableResource(R.drawable.main_bg);

        //creating a string array for gender spinner
        String[] items = new String[]{"Select", "Male", "Female"};

        //array adapter to set this string array into the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, items) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        //set the spinners adapter to the previously created one.
        editTextFilledExposedGender.setAdapter(adapter);
        editTextFilledExposedGender.setOnItemClickListener((adapterView, view, i, l) -> {
            Gender = adapterView.getItemAtPosition(i).toString();
            adapter.notifyDataSetChanged();
        });
        editTextFilledExposedGender.setOnDismissListener(() -> {
            editTextFilledExposedGender.clearFocus();
            editTextFilledExposedGender.dismissDropDown();
        });

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

        states = getResources().getStringArray(R.array.states);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, states) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedState.setAdapter(spinnerArrayAdapter);
        editTextFilledExposedState.setOnItemClickListener((adapterView, view, i, l) -> {
            String s = adapterView.getItemAtPosition(i).toString();
            editTextFilledExposedCity.setText("Select");
            switch (s) {
                case "Andaman and Nicobar Islands":
                    andaman();
                    break;
                case "Andhra Pradesh":
                    andhra();
                    break;
                case "Arunachal Pradesh":
                    arunachal();
                    break;
                case "Assam":
                    assam();
                    break;
                case "Bihar":
                    bihar();
                    break;
                case "Chandigarh":
                    chandigarh();
                    break;
                case "Chattisgarh":
                    chattisgarh();
                    break;
                case "Darda and Nagar Haveli":
                    dadra();
                    break;
                case "Daman and Diu":
                    daman();
                    break;
                case "Delhi":
                    delhi();
                    break;
                case "Goa":
                    goa();
                    break;
                case "Gujarat":
                    gujarat();
                    break;
                case "Haryana":
                    haryana();
                    break;
                case "Himachal Pradesh":
                    himachal();
                    break;
                case "Jammu and Kashmir":
                    jammu();
                    break;
                case "Jharkhand":
                    jharkhand();
                    break;
                case "Karnataka":
                    karnataka();
                    break;
                case "Kerala":
                    kerala();
                    break;
                case "Lakshwadweep":
                    lakshwadeep();
                    break;
                case "Madhya Pradesh":
                    madhyapradesh();
                    break;
                case "Maharashtra":
                    maharashtra();
                    break;
                case "Manipur":
                    manipur();
                    break;
                case "Meghalaya":
                    meghalaya();
                    break;
                case "Mizoram":
                    mizoram();
                    break;
                case "Nagaland":
                    nagaland();
                    break;
                case "Orissa":
                    orissa();
                    break;
                case "Pondicherry":
                    pondicherry();
                    break;
                case "Punjab":
                    punjab();
                    break;
                case "Rajasthan":
                    rajasthan();
                    break;
                case "Sikkim":
                    sikkim();
                    break;
                case "Tamil Nadu":
                    tamilnadu();
                    break;
                case "Tripura":
                    tripura();
                    break;
                case "Uttar Pradesh":
                    uttarpradesh();
                    break;
                case "Uttaranchal":
                    uttaranchal();
                    break;
                case "West Bengal":
                    westbengal();
                    break;
            }
        });
        editTextFilledExposedState.setOnDismissListener(() -> {
            editTextFilledExposedState.clearFocus();
            editTextFilledExposedState.dismissDropDown();
        });
        editTextFilledExposedCity.setOnDismissListener(() -> {
            editTextFilledExposedCity.clearFocus();
            editTextFilledExposedCity.dismissDropDown();
        });
    }

    public void init() {
        Userlist = new ArrayList<>();
        editTextEmail = findViewById(R.id.editTextEmail);
       // editTextPassword = findViewById(R.id.editTextPassword);
       // editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
       // editTextAadhar_No = findViewById(R.id.editTextAadhar_No);
        editTextStreet = findViewById(R.id.editTextStreet);
        editTextName = findViewById(R.id.editTextName);
        editTextPincode = findViewById(R.id.editTextPincode);
       // editTextContact_No = findViewById(R.id.editTextContact_No);
        editTextAlternate_contact_No = findViewById(R.id.editTextAlternate_No);
        radioButtonrecruiter = findViewById(R.id.Radio_Btn_recruiter);
        radioButtonseeker = findViewById(R.id.Radio_btn_seeker);
        editTextFilledExposedGender = findViewById(R.id.spinner_gender);
        editTextFilledExposedState = findViewById(R.id.statespinner);
        editTextFilledExposedCity = findViewById(R.id.cityspinner);
        editTextFilledExposedProfession = findViewById(R.id.spinner_profession);

        pd = new ProgressDialog(this);
    }

    public void test(View v) {
        createUser();
    }

    public void onRadioButtonclicked(View view) {
        switch (view.getId()) {
            case R.id.Radio_btn_seeker:
                if (radioButtonseeker.isChecked()) {
                    editTextFilledExposedProfession.setEnabled(true);
                    editTextFilledExposedProfession.setClickable(true);
                    //Toast.makeText(SignUp.this,"Hello",Toast.LENGTH_SHORT).show();
                    Type = "Seeker";
                }
            case R.id.Radio_Btn_recruiter:
                if (radioButtonrecruiter.isChecked()) {
                    editTextFilledExposedProfession.setEnabled(false);
                    editTextFilledExposedProfession.setClickable(false);
                    //Toast.makeText(SignUp.this,"Hello",Toast.LENGTH_SHORT).show();
                    Profession = "";
                    Type = "Recruiter";
                }
        }
    }

    public void createUser() {
        pd.setTitle("Registering User Please Wait...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        Email = editTextEmail.getText().toString();
      //  Password = editTextPassword.getText().toString();
        Name = editTextName.getText().toString();
       // Contact_Number = editTextContact_No.getText().toString();
      //  Aadhar_Number = editTextAadhar_No.getText().toString();
        Alternate_Contact_Number = editTextAlternate_contact_No.getText().toString();
        State = editTextFilledExposedState.getText().toString();
        Street_No = editTextStreet.getText().toString();
        City = editTextFilledExposedCity.getText().toString();
        Pincode = editTextPincode.getText().toString();
        //Confirm_password = editTextConfirmPassword.getText().toString();

        String m = editTextEmail.getText().toString();
       // String p = editTextPassword.getText().toString();

        if (!validation()) {
            Toast.makeText(getApplication() , "Reached till validation" , Toast.LENGTH_LONG).show();
            pd.dismiss();
            return;
        }
        //Toast.makeText(getApplication() , "outside" , Toast.LENGTH_LONG).show();
        //pd.setTitle("Registering Please Wait");
        //pd.show();
      /*  mAuth.createUserWithEmailAndPassword(m, p).addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignUpActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                id = currentUser.getUid();
                writedata();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(SignUpActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });*/
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        id = currentUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mref = mDatabase.child("Users").child(id);
        Intent intent = getIntent();
        if (intent.getStringExtra("mobile")!=null){
            String mobile = intent.getStringExtra("mobile");
            mref.child("Mobile").setValue(mobile);
        }

        writedata();

    }

    public void duplicate() {
        //Toast.makeText(getApplicationContext(), "Inside", Toast.LENGTH_LONG).show();
      //  User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
        User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
        subref = dup_ref.child(id);
        subref.child("Email").setValue(u.getEmail());
        subref.child("Name").setValue(u.getName());
        subref.child("Id").setValue(u.getId());
        //subref.child("Mobile").setValue(u.getContact_Number());
        subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
       // subref.child("Aadhaar").setValue(u.getAadhar_Number());
        subref.child("Address").setValue(u.getStreet_No());
        subref.child("Pincode").setValue(u.getPincode());
        subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
        subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
        subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
        subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
        subref.child("Type").setValue(u.getType());

        //Toast.makeText(this , "User Data Write in DB Successful" , Toast.LENGTH_SHORT).show();
    }

    public void writedata() {
        if (radioButtonrecruiter.isChecked()) {
            duplicate();
            Toast.makeText(this, "Successfully register", Toast.LENGTH_SHORT).show();
            subref = rec_ref.child(id);
           // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
            User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
            subref.child("Email").setValue(u.getEmail());
            subref.child("Name").setValue(u.getName());
            subref.child("Id").setValue(u.getId());
           // subref.child("Mobile").setValue(u.getContact_Number());
            subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
           // subref.child("Aadhaar").setValue(u.getAadhar_Number());
            subref.child("Address").setValue(u.getStreet_No());
            subref.child("Pincode").setValue(u.getPincode());
            subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
            subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
            subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
            subref.child("Profession").setValue("");
            subref.child("Type").setValue(u.getType());
           // Toast.makeText(this, "hiiiiii", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Profession").setValue("");
           // startActivity(new Intent(SignUpActivity.this, RecruiterMain.class));
            Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
           // intent.putExtra("mobile",  "9931381277");
            intent.putExtra("type", "Recuriter");
            startActivity(intent);
            finish();

        } else if (radioButtonseeker.isChecked()) {
            if (editTextFilledExposedProfession.getText().toString().equals("Electrician")) {
                subref = seeker_ref.child("Electrician").child(id);
             //   User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
                //subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", u.getContact_Number());
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            } else if (editTextFilledExposedProfession.getText().toString().equals("Mason")) {
                subref = seeker_ref.child("Mason").child(id);
                //User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
               // subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
                //subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
                //intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            } else if (editTextFilledExposedProfession.getText().toString().equals("Carpenter")) {
                subref = seeker_ref.child("Carpenter").child(id);
               // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
              //  subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            } else if (editTextFilledExposedProfession.getText().toString().equals("Painter")) {
                subref = seeker_ref.child("Painter").child(id);
                //User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
               // subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
              //  subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
                //startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
                //intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            } else if (editTextFilledExposedProfession.getText().toString().equals("Plumber")) {
                subref = seeker_ref.child("Plumber").child(id);
               // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
               // subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            } else if (editTextFilledExposedProfession.getText().toString().equals("Labour")) {
                subref = seeker_ref.child("Labour").child(id);
                //User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
               // subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
                //intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            }
            else if (editTextFilledExposedProfession.getText().toString().equals("Android_Developer")) {
                subref = seeker_ref.child("Android_Developer").child(id);
               // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
               // subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
               // startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            }
            else if (editTextFilledExposedProfession.getText().toString().equals("Web_Developer")) {
                subref = seeker_ref.child("Web_Developer").child(id);
               // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
                //subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
                //subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
              //  startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            }

            else if (editTextFilledExposedProfession.getText().toString().equals("Tutor")) {
                subref = seeker_ref.child("Tutor").child(id);
               // User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
                //subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
              //  startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            }

            else if (editTextFilledExposedProfession.getText().toString().equals("General_Surgeon")) {
                subref = seeker_ref.child("General_Surgeon").child(id);
              //  User u = new User(Email, id, Contact_Number, Aadhar_Number, Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                User u = new User(Email, id,  Street_No, Pincode, State, City, Gender, Profession, Type, Name, Alternate_Contact_Number);
                subref.child("Email").setValue(u.getEmail());
                subref.child("Name").setValue(u.getName());
                subref.child("Id").setValue(u.getId());
                //subref.child("Mobile").setValue(u.getContact_Number());
                subref.child("AlternateMobile").setValue(u.getAlternate_Contact_Number());
               // subref.child("Aadhaar").setValue(u.getAadhar_Number());
                subref.child("Address").setValue(u.getStreet_No());
                subref.child("Pincode").setValue(u.getPincode());
                subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
                subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
                subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
                subref.child("Profession").setValue(editTextFilledExposedProfession.getText().toString());
                subref.child("Type").setValue(u.getType());
                duplicate();
              //  startActivity(new Intent(SignUpActivity.this, SeekerMain.class));
                Intent intent=new Intent(SignUpActivity.this,RecruiterMain.class);
               // intent.putExtra("mobile", (Parcelable) editTextContact_No);
                intent.putExtra("type", "seeker");
                startActivity(intent);
                finish();
            }




        }
    }

    private boolean validation() {
        boolean valid = true;

        TextInputLayout emailTextInputLayout = findViewById(R.id.editTextEmailLayout);
        //TextInputLayout passwordTextInputLayout = findViewById(R.id.editTextPasswordLayout);
        TextInputLayout textProfessIn = findViewById(R.id.professionLayout);
        TextInputLayout PINTextInputLayout = findViewById(R.id.editTextPincodeLayout);
        //TextInputLayout contactTextIputLayout = findViewById(R.id.editTextContact_NoLayout);
       // TextInputLayout aadharTextInputLayout = findViewById(R.id.editTextAadhar_NoLayout);
        TextInputLayout streetInputLayout = findViewById(R.id.editTextStreetLayout);
        TextInputLayout nameInputLayout = findViewById(R.id.editTextNameLayout);

        if (Email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            emailTextInputLayout.setError("Please Enter valid Email address");
            emailTextInputLayout.requestFocus();
            valid = false;
        } else {
            emailTextInputLayout.setError(null);
        }

       /* if (Password.length() < 6) {
            passwordTextInputLayout.setError("Minimum password length is 6");
            passwordTextInputLayout.requestFocus();
            valid = false;
        } else {
            passwordTextInputLayout.setError(null);
        }*/
        if (Type.equals("SeekerProfile")) {
            if (Profession.isEmpty()) {
                textProfessIn.requestFocus();
                textProfessIn.setError("Choose Your Option");
                valid = false;
            } else {
                textProfessIn.setError(null);
            }
        }
        if (Gender.isEmpty()) {
            editTextFilledExposedGender.requestFocus();
            valid = false;
        }
        if (Pincode.isEmpty()) {
            PINTextInputLayout.setError("Enter Pincode");
            PINTextInputLayout.requestFocus();
            valid = false;
        } else {
            PINTextInputLayout.setError(null);
        }
      /*  if (Contact_Number.length() != 10) {
            contactTextIputLayout.setError("Enter Valid Mobile Number");
            contactTextIputLayout.requestFocus();
            valid = false;
        } else {
            contactTextIputLayout.setError(null);
        }
        if (Aadhar_Number.length() != 12) {
            aadharTextInputLayout.setError("Enter Valid Aadhaar Number");
            aadharTextInputLayout.requestFocus();
            valid = false;
        } else {
            aadharTextInputLayout.setError(null);
        }*/
        if (Street_No.isEmpty()) {
            streetInputLayout.setError("Enter the Street Name or Number ");
            streetInputLayout.requestFocus();
            valid = false;
        } else {
            streetInputLayout.setError(null);
        }
        if (Name.isEmpty()) {
            nameInputLayout.setError("Enter name");
            nameInputLayout.requestFocus();
        } else {
            nameInputLayout.setError(null);
        }

        return valid;
    }

    public void andaman() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.andaman, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void andhra() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.andhrapradesh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void arunachal() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.arunachalpradesh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void assam() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.assam, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void bihar() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.bihar, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void chandigarh() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.chandigarh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void chattisgarh() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.chattisgarh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void dadra() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.dadranagarhaveli, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void daman() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.damandiu, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void delhi() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.Delhi, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void goa() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.Goa, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void gujarat() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.gujarat, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void haryana() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.haryana, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void himachal() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.himachal, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void jammu() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.jammu, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void jharkhand() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.jarkhand, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void karnataka() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.karnataka, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void kerala() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.kerala, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void lakshwadeep() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.lakshwadeep, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void madhyapradesh() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.madhyapradesh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void maharashtra() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.maharashtra, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void manipur() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.manipur, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void meghalaya() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.meghalaya, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void mizoram() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.mizoram, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void nagaland() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.nagaland, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void orissa() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.orissa, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void pondicherry() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.pondicherry, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void punjab() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.Punjab, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void rajasthan() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.rajasthan, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void sikkim() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.sikkim, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void tamilnadu() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.tamilnadu, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void tripura() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.tripura, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void uttarpradesh() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.uttarpradesh, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void uttaranchal() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.uttaranchal, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

    public void westbengal() {
        ArrayAdapter<CharSequence> adap2 = ArrayAdapter.createFromResource(this, R.array.westbengal, R.layout.dropdown_menu_popup_item);
        adap2.setDropDownViewResource(R.layout.dropdown_menu_popup_item);
        editTextFilledExposedCity.setAdapter(adap2);
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/
}
