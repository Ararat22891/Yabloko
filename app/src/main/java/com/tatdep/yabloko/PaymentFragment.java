package com.tatdep.yabloko;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentFragment extends Fragment {

    private static final String API_KEY = "DEM20QP-J6PM957-HAQDG6J-938HZ1Z";
    private static final String API_URL = "https://api.nowpayments.io/v1/invoice";

    private EditText amountEditText;
    private Button payButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        amountEditText = v.findViewById(R.id.amountEditText);
        payButton = v.findViewById(R.id.payButton);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = Double.parseDouble(amountEditText.getText().toString());
                String currency = "USD"; // валюта оплаты
                String cryptoCurrency = "BTC"; // тип криптовалюты для оплаты
                String orderId = UUID.randomUUID().toString(); // уникальный идентификатор заказа

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("price_amount", amount);
                    requestBody.put("price_currency", currency);
                    requestBody.put("pay_currency", cryptoCurrency);
                    requestBody.put("order_id", orderId);
                    requestBody.put("ipn_callback_url","https://nowpayments.io");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(mediaType, requestBody.toString());

                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("x-api-key", API_KEY)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBodyString = response.body().string();
                        try {
                            JSONObject responseBody = new JSONObject(responseBodyString);
                            String paymentUrl = responseBody.getString("invoice_url");

                            // Откройте полученную ссылку на страницу оплаты
                            // либо передайте ссылку пользователю, чтобы он мог совершить оплату самостоятельно
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(paymentUrl));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return v;
    }
}