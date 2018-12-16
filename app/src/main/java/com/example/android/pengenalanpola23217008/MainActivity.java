package com.example.android.pengenalanpola23217008;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Menu and Welcome Message
        setContentView(R.layout.activity_main);

        // Find the View that shows the Tugas 1
        TextView tugas1 = (TextView) findViewById(R.id.tugas1);

        // Set a click listener on that View
        tugas1.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas1Intent = new Intent(MainActivity.this, Tugas1.class);
                startActivity(tugas1Intent);
            }
        });

        // Find the View that shows the Tugas 2
        TextView tugas2 = (TextView) findViewById(R.id.tugas2);

        // Set a click listener on that View
        tugas2.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas2Intent = new Intent(MainActivity.this, Tugas2.class);
                startActivity(tugas2Intent);
            }
        });

        // Find the View that shows the Tugas 2b
        TextView tugas2b = (TextView) findViewById(R.id.tugas2b);

        // Set a click listener on that View
        tugas2b.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas2bIntent = new Intent(MainActivity.this, Tugas2b.class);
                startActivity(tugas2bIntent);
            }
        });

        // Find the View that shows the Tugas 3a
        TextView tugas3a = (TextView) findViewById(R.id.tugas3a);

        // Set a click listener on that View
        tugas3a.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas3aIntent = new Intent(MainActivity.this, Tugas3a.class);
                startActivity(tugas3aIntent);
            }
        });

        // Find the View that shows the Tugas 3b
        TextView tugas3b = (TextView) findViewById(R.id.tugas3b);

        // Set a click listener on that View
        tugas3b.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas3bIntent = new Intent(MainActivity.this, Tugas3b.class);
                startActivity(tugas3bIntent);
            }
        });

        // Find the View that shows the Tugas 4a
        TextView tugas4a = (TextView) findViewById(R.id.tugas4a);

        // Set a click listener on that View
        tugas4a.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas4aIntent = new Intent(MainActivity.this, Tugas4a.class);
                startActivity(tugas4aIntent);
            }
        });

        // Find the View that shows the Tugas 4b
        TextView tugas4b = (TextView) findViewById(R.id.tugas4b);

        // Set a click listener on that View
        tugas4b.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas4bIntent = new Intent(MainActivity.this, Tugas4b.class);
                startActivity(tugas4bIntent);
            }
        });

        // Find the View that shows the Tugas 5
        TextView tugas5 = (TextView) findViewById(R.id.tugas5);

        // Set a click listener on that View
        tugas5.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas5Intent = new Intent(MainActivity.this, Tugas5.class);
                startActivity(tugas5Intent);
            }
        });

        // Find the View that shows the Tugas 6
        TextView tugas6 = (TextView) findViewById(R.id.tugas6);

        // Set a click listener on that View
        tugas6.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas6Intent = new Intent(MainActivity.this, UTS.class);
                startActivity(tugas6Intent);
            }
        });

        // Find the View that shows the Tugas 7
        TextView tugas7 = (TextView) findViewById(R.id.tugas7);

        // Set a click listener on that View
        tugas7.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas7Intent = new Intent(MainActivity.this, Tugas7.class);
                startActivity(tugas7Intent);
            }
        });

        // Find the View that shows the Tugas 8 & 9
        TextView tugas8dan9 = (TextView) findViewById(R.id.tugas8dan9);

        // Set a click listener on that View
        tugas8dan9.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas8dan9Intent = new Intent(MainActivity.this, Tugas8dan9.class);
                startActivity(tugas8dan9Intent);
            }
        });

        // Find the View that shows the Tugas 10
        TextView tugas10 = (TextView) findViewById(R.id.tugas10);

        // Set a click listener on that View
        tugas10.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent tugas10Intent = new Intent(MainActivity.this, UAS.class);
                startActivity(tugas10Intent);
            }
        });
    }
}
