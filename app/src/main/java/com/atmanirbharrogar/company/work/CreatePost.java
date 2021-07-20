package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePost extends AppCompatActivity {
    AutoCompleteTextView editTextFilledExposedProfession,editTextFilledExposedWorkerTime,editTextFilledExposedJudgeWork;
    EditText title,longdescription;
    Button createButton;
    private String Title;
    private String Category;
    private String Longdescripti;
    private String workertime;
    private String judgework;
    ProgressDialog pd;
    FirebaseAuth mAuth;
    //TextView SeePreviousPost;
    CardView seepreviouspost;

    DatabaseReference database,database2,database3;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        seepreviouspost=(CardView) findViewById(R.id.seepreviouspost);

        pd = new ProgressDialog(this);

        new checkInternetConnection(this).checkConnection();


        mAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Order").child(mAuth.getCurrentUser().getUid());
        database2= FirebaseDatabase.getInstance().getReference("NewOrder");
        database3= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

        editTextFilledExposedProfession=findViewById(R.id.spinner_profession);
        editTextFilledExposedJudgeWork=findViewById(R.id.spinner_judgework);
        editTextFilledExposedWorkerTime=findViewById(R.id.spinner_worktime);
        title=findViewById(R.id.title);
        longdescription=findViewById(R.id.longdescription);
        createButton=findViewById(R.id.createbutton);

        seepreviouspost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreatePost.this,ShowCreatedPost.class);
                startActivity(intent);
            }
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
        editTextFilledExposedProfession.setEnabled(true);
        editTextFilledExposedProfession.setClickable(true);
        editTextFilledExposedProfession.setAdapter(profession_adapter);


        String[] items2=new String[]{"Under 3 days","Under 1 week","Under 1 month"};
        ArrayAdapter<String> workertime=new ArrayAdapter<String>(this,R.layout.dropdown_menu_popup_item,items2){
            @Override
            public boolean isEnabled(int position) {
                return super.isEnabled(position);
            }
        };

        editTextFilledExposedWorkerTime.setEnabled(true);
        editTextFilledExposedWorkerTime.setClickable(true);
        editTextFilledExposedWorkerTime.setAdapter(workertime);


        String[] items3=new String[]{"yes","No"};
        ArrayAdapter<String> judgework=new ArrayAdapter<String>(this,R.layout.dropdown_menu_popup_item,items3){
            @Override
            public boolean isEnabled(int position) {
                return super.isEnabled(position);
            }
        };

        editTextFilledExposedJudgeWork.setEnabled(true);
        editTextFilledExposedJudgeWork.setClickable(true);
        editTextFilledExposedJudgeWork.setAdapter(judgework);


    }
    
    public void create(View view){


        pd.setTitle("Creating Post Please Wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        TextInputLayout workertimeLayout=findViewById(R.id.worktimelayout);
        TextInputLayout categroyLayout=findViewById(R.id.categorylayout);
        Category=editTextFilledExposedProfession.getText().toString();
        workertime=editTextFilledExposedWorkerTime.getText().toString();
        judgework=editTextFilledExposedJudgeWork.getText().toString();
        Title=title.getText().toString();
        Longdescripti=longdescription.getText().toString();


        if (Category.isEmpty()||Category.equals("Select")){
            categroyLayout.setError("Enter the category of your work");
            categroyLayout.requestFocus();
            pd.dismiss();
           return;
        }
        if (workertime.isEmpty()||workertime.equals("Select"))
        {
            workertimeLayout.setError("please select an option");
            workertimeLayout.requestFocus();
            pd.dismiss();
            return;
        }

       if (Title.isEmpty())
       {
           title.setError("Enter short description about your work");
           title.requestFocus();
           pd.dismiss();
           return;
       }

       if (Longdescripti.isEmpty())
       {
           longdescription.setError("Enter long description about your work");
           longdescription.requestFocus();
           pd.dismiss();
           return;
       }


  database3.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        database.push().setValue(new PostAndBid(Category,workertime,judgework,Title,Longdescripti));
        database2.push().setValue(new PostAndBid(Category,workertime,judgework,Title,Longdescripti,snapshot.child("Address").getValue(String.class),snapshot.child("Mobile").getValue(String.class),snapshot.child("Pincode").getValue(String.class)));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


       PostAndBid u=new PostAndBid(Category,workertime,judgework,Title,Longdescripti);

        Intent intent=new Intent(CreatePost.this,ShowCreatedPost.class);
        startActivity(intent);
        Toast.makeText(this, "Successfully created your post", Toast.LENGTH_LONG).show();
        finish();
      /* ref.push().child("Profession").setValue(u.getCategory());
       ref.push().child("WorkerTime").setValue(u.getWorkerTime());
       ref.push().child("JudgeWork").setValue(u.getJudgeWork());
       ref.push().child("Title").setValue(u.getTitle());
       ref.push().child("LongDescription").setValue(u.getLongDescription());*/



    }


}