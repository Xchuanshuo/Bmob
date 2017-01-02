package com.baidu.maptest;

import android.os.*;
import android.support.v7.app.AppCompatActivity;
import com.baidu.mapapi.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import android.view.*;
import android.widget.ActionMenuView.*;
import android.widget.AdapterView.*;
import com.baidu.location.*;
import android.location.*;
import android.graphics.*;
import com.baidu.mapapi.model.*;
import com.baidu.maptest.MySensorListener.OnOrientationListener;

public class MainActivity extends AppCompatActivity 
{
	/*
	 *地图实例
	 */
	private BaiduMap mMap;
    /*
	 *地图客户端
	 */
	private MapView MapView;
	//定位客户端	
	private LocationClient mLocationCilent;
	/*
	 *定位的监听器
	 */
	private MyLocationListener mMyLocationListener;
	/*
	 *定位的模式
	 */
	private LocationMode mLocationMode=LocationMode.NORMAL;
	/*
	 *是否是第一次进行定位
	 */
	private volatile boolean isFirstLocation=true;
	/*
	 *最新一次的经纬度
	 */
	private double mCurrenLantitude;
	private double mCurrenlongtitude;
	/*
	 *最新的精度
	 */
	private float mCurrentAccary;
	/*
	 *方向传感器的监听器
	 */
	private MySensorListener mMySensorListener;
	/*
	 *x方向的坐标
	 */
	private float newX;
	/*
	 *地图的定位模式
	 */
	private String[] mStyles = new String[] { "地图模式【正常】", "地图模式【跟随】",
"地图模式【罗盘】" };
	
	private int mCurrentStyle = 0;

	private void initMyLocation(){
		//定位初始化
		mLocationCilent=new LocationClient(this);
		mMyLocationListener=new MyLocationListener();
		mLocationCilent.registerLocationListener(mMyLocationListener);
		//设置定位相关的配置
		LocationClientOption option=new LocationClientOption();
		option.setOpenGps(true);
		//设置坐标类型
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocationCilent.setLocOption(option);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.main);
		isFirstLocation=true;
		
        MapView=(MapView)findViewById(R.id.mapView);	
		mMap=MapView.getMap();
		MapStatusUpdate msu=MapStatusUpdateFactory.zoomBy(15.0f);
		mMap.setMapStatus(msu);
		initMyLocation();
		MySensor();
	}

	//@Override
	protected void onStart()
	{
		//开启图层定位
		mMap.setMyLocationEnabled(true);
		if(!mLocationCilent.isStarted()){
			mLocationCilent.start();
			}
		//开启方向传感器
		mMySensorListener.start();
		// TODO: Implement this method
		super.onStart();
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

	@Override
	protected void onStop()
	{ 
	    //关闭图层定位
		mMap.setMyLocationEnabled(false);
		mLocationCilent.stop();
		//关闭传感器
		mMySensorListener.stop();
		// TODO: Implement this method
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_item,menu);
		// TODO: Implement this method
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
			case R.id.test1:
				//设置普通地图
				mMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
				break;
			case R.id.test2:
				//设置卫星地图
				mMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				break;
			case R.id.test3:
				//设置交通图
				if(mMap.isTrafficEnabled()){
					item.setTitle("开启交通图");
					mMap.setTrafficEnabled(false);
				}else{
				    item.setTitle("关闭交通图");
			    	mMap.setTrafficEnabled(true);
				}
				break;
			case R.id.test4:
				myloctation();
				break;
			case R.id.test5:
				mCurrentStyle=(++mCurrentStyle)% mStyles.length;
			    item.setTitle(mStyles[mCurrentStyle]);
				switch(mCurrentStyle){
					case 0:
			mLocationMode=LocationMode.NORMAL;
			        case 1:
			mLocationMode=LocationMode.FOLLOWING;
			        case 2:
			mLocationMode=LocationMode.COMPASS;
			    break;
				}
		 BitmapDescriptor bdsc=BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		 MyLocationConfiguration config=new MyLocationConfiguration(mLocationMode,true,bdsc);
		 mMap.setMyLocationConfigeration(config);
		 break;
		}
		
		return true;
	}
	/*
	 *实现定位回调
	 */
	public class MyLocationListener implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			//如果地图被销毁 不再处理定位
			if(location==null||MapView==null){
				return;
			}
			//构造定位数据
			MyLocationData data=new MyLocationData.Builder()
			                   .accuracy(location.getRadius())
							   .direction(newX).latitude(location.getLatitude())
							   .longitude(location.getLongitude()).build();
			mCurrentAccary=location.getRadius();
			//设置定位数据
			mMap.setMyLocationData(data);
			mCurrenLantitude=location.getLatitude();
			mCurrenlongtitude=location.getLongitude();
			//设置自定义图标
			BitmapDescriptor mCurrentMaker=BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
			MyLocationConfiguration config=new MyLocationConfiguration(mLocationMode,true,mCurrentMaker);
			mMap.setMyLocationConfigeration(config);
			//第一次进行定位时 将地图移动到当前位置
			if(isFirstLocation){
				isFirstLocation=false;
				LatLng mLatLng=new LatLng(location.getLatitude(),location.getLongitude());
				MapStatusUpdate ms=MapStatusUpdateFactory.newLatLng(mLatLng);
				mMap.animateMapStatus(ms);
			}
		}

	}
	
	private void myloctation(){
		LatLng ll=new LatLng(mCurrenLantitude,mCurrenlongtitude);
		MapStatusUpdate ms=MapStatusUpdateFactory.newLatLng(ll);
		mMap.animateMapStatus(ms);
	}
	//初始化方向传感器
	private void MySensor(){
		mMySensorListener=new MySensorListener(getApplicationContext());
		mMySensorListener.setOnOrientationListener(new MySensorListener.OnOrientationListener(){

				@Override
				public void onOrientationChanged(float x)
				{
		  newX=(int)x;
		  MyLocationData data=new MyLocationData.Builder()
		                      .accuracy(mCurrentAccary)
							  .direction(newX)
							  .latitude(mCurrenLantitude)
							  .longitude(mCurrenlongtitude).build();
		  mMap.setMyLocationData(data);
		  				// 设置自定义图标
						BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
								.fromResource(R.drawable.navi_map_gps_locked);
						MyLocationConfiguration config = new MyLocationConfiguration(
								mLocationMode, true, mCurrentMarker);
						mMap.setMyLocationConfigeration(config);
					// TODO: Implement this method
				}
			
		});
}
}
