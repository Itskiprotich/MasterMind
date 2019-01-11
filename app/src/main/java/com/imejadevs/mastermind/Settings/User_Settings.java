package com.imejadevs.mastermind.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.imejadevs.mastermind.R;

public class User_Settings extends AppCompatActivity {
    private EditText A, B;
    private CheckedTextView C;
    String code, gueses;
    int code_number,gueses_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        A = (EditText) findViewById(R.id.aa);
        B = (EditText) findViewById(R.id.bb);
        C = (CheckedTextView) findViewById(R.id.cc);
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                C.toggle();
            }
        });
    }

    public void saveSettings(View view) {
        code = A.getText().toString();
        gueses = B.getText().toString();
        if (code.isEmpty() || gueses.isEmpty()) {
            Toast.makeText(this, "Check all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean check = check_data();
            code_number=Integer.parseInt(code);
            gueses_number=Integer.parseInt(gueses);
            if (code_number<3||code_number>6){
                Toast.makeText(this, "The minimum should be 3 and maximum 6", Toast.LENGTH_SHORT).show();
            }else{
                if (gueses_number<5||gueses_number>15){
                    Toast.makeText(this, "The number should be between 5 and 15", Toast.LENGTH_SHORT).show();
                }else{
                    saveNow(code_number, gueses_number, check);
                }
            }


        }
    }

    private void saveNow(int code, int gueses, boolean check) {
        SharedPreferences sharedPreferences = getSharedPreferences("Game_Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Duplicate", check);
        editor.putInt("Gueses", gueses);
        editor.putInt("Code", code);
        editor.commit();
        Toast.makeText(this, "Settings saved..", Toast.LENGTH_SHORT).show();
    }

    private boolean check_data() {
        if (C.isChecked()) {
            return true;
        } else {
            return false;
        }

    }
}
