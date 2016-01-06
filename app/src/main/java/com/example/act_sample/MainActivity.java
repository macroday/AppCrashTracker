package com.example.act_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Kosh on 05/01/16 11:08 AM
 */
public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.crash).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                throw new NullPointerException("NullPointerException");
            }
        });
    }
}
