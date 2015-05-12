<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.fok.speedfix"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk
            android:minSdkVersion="8"
            android:targetSdkVersion="19"/>

    <permission
            android:name="com.google.maps.android.utils.permission.MAPS_RECEIVE"
            android:protectionLevel="signature"/>
    <uses-permission android:name="com.google.maps.android.utils.permission.MAPS_RECEIVE"/>

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>

    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

    <uses-feature	
            android:glEsVersion="0x00020000"
            android:required="true"/>

    <application
            android:allowBackup="true"
            android:icon="@drawable/ic_launcher"
            android:label="@string/app_name"
            android:theme="@style/AppTheme" >

        <meta-data
                android:name="com.google.android.gms.version"
                android:value="@integer/google_play_services_version" />

        <meta-data
                android:name="com.google.android.maps.v2.API_KEY"
                android:value="AIzaSyCsbE9n--3oTaFg7bQTGDFaE27d58INDF8"/>

        <activity
                android:name="com.fok.speedfix.ActivityGetUsers"
                android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
                android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityCompanyList"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityEditUser"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityGetUsers"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityMap"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityNewUser"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityPhoneInfo"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivityPhoneList"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="ActivitySignIn"/>
		<activity 
		    	android:label="@string/app_name"
		    	android:screenOrientation="portrait"
  				android:configChanges="orientation|keyboardHidden"
		    	android:name="BaseMapActivity"/>
    </application>

</manifest>
