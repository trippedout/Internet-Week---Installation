package com.lbi.internetweek.view.boids;

import processing.core.PVector;

public class Obj
{
	Flock fl;
	PVector pos;
	PVector vel;
	int rad = 45;
	int aura;

	Obj(Flock flock, PVector p){
		init( flock, p );
	}
	
	void init(Flock flock, PVector p)
	{
		fl = flock;
		pos = p;
		aura = 4 * rad;
	}
}
