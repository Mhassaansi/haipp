package com.appsnado.haipp.Applocakpacakges.activities.main;

import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.appsnado.haipp.Applocakpacakges.Applocakpacakges.R;


public class Child_act extends AppCompatActivity {


    Button parent,child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_act_lock);

        parent = (Button)findViewById(R.id.button);
        child = (Button)findViewById(R.id.button6);


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Child_act.this, MainActivity.class);
                startActivity(intent);


            }
        });


    }
}