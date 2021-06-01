package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLoginRegistrationActivity extends AppCompatActivity {

    private Button mLogin, mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);

        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);

        mLogin.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseLoginRegistrationActivity.this, LoginActivity.class); // ChooseLoginRegistrationActivity에서 LoginActivity를 호출
            startActivity(intent);
            finish(); //액티비티 종료
            return;
        });

        mRegister.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseLoginRegistrationActivity.this, RegistrationActivity.class); // ChooseLoginRegistrationActivity에서 RegistrationActivity를 호출
            startActivity(intent);
            finish(); //액티비티 종료
            return;
        });

    }
}