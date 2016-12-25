package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import cn.bmob.v3.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.location.*;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.exception.*;

public class MainActivity extends Activity  implements OnClickListener
{

	@Override
	public void onClick(View v)
	{
	   switch(v.getId()){
		  case R.id.btn:
	    addData();
		break;
		// TODO: Implement this method
	}
}

	private EditText edt1,edt2;
	private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Bmob.initialize(this,"26bfd80a5c2f2e022bbe4b4c52aba47b");
        setContentView(R.layout.main);
        edt1=(EditText)findViewById(R.id.edt1);
		edt2=(EditText)findViewById(R.id.edt2);
		btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }
	private void addData(){
			   		   String name=edt1.getText().toString();
			   String address=edt2.getText().toString();
			   if(name.equals(" ")||address.equals(" ")){
				   return;
			   }
			   BmobBeen been=new BmobBeen();
			   been.setName(name);
			   been.setAddress(address);
			   been.save(new SaveListener<String>(){

					   @Override
					   public void done(String p1, BmobException p2)
					   {
						 if(p2==null){
					Toast.makeText(MainActivity.this,"添加数据成功"+p1,Toast.LENGTH_SHORT).show();
						 }else{
					Toast.makeText(MainActivity.this,"添加数据失败",Toast.LENGTH_SHORT).show();
						 }
					   }
				   
			   });
	}	  
}
