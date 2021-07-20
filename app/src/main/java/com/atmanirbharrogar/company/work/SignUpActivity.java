package com.atmanirbharrogar.company.work;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    String[] states;
    String LandMark = "";
    String id;
    String Street_No;
    String Pincode;
    String State;
    String City;
    String Gender = "";
    String Name = "";
    String Birth;
    TextInputEditText editTextLandMark;
    TextInputEditText editTextStreet;
    TextInputEditText editTextPincode;
    TextInputEditText editTextName;
    TextInputEditText editTextBirth;

    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mref,mDatabase,database, subref, dup_ref;
    ArrayList<User> Userlist;
    AutoCompleteTextView editTextFilledExposedGender, editTextFilledExposedState, editTextFilledExposedCity;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initDatePicker();
       // editTextDate=findViewById(R.id.editTextDate);
        editTextBirth=findViewById(R.id.editTextBirth);


        editTextBirth.setText(getTodayDate());






        new checkInternetConnection(this).checkConnection();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference();
        dup_ref = database.child("Users");



        init();

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
        editTextBirth=findViewById(R.id.editTextBirth);
        editTextLandMark = findViewById(R.id.editTextLandMark);
        editTextStreet = findViewById(R.id.editTextStreet);
        editTextName = findViewById(R.id.editTextName);
        editTextPincode = findViewById(R.id.editTextPincode);
        editTextFilledExposedGender = findViewById(R.id.spinner_gender);
        editTextFilledExposedState = findViewById(R.id.statespinner);
        editTextFilledExposedCity = findViewById(R.id.cityspinner);

        pd = new ProgressDialog(this);
    }

    public void test(View v) {
        pd.setTitle("Registering User Please Wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        createUser();
    }

    public void createUser() {
     //   pd.setTitle("Registering User Please Wait...");
       // pd.show();
       // pd.setCancelable(false);
        //pd.setCanceledOnTouchOutside(false);
        Birth=editTextBirth.getText().toString();
        LandMark = editTextLandMark.getText().toString();
        Name = editTextName.getText().toString();
        State = editTextFilledExposedState.getText().toString();
        Street_No = editTextStreet.getText().toString();
        City = editTextFilledExposedCity.getText().toString();
        Pincode = editTextPincode.getText().toString();


        if (!validation()) {
           // Toast.makeText(getApplication() , "Reached till validation" , Toast.LENGTH_LONG).show();
            pd.dismiss();
            return;
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        id = currentUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mref = mDatabase.child("Users").child(id);
        Intent intent = getIntent();
        if (intent.getStringExtra("mobile")!=null){
            String mobile = intent.getStringExtra("mobile");
            //Log.d(mobile,"hi");
            mref.child("Mobile").setValue(mobile);
        }

        writedata();

    }

    public void writedata() {
        SharedPreferences pre=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor edito=pre.edit();

        User u = new User(LandMark, id,  Street_No, Pincode, State, City, Gender, Name, Birth);
        //pd.setTitle("Verifying code please  wait...");
        //pd.show();
      //  pd.setCancelable(false);
       // pd.setCanceledOnTouchOutside(false);
        subref = dup_ref.child(id);
        //subref.child("Email").setValue(u.getEmail());

        subref.child("Name").setValue(u.getName());
        subref.child("Id").setValue(u.getId());
        subref.child("Address").setValue(u.getStreet_No());
        subref.child("Pincode").setValue(u.getPincode());
        subref.child("State").setValue(editTextFilledExposedState.getText().toString().toLowerCase().trim());
        subref.child("City").setValue(editTextFilledExposedCity.getText().toString().toLowerCase().trim());
        subref.child("Gender").setValue(editTextFilledExposedGender.getText().toString());
        subref.child("LandMark").setValue(u.getEmail());
        subref.child("Birth").setValue(u.getBirth());



        Intent intent=new Intent(SignUpActivity.this,PhotoUpload.class);

        edito.putString("name", "notnull");
        edito.apply();
        //  pd.dismiss();
        startActivity(intent);

        finish();


    }

    private boolean validation() {
        boolean valid = true;

        TextInputLayout emailTextInputLayout = findViewById(R.id.editTextEmailLayout);
        TextInputLayout PINTextInputLayout = findViewById(R.id.editTextPincodeLayout);
        TextInputLayout streetInputLayout = findViewById(R.id.editTextStreetLayout);
        TextInputLayout nameInputLayout = findViewById(R.id.editTextNameLayout);
        if (LandMark.isEmpty()) {
        //if (LandMark.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(LandMark).matches()) {
            emailTextInputLayout.setError("Please Enter LandMark");
            emailTextInputLayout.requestFocus();
            valid = false;
        } else {
            emailTextInputLayout.setError(null);
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


    private String getTodayDate() {

        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONDAY);
        month = month+1;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date= makeDateString(day, month, year);
                editTextBirth.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONDAY);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);



    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month)+ " " +day+ " " + year ;
    }

    private String getMonthFormat(int month) {
        if (month ==1)
            return "JAN";
        if (month ==2)
            return "FEB";
        if (month ==3)
            return "MARCH";
        if (month ==4)
            return "APRIL";
        if (month ==5)
            return "MAY";
        if (month ==6)
            return "JUNE";
        if (month ==7)
            return "JULY";
        if (month ==8)
            return "AUG";
        if (month ==9)
            return "SEP";
        if (month ==10)
            return "OCT";
        if (month ==11)
            return "NOV";
        if (month ==12)
            return "DEC";

        return "JAN";
    }

   // public void DatePicker(View view){
   public void DatePicker(View view){
        datePickerDialog.show();

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

}