package com.org.appcrashtracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class ACT {

	private static Activity activity;
	
	public static void init(Context context,Class<?> name)
	{
		activity=(Activity)context;
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler((Activity) context,name));
	}
	
}
