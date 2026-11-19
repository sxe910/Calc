package com.example.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private double firstOperand = 0;
    private String pendingOp = "";
    private boolean startNewNumber = true;
    private double memory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.resultDisplay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void onDigitClick(View v) {
        String text = ((Button) v).getText().toString();

        if (startNewNumber) {
            display.setText(text.equals(".") ? "0." : text);
            startNewNumber = false;
        } else {
            String current = display.getText().toString();
            if (text.equals(".") && current.contains(".")) return;
            if (current.equals("0") && !text.equals(".")) {
                display.setText(text);
            } else {
                display.setText(current + text);
            }
        }
    }
}