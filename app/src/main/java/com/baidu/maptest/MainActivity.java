package com.baidu.maptest;

import android.app.*;
import android.os.*;
import android.support.v7.app.*;
import com.baidu.mapapi.*;
import com.baidu.mapapi.map.*;

public class MainActivity extends AppCompatActivity
{
	
	 MapView MapView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.main);
        MapView=(MapView)findViewById(R.id.mapView);	
		
	//    MapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
}
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		MapView.onDestroy();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		MapView.onResume();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		MapView.onPause();
		
	}
	
}
