package com.org.appcrashtracker;

import android.content.Context;

public class ACT {

    public static final String INTENT_DATA = "data";

    private ACT() {
        throw new IllegalArgumentException("ACT can't be accessed!, call {@link init(Context context, Class<?> name)}");
    }

    public static void init(Context context, Class className) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context, className));
    }
}
