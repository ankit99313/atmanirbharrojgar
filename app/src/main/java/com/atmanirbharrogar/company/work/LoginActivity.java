package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pd;




    //Adding a member variable for the key verification in progress
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    //Creating FirebaseAuth member variable
    private FirebaseAuth mAuth;

    TextInputEditText editTextNumber;
    TextInputEditText editTextOtp;
    MaterialButton button_continue;
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
        setContentView(R.layout.activity_login);
        editTextNumber=(TextInputEditText)findViewById(R.id.editTextNumber);
        button_continue=(MaterialButton)findViewById(R.id.button_continue);



        pd = new ProgressDialog(this);


        // Restoring the instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


        new checkInternetConnection(this).checkConnection();



        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextNumber.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextNumber.setError("Enter a valid mobile");
                    editTextNumber.requestFocus();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, OtpVerify.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                finish();
            }
        });


    }


    public void onBackPressed() {
            super.onBackPressed();
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}