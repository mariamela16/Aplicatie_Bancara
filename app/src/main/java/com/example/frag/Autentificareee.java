package com.example.frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Autentificareee extends AppCompatActivity {

    TextView textView;
    EditText phone, otp;
    Button buttonGenerate,buttonVerify;
    FirebaseAuth mAuth;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificareee);

        phone=findViewById(R.id.phone);
        textView=findViewById(R.id.textView);
        otp=findViewById(R.id.otp);
        buttonGenerate=findViewById(R.id.buttonGenerate);
        buttonVerify=findViewById(R.id.buttonVerify);
        mAuth=FirebaseAuth.getInstance();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=phone.getText().toString().trim();
                System.out.println("acesta este numarul1 "+number);

                Intent intent = new Intent(Autentificareee.this, MainActivity.class);
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNumberUser();
            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getText().toString().isEmpty()){
                    Toast.makeText(Autentificareee.this, "Wrong OTP Entered",Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(otp.getText().toString());

                }

            }
        });
    }


    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+40"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyCode(String Code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,Code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Autentificareee.this, "Login Succesfull",Toast.LENGTH_SHORT).show();
                            pass();

                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            final String code= credential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Autentificareee.this, "Verification Failed",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {


            super.onCodeSent(s,token);
            verificationId=s;

        }
    };

    public void pass(){
        String number=phone.getText().toString().trim();
        System.out.println("acesta este numarul1 "+number);

        Intent intent = new Intent(Autentificareee.this, MainActivity.class);
        intent.putExtra("number",number);
        startActivity(intent);

    }

    public void checkNumberUser(){

        String number=phone.getText().toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        if(number.isEmpty()){
            Toast.makeText(Autentificareee.this, "Insereaza un numar valid de telefon",Toast.LENGTH_SHORT).show();
        }else{
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(number)) {

                        sendVerificationCode(number);

                    } else {
                        Toast.makeText(Autentificareee.this, "Nu esti inregistrat in baza de date",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}