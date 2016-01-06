package com.org.appcrashtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;
    private String activityName;
    private Class<?> name;
    private String postUrl;
    private boolean class_name = false;
    private boolean message = false;
    private boolean localized_message = false;
    private boolean causes = false;
    private boolean stack_trace = false;
    private boolean brand_name = false;
    private boolean device_name = false;
    private boolean model_number = false;
    private boolean product_name = false;
    private boolean sdk_version = false;
    private boolean release = false;
    private boolean incremental = false;
    private boolean height = false;
    private boolean width = false;
    private boolean app_version = false;
    private boolean tablet = false;

    public ExceptionHandler(Context context, Class<?> name) {
        this.context = context;
        this.name = name;
        this.activityName = context.getClass().getSimpleName();
        initVariables(context.getResources());
    }

    private void initVariables(Resources res) {
        this.postUrl = res.getString(R.string.url);
        class_name = res.getBoolean(R.bool.class_name);
        message = res.getBoolean(R.bool.message);
        localized_message = res.getBoolean(R.bool.localized_message);
        causes = res.getBoolean(R.bool.causes);
        stack_trace = res.getBoolean(R.bool.stack_trace);
        brand_name = res.getBoolean(R.bool.brand_name);
        device_name = res.getBoolean(R.bool.device_name);
        model_number = res.getBoolean(R.bool.model_number);
        product_name = res.getBoolean(R.bool.product_name);
        sdk_version = res.getBoolean(R.bool.sdk_version);
        release = res.getBoolean(R.bool.release);
        incremental = res.getBoolean(R.bool.incremental);
        height = res.getBoolean(R.bool.height);
        width = res.getBoolean(R.bool.width);
        app_version = res.getBoolean(R.bool.app_version);
        tablet = res.getBoolean(R.bool.tablet);
    }

    @Override public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        final JSONObject jObjectData = initJsonData(exception, sw, context.getResources());
        if (hasInternetPermission(context)) {
            if (hasNetworkStatePermission(context)) {
                if (isConnectingToInternet(context)) {
                    if (class_name || message || localized_message || causes || stack_trace || brand_name || device_name || model_number ||
                            product_name || sdk_version || release || incremental || height || width || app_version || tablet) {
                        new UploadData(postUrl, jObjectData.toString()).execute();
                    } else
                        logE("Not configured. Set configuration in string.xml");
                } else
                    logE("Network not found");
            } else
                logE("Need to add Access network state permission");
        } else
            logE("Need to add internet permission");

        Intent intent = new Intent(context, name);
        intent.putExtra(ACT.INTENT_DATA, jObjectData.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private JSONObject initJsonData(Throwable exception, StringWriter stackTrace, Resources res) {
        final JSONObject jObjectData = new JSONObject();
        try {
            if (class_name)
                jObjectData.put("Class", activityName);
            if (message)
                jObjectData.put("Message", exception.getMessage());
            if (localized_message)
                jObjectData.put("Localized_Message", exception.getLocalizedMessage());
            if (causes)
                jObjectData.put("Cause", exception.getCause());
            if (stack_trace)
                jObjectData.put("Stack_Trace", stackTrace.toString());
            if (brand_name)
                jObjectData.put("Brand", Build.BRAND);
            if (device_name)
                jObjectData.put("Device", Build.DEVICE);
            if (model_number)
                jObjectData.put("Model", Build.MODEL);
            if (product_name)
                jObjectData.put("Product", Build.PRODUCT);
            if (sdk_version)
                jObjectData.put("SDK", Build.VERSION.SDK_INT);
            if (release)
                jObjectData.put("Release", Build.VERSION.RELEASE);
            if (incremental)
                jObjectData.put("Incremental", Build.VERSION.INCREMENTAL);
            if (height)
                jObjectData.put("Height", res.getDisplayMetrics().heightPixels);
            if (width)
                jObjectData.put("Width", res.getDisplayMetrics().widthPixels);
            if (app_version)
                jObjectData.put("App_Version", getAppVersion(context));
            if (tablet)
                jObjectData.put("Tablet", isTablet(context));
        } catch (JSONException e) {
            logE("JSON Exception");
        }
        return jObjectData;
    }

    private String getAppVersion(Context con) {
        PackageManager manager = con.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(con.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            logE("Name not found Exception");
        }
        return info != null ? info.versionName : null;
    }

    private boolean isTablet(Context con) {
        boolean xlarge = ((con.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((con.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration
                .SCREENLAYOUT_SIZE_LARGE);
        return xlarge || large;
    }

    private String getQuery(List<PostValuesPair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (PostValuesPair pair : params) {
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private boolean isConnectingToInternet(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network networks = cm.getActiveNetwork();
            NetworkInfo netInfo = cm.getNetworkInfo(networks);
            haveConnectedWifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            haveConnectedMobile = netInfo.getType() == ConnectivityManager.TYPE_MOBILE && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            return haveConnectedWifi || haveConnectedMobile;
        } else {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) haveConnectedWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) haveConnectedMobile = true;
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
    }

    private boolean hasInternetPermission(Context context) {
        return context.getPackageManager()
                .checkPermission(Manifest.permission.INTERNET,
                                 context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasNetworkStatePermission(Context context) {
        return context.getPackageManager()
                .checkPermission(Manifest.permission.ACCESS_NETWORK_STATE,
                                 context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    private class UploadData extends AsyncTask<Void, Void, Void> {
        private String pUrl;
        private String data;

        private UploadData(String postUrl, String jsonData) {
            this.pUrl = postUrl;
            this.data = jsonData;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(pUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                List<PostValuesPair> params1 = new ArrayList<>();
                params1.add(new PostValuesPair("error_report", data));
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params1));
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void logE(String msg) {
        Log.e("" + context.getPackageName(), msg);
    }
}