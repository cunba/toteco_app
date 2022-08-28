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

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.domain.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createAuxEstablishment();
    }

    private void createAuxEstablishment() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        List<Establishment> establishments = db.establishmentDao().findAll();
        if (establishments.size() == 0) {
            Establishment establishment = new Establishment("aux", 0, 0, true, 0);
            db.establishmentDao().insert(establishment);
        }
    }

    public void signIn(View view) {
        // We get the values from the texts
        EditText etUsername = findViewById(R.id.main_username);
        EditText etPassword = findViewById(R.id.main_password);
        TextView tvError = findViewById(R.id.main_error);

        // Remove focus from edit texts and hide the keyboard
        etUsername.clearFocus();
        etPassword.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);

        // We convert the values into strings
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // If one or both fields are empty we show a message
        if (username.equals("") || password.equals("")) {
            tvError.setText(R.string.error_field_empty);
        } else {
            // Get the user
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco").allowMainThreadQueries().fallbackToDestructiveMigration().build();
            List<User> user = db.userDao().findByUsernameAndPassword(username, password);

            // If the list is empty means that the user with this username and password doesn't exists
            if (user.size() == 0) {
                tvError.setText(R.string.error_user);
            } else {
                tvError.setText("");

                // If nothing goes wrong we sign in and go to publications activity
                Intent intent = new Intent(this, PublicationsActivity.class);
                startActivity(intent);
            }
        }
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}