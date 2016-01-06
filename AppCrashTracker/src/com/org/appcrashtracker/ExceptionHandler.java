package com.org.appcrashtracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ExceptionHandler implements
		java.lang.Thread.UncaughtExceptionHandler {
	private final Activity activity;
	Intent intent ;
	JSONObject jObjectData;
	String ActivityName;
	Class<?> name;
	String Post_Url;
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
		
	public ExceptionHandler(Activity activity,Class<?> name) {
		this.activity = activity;
		this.name=name;
		ActivityName=activity.getClass().getSimpleName();

		this.Post_Url=activity.getResources().getString(R.string.url);
		if(Post_Url.equals("default_url"))
			Log.e(""+activity.getPackageName(), "Post url not specified");
		else
		{
			class_name = activity.getResources().getBoolean(R.bool.class_name);
			message = activity.getResources().getBoolean(R.bool.message);
			localized_message = activity.getResources().getBoolean(R.bool.localized_message);
			causes = activity.getResources().getBoolean(R.bool.causes);
			stack_trace = activity.getResources().getBoolean(R.bool.stack_trace);
			brand_name = activity.getResources().getBoolean(R.bool.brand_name);
			device_name = activity.getResources().getBoolean(R.bool.device_name);
			model_number = activity.getResources().getBoolean(R.bool.model_number);
			product_name = activity.getResources().getBoolean(R.bool.product_name);
			sdk_version = activity.getResources().getBoolean(R.bool.sdk_version);
			release = activity.getResources().getBoolean(R.bool.release);
			incremental = activity.getResources().getBoolean(R.bool.incremental);
			height = activity.getResources().getBoolean(R.bool.height);
			width = activity.getResources().getBoolean(R.bool.width);
			app_version = activity.getResources().getBoolean(R.bool.app_version);
			tablet = activity.getResources().getBoolean(R.bool.tablet);
			
		}
	}
	
	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
	    jObjectData = new JSONObject();
		    try {
		    	if(class_name)
		    		jObjectData.put("Class", ActivityName);
		    	if(message)
		    		jObjectData.put("Message", exception.getMessage());
		    	if(localized_message)
		    		jObjectData.put("Localized_Message", exception.getLocalizedMessage());
		    	if(causes)
		    		jObjectData.put("Cause", exception.getCause());
		    	if(stack_trace)
		    		jObjectData.put("Stack_Trace", stackTrace.toString());
		    	if(brand_name)
		    		jObjectData.put("Brand", Build.BRAND);
		    	if(device_name)
		    		jObjectData.put("Device", Build.DEVICE);
		    	if(model_number)
		    		jObjectData.put("Model", Build.MODEL);
		    	if(product_name)
		    		jObjectData.put("Product", Build.PRODUCT);
		    	if(sdk_version)
		    		jObjectData.put("SDK", Build.VERSION.SDK);
		    	if(release)
		    		jObjectData.put("Release",Build.VERSION.RELEASE);
		    	if(incremental)
		    		jObjectData.put("Incremental", Build.VERSION.INCREMENTAL);
		    	if(height)
		    		jObjectData.put("Height", activity.getResources().getDisplayMetrics().heightPixels);
		    	if(width)
		    		jObjectData.put("Width", activity.getResources().getDisplayMetrics().widthPixels);
		    	if(app_version)
		    		jObjectData.put("App_Version", getAppVersion(activity));
		    	if(tablet)
		    		jObjectData.put("Tablet", isTablet(activity));
		    } catch (JSONException e) {
				Log.e(""+activity.getPackageName(), "JSON Exception");
			}
		    
		if(activity.getPackageManager().checkPermission(Manifest.permission.INTERNET, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED)
		{
			if(activity.getPackageManager().checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED)
			{
				if(isConnectingToInternet(activity))
				{
					if(class_name || message || localized_message || causes || stack_trace || brand_name || device_name || model_number || product_name || sdk_version || release || incremental || height || width || app_version || tablet)
					{
						new AsyncTask<Void,Void,Void>() {
	
							@Override
							protected Void doInBackground(Void... arg0) {
								try{
							        URL url = null;
									try {
										url = new URL(Post_Url);
									} catch (MalformedURLException e1) {
										Log.e(""+activity.getPackageName(), "MalformedURLExcpetion");
									}
									HttpURLConnection conn = null;
									try {
										conn = (HttpURLConnection) url.openConnection();
									} catch (IOException e1) {
										Log.e(""+activity.getPackageName(), "IOException");
									}
									try {
										conn.setRequestMethod("POST");
									} catch (ProtocolException e1) {
										Log.e(""+activity.getPackageName(), "ProtocolException");
									}
									conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
									conn.setDoInput(true);
									conn.setDoOutput(true);
							        List<PostValuesPair> params1 = new ArrayList<PostValuesPair>();
							        params1.add(new PostValuesPair("error_report", jObjectData.toString()));
							        try{
								        OutputStream os = conn.getOutputStream();
								        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
								        writer.write(getQuery(params1));
								        writer.flush();
								        writer.close();
								        os.close();
							        }
							        catch(Exception ee)
							        {
										Log.e(""+activity.getPackageName(), "Buffer Write Exception");
							        }
						            try {
										conn.connect();
									} catch (IOException e1) {
										Log.e(""+activity.getPackageName(), "IOException");
									}
							    } catch (Exception e) {
							        Log.e(""+activity.getPackageName(), "Exception Occurred");
							    }
							
								return null;
							}
						};
					}
					else
						Log.e(""+activity.getPackageName(), "Not configured. Set configuration in string.xml");
				}
				else
					Log.e(""+activity.getPackageName(), "Network not found");
			}
			else
				Log.e(""+activity.getPackageName(), "Need to add Access newtork state permission");
		}
		else
			Log.e(""+activity.getPackageName(), "Need to add internet permission");

		intent = new Intent(activity, name);
		activity.startActivity(intent);

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
		
	}
	public String getAppVersion(Context con)
	{
		PackageManager manager = con.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(con.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e(""+activity.getPackageName(), "Name not found Exception");
		}
		return info.versionName;
	}
	
	public boolean isTablet(Context con) {
		boolean xlarge = ((con.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((con.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return xlarge || large;
	}
	
	public String getQuery(List<PostValuesPair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (PostValuesPair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}
	
	@SuppressWarnings("deprecation")
	public boolean isConnectingToInternet(Activity act){
		boolean isthere = false;
		TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE); 
		if (tm.getSimState() != TelephonyManager.SIM_STATE_UNKNOWN){
			ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
			if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
				isthere = true;
		} else {
			ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
			if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
				isthere = true;
		}
		return isthere;
	}
}