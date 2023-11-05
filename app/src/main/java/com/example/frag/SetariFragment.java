package com.example.frag;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;


public class SetariFragment extends Fragment {

    TextView schimbValutar, deconectare, datePersonale;
    Dialog veziSchimb, veziDatePersonale;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String number = getArguments().getString("number");
        View view = inflater.inflate(R.layout.fragment_setari, container, false);

        schimbValutar=view.findViewById(R.id.textView45);
        deconectare=view.findViewById(R.id.textView47);
        datePersonale=view.findViewById(R.id.textView46);
        veziSchimb=new Dialog(getContext());
        veziDatePersonale=new Dialog(getContext());
        schimbValutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veziSchimb.setContentView(R.layout.schimb_valutar);
                veziSchimb.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                veziSchimb.show();

                View dialogView = veziSchimb.findViewById(android.R.id.content);
                TextView eur, usd, lira, calculeaza,sumafin;
                EditText sumainitiala;
                RadioGroup radioGroup;
                eur=dialogView.findViewById(R.id.textView44);
                usd=dialogView.findViewById(R.id.textView48);
                lira=dialogView.findViewById(R.id.textView49);
                calculeaza=dialogView.findViewById(R.id.textView58);
                sumafin=dialogView.findViewById(R.id.textView57);
                sumainitiala=dialogView.findViewById(R.id.editTextTextPersonName8);
                radioGroup=dialogView.findViewById(R.id.radioGroup);




                calculeaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String baseCurrency = "RON";
                        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();//selectare radio button
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                        String selectedValue = selectedRadioButton.getText().toString();// preia moneda
                        Double si=Double.parseDouble(sumainitiala.getText().toString());// transforma suma introdusa in double
                        if(selectedValue.equals("EUR")){
                            String targetCurrency = "EUR";
                            new ExchangeRateTask().execute(baseCurrency, targetCurrency, String.valueOf(si));
                            sumafin.setText(Double.toString(si/Double.parseDouble(eur.getText().toString())));

                            //double exchangeRate = CurrencyApiService.getExchangeRate(baseCurrency, targetCurrency);
                            //sumafin.setText(Double.toString(si* exchangeRate));
                        }else if (selectedValue.equals("USD")){
                            String targetCurrency = "USD";
                            new ExchangeRateTask().execute(baseCurrency, targetCurrency, String.valueOf(si));
                            sumafin.setText(Double.toString(si/Double.parseDouble(usd.getText().toString())));
                            /*double exchangeRate = CurrencyApiService.getExchangeRate(baseCurrency, targetCurrency);
                            sumafin.setText(Double.toString(si* exchangeRate));*/
                        }else {
                            String targetCurrency = "GPB";
                            new ExchangeRateTask().execute(baseCurrency, targetCurrency, String.valueOf(si));
                            sumafin.setText(Double.toString(si/Double.parseDouble(lira.getText().toString())));
                            /*double exchangeRate = CurrencyApiService.getExchangeRate(baseCurrency, targetCurrency);
                            sumafin.setText(Double.toString(si* exchangeRate));*/
                        }

                    }
                });

            }
        });

        deconectare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Autentificareee.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        datePersonale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                veziDatePersonale.setContentView(R.layout.date_personale);
                veziDatePersonale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                veziDatePersonale.show();
                View dialogView3 = veziDatePersonale.findViewById(android.R.id.content);
                Button showmap;
                showmap=dialogView3.findViewById(R.id.button5);
                showmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        startActivityForResult(intent, 123);

                    }
                });

            }
        });



        return view;
    }

    private class ExchangeRateTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            String baseCurrency = params[0];
            String targetCurrency = params[1];
            double amount = Double.parseDouble(params[2]);
            double exchangeRate = CurrencyApiService.getExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = amount * exchangeRate;

            return convertedAmount;
        }

        @Override
        protected void onPostExecute(Double result) {
            super.onPostExecute(result);
            TextView sumafin = veziSchimb.findViewById(R.id.textView57);
            //Toast.makeText(getActivity(), "Rata de schimb este: " + String.valueOf(result), Toast.LENGTH_SHORT).show();
        }
    }

}