package com.lbi.internetweek.view.components;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Poof
{
	private static final float MIN_V = -8;
	private static final float MAX_V = 8;

	private Installation			_pa;
	
	//POOF STUFF
	private int						_poofTotalFrames	=	4;
	private PImage[]				_poofFrames;

	private PVector					_poofLoc;
	private int 					_poofCount 			= 0;
	private int 					_poofFrame 			= 0;
	private int						_poofFrameFreq 		= 6;
	
	//FEATHER STUFF
	private PImage					_feather;
	private int						_numFeathers		= 6;
	private Feather[]				_feathers			= new Feather[_numFeathers]; 
	
	//LIFE STUFF
	public int						totalLife	=	100;
	public boolean					hasLife		=	true;
	
	public Poof( PImage[] poofFrames, PImage featherSprite, PVector p )
	{
		_pa			=	ApplicationFacade.app;
		_poofFrames =	poofFrames;
		_poofLoc	=	p;
		_feather	=	featherSprite;
		
		createFeathers();
	}
	
	private void createFeathers()
	{
		for( int i = 0; i < _numFeathers; ++i )
		{
			_feathers[i]		=	new Feather(_feather, new PVector( _pa.random(MIN_V, MAX_V), _pa.random(MIN_V, MAX_V) ) );
		}
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
		for( int i = 0; i < _numFeathers; ++i )
		{
			_feathers[i].draw();
		}
	}
	
	public void kill()
	{
		_poofFrames = null;
		_poofLoc = null;
		_feathers = null;
	}
}
