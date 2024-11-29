package com.example.assignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText elcInput;
    private SeekBar seekBar;
    private Button calcButton, resetButton;
    private TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        elcInput = findViewById(R.id.elcInput);
        seekBar = findViewById(R.id.seekBar);
        calcButton = findViewById(R.id.calcButton);
        tvOutput = findViewById(R.id.tvOutput);
        resetButton = findViewById(R.id.resetButton);





        elcInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(elcInput, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        View rootLayout = findViewById(R.id.main);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tvOutput.setText(String.format("Rebate: %d%%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        resetButton.setOnClickListener(v -> {

            elcInput.setText("");
            seekBar.setProgress(0);
            tvOutput.setText("");
            Toast.makeText(this, "reset calculator", Toast.LENGTH_SHORT).show();
        });


        calcButton.setOnClickListener(v -> {
            String unitsStr = elcInput.getText().toString();


            if (TextUtils.isEmpty(unitsStr)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid Input")
                        .setMessage("Please enter the number of units used")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            int units = Integer.parseInt(unitsStr);
            int rebate = seekBar.getProgress();


            double totalCharges = calculateBill(units);
            double finalCost = totalCharges - (totalCharges * rebate / 100);


            tvOutput.setText(String.format("Total: RM %.2f (Rebate: %d%%)\n\n Final Cost: RM %.2f", totalCharges, rebate, finalCost));
        });


      ImageView instrButton = findViewById(R.id.instrButton);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Instructions")
                        .setMessage("1. Enter numerical value for electricity unit\n\n" +
                                "2. Slide the seekbar to input percentage of rebate (0% to 5%)\n\n" +
                                "3. Click the \"Calculate\" button to determine total electricity bill charge\n\n" +
                                "4. Click \"Reset\" button to re-enter electricity unit and rebate percentage")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        ImageView aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Profile Page", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MainActivity.this, About.class);


                startActivity(intent);
            }
        });
    }


    private double calculateBill(int units) {
        double total = 0;

        if (units <= 200) {
            total = units * 0.218;
        } else if (units <= 300) {
            total = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            total = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            total = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }

        return total;
    }
}
