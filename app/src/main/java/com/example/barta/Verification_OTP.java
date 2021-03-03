package com.example.barta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Verification_OTP extends AppCompatActivity {
    TextInputEditText etOTP;
    Button verirfy_otp_button;
    String otp_str;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification__o_t_p);

        etOTP=findViewById(R.id.OTP);
        verirfy_otp_button=findViewById(R.id.verirfy_otp_button);
        firebaseAuth=FirebaseAuth.getInstance();

        otp_str=getIntent().getStringExtra("auth");

        verirfy_otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code=etOTP.getText().toString();
                if(!verification_code.isEmpty())
                {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otp_str,verification_code);
                    signIn(credential);
                }
                else
                {
                    Toast.makeText(Verification_OTP.this,"Please Enter OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signIn(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    sendToMain();
                }
                else
                {
                    Toast.makeText(Verification_OTP.this,"Verification Failed",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            sendToMain();
        }
    }

    private void sendToMain()
    {
        startActivity(new Intent(Verification_OTP.this,MainActivity2.class));
        finish();
    }
}