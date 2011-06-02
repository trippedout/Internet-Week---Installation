package com.lbi.internetweek.view.boids;

import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Rect;

public class Zone 
{
	public Flock flock;
	public Rect rect;
//	int x, y, w, h;

	public Zone(Flock f, Rect r)
	{
		flock = f;
		rect = r;
		//this(f, (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
	}
	
	public Zone(Flock f, int _x, int _y, int _w, int _h)
	{
//		flock = f;
//		x = _x;
//		y = _y;
//		w = _w;
//		h = _h;
	}

	// define if coord is in zone
	public boolean isIn(PVector v){
		return this.isIn(PApplet.floor(v.x), PApplet.floor(v.y)); 
	}
	
	public boolean isIn(int px, int py){
		if(px > rect.x && px < rect.x + rect.width &&
				py > rect.y && py < rect.y + rect.height)
			return true;
		else return false; 
	}

	// return a random pos vector in zone
	public PVector randomPos(){
		return new PVector(flock.pa.random(rect.x, rect.x + rect.width), flock.pa.random(rect.y, rect.y + rect.height));
	}
	
	public float x()
	{
		return rect.x;
	}
	
	public float y()
	{
		return rect.y;
	}
	
	public float w()
	{
		return rect.width;
	}
	
	public float h()
	{
		return rect.height;
	}
	
}
