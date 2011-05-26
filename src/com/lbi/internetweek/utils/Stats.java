package com.lbi.internetweek.utils;

import processing.core.PApplet;

public class Stats 
{
	PApplet p;
	  
	public Stats( PApplet parent )
	{
		p = parent;
	}
  
	public void draw( int x, int y )
	{
		p.fill( 50, 50, 200 );
		p.rect( x, y, 150, 30 );
		p.fill(255);
		p.textAlign(p.LEFT);
		p.textSize(14);
		p.text( "FrameRate: " + PApplet.floor(p.frameRate), x + 10, y + 20 );
	}
}
