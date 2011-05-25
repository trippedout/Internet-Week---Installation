package com.lbi.internetweek.views;

import processing.core.PApplet;
import toxi.geom.Vec2D;

public class Perch 
{
	PApplet 	pa;
	
	Vec2D[] 	spots;
	int 		numberOfSpots;
	
	public Perch( PApplet p, int numBirds)
	{
		pa				=	p;
		numberOfSpots 	=	numBirds;
		spots			=	new Vec2D[numberOfSpots];
		
		setSpots();
	}

	private void setSpots() 
	{
		for( int i = 0; i < numberOfSpots; ++i )
		{
			spots[i] = new Vec2D(pa.random(pa.width-100)+50, pa.random(pa.height-400)+200);
		}
	}

	public Vec2D getSpot(int i) 
	{
		return spots[i];
	}
}
