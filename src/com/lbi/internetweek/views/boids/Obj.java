package com.lbi.internetweek.views.boids;

import processing.core.PVector;

public class Obj
{
	Flock fl;
	PVector pos;
	int rad = 75;
	int aura;

	Obj()
	{
		//init(boids.zone.randomPos());
	}
	
	Obj(Flock flock, PVector p){
		init(flock, p );
	}
	
	void init(Flock flock, PVector p)
	{
		fl = flock;
		pos = p;
		aura = 4 * rad;
	}
}
