package com.tatdep.yabloko;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class YourCallbackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Обработка перенаправления по схеме
        Uri data = getIntent().getData();
        if (data != null) {
            // Ваш код обработки перенаправления
        }

        finish(); // Закрытие активности
    }
}
