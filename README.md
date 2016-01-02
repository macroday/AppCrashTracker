# AppCrashTracker
Its a kind of toolkit to track the exception arising in the application and it will generate a json and can upload in your server using your own post url.

No need to worry and think to add more line of code. Simple is better. So a single invoke is enough to make better app.

#Input

```java

ACT.init(this,MainActivity.class);

```

#Output
```jsonobject
{

"Brand" : Google Nexus,

"Model" : Nexus 5,

"Product" : Nexus 5,

"Device" : 5x,

"Message" : Unable to start activity ComponentInfo{com.example.act_sample\/com.example.act_sample.MainActivity}: java.lang.NumberFormatException: Invalid int: \"asdf\",

"Incremental" : eng.terry.1365412624,

"Height" : 854,

"SDK" : 16,

"Release" : 4.1.1,

"Localized_Message" : Unable to start activity ComponentInfo{com.example.act_sample\/com.example.act_sample.MainActivity}: java.lang.NumberFormatException: Invalid int: \"asdf\",

"Tablet" : false,

"Class" : MainActivity,

"App_Version" : 1.0,

"Width" : 480,

"Stack_Trace" : java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.act_sample\/com.example.act_sample.MainActivity}: 
java.lang.NumberFormatException: Invalid int: \"asdf\"
android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2184)
android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2211)
android.app.ActivityThread.access$600(ActivityThread.java:149)
android.app.ActivityThread$H.handleMessage(ActivityThread.java:1300)
android.os.Handler.dispatchMessage(Handler.java:99)
android.os.Looper.loop(Looper.java:153)
android.app.ActivityThread.main(ActivityThread.java:5086)
java.lang.reflect.Method.invokeNative(Native Method)
java.lang.reflect.Method.invoke(Method.java:511)
com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:821)
com.android.internal.os.ZygoteInit.main(ZygoteInit.java:584)
dalvik.system.NativeStart.main(Native Method)
Caused by: java.lang.NumberFormatException: Invalid int: \"asdf\"
java.lang.Integer.invalidInt(Integer.java:138)
java.lang.Integer.parse(Integer.java:375)
java.lang.Integer.parseInt(Integer.java:366)
java.lang.Integer.parseInt(Integer.java:332)
com.example.act_sample.MainActivity.onCreate(MainActivity.java:17)
android.app.Activity.performCreate(Activity.java:5020)
android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1080)
android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2148)
... 11 more,

"Cause" : java.lang.NumberFormatException: Invalid int: \"asdf\"
}
```

#Requirements

<ul>
<li>Init from onCreate Method with context and default class<li>
<li>Add Interent Permission<li>
<li>Add Access Network State Permission<li>
<li>Add Configuration in String.xml . Please refer <a href="https://github.com/macroday/AppCrashTracker/blob/master/README.md#configuration">Configuration</a> link</li>
</ul>

#Configuration
Its nothing but, you can customize the data come out from the result. For example, if you want to see the sdk version of the device means, you just add a bool in to string.xml to true. Enough! 
The bool must the same name. Those are following

```xml
<string name="url">post_url</string>
<bool name="class_name">ture</bool>
<bool name="message">true</bool>
<bool name="localized_message">true</bool>
<bool name="causes">true</bool>
<bool name="stack_trace">true</bool>
<bool name="brand_name">false</bool>
<bool name="device_name">false</bool>
<bool name="model_number">false</bool>
<bool name="product_name">false</bool>
<bool name="sdk_version">false</bool>
<bool name="release">false</bool>
<bool name="incremental">false</bool>
<bool name="height">false</bool>
<bool name="width">false</bool>
<bool name="app_version">false</bool>
<bool name="tablet">false</bool>

```



#API Level
2.2 to Latest

Please give me a feedback! Thanks!!
