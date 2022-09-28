package com.atmanirbharrogar.company.work;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordModule extends AppCompatActivity {
    TextInputEditText editTextEmail;
    TextInputLayout editTextEmailLayout;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_module);
        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

    }

    private void PasswordReset(String Email) {
        pd.setMessage("Sending");
        pd.show();

        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Reset Password intruction has sent to your Registered mail id", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Verification failed Invalid mail id try again", Toast.LENGTH_SHORT).show();
        });
    }

    boolean validation(String Email) {
        boolean valid = true;
        if (Email.isEmpty()) {
            editTextEmailLayout.setError("Email is required");
            editTextEmailLayout.requestFocus();
            valid = false;
        } else {
            editTextEmailLayout.setError(null);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            editTextEmailLayout.setError("Please Enter valid Email address");
            editTextEmailLayout.requestFocus();
            valid = false;
        } else {
            editTextEmailLayout.setError(null);
        }
        return valid;
    }

    public void Reset(View view) {
        editTextEmail = findViewById(R.id.editTextForgotPassword);
        editTextEmailLayout = findViewById(R.id.editTextForgotPasswordLayout);
        String Email = editTextEmail.getText().toString();
        if (validation(Email)) {
            editTextEmailLayout.setError(null);
            PasswordReset(Email);
        }
    }

}
