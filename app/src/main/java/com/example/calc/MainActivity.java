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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private double firstOperand = 0;
    private String pendingOp = "";
    private boolean startNewNumber = true;


    private boolean error = false;

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

        if (startNewNumber || error) {
            display.setText(text.equals(".") ? "0." : text);
            startNewNumber = false;
            error =false;
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

    public void onOperatorClick(View v) {
        if (error) return;
        String op = ((Button) v).getText().toString();

        if (!pendingOp.isEmpty() && !startNewNumber) {
            onEqualsClick(v);
            if (error) return;
        }

        firstOperand = Double.parseDouble(display.getText().toString());
        pendingOp = op;
        startNewNumber = true;
    }

    public void onEqualsClick(View v){
        if (error || pendingOp.isEmpty() ) return;
        double second = Double.parseDouble(display.getText().toString());
        double result;

        switch (pendingOp){   // switch casai, kad skaiciuotu priklausomai nuo aritmetines operacijos zenklo
            case "+": result = firstOperand + second; break;
            case "-": result = firstOperand - second; break;
            case "*": result = firstOperand * second; break;
            case "/":
                if(second == 0){ //tikrinam ar bandoma dalinti is nulio, nes tai yra ILLEGAL, CRIMINAL.
                    display.setText("Dalyba is nulio negalima!!!!");
                    error=true;
                    firstOperand= 0;
                    pendingOp="";
                    startNewNumber=true;
                    return;         //realiai tiesiog atstatom i pradine busena
                }
                result = firstOperand /second;
                break;
            default:
                return;
        }
        firstOperand= result;
        display.setText(new BigDecimal(result).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        pendingOp= "";
        startNewNumber= true;
    }
    public void onSqrtClick(View v) {
        if (error) return;
        double value = Double.parseDouble(display.getText().toString());
        if (value < 0) {
            display.setText("SAKNIYS IS NEIGIAMO SKAICIAUS? NIU NIU NIU");
            error = true;
            firstOperand = 0;
            pendingOp = "";
            startNewNumber = true;
            return;
        }
        display.setText(new BigDecimal(Math.sqrt(value)).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        startNewNumber = true;
    }
    public void onBackspaceClick(View v){  //trinam tik viena, beveik visad veikia, kartais neveikia nzn kodel :(
        if (error){
            onClearClick(v);
            return;
        }
        if(startNewNumber) return;

        String current =display.getText().toString();
        if(current.length() <=1 || (current.length() == 2 && current.startsWith("-"))){  //tiesiog jei yra tik vienas elementas ant ekrano, taip pat tikrinam 2 su -, nes tarkim skaicius "-1" butu traktuojamas kaip length 2, nepaisant to, kad realiai tai veinas skaicius.
            display.setText("0");
            startNewNumber=true;
        }else{
            display.setText(current.substring(0, current.length() - 1 ));
        }
    }

    public void onClearClick(View v){  //tiesiog trinam viska
        display.setText("0");
        firstOperand= 0;
        pendingOp = "";
        startNewNumber = true;
        error=false;
    }

    public void onFlipClick(View v){  //pakeicia skaiciaus zenkla
        if (error) return;

        String current = display.getText().toString();
        if (current.equals("0")) return;

        if(current.startsWith("-")){
            display.setText(current.substring(1));
        } else {
            display.setText("-" +current);
        }

    }
}