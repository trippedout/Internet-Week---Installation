package com.lbi.internetweek.views.boids;

import processing.core.PApplet;
import processing.core.PVector;

public class Zone 
{
	Flock flock;
	int x, y, w, h;

	public Zone(Flock f, int _x, int _y, int _w, int _h)
	{
		flock = f;
		x = _x;
		y = _y;
		w = _w;
		h = _h;
	}

	// define if coord is in zone
	public boolean isIn(PVector v){
		return this.isIn(PApplet.floor(v.x), PApplet.floor(v.y)); 
	}
	
	public boolean isIn(int px, int py){
		if(px > x && px < x + w &&
				py > y && py < y + h)
			return true;
		else return false; 
	}

	// return a random pos vector in zone
	public PVector randomPos(){
		return new PVector(flock.pa.random(x, x + w), flock.pa.random(y, y + h));
	}
	
}
