package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServiceManForm extends AppCompatActivity {
    ProgressDialog pd;
    TextInputEditText editTextExperience;
    TextInputEditText editTextOrganisitonName;
    TextInputEditText editTextOrganisitonAdress;
    TextInputEditText editTextSkill,Rate;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mref,mDatabase,database, subref, dup_ref;
    AutoCompleteTextView editTextFilledExposedProfession, editTextFilledExposedWorkMode;
    private String Experinece,OrganisitionName,OrganisitonAdress,Skill,Profession,WorkMode,Charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_man_form);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        dup_ref = database.child("Users");


        init();



        //create a list of items for the spinner.
        String[] items1 = new String[]{"Select","Android_Developer","Web_Developer","Tutor","General_Surgeon","Electrician", "Mason", "Carpenter", "Painter", "Plumber", "Labour"};

        ArrayAdapter<String> profession_adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, items1) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        //set the spinners adapter to the previously created one.
        editTextFilledExposedProfession.setEnabled(true);
        editTextFilledExposedProfession.setClickable(true);
        editTextFilledExposedProfession.setAdapter(profession_adapter);






        //create a list of items for the spinner.
        String[] items2 = new String[]{"Select","In the home town","Out of the home town","Both"};

        ArrayAdapter<String> work_mode_adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, items2) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        //set the spinners adapter to the previously created one.
        editTextFilledExposedWorkMode.setEnabled(true);
        editTextFilledExposedWorkMode.setClickable(true);
        editTextFilledExposedWorkMode.setAdapter(work_mode_adapter);



    }











    public void init() {

        editTextExperience = findViewById(R.id.editTextExperience);
        editTextOrganisitonName = findViewById(R.id.editTextOrganisationName);
        editTextOrganisitonAdress = findViewById(R.id.editTextOrganisationAdress);
        editTextSkill = findViewById(R.id.DescribeSkill);
        Rate=findViewById(R.id.Rate);
        editTextFilledExposedProfession = findViewById(R.id.spinner_profession);
        editTextFilledExposedWorkMode = findViewById(R.id.Work_venue);


        pd = new ProgressDialog(this);
    }


    public void save(View view) {
        pd.setTitle("Registering User Please Wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        createUser();
    }

    public void createUser() {
        Charge=Rate.getText().toString();
        Experinece = editTextExperience.getText().toString();
        OrganisitionName = editTextOrganisitonName.getText().toString();
        OrganisitonAdress = editTextOrganisitonAdress.getText().toString();
        Skill = editTextSkill.getText().toString();
        Profession = editTextFilledExposedProfession.getText().toString();
        WorkMode = editTextFilledExposedWorkMode.getText().toString();



        if (!validation()) {
            // Toast.makeText(getApplication() , "Reached till validation" , Toast.LENGTH_LONG).show();
            pd.dismiss();
            return;
        }


        writedata();

    }

    private void writedata() {

        SharedPreferences pre=getSharedPreferences("ankit", Context.MODE_PRIVATE);
        SharedPreferences.Editor edito=pre.edit();

        User u = new User(Profession, Experinece, OrganisitionName,OrganisitonAdress, WorkMode, Skill, Charge);
        subref = dup_ref.child(mAuth.getCurrentUser().getUid());
        subref.child("Profession").setValue(u.getProfession());
        subref.child("Experience").setValue(u.getExperience());
        subref.child("OrganisitionName").setValue(u.getOrganisitionName());
        subref.child("OrganisitonAdress").setValue(u.getOrganisitonAdress());
        subref.child("WorkMode").setValue(u.WorkMode);
        subref.child("Skill").setValue(u.getSkill());
        subref.child("Charge").setValue(u.getCharge());
        subref.child("type").setValue("work");


       // Intent iin= getIntent();
        //int bundle = iin.getIntExtra("switch1",0);

        if (getIntent().getIntExtra("switch12",0)==1){
            Intent intent=new Intent(ServiceManForm.this,WorkMain.class);
            startActivity(intent);
            edito.putString("workmode",u.WorkMode);
            edito.apply();


            edito.putString("Profession",u.getProfession());
            edito.apply();
            finish();

        }

        else {

            Intent intent=new Intent(ServiceManForm.this,WorkMain.class);
            startActivity(intent);
            edito.putString("workmode",u.WorkMode);
            edito.apply();


            edito.putString("Profession",u.getProfession());
            edito.apply();




            edito.putString("option","work");
            edito.apply();
            finish();
        }


    }

    private boolean validation() {
        boolean valid = true;
        TextInputLayout chargeLayout=findViewById(R.id.editTextRateLayout);
        TextInputLayout categroyLayout=findViewById(R.id.categorylayout);
        TextInputLayout workvenueLayout=findViewById(R.id.WorkVenuelayout);
        Profession = editTextFilledExposedProfession.getText().toString();
        WorkMode = editTextFilledExposedWorkMode.getText().toString();
        Charge=Rate.getText().toString();


        if (Profession.isEmpty()||Profession.equals("Select")){
            categroyLayout.setError("Enter the category of your work");
            categroyLayout.requestFocus();
           valid=false;
        }

        if (WorkMode.isEmpty()||WorkMode.equals("Select")){
            workvenueLayout.setError("Enter the work mode");
            workvenueLayout.requestFocus();
            valid=false;
        }

        if (Charge.isEmpty()){
            chargeLayout.setError("Enter your charge rate");
            chargeLayout.requestFocus();
            valid=false;
        }


        return valid;
    }
}