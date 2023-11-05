package com.example.frag;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class TranzactiiFragment extends Fragment {


    RecyclerView recyclerView;
    AdapterTranzactii adapterTranzactii;
    ArrayList<Tranzactii> list;
    TextView buttonplus;
    Dialog tran;
    SearchView searchView;
    Button sort, general,desc, venituri, cheltuieli, facturi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String number = getArguments().getString("number");
        View view = inflater.inflate(R.layout.fragment_tranzactii, container, false);
        recyclerView = view.findViewById(R.id.transactionlist);
        searchView=view.findViewById(R.id.searchView);
        sort=view.findViewById(R.id.button7);
        general=view.findViewById(R.id.button9);
        desc=view.findViewById(R.id.button8);
        venituri=view.findViewById(R.id.button10);
        cheltuieli=view.findViewById(R.id.button11);
        facturi=view.findViewById(R.id.button12);

        searchView.clearFocus();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(number).child("tranzactii");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        adapterTranzactii = new AdapterTranzactii(getContext(), list);
        recyclerView.setAdapter(adapterTranzactii);


        reference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tranzactii tranzactii = dataSnapshot.getValue(Tranzactii.class);

                    list.add(0, tranzactii);

                }
                adapterTranzactii.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "N-am gasit nimic, stai cuminte", Toast.LENGTH_SHORT).show();

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListCrescator();

            }
        });

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterTranzactii = new AdapterTranzactii(getContext(), list);
                recyclerView.setAdapter(adapterTranzactii);
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListDesc();
            }
        });

        venituri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListVenituri();
            }
        });

        cheltuieli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListCheltuieli();
            }
        });

        facturi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListFacturi();
            }
        });



        buttonplus=view.findViewById(R.id.buttonplus);
        tran=new Dialog(getContext());
        buttonplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tran.setContentView(R.layout.add_transaction);
                tran.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tran.show();

                View dialogView = tran.findViewById(android.R.id.content);
                EditText sum=dialogView.findViewById(R.id.editTextTextPersonName);
                TextView tv=dialogView.findViewById(R.id.textView14);
                Button button=dialogView.findViewById(R.id.button);
                Spinner spinner=dialogView.findViewById(R.id.spinner);
                SwitchCompat switchCompat= dialogView.findViewById(R.id.switchCompat);
                EditText nr= dialogView.findViewById(R.id.editTextTextPersonName2);
                EditText data= dialogView.findViewById(R.id.editTextTextPersonName4);
                EditText descriere=dialogView.findViewById(R.id.editTextTextPersonName3);
                EditText catre=dialogView.findViewById(R.id.editTextTextPersonName5);

                        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            spinner.setVisibility(View.VISIBLE);
                        } else {
                            spinner.setVisibility(View.GONE);
                        }
                    }
                });
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedValue = spinner.getItemAtPosition(i).toString();
                        if(selectedValue.equals("Apanova")) {
                            nr.setText("0433564348");
                            catre.setText("Apanova");
                        }else if(selectedValue.equals("EON")){
                            nr.setText("0222564348");
                            catre.setText("EON");
                        }else if(selectedValue.equals("Enel")){
                            nr.setText("0999999111");
                            catre.setText("Enel");
                        }else if(selectedValue.equals("Digi")){
                            nr.setText("0212564348");
                            catre.setText("Digi");
                        }else if(selectedValue.equals("Orange")){
                            nr.setText("5555564521");
                            catre.setText("Orange");
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(), R.array.furnizori, android.R.layout.simple_spinner_item );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDate = dateFormat.format(calendar.getTime());
                data.setText(currentDate);



                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String suma= "-"+sum.getText().toString();
                        String suma2= sum.getText().toString();
                        String destinatar=nr.getText().toString();
                        String desc=descriere.getText().toString();
                        String datacalendar=data.getText().toString();
                        String spre=catre.getText().toString();

                        HelperTran helperClass=new HelperTran(spre, desc,suma,datacalendar);


                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users").child(number).child("tranzactii").push();
                        reference2.setValue(helperClass);


                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users");
                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(destinatar)) {
                                    int cont=snapshot.child(destinatar).child("sold").getValue(Integer.class);
                                    String dela=snapshot.child(number).child("nume").getValue(String.class);
                                    reference3.child(destinatar).child("sold").setValue(cont+Integer.parseInt(suma2));
                                    Integer soldFromDb = snapshot.child(number).child("sold").getValue(Integer.class);
                                    reference3.child(number).child("sold").setValue(soldFromDb- Integer.parseInt(suma2));

                                    HelperTran helperClassforDestinatar=new HelperTran(dela, desc,suma2,datacalendar);
                                    DatabaseReference reference50 = FirebaseDatabase.getInstance().getReference("users").child(destinatar).child("tranzactii").push();
                                    reference50.setValue(helperClassforDestinatar);



                                }else{
                                    Integer soldFromDb = snapshot.child(number).child("sold").getValue(Integer.class);
                                    reference3.child(number).child("sold").setValue(soldFromDb- Integer.parseInt(suma2));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Toast.makeText(getActivity(), "Am efectuat transferul", Toast.LENGTH_SHORT).show();
                        list.clear();
                        adapterTranzactii.notifyDataSetChanged();
                        tran.dismiss();



                    }
                });

            }
        });



    return view;
    }

    public  void searchList(String text){
        ArrayList<Tranzactii> searchList= new ArrayList<>();
        for(Tranzactii tranzactie: list){
            if(tranzactie.getNume().toLowerCase().contains(text.toLowerCase())){
                searchList.add(tranzactie);
            }
        }
        adapterTranzactii.SearchDataList(searchList);
    }

    public  void sortListCrescator(){
        ArrayList<Tranzactii> sortedList= new ArrayList<>(list);;
        Comparator<Tranzactii> compareByNumber = new Comparator<Tranzactii>() {
            @Override
            public int compare(Tranzactii t1, Tranzactii t2) {
                return Integer.parseInt(t1.getSold()) - Integer.parseInt(t2.getSold());
            }
        };
        Collections.sort(sortedList, compareByNumber);
        adapterTranzactii.sortAfterSuma(sortedList);
    }


    public  void sortListDesc(){
        ArrayList<Tranzactii> sortedList= new ArrayList<>(list);
        Comparator<Tranzactii> compareByNumber = new Comparator<Tranzactii>() {
            @Override
            public int compare(Tranzactii t1, Tranzactii t2) {
                return Integer.parseInt(t2.getSold()) - Integer.parseInt(t1.getSold());
            }
        };
        Collections.sort(sortedList, compareByNumber);
        adapterTranzactii.sortAfterSuma(sortedList);
    }

    public  void sortListVenituri() {
        ArrayList<Tranzactii> sortedList2 = new ArrayList<>(list);
        for (Tranzactii tranzactie : list) {
            if (Integer.parseInt(tranzactie.getSold()) <0) {
                sortedList2.remove(tranzactie);
            }
        }
        adapterTranzactii.sortAfterVenituri(sortedList2);
    }


    public  void sortListCheltuieli() {
        ArrayList<Tranzactii> sortedList2 = new ArrayList<>(list);
        for (Tranzactii tranzactie : list) {
            if (Integer.parseInt(tranzactie.getSold()) >0) {
                sortedList2.remove(tranzactie);
            }
        }
        adapterTranzactii.sortAfterVenituri(sortedList2);
    }

    public  void sortListFacturi(){
        ArrayList<Tranzactii> searchList= new ArrayList<>(list);
        for(Tranzactii tranzactie: list){
            if(tranzactie.getDescriere().contains("Factura")){
                searchList.add(tranzactie);
            }else{
                searchList.remove(tranzactie);
            }
        }
        adapterTranzactii.SearchDataList(searchList);
    }
}