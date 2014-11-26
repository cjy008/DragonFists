package com.dragonfist;

public class Vector2 {
	float x;
	float y;
	public Vector2(float x,float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 cloneBase){
		this(cloneBase.x,cloneBase.y);
	}
	
	public double length(){
		return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
	}
	
	public double sqrtLessLength(){
		return Math.pow(x,2)+Math.pow(y,2);
	}
	
	public Vector2 subtract(Vector2 subtractee){
		return new Vector2(x-subtractee.x,y-subtractee.y);
	}
	
	public Vector2 add(Vector2 subtractee){
		return new Vector2(x+subtractee.x,y+subtractee.y);
	}
	
	public void multiply(float multiplicator){
		x = x*multiplicator;
		y = y*multiplicator;
	}
	
	public float dotProduct(Vector2 otherVec){
		return (x*otherVec.x+y*otherVec.y);
	}
	
	public void divide(float divisor){
		x = x/divisor;
		y = y/divisor;
	}
}
