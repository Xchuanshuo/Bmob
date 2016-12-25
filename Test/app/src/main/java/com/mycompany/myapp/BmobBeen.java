package com.mycompany.myapp;
import cn.bmob.v3.*;

public class BmobBeen extends BmobObject
{
	private String name;
	private String address;

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAddress()
	{
		return address;
	}


	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	}
