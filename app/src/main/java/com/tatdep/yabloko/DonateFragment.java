package com.tatdep.yabloko;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import okhttp3.*;

public class DonateFragment extends Fragment {
    private static final String DONATION_ALERTS_API_URL = "https://www.donationalerts.com/api/v1/alerts/donations";

    private EditText amountEditText;
    private Button donateButton;

    public DonateFragment() {
        // Пустой конструктор обязателен
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donat, container, false);

        amountEditText = view.findViewById(R.id.donationAmountEditText);
        donateButton = view.findViewById(R.id.donateButton);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = amountEditText.getText().toString();

                if (amountString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountString);

                makeDonation("5OsJqYGW4JgT64UFGqrQJVQWWrPvIkU8JjKdo4rF", amount);
            }
        });

        return view;
    }

    private void makeDonation(String accessToken, double amount) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"amount\": " + amount + "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(DONATION_ALERTS_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обработка ошибки при выполнении запроса
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failed to make donation", Toast.LENGTH_SHORT).show();
                        Log.e("DonateFragment", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Обработка ответа от сервера
                if (response.isSuccessful()) {
                    // Запрос успешно выполнен
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Donation made successfully", Toast.LENGTH_SHORT).show();
                            // Очистка поля ввода
                            amountEditText.setText("");
                        }
                    });
                } else {
                    // Ошибка при выполнении запроса
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Failed to make donation", Toast.LENGTH_SHORT).show();
                            Log.e("DonateFragment", response.message().toString());
                        }
                    });
                }
                response.close();
            }
        });
    }
}
