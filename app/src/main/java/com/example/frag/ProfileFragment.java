package com.example.frag;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {


    TextView t1,t2,t3,ochi, copy;
    String ibanochi;
    Dialog dorestiCopiere;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        String number = bundle.getString("number");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        t3=view.findViewById(R.id.t3);
        ochi=view.findViewById(R.id.ochi);
        copy=view.findViewById(R.id.copy);
        dorestiCopiere=new Dialog(getContext());

        showInfo(number);
        View cardView= view.findViewById(R.id.view);
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dorestiCopiere.setContentView(R.layout.copiuta);
                dorestiCopiere.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dorestiCopiere.show();

                View dialogView = dorestiCopiere.findViewById(android.R.id.content);
                TextView num=dialogView.findViewById(R.id.textView34);
                TextView iban=dialogView.findViewById(R.id.textView35);
                TextView mon=dialogView.findViewById(R.id.textView37);
                TextView ban=dialogView.findViewById(R.id.textView38);
                TextView clo=dialogView.findViewById(R.id.textView75);
                TextView cop=dialogView.findViewById(R.id.textView76);

                String text= num.getText().toString()+iban.getText().toString()+mon.getText().toString()+ban.getText().toString();
                clo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dorestiCopiere.dismiss();
                    }
                });

                cop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip=ClipData.newPlainText("copy", text);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Am copiat datele", Toast.LENGTH_SHORT).show();
                    }
                });



                return true;
            }
        });
        ochi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t3.getText().equals("**** **** **** ****")){
                    t3.setText(ibanochi);
                }
                else {
                    t3.setText("**** **** **** ****");
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard= (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("copy", t3.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Am copiat datele", Toast.LENGTH_SHORT).show();
            }
        });

    return view;
    }

    public void showInfo(String number) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(number)) {

                    //Toast.makeText(getActivity(), "Am gasit", Toast.LENGTH_SHORT).show();
                    String numeFromDb = snapshot.child(number).child("nume").getValue(String.class);
                    Integer soldFromDb = snapshot.child(number).child("sold").getValue(Integer.class);
                    String telefonFromDb = snapshot.child(number).child("telefon").getValue(String.class);
                    String ibanFromDb=snapshot.child(number).child("iban").getValue(String.class);

                    t1.setText(numeFromDb);
                    t2.setText(soldFromDb.toString()+" $");

                    ibanochi=ibanFromDb;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "N-am gasit nimic, stai cuminte", Toast.LENGTH_SHORT).show();

            }
        });

    }

}