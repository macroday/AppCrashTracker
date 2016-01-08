package com.example.act_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.org.appcrashtracker.ACT;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ACT.init(MainActivity.this,Activity2.class);
		setContentView(R.layout.activity_main);
		
		Log.i("", ""+Integer.parseInt("asdf"));
	}
	
}
