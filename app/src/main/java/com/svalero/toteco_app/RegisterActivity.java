package com.svalero.toteco_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.User;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        // We get the values from the text fields
        EditText etUsername = findViewById(R.id.register_username);
        EditText etName = findViewById(R.id.register_name);
        EditText etSurname = findViewById(R.id.register_surname);
        EditText etBirthday = findViewById(R.id.register_birthday);
        EditText etPassword = findViewById(R.id.register_password);
        EditText etConfirmPassword = findViewById(R.id.register_confirm_password);
        TextView tvError = findViewById(R.id.register_error);

        // Clear focuses and hide keyboard
        etUsername.clearFocus();
        etName.clearFocus();
        etSurname.clearFocus();
        etBirthday.clearFocus();
        etPassword.clearFocus();
        etConfirmPassword.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etConfirmPassword.getWindowToken(), 0);

        // We convert the values into strings
        String username = etUsername.getText().toString();
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String birthday = etBirthday.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // If there is any field empty we show the error in the text view error
        // If the passwords are not the same we show the error in the text view error, clean the texts
        // and put the focus on the first password text field
        if (username.equals("") ||
                name.equals("") ||
                surname.equals("") ||
                birthday.equals("") ||
                password.equals("") ||
                confirmPassword.equals("")) {

            tvError.setText(R.string.error_field_empty);
        } else if (!password.equals(confirmPassword)) {
            tvError.setText(R.string.error_password);
            etPassword.setText("");
            etConfirmPassword.setText("");
            etPassword.requestFocus();
        } else {
            tvError.setText("");

            // If there is not errors we create the user
            User newUser = new User(
                username,
                name,
                surname,
                birthday,
                password
            );

            // We insert the user into the local database and create a popup
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            db.userDao().insert(newUser);
            Toast.makeText(this, getString(R.string.user_create, username), Toast.LENGTH_SHORT).show();

            // We clear all the texts
            etUsername.setText("");
            etName.setText("");
            etSurname.setText("");
            etBirthday.setText("");
            etPassword.setText("");
            etConfirmPassword.setText("");

            // Move to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}