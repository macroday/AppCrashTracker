package com.org.appcrashtracker;

import android.app.Activity;
import android.content.Context;

public class ACT {

	public static void init(Context context,Class<?> name)
	{
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler((Activity) context,name));
	}
	
}
