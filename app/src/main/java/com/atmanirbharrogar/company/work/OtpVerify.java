package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpVerify extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    private Button resend;
    TextView textnumber, textresend;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    //Adding a member variable for PhoneAuthProvider.ForceResendingToken callback.
    private PhoneAuthProvider.ForceResendingToken mResendToken;
   // private String verificationId;
    //private static final String KEY_VERIFICATION_ID = "key_verification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        pd = new ProgressDialog(this);
        resend=(Button)findViewById(R.id.buttonresend);
        textnumber=(TextView)findViewById(R.id.textnumber);
        textresend=(TextView)findViewById(R.id.textresend);

        new checkInternetConnection(this).checkConnection();

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        textnumber.setText("+91 "+mobile);

        textresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(mobile, mResendToken);
            }
        });





        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                pd.setTitle("Verifying code please  wait...");
                pd.show();
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    pd.dismiss();
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

        // Restore instance state
        // put this code after starting phone number verification
       /* if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }*/

    }





    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Intent intent=new Intent(OtpVerify.this,LoginActivity.class);
            startActivity(intent);
            finish();
            //Toast.makeText(OtpVerify.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(OtpVerify.this, "something went wrong", Toast.LENGTH_LONG).show();


        }



        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpVerify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                           // Intent intent = new Intent(Ver.this, ProfileActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(intent);
                            //verification successful we will start the profile activity
                            // User u = new User(editTextNumber.getText().toString());

                            //checking user is already registered or not

                            SharedPreferences pre=getSharedPreferences("ankit", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edito=pre.edit();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");


                            userRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    //snapshot.child("type").getValue(String.class);

                                    edito.putString("workmode",snapshot.child("WorkMode").getValue(String.class));
                                    edito.apply();


                                    edito.putString("Profession",snapshot.child("Profession").getValue(String.class));
                                    edito.apply();

                                    if (snapshot.child("type").getValue(String.class)!=null) {


                                        if (snapshot.child("type").getValue(String.class).equals("work")) {
                                            startActivity(new Intent(OtpVerify.this, WorkMain.class));
                                            edito.putString("option", "work");
                                            edito.apply();
                                            pd.dismiss();
                                            finish();
                                        } else if (snapshot.child("type").getValue(String.class).equals("hire")) {
                                            startActivity(new Intent(OtpVerify.this, HireMain.class));
                                            edito.putString("option", "hire");
                                            edito.apply();
                                            pd.dismiss();
                                            finish();
                                        } else if (snapshot.child("type").getValue(String.class).equals("student")) {
                                            startActivity(new Intent(OtpVerify.this, HireMain.class));
                                            edito.putString("option", "student");
                                            edito.apply();
                                            pd.dismiss();
                                            finish();
                                        }

                                    }

                                    else if (snapshot.child("Name").getValue(String.class)!=null)
                                    {
                                        //edito.putString("name", "notnull");
                                        //edito.apply();
                                        startActivity(new Intent(OtpVerify.this,AccountType.class));
                                        finish();
                                    }


                                    else
                                    {

                                        edito.putString("signup","yes");
                                        edito.apply();
                                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                        intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
                                        startActivity(intent);
                                        pd.dismiss();
                                        finish();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                pd.dismiss();
                                message = "Invalid code entered...";
                                Toast.makeText(OtpVerify.this, "Invalid code entered...", Toast.LENGTH_SHORT).show();
                                editTextCode.setError("Enter valid code");
                                editTextCode.requestFocus();
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }




    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(OtpVerify.this, LoginActivity.class);
        startActivity(intent);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}



