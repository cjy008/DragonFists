package com.example.dragonfist;

public class Enemy {
	public float x,y, radius;
	public double velx,vely,accx,accy;
	
	public Enemy (float x,float y,double velx,double vely)
	{
		this.x = x;
		this.y = y;
		this.velx = velx;
		this.vely = vely;
	}
	
	public void update(float passedTime)
	{
		x= (float) (x+(velx*passedTime));
		velx = velx+(accx*passedTime);
		vely = vely+(accy*passedTime);
		y= (float) (y+(vely*passedTime));
	}
}
