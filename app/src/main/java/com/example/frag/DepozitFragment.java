package com.example.frag;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DepozitFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Depozit_name> list;
    AdapterDepozite adapterDepozite;
    TextView button;
    Dialog addDepo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String number = getArguments().getString("number");
        View view = inflater.inflate(R.layout.fragment_depozit, container, false);
        recyclerView=view.findViewById(R.id.transactionlist);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(number).child("depozite");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        adapterDepozite = new AdapterDepozite(getContext(), list);
        recyclerView.setAdapter(adapterDepozite);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Depozit_name depozit_name = dataSnapshot.getValue(Depozit_name.class);

                    list.add(0, depozit_name);

                }
                adapterDepozite.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "N-am gasit nimic, stai cuminte", Toast.LENGTH_SHORT).show();

            }
        });

        button= view.findViewById(R.id.buttonplus);
        addDepo=new Dialog(getContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDepo.setContentView(R.layout.add_deposit);
                addDepo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addDepo.show();

                View dialogView = addDepo.findViewById(android.R.id.content);
                EditText numed=dialogView.findViewById(R.id.editTextTextPersonName6);
                EditText sumad=dialogView.findViewById(R.id.editTextTextPersonName7);
                Button buttond=dialogView.findViewById(R.id.button);
                TextView ratad=dialogView.findViewById(R.id.textView31);

                Spinner spinner=dialogView.findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(), R.array.perioadaDepo, android.R.layout.simple_spinner_item );
                ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(getContext(), R.array.perioadaDeposub1000, android.R.layout.simple_spinner_item );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                sumad.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        spinner.setAdapter(adapter);
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        String sumaText = sumad.getText().toString();
                        int suma = 0;
                        if (!sumaText.isEmpty()) {
                            suma = Integer.parseInt(sumaText);
                        }
                        if (suma<1000) {
                            spinner.setAdapter(adapter2);
                        }else{
                            spinner.setAdapter(adapter);
                        }

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String selectedValue = spinner.getItemAtPosition(i).toString();
                                switch (selectedValue){
                                    case "180":
                                        ratad.setText("2");
                                        break;
                                    case "360":
                                        ratad.setText("3");
                                        break;
                                    case "720":
                                        ratad.setText("4");
                                        break;
                                    case "1080":
                                        ratad.setText("5");
                                        break;
                                    case "1800":
                                        ratad.setText("6");
                                        break;
                                    case "2520":
                                        ratad.setText("7");
                                        break;
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });



                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedValue = spinner.getItemAtPosition(i).toString();
                        switch (selectedValue){
                            case "360":
                                ratad.setText("3");
                                break;
                            case "720":
                                ratad.setText("4");
                                break;
                            case "1080":
                                ratad.setText("5");
                                break;
                            case "1800":
                                ratad.setText("6");
                                break;
                            case "2520":
                                ratad.setText("7");
                                break;
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                buttond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String currentDate = dateFormat.format(calendar.getTime());

                        String numef=numed.getText().toString();
                        int soldf=Integer.parseInt(sumad.getText().toString());
                        int perioadaf=Integer.parseInt(spinner.getSelectedItem().toString());
                        int rataf=Integer.parseInt(ratad.getText().toString());



                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users").child(number).child("depozite").push();
                        String depoId=reference2.getKey();
                        HelperDepo helperDepo= new HelperDepo(numef,perioadaf,rataf,soldf,currentDate,depoId,number);
                        reference2.setValue(helperDepo);

                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users").child(number);
                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int cont=snapshot.child("sold").getValue(Integer.class);
                                reference3.child("sold").setValue(cont-soldf);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Toast.makeText(getActivity(), "Depozitul a fost creat cu succes", Toast.LENGTH_SHORT).show();
                        list.clear();
                        adapterDepozite.notifyDataSetChanged();
                        addDepo.dismiss();


                    }
                });


            }
        });


        return view;
    }
}