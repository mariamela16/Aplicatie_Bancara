package com.example.frag;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class GraficFragment extends Fragment {

    PieChart pieChart;
    Button general, lunar, predictie;
    TextView sumaprezisa;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String number = getArguments().getString("number");
        View view = inflater.inflate(R.layout.fragment_grafic, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        general = view.findViewById(R.id.button3);
        lunar = view.findViewById(R.id.button2);
        predictie = view.findViewById(R.id.button4);
        sumaprezisa = view.findViewById(R.id.textView32);

        afisareGeneral(number);

        lunar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (i1 < 10) {
                            String iNou = "0" + i1;
                            String lunaSiAn = iNou + "/" + i;
                            afisareLunar(number, lunaSiAn);
                            lunar.setText(lunaSiAn);

                        } else {
                            String iNou = Integer.toString(i);
                            String lunaSiAn = iNou + i;
                            afisareLunar(number, lunaSiAn);
                        }

                    }
                });

                pd.show(getFragmentManager(), "MonthYearPickerDialog");

            }
        });


        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afisareGeneral(number);
            }
        });

        predictie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predictie(number);
            }
        });


        return view;
    }


    public void afisareGeneral(String number) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(number).child("tranzactii");
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float transferuri = 0;
                float restaurant = 0;
                float facturi = 0;
                float transport = 0;
                float chirie = 0;
                float cumparaturiDiverse = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String descriere = dataSnapshot.child("descriere").getValue(String.class);
                    String soldDepozit = dataSnapshot.child("sold").getValue(String.class);
                    if (soldDepozit.contains("-")) {
                        soldDepozit = soldDepozit.substring(1);
                        if (descriere.toLowerCase().contains("transfer")) {
                            transferuri += Float.parseFloat(soldDepozit);
                        } else if (descriere.toLowerCase().contains("chirie")) {
                            chirie += Float.parseFloat(soldDepozit);
                        } else if (descriere.toLowerCase().contains("masa") || descriere.toLowerCase().contains("restaurant") || descriere.toLowerCase().contains("mancare")) {
                            restaurant += Float.parseFloat(soldDepozit);

                        } else if (descriere.toLowerCase().contains("factura")) {
                            facturi += Float.parseFloat(soldDepozit);

                        } else if (descriere.toLowerCase().contains("masina") || descriere.toLowerCase().contains("motorina") || descriere.toLowerCase().contains("benzina") || descriere.toLowerCase().contains("transport") || descriere.toLowerCase().contains("carburant")) {
                            transport += Float.parseFloat(soldDepozit);

                        } else {
                            cumparaturiDiverse += Float.parseFloat(soldDepozit);
                        }

                    }
                    int[] culoriPersonalizate = new int[]{Color.rgb(217, 80, 138),
                            Color.rgb(254, 149, 7),
                            Color.rgb(254, 247, 120),
                            Color.rgb(106, 167, 134),
                            Color.rgb(53, 194, 209),
                            Color.rgb(155, 89, 182)};
                    ArrayList<PieEntry> cheltuieli = new ArrayList<>();

                    cheltuieli.add(new PieEntry(transferuri, "Transferuri"));
                    cheltuieli.add(new PieEntry(restaurant, "Restaurant"));
                    cheltuieli.add(new PieEntry(facturi, "Facturi"));
                    cheltuieli.add(new PieEntry(transport, "Transport"));
                    cheltuieli.add(new PieEntry(chirie, "Chirie"));
                    cheltuieli.add(new PieEntry(cumparaturiDiverse, "Cumparaturi diverse"));


                    PieDataSet pieDataSet = new PieDataSet(cheltuieli, "");
                    pieDataSet.setColors(culoriPersonalizate);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(16f);
                    pieDataSet.setSliceSpace(3f);

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setDrawValues(true);

                    pieChart.setData(pieData);

                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Distributie cheltuieli");
                    pieChart.animateXY(1000, 1000);
                    pieChart.animate();

                    Legend l = pieChart.getLegend();
                    l.setTextSize(10f);
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(false);
                    l.setEnabled(true);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(getContext(), null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }


    public void afisareLunar(String number, String data) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(number).child("tranzactii");
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float transferuri = 0;
                float restaurant = 0;
                float facturi = 0;
                float transport = 0;
                float chirie = 0;
                float cumparaturiDiverse = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String descriere = dataSnapshot.child("descriere").getValue(String.class);
                    String soldDepozit = dataSnapshot.child("sold").getValue(String.class);
                    String dataDepozit = dataSnapshot.child("data").getValue(String.class);

                    if (dataDepozit.contains(data)) {

                        if (soldDepozit.contains("-")) {
                            soldDepozit = soldDepozit.substring(1);
                            if (descriere.toLowerCase().contains("transfer")) {
                                transferuri += Float.parseFloat(soldDepozit);
                            } else if (descriere.toLowerCase().contains("chirie")) {
                                chirie += Float.parseFloat(soldDepozit);
                            } else if (descriere.toLowerCase().contains("masa") || descriere.toLowerCase().contains("restaurant") || descriere.toLowerCase().contains("mancare")) {
                                restaurant += Float.parseFloat(soldDepozit);

                            } else if (descriere.toLowerCase().contains("factura")) {
                                facturi += Float.parseFloat(soldDepozit);

                            } else if (descriere.toLowerCase().contains("masina") || descriere.toLowerCase().contains("motorina") || descriere.toLowerCase().contains("benzina") || descriere.toLowerCase().contains("transport") || descriere.toLowerCase().contains("carburant")) {
                                transport += Float.parseFloat(soldDepozit);

                            } else {
                                cumparaturiDiverse += Float.parseFloat(soldDepozit);
                            }

                        }
                    }
                }
                int[] culoriPersonalizate = new int[]{Color.rgb(217, 80, 138),
                        Color.rgb(254, 149, 7),
                        Color.rgb(254, 247, 120),
                        Color.rgb(106, 167, 134),
                        Color.rgb(53, 194, 209),
                        Color.rgb(155, 89, 182)};
                ArrayList<PieEntry> cheltuieli = new ArrayList<>();

                if (transferuri == 0 && restaurant == 0 && facturi == 0 && transport == 0 && chirie == 0 && cumparaturiDiverse == 0) {

                    Toast.makeText(getActivity(), "Nu exista inregistrari in luna respectiva", Toast.LENGTH_LONG).show();
                    cheltuieli.add(new PieEntry(0, "NICIO TRANZACTIE"));
                } else {
                    cheltuieli.add(new PieEntry(transferuri, "Transferuri"));
                    cheltuieli.add(new PieEntry(restaurant, "Restaurant"));
                    cheltuieli.add(new PieEntry(facturi, "Facturi"));
                    cheltuieli.add(new PieEntry(transport, "Transport"));
                    cheltuieli.add(new PieEntry(chirie, "Chirie"));
                    cheltuieli.add(new PieEntry(cumparaturiDiverse, "Cumparaturi diverse"));
                }


                PieDataSet pieDataSet = new PieDataSet(cheltuieli, "");
                pieDataSet.setColors(culoriPersonalizate);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);
                pieDataSet.setSliceSpace(3f);

                PieData pieData = new PieData(pieDataSet);
                pieData.setDrawValues(true);

                pieChart.setData(pieData);

                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Distributie cheltuieli");
                pieChart.animateXY(1000, 1000);
                pieChart.animate();

                Legend l = pieChart.getLegend();
                l.setTextSize(10f);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public  void predictie(String number){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(number).child("tranzactii");
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float minus1 = 0;
                float minus2 = 0;

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                int lunaTrecuta = calendar.get(Calendar.MONTH) + 1;
                calendar.add(Calendar.MONTH, -1);
                int douaDinUrma = calendar.get(Calendar.MONTH) + 1;
                //Toast.makeText(getActivity(), "DATELE"+lunaTrecuta+" "+ douaDinUrma, Toast.LENGTH_LONG).show();

                String lunaTrecutaNou="";
                String douaDinUrmaNou="";

                if (lunaTrecuta < 10) {
                    lunaTrecutaNou = "0" + Integer.toString(lunaTrecuta)+"/2023";
                } else {
                    lunaTrecutaNou = Integer.toString(lunaTrecuta)+"/2023";
                }

                if (douaDinUrma < 10) {
                    douaDinUrmaNou = "0" + Integer.toString(douaDinUrma)+"/2023";
                } else {
                    douaDinUrmaNou = Integer.toString(douaDinUrma)+"/2023";
                }


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String soldDepozit = dataSnapshot.child("sold").getValue(String.class);
                    String dataDepozit = dataSnapshot.child("data").getValue(String.class);

                    if (dataDepozit.contains(lunaTrecutaNou)) {

                        if (soldDepozit.contains("-")) {
                            soldDepozit = soldDepozit.substring(1);
                                minus1 += Float.parseFloat(soldDepozit);


                        }
                    }else if(dataDepozit.contains(douaDinUrmaNou)){
                        soldDepozit = soldDepozit.substring(1);
                        minus2 += Float.parseFloat(soldDepozit);
                    }
                    else {

                    }
                }

                float media=(minus1+minus2)/2;
                sumaprezisa.setText(media+" lei");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}


