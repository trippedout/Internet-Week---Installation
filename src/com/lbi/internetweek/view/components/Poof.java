package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Poof
{
	private Installation			_pa;
	
	private int						_poofTotalFrames	=	4;
	private PImage[]				_poofFrames;

	private PVector					_poofLoc;
	private int 					_poofCount = 0;
	private int 					_poofFrame = 0;
	private int						_poofFrameFreq = 6;
	
	public int						totalLife	=	300;
	public boolean					hasLife		=	true;
	
	public Poof( PImage[] poofFrames, PVector p )
	{
		_pa			=	ApplicationFacade.app;
		_poofFrames =	poofFrames;
		_poofLoc	=	p;
	}
	
	public void draw()
	{
		_pa.pushMatrix();
			_pa.translate(_poofLoc.x, _poofLoc.y);
			drawPoof();
			drawFeathers();
		_pa.popMatrix();
		
		totalLife--;		
		hasLife = totalLife > 0;
	}
	
	private void drawPoof()
	{
		if( _poofCount % _poofFrameFreq == 0 )
		{
			_poofFrame++;
		}
		
		if( _poofFrame < _poofTotalFrames )
		{			
			_pa.imageMode(PConstants.CENTER);
				_pa.image(_poofFrames[_poofFrame], 0, 0);
			_pa.imageMode(PConstants.CORNER);
		}
		
		_poofCount++;
	}

	private void drawFeathers()
	{
				
	}
	
	public void kill()
	{
		
	}
}
