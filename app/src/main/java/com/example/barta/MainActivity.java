package com.example.barta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etEmail,etName, etPassword, etConfirmPassword,etPhoneNumber;
    private  String email,name,password, confirmPassword,phone_number;
    CountryCodePicker ccp_sign_in;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2;
    Button sign_in_btn_phone_number,sign_in_btn_email;
    LinearLayout linearLayout1,linearLayout2;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhoneNumber=findViewById(R.id.Phone_Number);
        ccp_sign_in = (CountryCodePicker) findViewById(R.id.ccp_signIn);

        radioGroup=findViewById(R.id.radio_grp);

        radioButton1=findViewById(R.id.radio_phone_number);
        radioButton2=findViewById(R.id.radio_email);

        sign_in_btn_email=findViewById(R.id.btn_sign_up_1);
        sign_in_btn_phone_number=findViewById(R.id.btn_sign_up_2);

        linearLayout1=findViewById(R.id.linear_layout_phone_number);
        linearLayout2=findViewById(R.id.linear_layout_log_in);

        if(radioButton1.isChecked())
        {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        }
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);

            }
        });
        sign_in_btn_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number=etPhoneNumber.getText().toString().trim();
                if(phone_number.isEmpty())
                {
                    etPhoneNumber.setError("Enter Phone Number");
                    return;
                }
                else
                {
                    phone_number= ccp_sign_in.getFullNumberWithPlus()+phone_number;
                    System.out.println("------------------------"+phone_number+"---------------------");


                    PhoneAuthOptions option= PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phone_number)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(option);
                }

            }
        });

        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"OTP HAS BEEN SENT",Toast.LENGTH_SHORT).show();
                        Intent otpIntent=new Intent(MainActivity.this,Verification_OTP.class);
                        otpIntent.putExtra("auth",s);
                        startActivity(otpIntent);
                    }
                },1000);
            }
        };
    }
    private void signIn(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    sendToMain();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendToMain()
    {
        Intent mainIntent=new Intent(MainActivity.this,MainActivity2.class);
        startActivity(mainIntent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null)
        {
            sendToMain();
        }
    }

}