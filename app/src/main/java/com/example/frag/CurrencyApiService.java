package com.example.frag;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyApiService {
    private static final String API_BASE_URL = "https://api.exchangeratesapi.io/v1/latest";
    private static final String API_KEY = "0d7d3c31169b5883b245e8a4d2592869";
    private static final String TAG = CurrencyApiService.class.getSimpleName();

    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        double exchangeRate = 0;

        try {
            URL url = new URL(API_BASE_URL + "? access_key=" + API_KEY + "?base=" + baseCurrency + "&symbols=" + targetCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject ratesObject = jsonObject.getJSONObject("rates");
                exchangeRate = ratesObject.getDouble(targetCurrency);

                reader.close();
            }

            connection.disconnect();
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching exchange rate: " + e.getMessage());
        }

        return exchangeRate;
    }
}


