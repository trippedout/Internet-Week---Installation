package com.lbi.internetweek.view.boids;

import processing.core.PApplet;
import processing.core.PVector;
import toxi.geom.Rect;

public class Zone 
{
	public Flock flock;
	public Rect rect;

	public Zone(Flock f, Rect r)
	{
		flock = f;
		rect = r;
	}

	public boolean isIn(PVector v)
	{
		return this.isIn(PApplet.floor(v.x), PApplet.floor(v.y)); 
	}
	
	public boolean isIn(int px, int py)
	{
		if( px > rect.x && px < rect.x + rect.width &&
				py > rect.y && py < rect.y + rect.height)
			return true;
		else return false; 
	}

	public PVector randomPos()
	{
		return new PVector( 
				flock.pa.random(rect.x, rect.x + rect.width), 
				flock.pa.random(rect.y, rect.y + rect.height)
				);
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
