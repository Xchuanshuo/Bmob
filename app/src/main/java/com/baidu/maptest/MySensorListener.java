package com.baidu.maptest;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.content.Context;
//传感器类
public class MySensorListener implements SensorEventListener
{
    private Context context;
	private SensorManager sensorManager;
	private Sensor sensor;
	private float lastx;

	private OnOrientationListener orient;
	public MySensorListener(Context context){
		this.context=context;
	}
	public void start(){
		//获得传感器管理器
		sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		if(sensorManager!=null){
		//获得方向传感器
			sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		//注册
		if(sensor!=null){
			sensorManager.registerListener(this,
			  sensor,sensorManager.SENSOR_DELAY_UI);
		}
	}
	//停止检测
	public void stop(){
		sensorManager.unregisterListener(this);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		//接受传感器的类型
		if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
			float x=event.values[SensorManager.DATA_X];
			if(Math.abs( x - lastx)>1.0){
				orient.onOrientationChanged(x);
			}
			lastx=x;
		}
		// TODO: Implement this method
	}

	@Override
	public void onAccuracyChanged(Sensor p1, int p2)
	{
		// TODO: Implement this method
	}
	public void setOnOrientationListener(OnOrientationListener orient){
		this.orient=orient;
	}
	
    public interface OnOrientationListener{
		void onOrientationChanged(float x);
	}
}
