package com.org.appcrashtracker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;

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
	private boolean device_orientation = false;
	private boolean screen_layout = false;
	private boolean vm_heap_size = false;
	private boolean allocated_vm_size = false;
	private boolean vm_max_heap_size = false;
	private boolean vm_free_heap_size = false;
	private boolean native_allocated_size = false;
	private boolean battery_percentage = false;
	private boolean battery_charging = false;
	private boolean battery_charging_via= false;
	private boolean sd_card_status= false;
	private boolean internal_memory_size= false;
	private boolean external_memory_size= false;
	private boolean internal_free_space= false;
	private boolean external_free_space= false;
	private boolean package_name= false;
	private boolean device_rooted= false;
	private boolean network_mode= false;
	private boolean country= false;
		
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
			device_orientation=activity.getResources().getBoolean(R.bool.device_orientation);
			screen_layout=activity.getResources().getBoolean(R.bool.screen_layout);
			vm_heap_size = activity.getResources().getBoolean(R.bool.vm_heap_size);
			allocated_vm_size = activity.getResources().getBoolean(R.bool.allocated_vm_size);
			vm_max_heap_size = activity.getResources().getBoolean(R.bool.vm_max_heap_size);
			vm_free_heap_size = activity.getResources().getBoolean(R.bool.vm_free_heap_size);
			native_allocated_size = activity.getResources().getBoolean(R.bool.native_allocated_size);
			battery_percentage = activity.getResources().getBoolean(R.bool.battery_percentage);
			battery_charging = activity.getResources().getBoolean(R.bool.battery_charging);
			battery_charging_via = activity.getResources().getBoolean(R.bool.battery_charging_via);
			sd_card_status = activity.getResources().getBoolean(R.bool.sd_card_status);
			internal_memory_size = activity.getResources().getBoolean(R.bool.internal_memory_size);
			external_memory_size = activity.getResources().getBoolean(R.bool.external_memory_size);
			internal_free_space = activity.getResources().getBoolean(R.bool.internal_free_space);
			external_free_space = activity.getResources().getBoolean(R.bool.external_free_space);
			package_name = activity.getResources().getBoolean(R.bool.package_name);
			device_rooted = activity.getResources().getBoolean(R.bool.device_rooted);
			network_mode = activity.getResources().getBoolean(R.bool.network_mode);
			country = activity.getResources().getBoolean(R.bool.country);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
	    jObjectData = new JSONObject();
		    try {
		    	if(package_name)
		    		jObjectData.put("Package_Name", activity.getPackageName());
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
		    	if(device_orientation)
		    		jObjectData.put("Device_Orientation", getScreenOrientation(activity));
		    	if(screen_layout)
		    		jObjectData.put("Screen_Layout", getScreenLayout(activity));
		    	if(vm_heap_size)
		    		jObjectData.put("VM_Heap_Size", ConvertSize(Runtime.getRuntime().totalMemory()));
		    	if(allocated_vm_size)
		    		jObjectData.put("Allocated_VM_Size", ConvertSize(Runtime.getRuntime().freeMemory()));
		    	if(vm_max_heap_size)
		    		jObjectData.put("VM_Max_Heap_Size", ConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
		    	if(vm_free_heap_size)
		    		jObjectData.put("VM_free_Heap_Size", ConvertSize(Runtime.getRuntime().maxMemory()));
		    	if(native_allocated_size)
		    		jObjectData.put("Native_Allocated_Size", ConvertSize(Debug.getNativeHeapAllocatedSize()));
		    	if(battery_percentage)
		    		jObjectData.put("Battery_Percentage", activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
		    	if(battery_charging)
		    		jObjectData.put("Battery_Charging_Status", getBatteryStatus(activity));
		    	if(battery_charging_via)
		    		jObjectData.put("Battery_Charging_Via", getBatteryChargingMode(activity));
		    	if(sd_card_status)
		    		jObjectData.put("SDCard_Status", getSDCardStatus(activity));
		    	if(internal_memory_size)
		    		jObjectData.put("Internal_Memory_Size",  getTotalInternalMemorySize());
		    	if(external_memory_size)
		    		jObjectData.put("External_Memory_Size", getTotalExternalMemorySize());
		    	if(internal_free_space)
		    		jObjectData.put("Internal_Free_Space",  getAvailableInternalMemorySize());
		    	if(external_free_space)
		    		jObjectData.put("External_Free_Space",  getAvailableExternalMemorySize());
		    	if(device_rooted)
		    		jObjectData.put("Device_IsRooted", isRooted());
		    	if(network_mode)
		    		jObjectData.put("Network_Mode", getNetworkMode(activity));
		    	if(country)
		    		jObjectData.put("Country", new Locale("",activity.getResources().getConfiguration().locale.getCountry()).getDisplayCountry());
		    } catch (JSONException e) {
				Log.e(""+activity.getPackageName(), "JSON Exception");
			}
		    Log.i("", ">>>>>>>>>>>>>>>>>>"+((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
		    
		    
		if(activity.getPackageManager().checkPermission(Manifest.permission.INTERNET, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED)
		{
			if(activity.getPackageManager().checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED)
			{
				if(isConnectingToInternet(activity))
				{
					 
					if (class_name || message || localized_message || causes
							|| stack_trace || brand_name || device_name
							|| model_number || product_name || sdk_version
							|| release || incremental || height || width
							|| app_version || tablet || device_orientation
							|| screen_layout || vm_heap_size
							|| allocated_vm_size || vm_max_heap_size
							|| vm_free_heap_size || native_allocated_size
							|| battery_percentage || battery_charging
							|| battery_charging_via || sd_card_status
							|| internal_memory_size || external_memory_size
							|| internal_free_space || external_free_space
							|| package_name || device_rooted || network_mode || country) {
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
						}.execute();
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
	
	@SuppressWarnings("deprecation")
	public String getScreenOrientation(Activity act)
	{
	    Display getOrient = act.getWindowManager().getDefaultDisplay();
	    if(getOrient.getWidth()==getOrient.getHeight()){
	    	return "Square";
	    }
	    else
	    { 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	        	return "Portrait";
	        }
	        else
	        { 
	        	return "Landscape";
	        }
	    }
	}
	public String getScreenLayout(Activity act)
	{
		int screenSize = act.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			return "Large Screen";
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			return  "Normal Screen";
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			return  "Small Screen";
		default:
			return "Screen size is neither large, normal or small";
		}
	}
	
	public String ConvertSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public String getBatteryStatus(Activity act)
	{
		int status=act.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_STATUS,-1);
 		if(status==BatteryManager.BATTERY_STATUS_CHARGING)
 			return "Charging";
 		else if (status==BatteryManager.BATTERY_STATUS_DISCHARGING)
 			return "Discharging";
 		else if(status==BatteryManager.BATTERY_STATUS_FULL)
 			return "Full";
		return "NULL";
	}
	
	public String getBatteryChargingMode(Activity act)
	{
        int plugged = act.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if(plugged == BatteryManager.BATTERY_PLUGGED_AC)
        	return "AC";
        else if(plugged == BatteryManager.BATTERY_PLUGGED_USB)
        	return "USB";
        else if(plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS)
        	return "WireLess";
        return "NULL";
	}
	
	public String getSDCardStatus(Activity act)
	{
		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSDPresent)
			return "Mounted";
		else 
			return "Not mounted";
	}

    @SuppressWarnings("deprecation")
    public String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
        return ConvertSize(availableBlocks * blockSize);
    }

    @SuppressWarnings("deprecation")
    public String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return ConvertSize(totalBlocks * blockSize);
    }

    @SuppressWarnings("deprecation")
    public String getAvailableExternalMemorySize() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return ConvertSize(availableBlocks * blockSize);
        } else {
            return "SDCard not present";
        }
    }

    @SuppressWarnings("deprecation")
    public String getTotalExternalMemorySize() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return ConvertSize(totalBlocks * blockSize);
        } else {
            return "SDCard not present";
        }
    }
    public boolean isRooted() {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if ( new File( where + "su" ).exists() ) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
    @SuppressWarnings("deprecation")
	public String getNetworkMode(Activity act) {
        ConnectivityManager connMgr = (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            return "Wifi";
        } 
        else if (mobile.isConnectedOrConnecting()) {
        	
        	 if(Build.VERSION.SDK_INT>Build.VERSION_CODES.ECLAIR_MR1)
        	 {	 
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_1xRTT)
	                 return "1xRTT";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_CDMA)
	                 return "CDMA";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_EDGE)
	                 return "EDGE";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_EVDO_0)
	                 return "EVDO 0";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_EVDO_A)
	                 return "EVDO A";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_GPRS)
	                 return "GPRS";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_HSDPA)
	                 return "HSDPA";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_HSPA)
	                 return "HSPA";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_HSUPA)
	                 return "HSUPA";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_UMTS)
	                 return "UMTS";
	             if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB)
		             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_EHRPD) 
		                 return "EHRPD";
	             if(Build.VERSION.SDK_INT>Build.VERSION_CODES.FROYO)
		             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_IDEN)
		                 return "IDEN"; 
	             if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1)
		             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_EVDO_B)
		                 return "EVDO B";
	             if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB)
		             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_LTE)
		                 return "LTE";
	             if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB_MR2)
		             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_HSPAP)
		                 return "HSPAP";
	             if(mobile.getSubtype() ==  TelephonyManager.NETWORK_TYPE_UNKNOWN)
	            	 return "UNKNOWN";
        	 }
        } 
        else 
        {
        	return "No Network";
        }
		return "NULL";
    }
    public boolean isSimSupport(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }
}
