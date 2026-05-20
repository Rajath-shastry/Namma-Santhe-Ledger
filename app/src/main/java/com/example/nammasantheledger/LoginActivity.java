package com.example.nammasantheledger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,
            etPassword;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etUsername =
                findViewById(R.id.etUsername);

        etPassword =
                findViewById(R.id.etPassword);

        btnLogin =
                findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            String username =
                    etUsername.getText()
                            .toString()
                            .trim();

            String password =
                    etPassword.getText()
                            .toString()
                            .trim();

            if (username.equals("admin")
                    && password.equals("1234")) {

                Toast.makeText(
                        this,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent =
                        new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        );

                startActivity(intent);

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Invalid Username or Password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}